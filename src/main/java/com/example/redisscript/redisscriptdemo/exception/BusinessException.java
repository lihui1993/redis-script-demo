package com.example.redisscript.redisscriptdemo.exception;

import com.example.redisscript.redisscriptdemo.utils.IResponseEnum;

public class BusinessException extends BaseException {
    private static final long serialVersionUID = 1L;

    public BusinessException(IResponseEnum responseEnum, String message) {
        super(responseEnum, message);
    }

    public BusinessException(IResponseEnum responseEnum, String message, Throwable cause) {
        super(responseEnum, message, cause);
    }
}
