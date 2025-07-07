package org.example.teacherservice.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 作业提交记录视图对象
 */
@Data
public class SubmissionVO {
    private Integer submissionId;
    private Integer assignmentId;
    private String assignmentTitle;
    private Integer studentId;
    private String studentName;
    private Date submitTime;
    private Integer resourceId;
    private BigDecimal score;      // 可能为null（未批改）
    private String feedback;
    private Boolean isGraded;      // 是否已批改
}