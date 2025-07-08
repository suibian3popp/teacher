package org.example.teacherservice.vo.exam;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷搜索结果视图对象
 */
@Data
public class ExamSearchResult {
    private Integer examId;
    private String title;
    private Double totalScore;
    private Integer timeLimit;
    private Integer creatorId;
    private String creatorName;
    private LocalDateTime createTime;
    private Integer publishStatus; // 0未发布/1已发布
    private Integer classCount; // 关联班级数量
    private LocalDateTime nearestExamTime; // 最近的考试时间
}