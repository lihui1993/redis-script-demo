package com.example.redisscript.redisscriptdemo.service;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AccessParams;
import com.alipay.api.domain.AlipayUserAgreementPageSignModel;
import com.alipay.api.domain.PeriodRuleParams;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.example.redisscript.redisscriptdemo.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

@Slf4j
@Service
public class AliPayChannel {

    @Autowired
    private AlipayClient alipayClient;


    /**
     * 支付宝通用支付
     *
     * @param subject
     * @param outTradeNo
     * @param totalAmount
     */
    public AlipayTradeCreateResponse commonTradeCreate(String subject, String outTradeNo, BigDecimal totalAmount, String buyerId, String notifyUrl) {

        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(notifyUrl);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("total_amount", totalAmount);
        bizContent.put("subject", subject);
        bizContent.put("buyer_id", buyerId);
        //        交易支付使用的资金渠道。只有在签约中指定需要返回资金明细，或者入参的query_options中指定时才返回该字段信息。
        bizContent.put("query_options", new String[]{"fund_bill_list"});

        request.setBizContent(bizContent.toString());
        log.info("out_trade_no:{},统一收单交易创建接口参数：{}", outTradeNo, bizContent.toString());
        AlipayTradeCreateResponse response = null;
        try {
            response = alipayClient.certificateExecute(request);
            log.info("out_trade_no:{},统一收单交易创建接口返回参数：{}", outTradeNo, response.getBody());
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{},统一收单交易创建接口异常：{}", outTradeNo, e);
        }
        return response;
    }

    /**
     * <p>统一收单交易关闭接口</p>
     * <p>用于交易创建后，用户在一定时间内未进行支付，可调用该接口直接将未付款的交易进行关闭</p>
     *
     * @param out_trade_no 商户订单号
     * @param notify_url 异步通知地址
     */
    public AlipayTradeCloseResponse alipayTradeClose(String out_trade_no,String notify_url) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setNotifyUrl(notify_url);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", out_trade_no);
        request.setBizContent(bizContent.toString());
        AlipayTradeCloseResponse response = new AlipayTradeCloseResponse();
        try {
            log.info("out_trade_no:{},调用支付交易关闭接口的参数是：{}", out_trade_no, bizContent.toString());
            response = alipayClient.certificateExecute(request);
            log.info("out_trade_no:{},调用支付交易关闭接口返回参数：{}", out_trade_no, response.getBody());
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{},调用支付交易关闭接口异常：", out_trade_no, e);
            response.setSubCode(e.getErrCode());
            response.setSubMsg("调用支付宝接口异常：".concat(e.getErrMsg()));
        }
        return response;
    }

    /**
     * 支付宝花呗分期
     *
     * @param subject
     * @param outTradeNo
     * @param totalAmount
     */
    public AlipayTradeCreateResponse huabeiTradeCreate(String subject, String outTradeNo, BigDecimal totalAmount, String buyerId, HuabeiConfig huabeiConfig, String notifyUrl) {
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(notifyUrl);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("total_amount", totalAmount);
        bizContent.put("subject", subject);
        bizContent.put("buyer_id", buyerId);

        JSONObject extendParams = new JSONObject();
        extendParams.put("hb_fq_num", huabeiConfig.getHbFqNum());
        extendParams.put("hb_fq_seller_percent", huabeiConfig.getHbFqSellerPercent());
        bizContent.put("extend_params", extendParams);

        request.setBizContent(bizContent.toString());
        log.info("out_trade_no:{}调用支付宝花呗分期参数{}", outTradeNo, bizContent);
        AlipayTradeCreateResponse response = new AlipayTradeCreateResponse();
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{}调用支付宝花呗分期异常{}", outTradeNo, e);
        }
        return response;
    }


    /**
     * 支付宝交易查询
     */
    public AlipayTradeQueryResponse tradeQuery(AliPayChannelQueryParam param) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        if (StringUtils.hasText(param.getOutTradeNo())) {
            bizContent.put("out_trade_no", param.getOutTradeNo());
        }

        if (StringUtils.hasText(param.getTradeNo())) {
            bizContent.put("trade_no", param.getTradeNo());
        }
