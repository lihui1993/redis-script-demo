package com.example.redisscript.redisscriptdemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "支付宝纯扣款协议修改扣款日期参数")
public class CyclePayUserAgreementModifyRequest {
    @Schema(title = "支付宝系统中用以唯一标识用户签约记录的编号",name = "agreementNo",required = true)
    private String agreementNo;
    @Schema(title = "扣款日期，格式：yyyy-MM-dd",name = "deductTime",required = true)
    private String deductTime;
    @Schema(title = "备注",name = "memo",required = true)
    private String memo;
}
