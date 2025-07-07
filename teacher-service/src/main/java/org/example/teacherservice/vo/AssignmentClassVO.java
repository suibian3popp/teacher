package org.example.teacherservice.vo;

import lombok.Data;

/**
 * 作业-班级关联视图对象
 * - assignmentId: 作业ID（示例值：1）
 * - className: 班级名称（示例值："三年级二班"）
 * - studentCount: 班级学生人数（示例值：45）
 */
@Data
public class AssignmentClassVO {
    private Integer assignmentId;
    private String className;
    private Integer studentCount;
}