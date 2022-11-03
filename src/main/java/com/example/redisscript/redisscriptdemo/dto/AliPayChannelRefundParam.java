package com.example.redisscript.redisscriptdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AliPayChannelRefundParam {
    private String outTradeNo;
    private String refundAmt;
    private String refundNo;
    private String refundReason;
}
