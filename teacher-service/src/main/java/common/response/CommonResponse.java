package common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.io.Serializable;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> implements Serializable {
    private final int code;
    private final String msg;
    private final T data;

    // 私有构造器
    private CommonResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功响应（简化版）
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(
                ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getDescription(),
                data
        );
    }

    // 失败响应
    public static <T> CommonResponse<T> error(ResponseCode code) {
        return new CommonResponse<>(
                code.getCode(),
                code.getDescription(),
                null
        );
    }

    // 自定义错误消息
    public static <T> CommonResponse<T> error(int code, String msg) {
        return new CommonResponse<>(code, msg, null);
    }
}