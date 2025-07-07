package org.example.teacherservice.vo.assignment;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 作业资源关联视图
 */
@Data
public class AssignmentResourceVO {
    private Integer assignmentId;
    private String title;
    private LocalDateTime deadline;
    private String resourceUrl;
}