//        查询选项，商户传入该参数可定制本接口同步响应额外返回的信息字段，数组格式。支持枚举如下：trade_settle_info：返回的交易结算信息，包含分账、补差等信息；
//fund_bill_list：交易支付使用的资金渠道
        bizContent.put("query_options", new String[]{"fund_bill_list"});
        request.setBizContent(bizContent.toString());
        log.info("out_trade_no:{}调用支付宝交易查询参数{}", param.getOutTradeNo(), bizContent);
        AlipayTradeQueryResponse response = new AlipayTradeQueryResponse();
        try {
            response = alipayClient.certificateExecute(request);
            log.info("out_trade_no:{}，调用支付交易查询返回参数：{}", param.getOutTradeNo(), response.getBody());
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{}调用支付宝查询异常{}", param.getOutTradeNo(), e);
        }
        return response;
    }


    /**
     * 支付宝交易退款
     *
     * @param
     */
    public AlipayTradeRefundResponse tradeRefund(AliPayChannelRefundParam param) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", param.getOutTradeNo());
        bizContent.put("refund_amount", param.getRefundAmt());
        bizContent.put("out_request_no", param.getRefundNo());
        bizContent.put("refund_reason", param.getRefundReason());
//        商户通过上送该参数来定制同步需要额外返回的信息字段，数组格式。支持：refund_detail_item_list：退款使用的资金渠道
        bizContent.put("query_options", new String[]{"refund_detail_item_list"});
        request.setBizContent(bizContent.toString());
        log.info("out_trade_no:{}调用支付宝退款参数{}", param.getOutTradeNo(), bizContent);
        AlipayTradeRefundResponse response = new AlipayTradeRefundResponse();
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{}调用支付宝退款异常{}", param.getOutTradeNo(), e);
        }
        return response;
    }

    /**
     * 支付宝交易退款查询
     *
     * @param outTradeNo
     */
    public AlipayTradeFastpayRefundQueryResponse tradeRefundQuery(String outTradeNo, String refundNo) {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("out_request_no", refundNo);
//        查询选项，商户通过上送该参数来定制同步需要额外返回的信息字段，数组格式。枚举支持：
//refund_detail_item_list：本次退款使用的资金渠道；
//gmt_refund_pay：退款执行成功的时间；
        bizContent.put("query_options", new String[]{"refund_detail_item_list", "gmt_refund_pay"});
        request.setBizContent(bizContent.toString());
        log.info("out_trade_no:{}调用支付宝退款查询参数{}", outTradeNo, bizContent);
        AlipayTradeFastpayRefundQueryResponse response = new AlipayTradeFastpayRefundQueryResponse();
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{}调用支付宝退款查询异常{}", outTradeNo, e);
        }
        return response;
    }


    /**
     * 支付宝周期扣款协议查询
     *
     * @param externalAgreementNo
     */
    public AlipayUserAgreementQueryResponse cyclePayUserAgreementQuery(String externalAgreementNo) {
        AlipayUserAgreementQueryRequest request = new AlipayUserAgreementQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("personal_product_code", "CYCLE_PAY_AUTH_P");///周期扣款产品时必传签约其它产品时无需传入
        bizContent.put("sign_scene", "INDUSTRY|APPSTORE");//签约场景：电子商城行业
        bizContent.put("external_agreement_no", externalAgreementNo);//商户签约号
        request.setBizContent(bizContent.toString());
        log.info("externalAgreementNo:{}调用支付宝周期扣款签约查询参数{}", externalAgreementNo, bizContent);
        AlipayUserAgreementQueryResponse response = new AlipayUserAgreementQueryResponse();
        try {
            response = alipayClient.certificateExecute(request);
            log.info("externalAgreementNo:{}调用支付宝周期扣款签约查询，返回参数：{}", externalAgreementNo, response.getBody());
        } catch (AlipayApiException e) {
            log.error("externalAgreementNo:{}调用支付宝周期扣款签约查询异常", externalAgreementNo, e);
        }
        return response;
    }

    /**
     * 周期性扣款协议执行计划修改（当超过扣款时效未扣到款需需修改当期扣款时间）
     *
     * @param agreementNo
     * @param deductTime
     * @param memo
     */
    public AlipayUserAgreementExecutionplanModifyResponse cyclePayUserAgreementExecutionplanModify(String agreementNo, String deductTime, String memo) {
        AlipayUserAgreementExecutionplanModifyRequest request = new AlipayUserAgreementExecutionplanModifyRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("agreement_no", agreementNo);
        bizContent.put("deduct_time", deductTime);
        bizContent.put("memo", memo);
        request.setBizContent(bizContent.toString());
        log.info("agreementNo:{}周期性扣款协议执行计划修改参数{}", agreementNo, bizContent);
        AlipayUserAgreementExecutionplanModifyResponse response = new AlipayUserAgreementExecutionplanModifyResponse();
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.error("agreementNo:{}调用周期性扣款协议执行计划修改异常{}", agreementNo, e);
        }
        return response;
    }

    /**
     * 支付宝周期扣款协议解除
     *
     * @param externalAgreementNo
     */
    public AlipayUserAgreementUnsignResponse cyclePayUserAgreementUnsign(String alipayUserId, String externalAgreementNo,
                                                                         String channelAgreementNo, UnsignOperateTypeEnum operateType) {
        AlipayUserAgreementUnsignRequest request = new AlipayUserAgreementUnsignRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("alipay_user_id", alipayUserId);
        bizContent.put("personal_product_code", "CYCLE_PAY_AUTH_P");///周期扣款产品时必传签约其它产品时无需传入
        bizContent.put("sign_scene", "INDUSTRY|APPSTORE");//签约场景：电子商城行业
        //商户签约号
        if (StringUtils.hasText(externalAgreementNo)) {
            bizContent.put("external_agreement_no", externalAgreementNo);
        }
//        支付宝系统中用以唯一标识用户签约记录的编号（用户签约成功后的协议号 ），如果传了该参数，其他参数会被忽略 。本参数与 external_agreement_no 不可同时为空
        if (StringUtils.hasText(channelAgreementNo)) {
            bizContent.put("agreement_no", channelAgreementNo);
        }
//        操作类型：confirm（解约确认）；invalid（解约作废）。注意：仅异步解约需传入，其余情况无需传递本参数
        bizContent.put("operate_type", operateType.getOperateType());
//        类型为invalid时，extend_params参数必须传，固定
        if (UnsignOperateTypeEnum.invalid.equals(operateType)) {
            bizContent.put("extend_params", JSONObject.parse("{\"UNSIGN_ERROR_CODE\": \"USER_OWE_MONEY\",\"UNSIGN_ERROR_INFO\":\"10.00\"}"));
        }
        log.info("externalAgreementNo：{}、agreement_no：{}周期性扣款协议解除参数{}", externalAgreementNo, channelAgreementNo, bizContent);
        request.setBizContent(bizContent.toJSONString());
        AlipayUserAgreementUnsignResponse response = new AlipayUserAgreementUnsignResponse();
        try {
            response = alipayClient.certificateExecute(request);
            log.info("externalAgreementNo：{}、agreement_no：{}周期性扣款协议解除，返回参数{}", externalAgreementNo, channelAgreementNo, response.getBody());
        } catch (AlipayApiException e) {
            log.error("externalAgreementNo：{}、agreement_no：{}周期性扣款协议解除异常{}", externalAgreementNo, channelAgreementNo, e);
        }
        return response;
    }


    /**
     * 支付宝周期扣款
     */
    public AlipayTradePayResponse cycleTradePay(String subject, String outTradeNo, BigDecimal totalAmount, String notifyUrl, String agreementNo) {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setNotifyUrl(notifyUrl);//支付异步通知地址
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);//商户订单号。由商家自定义确保唯一
        bizContent.put("total_amount", totalAmount);//订单总金额。单位为元
        bizContent.put("subject", subject);//订单标题
        bizContent.put("product_code", "CYCLE_PAY_AUTH");//产品码
        JSONObject agreementParams = new JSONObject();//代扣信息
        agreementParams.put("agreement_no", agreementNo);//用户签约成功后的协议号
        bizContent.put("agreement_params", agreementParams);
