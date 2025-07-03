package org.demo_hd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有接口生效
                .allowedOrigins("*") // 允许所有前端来源，生产可限定具体域名
                .allowedMethods("*") // 允许所有 HTTP 方法
                .allowedHeaders("*") // 允许所有请求头
                .maxAge(3600L); // 预检缓存时间
    }
}
