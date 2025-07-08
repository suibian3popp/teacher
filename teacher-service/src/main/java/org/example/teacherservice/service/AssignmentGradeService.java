package org.example.teacherservice.service;

import org.example.teacherservice.dto.assignment.AssignmentGradeDTO;
import org.example.teacherservice.vo.GradeDetailVO;
import org.example.teacherservice.vo.assignment.GradeStatsVO;
import org.example.teacherservice.vo.GradingProgressVO;
import org.example.teacherservice.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * 作业评分与批改服务
 * 提供作业批改、成绩统计和进度跟踪功能
 */
public interface AssignmentGradeService {
    /**
     * 批改或更新作业成绩
     * @param dto 包含提交ID、评分人、分数和评语
     * @throws BusinessException 当提交记录不存在或分数超过作业满分时抛出
     */
    void gradeAssignment(AssignmentGradeDTO dto);

    /**
     * 删除批改记录
     * @param gradeId 批改ID
     */
    void deleteGrade(Integer gradeId);

    /**
     * 获取作业成绩统计概览
     * @param assignmentClassId 作业-班级关联ID
     * @return 包含平均分、最高分、最低分等统计数据的视图对象
     */
    GradeStatsVO getAssignmentStats(Integer assignmentClassId);

    /**
     * 获取班级批改进度
     * @param assignmentClassId 作业-班级关联ID
     * @return 包含已批改和未批改数量的进度视图
     */
    GradingProgressVO getGradingProgress(Integer assignmentClassId);

    /**
     * 获取作业的所有批改记录
     * @param assignmentClassId 作业-班级关联ID
     * @return 按分数降序排列的批改记录列表
     */
    List<GradeDetailVO> listAssignmentGrades(Integer assignmentClassId);

    /**
     * 批量获取作业的已批改数量
     * @param assignmentClassIds 作业-班级关联ID列表
     * @return 作业-班级关联ID->已批改数量的映射
     */
    Map<Long, Integer> countGradedAssignments(List<Integer> assignmentClassIds);

    /**
     * 获取学生某次作业的成绩
     * @param assignmentClassId 作业-班级关联ID
     * @param studentId 学生ID
     * @return 成绩详情（可能为null表示未批改）
     */
    GradeDetailVO getStudentGrade(Integer assignmentClassId, Integer studentId);

    /**
     * 批量更新作业成绩
     * @param gradeDTOs 批改信息列表
     * @return 成功更新的数量
     */
    int batchUpdateGrades(List<AssignmentGradeDTO> gradeDTOs);
}