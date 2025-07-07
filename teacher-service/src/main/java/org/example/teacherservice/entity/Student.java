package org.example.teacherservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("students")
public class Student {
    @TableId(value = "student_id", type = IdType.AUTO)
    private Integer studentId;

    @TableField("student_no")
    private String studentNo;

    @TableField("real_name")
    private String realName;

    @TableField("department_id")
    private Integer departmentId;
}