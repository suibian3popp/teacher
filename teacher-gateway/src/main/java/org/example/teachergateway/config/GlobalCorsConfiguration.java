package org.example.teachergateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;

/**
 * 全局CORS配置
 * 使用最高优先级确保只有这一个CORS配置生效
 */
@Configuration
@RefreshScope
public class GlobalCorsConfiguration {

    @Value("${cors.allowed-origins:http://localhost:5173,http://localhost:3000}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Bean
    @Primary
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 解析允许的源站
        String[] origins = allowedOrigins.split(",");
        for (String origin : origins) {
            config.addAllowedOrigin(origin.trim());
        }
        
        // 解析允许的HTTP方法
        String[] methods = allowedMethods.split(",");
        for (String method : methods) {
            config.addAllowedMethod(method.trim());
        }
        
        // 允许所有请求头
        config.addAllowedHeader("*");
        
        // 设置是否允许携带凭证
        config.setAllowCredentials(allowCredentials);
        
        // 设置预检请求的有效期
        config.setMaxAge(maxAge);
        
        // 创建URL匹配器
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
    
    /**
     * 添加一个专门处理OPTIONS预检请求的过滤器
     * 确保所有OPTIONS请求都能正确响应，不被其他过滤器拦截
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebFilter optionsFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // 如果是OPTIONS请求，直接返回200 OK
            if (request.getMethod() == HttpMethod.OPTIONS) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.OK);
                
                // 设置CORS响应头
                HttpHeaders headers = response.getHeaders();
                
                // 获取请求的Origin
                String origin = request.getHeaders().getOrigin();
                if (origin != null) {
                    headers.add("Access-Control-Allow-Origin", origin);
                }
                
                headers.add("Access-Control-Allow-Methods", allowedMethods);
                headers.add("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
                headers.add("Access-Control-Allow-Credentials", String.valueOf(allowCredentials));
                headers.add("Access-Control-Max-Age", String.valueOf(maxAge));
                
                return Mono.empty();
            }
            
            return chain.filter(exchange);
        };
    }
} 