package com.example.redisscript.redisscriptdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayUserAgreementExecutionplanModifyResponse;
import com.alipay.api.response.AlipayUserAgreementQueryResponse;
import com.alipay.api.response.AlipayUserAgreementUnsignResponse;
import com.example.redisscript.redisscriptdemo.dto.*;
import com.example.redisscript.redisscriptdemo.entity.OrderInfo;
import com.example.redisscript.redisscriptdemo.respEnum.ArgumentResponseEnum;
import com.example.redisscript.redisscriptdemo.service.AliPayChannel;
import com.example.redisscript.redisscriptdemo.service.OrderInfoService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Tag(name = "纯支付宝操作")
@RestController
@RequestMapping("/alipayOperate")
public class AlipayOperateController {

    @Resource
    private AliPayChannel aliPayChannel;
    @Resource
    private OrderInfoService orderInfoService;

    @Operation(
            summary = "支付宝纯退款接口"
            , parameters = {@Parameter(name = "body", description = "支付宝纯退款参数", schema = @Schema(oneOf = OrderRefundRequest.class))}
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
            summary = "支付宝纯退款接口（批量）"
            , parameters = {@Parameter(name = "body", description = "支付宝纯退款参数(批量)", schema = @Schema(oneOf = OrderRefundBatchRequest.class))}
            , responses = {@ApiResponse(content = {@Content(schema = @Schema(oneOf = {BaseResponse.class, CommonResponse.class, ErrorResponse.class}))})}
    )
    @PostMapping("/orderRefundOnlyBatch")
    public ResponseEntity<BaseResponse> orderRefundOnlyBatch(@RequestBody @Validated OrderRefundBatchRequest request) {
        log.info("支付宝纯退款接口（批量），参数：{}", request.getOrderRefundString());
        List<String> orderInfoList = Arrays.asList(request.getOrderRefundString().split("\\|"));
        JSONObject rj = new JSONObject();
        for (int i = 0; i < orderInfoList.size(); i++) {
            String orderInfo = orderInfoList.get(i);
            log.info("支付宝纯退款接口（批量），参数:{}={}", i, orderInfo);
            List<String> orderParamsList = Arrays.asList(orderInfo.split("\\^"));
            if (orderParamsList.size() != 2) {
                rj.put("orderParams".concat(orderInfo), "参数格式不正确！！");
            } else {
                String outTradeNo = orderParamsList.get(0);
                String refundAmt = orderParamsList.get(1);
                if (!StringUtils.hasText(outTradeNo) || !StringUtils.hasText(refundAmt)) {
                    rj.put("orderParams".concat(orderInfo), "参数格式不正确！！");
                } else {
                    AliPayChannelRefundParam param = new AliPayChannelRefundParam();
                    param.setRefundNo(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
                    param.setRefundAmt(refundAmt);
                    param.setOutTradeNo(outTradeNo);
                    param.setRefundReason("测试");
                    AlipayTradeRefundResponse response = aliPayChannel.tradeRefund(param);
                    rj.put("orderParams".concat(orderInfo), response.getBody());
                }
            }
        }
        return new ResponseEntity<>(new CommonResponse<>(rj), HttpStatus.OK);
    }


    @Operation(
            summary = "支付宝纯解除协议接口（批量）"
            , parameters = {@Parameter(name = "body", description = "解除协议参数", schema = @Schema(oneOf = OrderUnSignRequest.class))}
            , responses = {@ApiResponse(content = {@Content(schema = @Schema(oneOf = {BaseResponse.class, CommonResponse.class, ErrorResponse.class}))})}
    )
    @PostMapping("/orderUnSignOnly")
    public ResponseEntity<BaseResponse> orderUnSignOnly(@RequestBody @Validated OrderUnSignRequest request) {

        String[] channelAgreementNoList = request.getChannelAgreementNoList().split(",");
        JSONObject rj = new JSONObject();
        for (String channelAgreementNo : channelAgreementNoList) {
            OrderInfo orderInfo = orderInfoService.checkIsTestOrderInfoByChannelAgreementNo(channelAgreementNo);
            if (Objects.nonNull(orderInfo)) {
                log.info("orderNo:{},channelAgreementNo:{},调用解约", orderInfo.getOrderNo(), channelAgreementNo);
                AlipayUserAgreementUnsignResponse response = aliPayChannel.cyclePayUserAgreementUnsign(request.getAliUserId(), null, channelAgreementNo, UnsignOperateTypeEnum.confirm);
                channelAgreementNo = "channelAgreementNo_".concat(channelAgreementNo).concat("_orderNo_").concat(orderInfo.getOrderNo());
                rj.put(channelAgreementNo, response.getBody());
            } else {
                channelAgreementNo = "channelAgreementNo_".concat(channelAgreementNo);
                rj.put(channelAgreementNo, "{\"checkIsTestOrder\":\"" + false + "\"}");
            }
        }
        return new ResponseEntity<>(new CommonResponse<>(rj), HttpStatus.OK);
    }

    @Operation(
            summary = "支付宝纯扣款协议查询接口"
            , parameters = {@Parameter(name = "body", description = "支付宝纯扣款协议查询参数", schema = @Schema(oneOf = CyclePayUserAgreementQueryRequest.class))}
            , responses = {@ApiResponse(content = {@Content(schema = @Schema(oneOf = {BaseResponse.class, CommonResponse.class, ErrorResponse.class}))})}
    )
    @PostMapping("/cyclePayUserAgreementQuery")
    public ResponseEntity<BaseResponse> cyclePayUserAgreementQuery(@RequestBody @Validated CyclePayUserAgreementQueryRequest request) {
        if (!StringUtils.hasText(request.getAgreementNo()) && !StringUtils.hasText(request.getExternalAgreementNo())) {
            return new ResponseEntity<>(new ErrorResponse(ArgumentResponseEnum.VALID_ERROR.getCode(), "参数不能都为空"), HttpStatus.OK);
        }
        AlipayUserAgreementQueryResponse response = aliPayChannel.cyclePayUserAgreementQuery(request.getExternalAgreementNo(), request.getAgreementNo());
        return new ResponseEntity<>(new CommonResponse<>(response.getBody()), HttpStatus.OK);
    }

    @Operation(
            summary = "支付宝纯扣款协议修改扣款日期接口"
            , parameters = {@Parameter(name = "body", description = "支付宝纯扣款协议修改扣款日期参数", schema = @Schema(oneOf = CyclePayUserAgreementModifyRequest.class))}
            , responses = {@ApiResponse(content = {@Content(schema = @Schema(oneOf = {BaseResponse.class, CommonResponse.class, ErrorResponse.class}))})}
    )
    @PostMapping("/cyclePayUserAgreementModify")
    public ResponseEntity<BaseResponse> cyclePayUserAgreementModify(@RequestBody @Validated CyclePayUserAgreementModifyRequest request) {
        AlipayUserAgreementExecutionplanModifyResponse response = aliPayChannel.cyclePayUserAgreementExecutionPlanModify(request.getAgreementNo(), request.getDeductTime(), request.getMemo());
        return new ResponseEntity<>(new CommonResponse<>(response.getBody()), HttpStatus.OK);
    }


}
