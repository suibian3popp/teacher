package org.example.teacherservice.vo;

import lombok.Data;

/**
 * 作业统计视图对象
 * - assignmentId: 作业ID（示例值：1）
 * - count: 统计数量（示例值：30）
 *   - 当type=submit时表示提交数
 *   - 当type=grade时表示批改数
 * - type: 统计类型（示例值："submit"）
 *   - 可选值: submit（提交数）, grade（批改数）
 */
@Data
public class AssignmentStatsVO {
    private Integer assignmentId;
    private Integer count;
    private String type;
}