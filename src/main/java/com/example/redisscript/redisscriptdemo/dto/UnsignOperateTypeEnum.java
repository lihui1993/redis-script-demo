package com.example.redisscript.redisscriptdemo.dto;

import lombok.Getter;

@Getter
public enum UnsignOperateTypeEnum {
//    操作类型：confirm（解约确认）；invalid（解约作废）。注意：仅异步解约需传入，其余情况无需传递本参数
//    类型为invalid时，extend_params参数必须传，固定
    confirm("confirm"),
    invalid("invalid"),
    ;

    private String operateType;

    UnsignOperateTypeEnum(String operateType) {
        this.operateType = operateType;
    }
}
