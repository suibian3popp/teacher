package org.example.teacherauth.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * 密码加密
 */
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("------------配置服务------------");
        System.out.println("------------------------------------");
        http
                .csrf(csrf -> csrf.disable())  // 禁用 CSRF 保护
                .cors(cors -> cors.disable())  // 禁用 CORS 配置(使用自定义配置)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // 完全放行auth路径下的所有请求
                        .anyRequest().authenticated() // 其他请求需要认证
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
