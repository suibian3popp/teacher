package org.example.teacherservice.vo.assignment;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 作业搜索结果
 */
@Data
public class AssignmentSearchResult {
    private Integer assignmentId;
    private String title;
    private String creatorName;
    private Integer status; // 1-未开始 2-进行中 3-已截止
    private LocalDateTime deadline;
}