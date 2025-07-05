package org.example.teachergateway.utils;

import org.example.teachercommon.entity.JwtUserInfo;
import org.example.teachercommon.utils.JwtTokenProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Order(-1) // 最高优先级
public class JwtAuthFilter implements GlobalFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 通过构造函数注入
    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 白名单
     * 用户登录和注册
     */
    private final List<String> WHITELIST = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/actuator/health"
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();

        // 1. 白名单放行
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            System.out.println("白名单放行"+path);
            return chain.filter(exchange);
        }

        // 2. 提取并验证JWT
        String token = extractToken(exchange.getRequest());
        if (!jwtTokenProvider.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 3. 注入用户信息到下游服务
        JwtUserInfo userInfo = jwtTokenProvider.parseTokenToUserInfo(token);
        exchange.getRequest().mutate()
                .header("X-User-Id", String.valueOf(userInfo.getUserId())) // 转换为String
                .header("X-User-Roles", userInfo.getUserType())
                .build();

        return chain.filter(exchange);
    }

    // 添加提取Token的方法
    private String extractToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst("Authorization");
        return (header != null && header.startsWith("Bearer ")) ?
                header.substring(7) : null;
    }
}
