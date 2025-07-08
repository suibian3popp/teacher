package org.example.teacherservice.response;

import lombok.Getter;

/**
 * 统一响应状态码（HTTP语义+业务扩展）
 * 规范：
 * - 2xx: 成功
 * - 4xx: 客户端错误
 * - 5xx: 服务端错误
 * - 6xxx: 扩展业务错误（按模块划分）
 */
@Getter
public enum ResponseCode {
    // ========== HTTP 标准状态码 ==========
    SUCCESS(200, "成功"),
    CREATED(201, "资源创建成功"),
    NO_CONTENT(204, "无内容"),

    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "资源冲突"),
    // 在 ResponseCode 枚举中添加
    PARAM_ERROR(4001, "参数错误"),
    SYSTEM_ERROR(5001, "系统错误"),
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // ========== 业务扩展状态码（6xxx） ==========
    // 用户模块 6000-6099
    BUSINESS_ERROR(6000, "业务处理异常"),
    USER_NOT_FOUND(6001, "用户不存在"),
    LOGIN_FAILED(6002, "登录失败"),

    // 作业模块 6100-6199
    ASSIGNMENT_NOT_FOUND(6101, "作业不存在"),
    SUBMISSION_EXPIRED(6102, "超过提交截止时间"),
    INVALID_GRADE(6103, "无效的评分值"),

    // 课程/章节模块
    COURSE_NOT_FOUND(6200, "课程不存在"),
    CHAPTER_HAS_CHILDREN(6201, "章节下存在子章节，无法删除"),
    PARAM_IS_INVALID(6202, "参数无效");

    private final int code;
    private final String description;


    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
