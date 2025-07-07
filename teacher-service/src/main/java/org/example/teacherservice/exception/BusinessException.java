package org.example.teacherservice.exception;

import org.example.teacherservice.response.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    // 构造方法1：直接传入错误码和消息
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    // 构造方法2：通过ResponseCode枚举构造
    public BusinessException(ResponseCode code) {
        this(code.getCode(), code.getDescription());
    }

    // 构造方法3：仅传入消息（使用默认业务错误码）
    public BusinessException(String message) {
        this(ResponseCode.BUSINESS_ERROR.getCode(), message);
    }
}