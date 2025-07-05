package org.example.teacherservice.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.teachercommon.entity.JwtUserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 用户信息拦截器（用于Service模块）
 */
@Component
public class UserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        // 1. 从网关注入的请求头获取用户信息
        String userId = request.getHeader("X-User-Id");
        String userRole = request.getHeader("X-User-Roles");

        // 2. 验证必要字段（网关已校验，此处做防御性检查）
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("缺失用户身份标识");
        }

        // 3. 构建用户对象并存入上下文
// 拦截器中修改为：
        // 3. 构建用户对象（使用Builder模式）
        JwtUserInfo userInfo = JwtUserInfo.builder()
                .userId(Integer.parseInt(userId))
                .userType(userRole)
                .build();
        UserContext.set(userInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        // 请求完成后清理上下文
        UserContext.clear();// 必须清理，否则内存泄漏
    }
}