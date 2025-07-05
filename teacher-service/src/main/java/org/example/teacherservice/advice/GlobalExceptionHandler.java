package org.example.teacherservice.advice;

import org.example.teacherservice.exception.BusinessException;
import org.example.teacherservice.response.CommonResponse;
import org.example.teacherservice.response.ResponseCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public CommonResponse<Void> handleBusinessException(BusinessException ex) {
        return CommonResponse.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<Void> handleValidException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();
        return CommonResponse.error(
                ResponseCode.PARAM_ERROR.getCode(),
                String.format("参数校验失败: %s", errorMsg)
        );
    }

    /**
     * 兜底异常处理（生产环境应记录日志）
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse<Void> handleException(Exception ex) {
        return CommonResponse.error(
                ResponseCode.SYSTEM_ERROR.getCode(),
                "系统繁忙，请稍后重试"
        );
    }
}