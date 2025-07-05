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
    @NotNull(message = "用户账号不能为空")
    private Integer userId;

    @NotBlank(message = "密码不能为空")
    private String password;
}
