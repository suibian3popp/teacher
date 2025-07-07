package org.example.teacherservice.service;

import org.example.teacherservice.entity.ExamGrade;
import org.example.teacherservice.vo.ExamGradeStatsVO;

/**
 * 试卷批改服务
 * 负责试卷评分及相关统计
 */
public interface ExamGradeService {

    /**
     * 批改试卷
     * @param examGrade 批改信息（包含分数、评语等）
     */
    void gradeExam(ExamGrade examGrade);

    /**
     * 更新批改结果
     * @param examGrade 更新的批改信息
     */
    void updateGrade(ExamGrade examGrade);

    /**
     * 获取某次考试的成绩统计
     * @param examId 试卷ID
     * @return 成绩统计信息
     */
    ExamGradeStatsVO getExamStats(Integer examId);

    /**
     * 获取班级考试成绩分布
     * @param examId 试卷ID
     * @param classId 班级ID
     * @return 成绩分布信息
     */
    GradeDistributionVO getClassGradeDistribution(Integer examId, Integer classId);

    /**
     * 获取学生的考试成绩
     * @param submissionId 提交ID
     * @return 成绩详情
     */
    ExamGrade getExamGrade(Integer submissionId);
}

