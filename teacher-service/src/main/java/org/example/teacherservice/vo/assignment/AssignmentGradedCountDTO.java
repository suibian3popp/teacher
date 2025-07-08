package org.example.teacherservice.vo.assignment;

import lombok.Data;

@Data
public class AssignmentGradedCountDTO {
    private Long assignmentClassId;
    private Integer gradedCount;
}