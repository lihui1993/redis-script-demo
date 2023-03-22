package com.example.redisscript.redisscriptdemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(name = "支付宝纯退款参数(批量)")
public class OrderRefundBatchRequest {
    @Schema(title = "参数，格式：交易订单号^退款金额|交易订单号^退款金额；退款原因默认'测试'",name = "orderRefundString",required = true)
    @NotBlank(message = "参数不能为空")
    private String orderRefundString;
}
