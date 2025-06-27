package common.exception;


import common.response.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResponseCode code) {
        this(code.getCode(), code.getDescription());
    }
}