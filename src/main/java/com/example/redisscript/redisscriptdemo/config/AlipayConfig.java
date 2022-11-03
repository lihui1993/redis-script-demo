package com.example.redisscript.redisscriptdemo.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class AlipayConfig {

    @Resource
    private AlipayProperties alipayProperties;

    @Bean
    public AlipayClient initAlipayClient() throws AlipayApiException {
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        BeanUtils.copyProperties(alipayProperties,certAlipayRequest);
        return new DefaultAlipayClient(certAlipayRequest);
    }
}
