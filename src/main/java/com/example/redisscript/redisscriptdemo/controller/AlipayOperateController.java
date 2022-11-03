package com.example.redisscript.redisscriptdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayUserAgreementUnsignResponse;
import com.example.redisscript.redisscriptdemo.dto.*;
import com.example.redisscript.redisscriptdemo.service.AliPayChannel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@Tag(name = "纯支付宝操作")
@RestController
@RequestMapping("/alipayOperate")
public class AlipayOperateController {

    @Resource
    private AliPayChannel aliPayChannel;

    @Operation(
            summary = "支付宝纯退款接口"
            , parameters = {@Parameter(name = "body", description = "支付宝纯退款参数")}
            , responses = {@ApiResponse(content = {@Content(schema = @Schema(oneOf = {BaseResponse.class, CommonResponse.class, ErrorResponse.class}))})}
    )
    @PostMapping("/orderRefundOnly")
    public ResponseEntity<BaseResponse> orderRefundOnly(@RequestBody @Validated OrderRefundRequest request) {

        AliPayChannelRefundParam param = new AliPayChannelRefundParam();
        BeanUtils.copyProperties(request, param);
        param.setRefundNo(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
        AlipayTradeRefundResponse response = aliPayChannel.tradeRefund(param);
        return new ResponseEntity<>(new CommonResponse<>(response), HttpStatus.OK);
    }


    @Operation(
            summary = "支付宝纯解除协议接口"
            , parameters = {@Parameter(name = "body", description = "解除协议参数")}
            , responses = {@ApiResponse(content = {@Content(schema = @Schema(oneOf = {BaseResponse.class, CommonResponse.class, ErrorResponse.class}))})}
    )
    @PostMapping("/orderUnSignOnly")
    public ResponseEntity<BaseResponse> orderUnSignOnly(@RequestBody @Validated OrderUnSignRequest request) {

        String[] channelAgreementNoList = request.getChannelAgreementNoList().split(",");
        JSONObject rj = new JSONObject();
        for (String channelAgreementNo : channelAgreementNoList) {
            AlipayUserAgreementUnsignResponse response = aliPayChannel.cyclePayUserAgreementUnsign(request.getAliUserId(), null, channelAgreementNo, UnsignOperateTypeEnum.confirm);
            rj.put(channelAgreementNo, response.getBody());
        }
        return new ResponseEntity<>(new CommonResponse<>(rj), HttpStatus.OK);
    }


}
