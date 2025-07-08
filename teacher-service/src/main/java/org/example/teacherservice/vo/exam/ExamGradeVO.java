package org.example.teacherservice.vo.exam;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试批改信息视图对象
 */
@Data
public class ExamGradeVO {
    private Integer gradeId;
    private Integer graderId;
    private String graderName; // 需要关联users表
    private Double score;
    private String feedback;
    private LocalDateTime gradeTime;
}
