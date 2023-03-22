package com.example.redisscript.redisscriptdemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(name = "支付宝纯扣款协议查询参数")
public class CyclePayUserAgreementQueryRequest {
    @Schema(title = "支付宝系统中用以唯一标识用户签约记录的编号，（其中之一不能为空）",name = "agreementNo")
    private String agreementNo;
    @Schema(title = "商户签约号，（其中之一不能为空）",name = "externalAgreementNo")
    private String externalAgreementNo;
}
