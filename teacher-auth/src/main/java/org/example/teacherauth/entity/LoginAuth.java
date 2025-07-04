package org.example.teacherauth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("login_auth")
public class LoginAuth {
    @TableId(value = "auth_id", type = IdType.AUTO)
    private Long authId;
    private Integer userId;
    private String password;
    private String passwordHash;
    private Date lastLogin;
}
