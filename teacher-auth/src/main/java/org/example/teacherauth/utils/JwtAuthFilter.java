package org.example.teacherauth.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.teachercommon.entity.JwtUserInfo;
import org.example.teachercommon.utils.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT认证过滤器
 * 核心职责：
 * 1. 拦截需要认证的API请求
 * 2. 验证JWT令牌有效性
 * 3. 注入用户身份信息到请求上下文
 */
public class JwtAuthFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;

    // 白名单路径（无需认证的接口）
    private static final List<String> WHITELIST = List.of(
            "/auth/login",
            "/auth/register",  // 添加注册接口
            "/actuator/health"
    );

    /**
     * 构造函数注入JwtTokenProvider
     */
    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 核心过滤逻辑
     * @param request 客户端请求
     * @param response 服务端响应
     * @param chain 过滤器链
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        System.out.println("[DEBUG] 请求路径: " + path);
        // 白名单直接放行
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            System.out.println("[DEBUG] 匹配白名单，放行");  // 添加日志
            chain.doFilter(request, response);
            return;
        }

        // 1. 从Authorization头提取令牌（格式：Bearer <token>）
        String token = extractBearerToken(httpRequest);
        if (token == null) {
            sendUnauthorizedError(response, "Authorization头缺失或格式错误");
            return;
        }

        try {
            if(!jwtTokenProvider.validateToken(token)) {
                sendUnauthorizedError(response,"无效令牌");
                return;
            }
            // 2. 解析令牌获取完整用户信息
            JwtUserInfo userInfo = jwtTokenProvider.parseTokenToUserInfo(token);
            if(userInfo == null) {
                sendUnauthorizedError(response,"无效用户信息");
                return;
            }
            // 3. 将用户关键信息注入请求属性（供后续Controller使用）
            injectUserInfoToRequest(httpRequest, userInfo);

            // 4.将信息注入Security中
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userInfo.getUsername(), null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("将信息注入Security中");

            // 5. 放行到下一个过滤器/控制器
            chain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            sendUnauthorizedError(response, "令牌已过期，请重新登录");
        } catch (JwtException | IllegalArgumentException ex) {
            sendUnauthorizedError(response, "无效令牌: " + ex.getMessage());
        }
    }

    /**
     * 从请求头提取Bearer Token
     * @return 成功返回纯token，失败返回null
     */
    private String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            System.out.println("----------测试------------");
            System.out.println("token:"+header.substring(7));
            return header.substring(7); // 去除"Bearer "前缀
        }
        System.out.println("失败");
        return null;
    }

    /**
     * 将用户信息注入请求属性
     */
    private void injectUserInfoToRequest(HttpServletRequest request, JwtUserInfo userInfo) {
        request.setAttribute("userId", userInfo.getUserId());
        System.out.println("用户ID"+userInfo.getUserId());
        request.setAttribute("username", userInfo.getUsername());
        System.out.println("用户名"+userInfo.getUsername());
        request.setAttribute("userType", userInfo.getUserType());
        System.out.println("用户类型"+userInfo.getUserType());
        // 可根据需要添加更多属性
    }

    /**
     * 发送标准化的401未授权响应
     * @param message 可读的错误信息
     */
    private void sendUnauthorizedError(ServletResponse response, String message) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write(String.format(
                "{\"code\":\"UNAUTHORIZED\",\"message\":\"%s\"}",
                message
        ));
    }
}