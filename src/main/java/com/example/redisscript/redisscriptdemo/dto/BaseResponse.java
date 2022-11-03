package com.example.redisscript.redisscriptdemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Schema(name = "基本返回参数")
public class BaseResponse {
    @Schema(name = "code",description = "响应码")
    private String code;
    @Schema(name = "msg",description = "响应码描述")
    private String msg;

    public BaseResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
