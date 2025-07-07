package org.example.teacherservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("student_class")
public class StudentClass {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("student_id")
    private Integer studentId;

    @TableField("class_id")
    private Integer classId;

    @TableField("course_id")
    private Integer courseId;
}