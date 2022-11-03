package com.example.redisscript.redisscriptdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AlipayProperties {
    private String appId;
    private String serverUrl;
    private String privateKey;
    private String charset;
    private String format;
    private String signType;
    private String certPath;
    private String alipayPublicCertPath;
    private String rootCertPath;
    private String notifyUrl;
}