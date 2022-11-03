package com.example.redisscript.redisscriptdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AliPayChannelQueryParam {
    private String outTradeNo;
    private String tradeNo;
    private String type;// 1-周期扣款，2-花呗分期
}
