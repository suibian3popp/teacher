package org.example.teacherauth.dto;

import lombok.Data;
import org.example.teachercommon.entity.UserType;

@Data
public class LoginResponseDTO {
    private String accessToken;       // JWT令牌
    private String tokenType = "Bearer";  // 默认值
    private Integer userId;          // 用户ID
    private String username;        // 用户名
    private UserType userType;      // 枚举类型
    private String realName;        // 真实姓名
    private String department;      // 可选字段

    // 全参数构造函数（匹配您的代码调用方式）
    public LoginResponseDTO(
            String accessToken,
            Integer userId,
            String username,
            UserType userType,
            String realName
    ) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.username = username;
        this.userType = userType;
        this.realName = realName;
        this.tokenType = "Bearer"; // 设置默认值
    }

    // 可选：如果需要department字段
    public LoginResponseDTO(
            String accessToken,
            Integer userId,
            String username,
            UserType userType,
            String realName,
            String department
    ) {
        this(accessToken, userId, username, userType, realName);
        this.department = department;
    }
}