package com.example.redisscript.redisscriptdemo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ErrorResponse extends BaseResponse {

    private String errorCode;

    private String errorMsg;

    public ErrorResponse(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