//        交易支付使用的资金渠道。只有在签约中指定需要返回资金明细，或者入参的query_options中指定时才返回该字段信息。
        bizContent.put("query_options", new String[]{"fund_bill_list"});

        request.setBizContent(bizContent.toString());
        log.info("out_trade_no:{}调用支付宝周期扣款参数{}", outTradeNo, bizContent);
        AlipayTradePayResponse response = new AlipayTradePayResponse();
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{}调用支付宝周期扣款异常{}", outTradeNo, e);
        }
        return response;
    }


    /**
     * 支付宝支付后签约
     */
    public AlipayTradeAppPayResponse cycleAliPayTradeAppPay(String subject, String outTradeNo, BigDecimal totalAmount
            , String notifyUrl, String externalAgreementNo, PeriodRuleParams periodRuleParams) {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setNotifyUrl(notifyUrl);//支付异步通知地址
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);//商户订单号。由商家自定义确保唯一
        bizContent.put("total_amount", totalAmount);//订单总金额。单位为元
        bizContent.put("subject", subject);//订单标题
        bizContent.put("product_code", "CYCLE_PAY_AUTH");//产品码

        AgreementSign agreementParams = new AgreementSign();//代扣信息
        agreementParams.setPersonalProductCode("CYCLE_PAY_AUTH_P");
        agreementParams.setSignScene("INDUSTRY|APPSTORE");
        agreementParams.setExternalAgreementNo(externalAgreementNo);
        AccessParams accessParams = new AccessParams();
        accessParams.setChannel("ALIPAYAPP");//支付宝客户端 H5页面签约
        agreementParams.setAccess_params(accessParams);
        agreementParams.setPeriod_rule_params(periodRuleParams);
        bizContent.put("agreement_sign_params", agreementParams);

        request.setBizContent(bizContent.toString());
        log.info("out_trade_no:{}调用支付宝周期扣款支付并签约参数{}", outTradeNo, bizContent);
        AlipayTradeAppPayResponse response = new AlipayTradeAppPayResponse();
        try {
            response = alipayClient.sdkExecute(request);
            response.setBody(URLEncoder.encode(response.getBody(), "UTF-8"));
            log.info("externalAgreementNo:{}调用支付宝周期扣款支付并签约返回参数：{}", externalAgreementNo, response.getBody());
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{}调用支付宝周期扣款支付并签约异常{}", outTradeNo, e);
        } catch (UnsupportedEncodingException e) {
            log.error("out_trade_no:{}调用支付宝周期扣款支付并签约，返回参数，body编码异常{}", outTradeNo, e);
        }
        return response;
    }

    /**
     * 支付宝签约后代扣
     */
    public AlipayUserAgreementPageSignResponse cycleAliPayTradeAppPaySignOnly(String notifyUrl, String externalAgreementNo, PeriodRuleParams periodRuleParams) {
        AlipayUserAgreementPageSignRequest request = new AlipayUserAgreementPageSignRequest();
        request.setNotifyUrl(notifyUrl);//支付异步通知地址

        AlipayUserAgreementPageSignModel model = new AlipayUserAgreementPageSignModel();//代扣信息
        model.setPersonalProductCode("CYCLE_PAY_AUTH_P");
        model.setSignScene("INDUSTRY|APPSTORE");
        model.setExternalAgreementNo(externalAgreementNo);
        AccessParams accessParams = new AccessParams();
        accessParams.setChannel("ALIPAYAPP");//支付宝客户端 H5页面签约
        model.setAccessParams(accessParams);
        model.setPeriodRuleParams(periodRuleParams);

        request.setBizModel(model);
        log.info("external_agreement_no:{}调用支付宝周期扣款签约后代扣参数{}", externalAgreementNo, JSONObject.toJSON(model));
        AlipayUserAgreementPageSignResponse response = new AlipayUserAgreementPageSignResponse();
        try {
            response = alipayClient.sdkExecute(request);
            response.setBody(URLEncoder.encode(response.getBody(), "UTF-8"));
            log.info("external_agreement_no:{}调用支付宝周期扣款签约后代扣返回参数：{}", externalAgreementNo, response.getBody());
        } catch (AlipayApiException e) {
            log.error("external_agreement_no:{}调用支付宝周期扣款签约后代扣异常{}", externalAgreementNo, e);
        } catch (UnsupportedEncodingException e) {
            log.error("external_agreement_no:{}调用支付宝周期扣款签约后代扣，返回参数，body编码异常{}", externalAgreementNo, e);
        }
        return response;
    }

    /**
     * 支付宝转账
     *
     * @param
     */
    public AlipayFundTransUniTransferResponse transfer(AliPayChannelTransferParam param) {
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_biz_no", param.getOutTradeNo());
        bizContent.put("trans_amount", param.getTransAmt());
        bizContent.put("product_code", "TRANS_ACCOUNT_NO_PWD");
        bizContent.put("biz_scene", "DIRECT_TRANSFER");
        bizContent.put("order_title", param.getTitle());
        JSONObject payeeInfo = new JSONObject();
        payeeInfo.put("identity", param.getPayeeAccountNo());
        payeeInfo.put("identity_type", "ALIPAY_LOGON_ID");
        payeeInfo.put("name", param.getPayeeAccountName());
        bizContent.put("payee_info", payeeInfo);
        request.setBizContent(bizContent.toString());
        log.info("out_trade_no:{}调用支付宝转账参数{}", param.getOutTradeNo(), bizContent);
        AlipayFundTransUniTransferResponse response = new AlipayFundTransUniTransferResponse();
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{}调用支付宝转账异常{}", param.getOutTradeNo(), e);
        }
        return response;
    }

    /**
     * 支付宝转账查询
     *
     * @param outTradeNo
     */
    public AlipayFundTransCommonQueryResponse transferQuery(String outTradeNo) {
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_biz_no", outTradeNo);
        bizContent.put("product_code", "TRANS_ACCOUNT_NO_PWD");
        bizContent.put("biz_scene", "DIRECT_TRANSFER");
        request.setBizContent(bizContent.toString());
        log.info("out_trade_no:{}调用支付宝转账查询参数{}", outTradeNo, bizContent);
        AlipayFundTransCommonQueryResponse response = new AlipayFundTransCommonQueryResponse();
        try {
            response = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.error("out_trade_no:{}调用支付宝转账查询异常{}", outTradeNo, e);
        }
        return response;
    }

    /**
     * alipay.data.bill.balance.query(支付宝商家账户当前余额查询)
     * https://opendocs.alipay.com/open/02awe3
     * 为支付宝商家提供支付宝账户当前余额信息，包括冻结金额，可用余额和总额，供对账使用
     *
     * @param billUserID
     * @return
     */
    public AlipayDataBillBalanceQueryResponse dataBillBalanceQuery(String billUserID) {
        AlipayDataBillBalanceQueryRequest request = new AlipayDataBillBalanceQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("bill_user_id", billUserID);
        request.setBizContent(bizContent.toJSONString());
        AlipayDataBillBalanceQueryResponse dataBillBalanceQueryResponse = new AlipayDataBillBalanceQueryResponse();
        try {
            dataBillBalanceQueryResponse = alipayClient.certificateExecute(request);
        } catch (AlipayApiException e) {
            log.error("支付宝商家账户当前余额查询报错", e);
        }
        return dataBillBalanceQueryResponse;
    }
}
