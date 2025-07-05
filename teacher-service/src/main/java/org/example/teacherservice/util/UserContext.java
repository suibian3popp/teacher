package org.example.teacherservice.util;

import org.example.teachercommon.entity.JwtUserInfo;

/**
 * 用户上下文工具类（线程安全）
 */
public class UserContext {
    private static final ThreadLocal<JwtUserInfo> holder = new ThreadLocal<>();

    // 存入用户信息
    public static void set(JwtUserInfo user) {
        if (user == null) {
            clear();
        } else {
            holder.set(user);
        }
    }

    // 获取当前用户
    public static JwtUserInfo get() {
        JwtUserInfo user = holder.get();
        if (user == null) {
            throw new IllegalStateException("未找到用户上下文，请检查是否已添加UserInfoInterceptor");
        }
        return user;
    }

    // 清除上下文（防止内存泄漏）
    public static void clear() {
        holder.remove();
    }
}
