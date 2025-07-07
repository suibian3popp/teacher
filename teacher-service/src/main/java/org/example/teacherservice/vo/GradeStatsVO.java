package org.example.teacherservice.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 批改统计视图对象
 */
@Data
public class GradeStatsVO {
    private Integer totalSubmissions;  // 总提交数
    private Integer gradedCount;      // 已批改数
    private BigDecimal avgScore;      // 平均分
    private BigDecimal maxScore;      // 最高分
    private BigDecimal minScore;      // 最低分
}
