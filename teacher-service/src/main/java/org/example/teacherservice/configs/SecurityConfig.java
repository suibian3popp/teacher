package org.example.teacherservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("------------配置服务------------");
        System.out.println("------------------------------------");
        http
                .csrf(csrf -> csrf.disable())  // 如果是 API 服务，可以禁用 CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/service/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/service/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/service/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/service/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/service/**").permitAll()
                        .requestMatchers(HttpMethod.HEAD, "/service/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/service/**").permitAll()
                        .anyRequest().authenticated() // 其他请求需要认证
                );
        return http.build();
    }
}
