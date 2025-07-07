package org.example.teacherservice.vo;

import lombok.Data;

/**
 * 批改进度视图对象
 */
@Data
public class GradingProgressVO {
    private Integer classId;
    private String className;
    private Integer totalStudents;
    private Integer submittedCount;
    private Integer gradedCount;
}
