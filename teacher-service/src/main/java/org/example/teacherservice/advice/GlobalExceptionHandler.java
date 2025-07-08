package org.example.teacherservice.advice;

import lombok.extern.slf4j.Slf4j;
import org.example.teacherservice.exception.BusinessException;
import org.example.teacherservice.response.CommonResponse;
import org.example.teacherservice.response.ResponseCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public CommonResponse<Void> handleBusinessException(BusinessException ex) {
        log.warn("业务异常 - code: {}, message: {}", ex.getCode(), ex.getMessage());
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
        String formattedMsg = String.format("参数校验失败: %s", errorMsg);
        log.warn(formattedMsg);
        return CommonResponse.error(
                ResponseCode.PARAM_ERROR.getCode(),
                formattedMsg
        );
    }

    /**
     * 兜底异常处理（生产环境应记录日志）
     */
//    @ExceptionHandler(Exception.class)
//    public CommonResponse<Void> handleException(Exception ex) {
//        log.error("系统未知异常", ex);
//        return CommonResponse.error(
//                ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
//                "系统繁忙，请稍后重试"
//        );
//    }
}