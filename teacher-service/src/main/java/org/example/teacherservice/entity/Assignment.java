package org.example.teacherservice.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 作业基本信息实体
 * - 包含全局作业信息
 * - 与具体班级关联的设置在AssignmentClasses中
 */
@Data
@TableName("assignments")
public class Assignment {
    @TableId(value = "assignment_id", type = IdType.AUTO)
    private Integer assignmentId;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    @TableField("deadline")
    private Date deadline;  // 全局截止时间（可选）

    @TableField("total_score")
    private BigDecimal totalScore;

    @TableField("resource_id")
    private Integer resourceId;

    // 新增字段
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Integer creatorId;  // 发布人ID

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;  // 发布时间

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;  // 更新时间
}
