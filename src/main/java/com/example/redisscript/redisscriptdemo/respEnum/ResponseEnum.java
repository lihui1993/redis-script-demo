package com.example.redisscript.redisscriptdemo.respEnum;

import com.example.redisscript.redisscriptdemo.utils.BusinessExceptionAssertUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum implements BusinessExceptionAssertUtils {
    /**
     * Bad licence type
     */
    BAD_LICENCE_TYPE("7001", "Bad licence type."),
    /**
     * Licence not found
     */
    LICENCE_NOT_FOUND("7002", "Licence not found."),

    LOCK_KEY_HAS_NOT_TEXT("","Lock key has not text."),
    ;
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;


}
