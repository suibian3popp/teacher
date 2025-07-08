package org.example.teacherservice.vo.assignment;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GradeDetailVO {
    private Long gradeId;
    private Long submissionId;
    private Long studentId;
    private Long assignmentClassId;
    private BigDecimal score;
    private String feedback;
    private LocalDateTime gradeTime;
    private LocalDateTime submitTime;
    private String graderName;
    private String graderType; // 新增批改人类型字段

}
