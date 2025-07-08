package org.example.teacherservice.vo.assignment;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 作业资源关联视图
 */
@Data
public class AssignmentResourceVO {
    private Integer assignmentId;
    private String title;
    private String description;
    private BigDecimal totalScore;
    private Integer creatorId;
    private Integer resourceId;
}

