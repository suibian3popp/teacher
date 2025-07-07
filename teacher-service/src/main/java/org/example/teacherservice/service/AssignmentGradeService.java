package org.example.teacherservice.service;

import org.example.teacherservice.dto.assignment.AssignmentGradeDTO;
import org.example.teacherservice.vo.GradeDetailVO;
import org.example.teacherservice.vo.GradeStatsVO;
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
     * 获取作业成绩统计概览
     * @param assignmentId 作业ID
     * @return 包含平均分、最高分、最低分等统计数据的视图对象
     */
    GradeStatsVO getAssignmentStats(Integer assignmentId);

    /**
     * 获取班级批改进度
     * @param assignmentId 作业ID
     * @param classId 班级ID
     * @return 包含已批改和未批改数量的进度视图
     */
    GradingProgressVO getGradingProgress(Integer assignmentId, Integer classId);

    /**
     * 获取作业的所有批改记录
     * @param assignmentId 作业ID
     * @return 按分数降序排列的批改记录列表
     */
    List<GradeDetailVO> listAssignmentGrades(Integer assignmentId);

    /**
     * 批量获取作业的已批改数量
     * @param assignmentIds 作业ID列表
     * @return 作业ID->已批改数量的映射
     */
    Map<Integer, Integer> countGradedAssignments(List<Integer> assignmentIds);
}