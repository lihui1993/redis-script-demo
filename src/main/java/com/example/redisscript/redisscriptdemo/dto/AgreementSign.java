package com.example.redisscript.redisscriptdemo.dto;

import com.alipay.api.domain.AccessParams;
import com.alipay.api.domain.AgreementSignParams;
import com.alipay.api.domain.PeriodRuleParams;
import lombok.Data;

@Data
public class AgreementSign extends AgreementSignParams {

    private AccessParams access_params;

    private PeriodRuleParams period_rule_params;
}
