package org.example.teacherservice.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 考试成绩统计视图对象
 */
@Data
public class ExamGradeStatsVO {
    private BigDecimal averageScore;
    private BigDecimal highestScore;
    private BigDecimal lowestScore;
    private Integer totalSubmissions;
    // getters and setters
}
