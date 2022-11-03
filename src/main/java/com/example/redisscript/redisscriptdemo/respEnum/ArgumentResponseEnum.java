package com.example.redisscript.redisscriptdemo.respEnum;

import com.example.redisscript.redisscriptdemo.utils.BusinessExceptionAssertUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArgumentResponseEnum implements BusinessExceptionAssertUtils {
    VALID_ERROR("1000001", "参数错误");
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;
}
