package org.example.teacherservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 作业-班级关联实体
 * - 记录作业与班级的关联关系
 * - 包含班级维度的特殊设置
 */
@Data
@TableName("assignment_classes")
public class AssignmentClasses {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("assignment_id")
    private Integer assignmentId;

    @TableField("class_id")
    private Integer classId;

    // 新增字段
    @TableField("deadline")
    private Date classDeadline;  // 班级专属截止时间

    @TableField("publish_status")
    private Integer publishStatus;  // 0-未发布 1-已发布

    @TableField("publish_time")
    private Date publishTime;  // 班级发布时间
}