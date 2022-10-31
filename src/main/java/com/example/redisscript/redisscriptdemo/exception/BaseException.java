package com.example.redisscript.redisscriptdemo.exception;

import com.example.redisscript.redisscriptdemo.utils.IResponseEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private String code;
    private String message;

    private IResponseEnum responseEnum;

    public BaseException(IResponseEnum responseEnum ) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
        this.responseEnum =responseEnum;
    }

    public BaseException(IResponseEnum responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
        this.message = message;
        this.responseEnum =responseEnum;
    }

    public BaseException(IResponseEnum responseEnum, String message, Throwable cause) {
        super(message,cause);
        this.code = responseEnum.getCode();
        this.message = message;
        this.responseEnum =responseEnum;

    }
}
