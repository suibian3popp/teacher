package org.example.teacherservice.vo;

import lombok.Data;

/**
 * 简化版班级信息视图对象
 * - classId: 班级ID（示例值：101）
 * - className: 班级名称（示例值："三年级二班"）
 * - studentCount: 班级人数（示例值：45）
 */
@Data
public class ClassSimpleVO {
    private Integer classId;
    private String className;
    private Integer studentCount;
}