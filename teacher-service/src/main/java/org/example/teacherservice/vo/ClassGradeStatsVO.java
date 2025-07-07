package org.example.teacherservice.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// 成绩统计VO
@Data
public class ClassGradeStatsVO {
    private BigDecimal averageScore;
    private BigDecimal maxScore;
    private BigDecimal minScore;
    private List<ScoreDistribution> distribution;
}