package org.example.teacherauth.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("departments")
public class Department {

    @TableId(value = "department_id", type = IdType.AUTO)
    private Integer departmentId;

    @TableField("name")
    private String name;

    @TableField("phone")
    private String phone;

    @TableField("description")
    private String description;

    @TableField("admin_id")
    private Integer adminId;

    @TableField("created_at")
    private LocalDateTime createdAt;
}