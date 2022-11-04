package com.example.redisscript.redisscriptdemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(name = "支付宝纯退款参数")
public class OrderRefundRequest {
    @Schema(title = "原交易订单号",name = "outTradeNo",required = true)
    @NotBlank(message = "原交易订单号不能为空")
    private String outTradeNo;
    @Schema(title = "退款金额",name = "refundAmt",required = true)
    @NotBlank(message = "退款金额不能为空")
    private String refundAmt;
    @Schema(title = "退款原因",name = "refundReason",required = true)
    @NotBlank(message = "退款原因不能为空")
    private String refundReason;
}
