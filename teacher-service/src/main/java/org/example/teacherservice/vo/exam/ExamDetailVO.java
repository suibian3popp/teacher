package org.example.teacherservice.vo.exam;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷详情视图对象
 */
@Data
public class ExamDetailVO {
    private Integer examId;
    private String title;
    private String description;
    private Double totalScore;
    private Integer timeLimit;
    private Integer resourceId;
    private String resourceUrl; // 需要关联resources表
    private Integer creatorId;
    private String creatorName;
    private String creatorEmail; // 来自users表
    private LocalDateTime createTime;
    private List<ExamClassVO> assignedClasses; // 关联班级信息
    private ExamStatisticsVO statistics; // 考试统计信息
}