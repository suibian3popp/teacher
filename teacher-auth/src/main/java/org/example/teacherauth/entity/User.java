package org.example.teacherauth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import org.example.teachercommon.entity.UserType;

@Data
@TableName("users")
public class User {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String username;
    private String phone;

    @TableField("type")
    private UserType userType; // 改为枚举类型

    // 可选：添加枚举与数据库值的转换器（若MyBatis-Plus无法自动处理）
    @JsonValue // 可选：序列化时输出枚举的value值
    public String getUserTypeValue() {
        return userType != null ? userType.getValue() : null;
    }

    private Integer departmentId;
    private String realName;
    private String email;

    @TableField(exist = false)
    private String departmentName; // 非数据库字段，用于显示
}