package com.example.redisscript.redisscriptdemo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(name = "解除协议参数")
public class OrderUnSignRequest {

    @Schema(title = "支付宝用户ID", name = "aliUserId", required = true)
    @NotBlank(message = "支付宝用户ID为空")
    private String aliUserId;
    @Schema(title = "渠道协议号，多个协议号以“,”分割",name = "channelAgreementNoList", required = true)
    @NotBlank(message = "渠道协议号不能为空")
    private String channelAgreementNoList;
}
