package org.example.teachercommon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * JWT令牌中的用户信息封装
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserInfo {
    private Integer userId;
    private String username;
    private String userType;
    private Date issuedAt;  // 令牌签发时间
    private Date expiresAt; // 令牌过期时间
}