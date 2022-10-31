package com.example.redisscript.redisscriptdemo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BaseResponse {
    private String code;

    private String msg;

    public BaseResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
