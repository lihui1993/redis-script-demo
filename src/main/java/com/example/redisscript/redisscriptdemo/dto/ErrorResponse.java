package com.example.redisscript.redisscriptdemo.dto;

import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Schema(name = "错误返回参数",
        extensions ={@Extension(
                name = "基本返回参数",
                properties = {
                        @ExtensionProperty(name = "code", value = ""),
                        @ExtensionProperty(name = "msg",value = "")})})
public class ErrorResponse extends BaseResponse {
    @Schema(name = "errorCode",description = "响应错误码")
    private String errorCode;
    @Schema(name = "errorMsg",description = "响应错误码描述")
    private String errorMsg;

    public ErrorResponse(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
