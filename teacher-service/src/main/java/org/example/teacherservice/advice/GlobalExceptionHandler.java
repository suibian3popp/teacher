package org.example.teacherservice.advice;

import org.example.teacherservice.exception.BusinessException;
import org.example.teacherservice.response.CommonResponse;
import org.example.teacherservice.response.ResponseCode;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.PrintWriter;
import java.io.StringWriter;

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
     * 兜底异常处理（返回详细异常信息方便调试）
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse<Void> handleException(Exception ex) {
        // 打印异常栈到控制台
        ex.printStackTrace();
        
        // 获取完整异常栈信息
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();
        
        // 构建详细错误消息
        String detailedMessage = String.format(
            "异常类型: %s, 异常消息: %s, 位置: %s",
            ex.getClass().getName(),
            ex.getMessage(),
            getFirstCauseLocation(stackTrace)
        );
        
        System.err.println("捕获到未处理异常: " + detailedMessage);
        
        return CommonResponse.error(
                ResponseCode.SYSTEM_ERROR.getCode(),
                detailedMessage // 返回详细异常信息，方便调试
        );
    }
    
    /**
     * 获取异常栈中的第一个位置信息
     */
    private String getFirstCauseLocation(String stackTrace) {
        String[] lines = stackTrace.split("\n");
        // 尝试找到第一个包含项目代码的行
        for (String line : lines) {
            if (line.contains("org.example.teacherservice")) {
                return line.trim();
            }
        }
        // 如果没找到，返回第一行异常位置
        for (String line : lines) {
            if (line.contains("at ")) {
                return line.trim();
            }
        }
        return "未找到位置信息";
    }
}