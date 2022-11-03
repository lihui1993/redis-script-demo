package com.example.redisscript.redisscriptdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AliPayChannelTransferParam {
    private String outTradeNo;
    private String transAmt;
    private String title;
    private String payeeAccountName;
    private String payeeAccountNo;
}
