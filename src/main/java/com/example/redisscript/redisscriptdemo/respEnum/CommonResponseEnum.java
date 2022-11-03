package com.example.redisscript.redisscriptdemo.respEnum;

import com.example.redisscript.redisscriptdemo.utils.BusinessExceptionAssertUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponseEnum implements BusinessExceptionAssertUtils {
    SERVER_ERROR("500","server.error"),
    SUCCESS("0000","server.ok"),
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
