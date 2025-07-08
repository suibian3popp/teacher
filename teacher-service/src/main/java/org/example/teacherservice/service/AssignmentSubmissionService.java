package org.example.teacherservice.service;


import org.example.teacherservice.dto.assignment.AssignmentSubmitDTO;
import org.example.teacherservice.vo.SubmissionVO;
import org.example.teacherservice.exception.BusinessException;


import java.util.List;
import java.util.Map;


/**
 * 学生作业提交管理服务
 * 处理作业提交、提交记录查询和提交状态统计
 */
public interface AssignmentSubmissionService {
    /**
     * 提交作业
     *
     * @param dto 包含作业ID、学生ID和资源ID
     * @throws BusinessException 当超过截止时间、重复提交或作业不存在时抛出
     */
    void submitAssignment(AssignmentSubmitDTO dto);

    /**
     * 获取学生单次作业提交记录
     *
     * @param assignmentClassId 作业ID
     * @param studentId    学生ID
     * @return 包含提交详情和批改状态的视图对象
     */
    SubmissionVO getStudentSubmission(Integer assignmentClassId, Integer studentId);

    /**
     * 获取学生的所有提交记录
     *
     * @param studentId 学生ID
     * @return 按提交时间倒序排列的提交记录列表
     */
    List<SubmissionVO> listStudentSubmissions(Integer studentId);

    /**
     * 批量获取作业的提交数量
     *
     * @param assignmentClassIds 作业ID列表
     * @return 作业ID->提交数量的映射
     */
    Map<Integer, Integer> countSubmissionsByAssignments(List<Integer> assignmentClassIds);

    /**
     * 检查学生是否已提交某作业
     *
     * @param assignmentClassId 作业ID
     * @param studentId    学生ID
     * @return 已提交返回true，否则false
     */
    boolean hasStudentSubmitted(Integer assignmentClassId, Integer studentId);


    /**
     * 新增方法：根据作业ID查询所有提交的学生ID列表
     *
     * @param assignmentClassId 作业-班级关联ID
     * @return 提交了该作业的学生ID列表
     */
    List<Integer> listSubmittedStudents(Integer assignmentClassId);

    /**
     * 新增方法：根据作业ID查询所有提交记录
     *
     * @param assignmentClassId 作业-班级关联ID
     * @return 该作业的所有提交记录
     */
    List<SubmissionVO> listSubmissionsByAssignment(Integer assignmentClassId);
}