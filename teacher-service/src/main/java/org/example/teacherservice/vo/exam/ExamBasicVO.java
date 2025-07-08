package org.example.teacherservice.vo.exam;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷基础信息视图对象
 */
@Data
public class ExamBasicVO {
    private Integer examId;
    private String title;
    private String description;
    private Double totalScore;
    private Integer timeLimit; // 考试时长(分钟)
    private Integer resourceId; // 试卷文件资源ID
    private Integer creatorId;
    private String creatorName; // 需要关联查询
    private LocalDateTime createTime;
    private Integer publishStatus; // 0未发布/1已发布 (需要聚合exam_classes表)
    private Integer assignedClassCount; // 关联班级数量
}