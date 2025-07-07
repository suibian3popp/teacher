package org.example.teacherservice.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批改详情视图对象
 */
@Data
public class GradeDetailVO {
    private Integer gradeId;
    private Integer submissionId;
    private Integer studentId;
    private String studentName;
    private BigDecimal score;
    private String feedback;
    private Date gradeTime;
}