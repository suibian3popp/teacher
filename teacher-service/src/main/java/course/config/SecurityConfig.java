package course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭csrf
                .csrf(csrf -> csrf.disable())
                // 配置认证路径
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 允许所有请求
                );
        return http.build();
    }
}

