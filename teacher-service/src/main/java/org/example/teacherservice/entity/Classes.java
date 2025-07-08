package org.example.teacherservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("classes")
public class Classes {
    @TableId(value = "class_id", type = IdType.AUTO)
    private Integer classId;

    @TableField("course_id")
    private Integer courseId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("student_count")
    private Integer studentCount;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;
}