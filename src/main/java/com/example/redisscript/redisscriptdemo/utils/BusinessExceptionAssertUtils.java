package com.example.redisscript.redisscriptdemo.utils;

import com.example.redisscript.redisscriptdemo.exception.BaseException;
import com.example.redisscript.redisscriptdemo.exception.BusinessException;

import java.text.MessageFormat;

public interface BusinessExceptionAssertUtils extends IResponseEnum, AssertUtils {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BusinessException(this, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);
        return new BusinessException(this, msg, t);
    }
}
