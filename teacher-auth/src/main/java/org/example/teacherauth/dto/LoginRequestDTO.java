package org.example.teacherauth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String userId;
    private String username;
    private String password;
    
    // 获取有效的用户标识（优先使用userId）
    public String getEffectiveId() {
        return (userId != null && !userId.isEmpty()) ? userId : username;
    }
    
    // userId 的 getter 和 setter
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    // username 的 getter 和 setter
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    // password 的 getter 和 setter
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
