package org.demo_hd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 标记为配置类，让 Spring 扫描加载
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 对所有路径生效（可按需调整，如 /api/**）
                .allowedOrigins("http://localhost:8081") // 明确指定前端域名
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true) // 允许携带凭证
                .maxAge(3600);
    }
}
