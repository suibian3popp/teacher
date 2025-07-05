package org.example.teacherservice.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("assignments")
public class Assignment {
    @TableId(value = "assignment_id", type = IdType.AUTO)
    private Integer assignmentId;

    @TableField("chapter_id")
    private Integer chapterId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("deadline")
    private Date deadline;

    @TableField("total_score")
    private BigDecimal totalScore;

    @TableField("resource_id")
    private Integer resourceId;
}
