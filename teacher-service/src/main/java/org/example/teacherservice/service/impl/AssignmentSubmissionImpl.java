package org.example.teacherservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.teacherservice.dto.assignment.AssignmentSubmitDTO;
import org.example.teacherservice.entity.*;
import org.example.teacherservice.mapper.*;
import org.example.teacherservice.service.AssignmentClassesService;
import org.example.teacherservice.service.AssignmentSubmissionService;
import org.example.teacherservice.service.StudentService;
import org.example.teacherservice.vo.SubmissionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.teacherservice.exception.BusinessException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentSubmissionImpl implements AssignmentSubmissionService {

    @Autowired
    private AssignmentSubmissionMapper submissionMapper;
    @Autowired
    private AssignmentClassesService assignmentClassesService;
    @Autowired
    private AssignmentClassesMapper assignmentClassesMapper;
    @Autowired
    private AssignmentGradeMapper assignmentGradeMapper;
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Override
    @Transactional
    public void submitAssignment(AssignmentSubmitDTO dto) {
        // 检查学生是否存在
        if (!studentService.existsById(dto.getStudentId())) {
            throw new BusinessException("学生不存在");
        }

        // 2. 检查作业-班级关联是否存在并获取截止时间
        AssignmentClasses assignmentClass = assignmentClassesMapper.selectById(dto.getAssignmentClassId());
        if (assignmentClass == null) {
            throw new BusinessException("作业不存在或未发布到班级");
        }

        // 3. 检查是否已过截止时间
        if (new Date().after(assignmentClass.getClassDeadline())) {
            throw new BusinessException("已超过作业提交截止时间");
        }

        // 检查是否已提交过
        LambdaQueryWrapper<AssignmentSubmission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AssignmentSubmission::getAssignmentClassId, dto.getAssignmentClassId())
                .eq(AssignmentSubmission::getStudentId, dto.getStudentId());
        if (submissionMapper.exists(queryWrapper)) {
            throw new BusinessException("已提交过该作业，请勿重复提交");
        }

        // 创建提交记录
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignmentClassId(dto.getAssignmentClassId());
        submission.setStudentId(dto.getStudentId());
        submission.setResourceId(dto.getResourceId());
        submission.setSubmitTime(new Date());

        if (submissionMapper.insert(submission) != 1) {
            throw new BusinessException("作业提交失败");
        }
    }

    @Override
    public SubmissionVO getStudentSubmission(Integer assignmentClassId, Integer studentId) {
        LambdaQueryWrapper<AssignmentSubmission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AssignmentSubmission::getAssignmentClassId, assignmentClassId)
                .eq(AssignmentSubmission::getStudentId, studentId);


        AssignmentSubmission submission = submissionMapper.selectOne(queryWrapper);

        AssignmentClasses assignmentClasses=assignmentClassesMapper.selectById(assignmentClassId);
        Assignment assignment=assignmentMapper.selectById(assignmentClasses.getAssignmentId());
        AssignmentGrade assignmentGrade=assignmentGradeMapper.selectBySubmissionId(submission.getSubmissionId());
        Student student=studentMapper.selectById(studentId);
        if (submission == null) {
            return null;
        }

        SubmissionVO vo = new SubmissionVO();
        if (assignmentGrade != null) {
            vo.setIsGraded(true);
        }
        BeanUtils.copyProperties(submission, vo);
        BeanUtils.copyProperties(assignmentGrade, vo);
        BeanUtils.copyProperties(assignment, vo);
        vo.setAssignmentTitle(assignment.getTitle());
        vo.setStudentName(student.getRealName());
        BeanUtils.copyProperties(student, vo);
        // 这里可以补充其他需要设置的信息，如作业详情、批改状态等
        return vo;
    }

    @Override
    public List<SubmissionVO> listStudentSubmissions(Integer studentId) {
        // 1. 查询学生的所有提交记录
        LambdaQueryWrapper<AssignmentSubmission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AssignmentSubmission::getStudentId, studentId)
                .orderByDesc(AssignmentSubmission::getSubmitTime);

        List<AssignmentSubmission> submissions = submissionMapper.selectList(queryWrapper);
        if (submissions == null || submissions.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 遍历 submissions，查询关联数据并构建 VO
        return submissions.stream()
                .map(submission -> {
                    SubmissionVO vo = new SubmissionVO();
                    BeanUtils.copyProperties(submission, vo);

                    // 查询关联的 assignment_class 和 assignment
                    AssignmentClasses assignmentClass = assignmentClassesMapper.selectById(submission.getAssignmentClassId());
                    if (assignmentClass != null) {
                        Assignment assignment = assignmentMapper.selectById(assignmentClass.getAssignmentId());
                        if (assignment != null) {
                            vo.setAssignmentId(assignment.getAssignmentId());
                            vo.setAssignmentTitle(assignment.getTitle());
                        }
                    }

                    // 查询成绩
                    AssignmentGrade grade = assignmentGradeMapper.selectBySubmissionId(submission.getSubmissionId());
                    if (grade != null) {
                        vo.setIsGraded(true);
                        BeanUtils.copyProperties(grade, vo); // 设置成绩信息
                    }

                    // 查询学生信息
                    Student student = studentMapper.selectById(studentId);
                    if (student != null) {
                        vo.setStudentName(student.getRealName());
                    }

                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Integer> countSubmissionsByAssignments(List<Integer> assignmentClassIds) {
        if (assignmentClassIds == null || assignmentClassIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return submissionMapper.selectList(Wrappers.<AssignmentSubmission>lambdaQuery()
                        .in(AssignmentSubmission::getAssignmentClassId, assignmentClassIds))
                .stream()
                .collect(Collectors.groupingBy(
                        AssignmentSubmission::getAssignmentClassId,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    @Override
    public boolean hasStudentSubmitted(Integer assignmentClassId, Integer studentId) {
        LambdaQueryWrapper<AssignmentSubmission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AssignmentSubmission::getAssignmentClassId, assignmentClassId)
                .eq(AssignmentSubmission::getStudentId, studentId);
        return submissionMapper.exists(queryWrapper);
    }

    /**
     * 新增方法：根据作业ID查询所有提交的学生ID列表
     * @param assignmentClassId 作业-班级关联ID
     * @return 提交了该作业的学生ID列表
     */
    public List<Integer> listSubmittedStudents(Integer assignmentClassId) {
        LambdaQueryWrapper<AssignmentSubmission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(AssignmentSubmission::getStudentId)
                .eq(AssignmentSubmission::getAssignmentClassId, assignmentClassId);

        return submissionMapper.selectObjs(queryWrapper).stream()
                .map(obj -> (Integer) obj)
                .collect(Collectors.toList());
    }

    /**
     * 新增方法：根据作业ID查询所有提交记录
     * @param assignmentClassId 作业-班级关联ID
     * @return 该作业的所有提交记录
     */
    public List<SubmissionVO> listSubmissionsByAssignment(Integer assignmentClassId) {
        LambdaQueryWrapper<AssignmentSubmission> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AssignmentSubmission::getAssignmentClassId, assignmentClassId)
                .orderByDesc(AssignmentSubmission::getSubmitTime);

        return submissionMapper.selectList(queryWrapper).stream()
                .map(submission -> getStudentSubmission(assignmentClassId, submission.getStudentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
