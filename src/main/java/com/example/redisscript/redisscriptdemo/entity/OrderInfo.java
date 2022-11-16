package com.example.redisscript.redisscriptdemo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("order_info")
public class OrderInfo {

    @TableId
    private Integer id;

    private String orderNo;

    private Integer merId;

    private Integer custId;

    private Integer xiaoErId;

    private Integer goodsId;

    private Integer goodsNum;

    private BigDecimal orderAmount;

    private String orderState;

    private String downPay;

    private Date orderTime;

    private Integer numberOfPeriods;

    private BigDecimal singlePeriodAmt;

    private String payWill;

    private String payMethod;

    private String name;

    private String mobile;

    private String detailAddr;

    private String billNo;

    private String idFrontKey;

    private String idBackKey;

    private String orderPreviewKey;

    private String zhimafenKey;

    private String remark;

    private String agreementNo;

    private String agreementState;

    private String channelAgreementNo;

    private String agreementExecuteTime;

    private String periodType;

    private String cyclePayFirst;

    private String stagesShStatus;

    private String stagesShId;

    private String stagesShType;

    private String stagesShName;

    private Date stagesShTime;

    private Date createTime;

    private Date updateTime;

}