package org.example.teacherservice.vo;

import lombok.Data;

/**
 * 分数分布统计项
 */
@Data
public class ScoreDistribution {
    //分数区间
    private String scoreRange;

    //人数
    private Integer count;

    //占比百分比
    private Double percentage;
}