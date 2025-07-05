package org.demo_hd.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户基本信息表
 * @TableName users
 */
@TableName(value ="users")
@Data
public class Users {
    /**
     * 用户ID（登录用）
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 用户类型
     */
    @TableField(value = "type")
    private Object type;

    /**
     * 所属院系ID
     */
    @TableField(value = "department_id")
    private Integer departmentId;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;
}