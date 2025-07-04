package org.example.teacherservice.common.response;

import lombok.Getter;

@Getter
public enum ResponseCode {
    // ========== 成功状态码 ==========
    SUCCESS(200, "成功"),

    // ========== 客户端错误 ==========
    PARAM_ERROR(400, "参数错误"),          // 新增参数错误码
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    // ========== 服务器错误 ==========
    SYSTEM_ERROR(500, "系统错误"),         // 新增系统错误码
    SERVICE_UNAVAILABLE(503, "服务不可用");

    private final int code;
    private final String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}