package org.example.teacherservice.vo.exam;

import lombok.Data;

import java.util.Map;

/**
 * 试卷统计信息视图对象
 */
@Data
public class ExamStatisticsVO {
    private Integer totalStudents; // 总应考人数
    private Integer submittedCount; // 已提交人数
    private Integer gradedCount; // 已批改人数
    private Double averageScore; // 平均分
    private Double maxScore; // 最高分
    private Double minScore; // 最低分
    private Map<String, Integer> scoreDistribution; // 分数段分布
    private Double submissionRate; // 提交率
}
