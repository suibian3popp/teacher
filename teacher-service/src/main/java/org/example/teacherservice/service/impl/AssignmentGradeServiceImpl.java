package org.example.teacherservice.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.example.teacherservice.entity.Classes;
import lombok.RequiredArgsConstructor;
import org.example.teacherservice.dto.assignment.AssignmentGradeDTO;
import org.example.teacherservice.entity.*;
import org.example.teacherservice.mapper.*;
import org.example.teacherservice.vo.GradeDetailVO;
import org.example.teacherservice.exception.BusinessException;
import org.example.teacherservice.service.AssignmentGradeService;

import org.example.teacherservice.vo.assignment.GradeStatsVO;
import org.example.teacherservice.vo.GradingProgressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentGradeServiceImpl implements AssignmentGradeService {

    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private AssignmentGradeMapper gradeMapper;
    @Autowired
    private AssignmentSubmissionMapper assignmentSubmissionMapper;
    @Autowired
    private AssignmentClassesMapper assignmentClassesMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ClassesMapper classesMapper;

    @Override
    @Transactional
    public void gradeAssignment(AssignmentGradeDTO dto) {
        // 1. 验证提交记录是否存在
        AssignmentSubmission submission = assignmentSubmissionMapper.selectById(dto.getSubmissionId());
        if (submission == null) {
            throw new BusinessException("提交记录不存在");
        }

        // 2. 验证批改人权限
        Users grader = usersMapper.selectOne(Wrappers.<Users>lambdaQuery()
                .select(Users::getType)
                .eq(Users::getUserId, dto.getGraderId()));
        if (grader == null || !Arrays.asList("teacher", "ta").contains(grader.getType())) {
            throw new BusinessException("无效的批改人，只有教师或助教可以批改作业");
        }

        // 3. 获取作业-班级关联信息
        AssignmentClasses assignmentClass = assignmentClassesMapper.selectOne(
                Wrappers.<AssignmentClasses>lambdaQuery()
                        .select(AssignmentClasses::getAssignmentId)
                        .eq(AssignmentClasses::getId, submission.getAssignmentClassId()));
        if (assignmentClass == null) {
            throw new BusinessException("作业班级关联信息不存在");
        }

        // 4. 获取作业满分值
        Assignment assignment = assignmentMapper.selectOne(
                Wrappers.<Assignment>lambdaQuery()
                        .select(Assignment::getTotalScore)
                        .eq(Assignment::getAssignmentId, assignmentClass.getAssignmentId()));
        if (assignment == null) {
            throw new BusinessException("作业信息不存在");
        }

        // 5. 验证分数有效性（使用BigDecimal的compareTo方法）
        if (dto.getScore() != null && assignment.getTotalScore() != null
                && dto.getScore().compareTo(assignment.getTotalScore()) > 0) {
            throw new BusinessException("分数超过作业满分 " + assignment.getTotalScore());
        }

        // 6. 保存或更新批改记录
        AssignmentGrade grade = new AssignmentGrade();
        BeanUtils.copyProperties(dto, grade);

        if (gradeMapper.exists(Wrappers.<AssignmentGrade>lambdaQuery()
                .eq(AssignmentGrade::getSubmissionId, dto.getSubmissionId()))) {
            // 更新现有记录
            gradeMapper.update(grade, Wrappers.<AssignmentGrade>lambdaQuery()
                    .eq(AssignmentGrade::getSubmissionId, dto.getSubmissionId()));
        } else {
            // 新增记录
            gradeMapper.insert(grade);
        }
    }

    @Override
    @Transactional
    public void deleteGrade(Integer gradeId) {
        /*
         * 删除批改记录流程：
         * 1. 根据gradeId删除assignment_grades表记录
         * 2. 验证删除是否成功（影响行数是否为1）
         * 3. 删除失败时抛出BusinessException
         */

        // 1. 参数校验
        if (gradeId == null) {
            throw new BusinessException("批改ID不能为空");
        }

        // 2. 执行删除操作
        int affectedRows = gradeMapper.deleteById(gradeId);

        // 3. 验证删除结果
        if (affectedRows != 1) {
            // 可能原因：记录不存在或数据库异常
            if (gradeMapper.selectById(gradeId) == null) {
                throw new BusinessException("批改记录不存在，gradeId: " + gradeId);
            } else {
                throw new BusinessException("删除批改记录失败，gradeId: " + gradeId);
            }
        }

        // 4. 可选：记录日志
        System.out.println("成功删除批改记录，gradeId: {}"+gradeId);
    }

    @Override
    public GradeStatsVO getAssignmentStats(Integer assignmentClassId) {
        // 参数校验
        if (assignmentClassId == null) {
            throw new BusinessException("作业班级ID不能为空");
        }

        // 1. 查询该作业所有提交ID
        List<Integer> submissionIds = assignmentSubmissionMapper.selectList(
                        Wrappers.<AssignmentSubmission>lambdaQuery()
                                .select(AssignmentSubmission::getSubmissionId)
                                .eq(AssignmentSubmission::getAssignmentClassId, assignmentClassId))
                .stream()
                .map(AssignmentSubmission::getSubmissionId)
                .collect(Collectors.toList());

        // 如果没有提交记录，返回空统计数据
        if (submissionIds.isEmpty()) {
            return new GradeStatsVO(0.0, 0.0, 0.0, 0L, 0L);
        }

        // 2. 查询相关批改记录
        List<AssignmentGrade> grades = gradeMapper.selectList(
                Wrappers.<AssignmentGrade>lambdaQuery()
                        .select(AssignmentGrade::getScore)
                        .in(AssignmentGrade::getSubmissionId, submissionIds));

        // 3. 计算统计数据
        List<BigDecimal> validScores = grades.stream()
                .map(AssignmentGrade::getScore)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 3.1 计算平均分
        double average = validScores.stream()
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .orElse(0.0);

        // 3.2 计算最高分
        double max = validScores.stream()
                .mapToDouble(BigDecimal::doubleValue)
                .max()
                .orElse(0.0);

        // 3.3 计算最低分
        double min = validScores.stream()
                .mapToDouble(BigDecimal::doubleValue)
                .min()
                .orElse(0.0);

        // 3.4 已批改数（有成绩的记录数）
        long gradedCount = validScores.size();

        // 3.5 总提交数
        long totalCount = submissionIds.size();

        // 4. 返回统计视图对象
        return new GradeStatsVO(
                roundTwoDecimal(average),
                roundTwoDecimal(max),
                roundTwoDecimal(min),
                gradedCount,
                totalCount
        );
    }

    // 辅助方法：保留两位小数
    private double roundTwoDecimal(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public GradingProgressVO getGradingProgress(Integer assignmentClassId) {
        // 1. 参数校验
        if (assignmentClassId == null) {
            throw new BusinessException("作业班级ID不能为空");
        }

        // 2. 获取作业-班级关联信息
        AssignmentClasses assignmentClass = assignmentClassesMapper.selectOne(
                Wrappers.<AssignmentClasses>lambdaQuery()
                        .select(AssignmentClasses::getClassId)
                        .eq(AssignmentClasses::getId, assignmentClassId));

        if (assignmentClass == null) {
            throw new BusinessException("未找到对应的作业班级关联信息");
        }

        // 3. 获取班级基本信息
        Classes classesInfo = classesMapper.selectOne(
                Wrappers.<Classes>lambdaQuery()
                        .select(Classes::getName, Classes::getStudentCount)
                        .eq(Classes::getClassId, assignmentClass.getClassId()));

        if (classesInfo == null) {
            throw new BusinessException("未找到对应的班级信息");
        }

        // 4. 统计提交数量（处理Long到Integer的类型转换）
        // 4.1 总提交数
        Long submittedCountLong = assignmentSubmissionMapper.selectCount(
                Wrappers.<AssignmentSubmission>lambdaQuery()
                        .eq(AssignmentSubmission::getAssignmentClassId, assignmentClassId));
        int submittedCount = submittedCountLong != null ? submittedCountLong.intValue() : 0;

        // 4.2 已批改数
        Long gradedCountLong = gradeMapper.selectCount(
                Wrappers.<AssignmentGrade>lambdaQuery()
                        .inSql(AssignmentGrade::getSubmissionId,
                                "SELECT submission_id FROM assignment_submissions WHERE assignment_class_id = " + assignmentClassId));
        int gradedCount = gradedCountLong != null ? gradedCountLong.intValue() : 0;

        // 5. 构建返回对象
        GradingProgressVO progressVO = new GradingProgressVO();
        progressVO.setClassId(assignmentClass.getClassId());
        progressVO.setClassName(classesInfo.getName());
        progressVO.setTotalStudents(classesInfo.getStudentCount());
        progressVO.setSubmittedCount(submittedCount);
        progressVO.setGradedCount(gradedCount);

        return progressVO;
    }

    @Override
    public List<GradeDetailVO> listAssignmentGrades(Integer assignmentClassId) {
        // 1. 验证作业班级是否存在
        AssignmentClasses assignmentClass = assignmentClassesMapper.selectOne(
                Wrappers.<AssignmentClasses>lambdaQuery()
                        .eq(AssignmentClasses::getId, assignmentClassId));
        if (assignmentClass == null) {
            throw new BusinessException("作业班级不存在");
        }

        // 2. 获取该作业班级的所有提交记录
        List<AssignmentSubmission> submissions = assignmentSubmissionMapper.selectList(
                Wrappers.<AssignmentSubmission>lambdaQuery()
                        .eq(AssignmentSubmission::getAssignmentClassId, assignmentClassId));

        if (CollectionUtils.isEmpty(submissions)) {
            return Collections.emptyList();
        }

        // 3. 批量获取学生信息
        List<Integer> studentIds = submissions.stream()
                .map(AssignmentSubmission::getStudentId)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, Users> studentMap = usersMapper.selectList(
                        Wrappers.<Users>lambdaQuery()
                                .in(Users::getUserId, studentIds))
                .stream()
                .collect(Collectors.toMap(Users::getUserId, Function.identity()));

        // 4. 获取批改记录
        List<Integer> submissionIds = submissions.stream()
                .map(AssignmentSubmission::getSubmissionId)
                .collect(Collectors.toList());

        List<AssignmentGrade> grades = gradeMapper.selectList(
                Wrappers.<AssignmentGrade>lambdaQuery()
                        .in(AssignmentGrade::getSubmissionId, submissionIds));

        // 5. 批量获取批改人信息
        List<Integer> graderIds = grades.stream()
                .map(AssignmentGrade::getGraderId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, Users> graderMap = CollectionUtils.isEmpty(graderIds) ? Collections.emptyMap() :
                usersMapper.selectList(
                                Wrappers.<Users>lambdaQuery()
                                        .in(Users::getUserId, graderIds))
                        .stream()
                        .collect(Collectors.toMap(Users::getUserId, Function.identity()));

        // 6. 组装VO列表
        List<GradeDetailVO> result = new ArrayList<>();

        for (AssignmentSubmission submission : submissions) {
            GradeDetailVO vo = new GradeDetailVO();
            vo.setSubmissionId(submission.getSubmissionId());
            vo.setStudentId(submission.getStudentId());

            // 设置学生姓名
            Users student = studentMap.get(submission.getStudentId());
            vo.setStudentName(student != null ? student.getUsername() : "未知学生");

            // 查找对应的批改记录
            Optional<AssignmentGrade> gradeOpt = grades.stream()
                    .filter(g -> g.getSubmissionId().equals(submission.getSubmissionId()))
                    .findFirst();

            if (gradeOpt.isPresent()) {
                AssignmentGrade grade = gradeOpt.get();
                vo.setGradeId(grade.getGradeId());
                vo.setScore(grade.getScore());
                vo.setFeedback(grade.getFeedback());
                vo.setGradeTime(grade.getGradeTime());

                // 设置批改人姓名
                if (grade.getGraderId() != null) {
                    Users grader = graderMap.get(grade.getGraderId());
                    vo.setStudentName(grader != null ? grader.getUsername() : "未知批改人");
                }
            }

            result.add(vo);
        }

        // 7. 按分数降序排序（未批改的排在最后）
        result.sort((a, b) -> {
            if (a.getScore() == null && b.getScore() == null) return 0;
            if (a.getScore() == null) return 1;
            if (b.getScore() == null) return -1;
            return b.getScore().compareTo(a.getScore());
        });

        return result;
    }

    @Override
    public Map<Long, Integer> countGradedAssignments(List<Integer> assignmentClassIds) {
        if (CollectionUtils.isEmpty(assignmentClassIds)) {
            return Collections.emptyMap();
        }

        // 1. 批量查询作业提交记录
        List<AssignmentSubmission> submissions = assignmentSubmissionMapper.selectList(
                Wrappers.<AssignmentSubmission>lambdaQuery()
                        .select(AssignmentSubmission::getSubmissionId, AssignmentSubmission::getAssignmentClassId)
                        .in(AssignmentSubmission::getAssignmentClassId, assignmentClassIds)
        );

        if (CollectionUtils.isEmpty(submissions)) {
            return assignmentClassIds.stream()
                    .collect(Collectors.toMap(
                            id -> id.longValue(),  // 将Integer转换为Long
                            id -> 0
                    ));
        }

        // 2. 查询批改状态（只查询有批改的记录）
        List<Integer> submissionIds = submissions.stream()
                .map(AssignmentSubmission::getSubmissionId)
                .collect(Collectors.toList());

        List<AssignmentGrade> grades = gradeMapper.selectList(
                Wrappers.<AssignmentGrade>lambdaQuery()
                        .select(AssignmentGrade::getSubmissionId)
                        .in(AssignmentGrade::getSubmissionId, submissionIds)
        );

        // 3. 构建已批改的提交ID集合
        Set<Integer> gradedSubmissionIds = grades.stream()
                .map(AssignmentGrade::getSubmissionId)
                .collect(Collectors.toSet());

        // 4. 按assignment_class_id分组统计
        Map<Integer, Long> gradedCountMap = submissions.stream()
                .collect(Collectors.groupingBy(
                        AssignmentSubmission::getAssignmentClassId,
                        Collectors.filtering(
                                sub -> gradedSubmissionIds.contains(sub.getSubmissionId()),
                                Collectors.counting()
                        )
                ));

        // 5. 处理结果，确保所有输入的assignmentClassId都有对应值，并转换为Long键
        return assignmentClassIds.stream()
                .collect(Collectors.toMap(
                        id -> id.longValue(),  // 将Integer转换为Long
                        id -> gradedCountMap.getOrDefault(id, 0L).intValue()
                ));
    }

    @Override
    public GradeDetailVO getStudentGrade(Integer assignmentClassId, Integer studentId) {
        // 1. 查询学生提交记录
        AssignmentSubmission submission = assignmentSubmissionMapper.selectOne(
                Wrappers.<AssignmentSubmission>lambdaQuery()
                        .eq(AssignmentSubmission::getAssignmentClassId, assignmentClassId)
                        .eq(AssignmentSubmission::getStudentId, studentId)
        );

        if (submission == null) {
            return null; // 或者抛出自定义异常，如 throw new BusinessException("该学生未提交作业");
        }

        // 2. 查询对应批改记录
        AssignmentGrade grade = gradeMapper.selectOne(
                Wrappers.<AssignmentGrade>lambdaQuery()
                        .eq(AssignmentGrade::getSubmissionId, submission.getSubmissionId())
        );

        // 3. 查询学生信息
        Users student = usersMapper.selectOne(
                Wrappers.<Users>lambdaQuery()
                        .select(Users::getUserId, Users::getUsername)
                        .eq(Users::getUserId, studentId)
        );

        // 4. 组装GradeDetailVO对象
        GradeDetailVO vo = new GradeDetailVO();
        vo.setSubmissionId(submission.getSubmissionId());
        vo.setStudentId(studentId);
        vo.setStudentName(student != null ? student.getUsername() : "未知学生");

        if (grade != null) {
            vo.setGradeId(grade.getSubmissionId());
            vo.setScore(grade.getScore());
            vo.setFeedback(grade.getFeedback());
            vo.setGradeTime(grade.getGradeTime());

//            // 查询批改人信息（如果有批改人）
//            if (grade.getGraderId() != null) {
//                Users grader = usersMapper.selectOne(
//                        Wrappers.<Users>lambdaQuery()
//                                .select(Users::getUserId, Users::getUsername)
//                                .eq(Users::getUserId, grade.getGraderId())
//                );
//                vo.setGraderName(grader != null ? grader.getUsername() : "未知批改人");
//            }
        }

        return vo;
    }

    @Override
    @Transactional
    public int batchUpdateGrades(List<AssignmentGradeDTO> gradeDTOs) {
        if (CollectionUtils.isEmpty(gradeDTOs)) {
            return 0;
        }

        int successCount = 0;

        for (AssignmentGradeDTO dto : gradeDTOs) {
            try {
                // 1. 验证DTO基本有效性
                if (dto.getSubmissionId() == null || dto.getGraderId() == null) {
                    System.out.println("批量批改跳过无效记录: submissionId={}, graderId={}"+
                            dto.getSubmissionId()+"  "+dto.getGraderId());
                    continue;
                }

                // 2. 查询提交记录
                AssignmentSubmission submission = assignmentSubmissionMapper.selectById(dto.getSubmissionId());
                if (submission == null) {
                    System.out.println("提交记录不存在: submissionId={}"+dto.getSubmissionId());
                    continue;
                }

                // 3. 验证批改人权限
                Users grader = usersMapper.selectOne(
                        Wrappers.<Users>lambdaQuery()
                                .select(Users::getType)
                                .eq(Users::getUserId, dto.getGraderId()));

                if (grader == null || !Arrays.asList("teacher", "ta").contains(grader.getType())) {
                    System.out.println("批改人无权限: graderId={}"+dto.getGraderId());
                    continue;
                }

                // 4. 获取作业满分值
                AssignmentClasses assignmentClass = assignmentClassesMapper.selectById(submission.getAssignmentClassId());
                if (assignmentClass == null) {
                    System.out.println("作业班级关联不存在: assignmentClassId={}"+submission.getAssignmentClassId());
                    continue;
                }

                Assignment assignment = assignmentMapper.selectById(assignmentClass.getAssignmentId());
                if (assignment == null) {
                    System.out.println("作业不存在: assignmentId={}"+assignmentClass.getAssignmentId());
                    continue;
                }

                // 5. 验证分数有效性
                if (dto.getScore() != null && assignment.getTotalScore() != null
                        && dto.getScore().compareTo(assignment.getTotalScore()) > 0) {
                    System.out.println("分数超过满分: score={}"+
                            dto.getScore()+", totalScore={}"+assignment.getTotalScore());
                    continue;
                }

                // 6. 创建或更新批改记录
                AssignmentGrade grade = new AssignmentGrade();
                BeanUtils.copyProperties(dto, grade);
                grade.setGradeTime(new Date());

                if (gradeMapper.exists(
                        Wrappers.<AssignmentGrade>lambdaQuery()
                                .eq(AssignmentGrade::getSubmissionId, dto.getSubmissionId()))) {
                    // 更新现有记录
                    gradeMapper.update(grade,
                            Wrappers.<AssignmentGrade>lambdaQuery()
                                    .eq(AssignmentGrade::getSubmissionId, dto.getSubmissionId()));
                } else {
                    // 新增记录
                    gradeMapper.insert(grade);
                }

                successCount++;

            } catch (Exception e) {
                System.out.println("批量批改作业失败: submissionId={}"+dto.getSubmissionId());
                // 事务不会回滚，继续处理下一条记录
            }
        }

        return successCount;
    }
}