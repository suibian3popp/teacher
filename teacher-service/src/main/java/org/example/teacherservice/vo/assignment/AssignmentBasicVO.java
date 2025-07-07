package org.example.teacherservice.vo.assignment;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 作业基础信息视图
 */
@Data
public class AssignmentBasicVO {
    private Integer assignmentId;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private BigDecimal totalScore;
    private Integer resourceId;
    private LocalDateTime createTime;
}
