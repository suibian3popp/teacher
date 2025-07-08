package org.example.teacherservice.vo.assignment;

import lombok.Data;

/**
 * 批改统计视图对象
 */
@Data
public class GradeStatsVO {
    private double averageScore;
    private double maxScore;
    private double minScore;
    private long gradedCount;
    private long totalCount;

    public GradeStatsVO(double avg, double max, double min, long graded, long total) {
        this.averageScore = avg;
        this.maxScore = max;
        this.minScore = min;
        this.gradedCount = graded;
        this.totalCount = total;
    }
}