package org.example.teacherservice.vo.assignment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssignmentGradeDTO {
    private Long submissionId;
    private Long graderId;
    private BigDecimal score;
    private String feedback;
}
