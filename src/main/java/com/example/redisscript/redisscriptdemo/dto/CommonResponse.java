package com.example.redisscript.redisscriptdemo.dto;

import com.example.redisscript.redisscriptdemo.respEnum.CommonResponseEnum;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Schema(name = "公共返回参数",
        extensions ={@Extension(
                name = "基本返回参数",
                properties = {
                        @ExtensionProperty(name = "code", value = ""),
                        @ExtensionProperty(name = "msg",value = "")})})
public class CommonResponse<T> extends BaseResponse {
    @Schema(name = "data", description = "业务响应实体类")
    public T data;

    public CommonResponse(String code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }

    public CommonResponse(T data) {
        super(CommonResponseEnum.SUCCESS.getCode(), CommonResponseEnum.SUCCESS.getMessage());
        this.data = data;
    }

}
