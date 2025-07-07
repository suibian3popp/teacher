package org.example.teacherservice.vo;

import lombok.Data;

import java.time.LocalDateTime;

// 班级作业详情VO
@Data
public class ClassAssignmentDetailVO {
    private Integer assignmentId;
    private String title;
    private LocalDateTime classDeadline;
    private Integer submittedCount;
    private Integer totalStudents;
}
