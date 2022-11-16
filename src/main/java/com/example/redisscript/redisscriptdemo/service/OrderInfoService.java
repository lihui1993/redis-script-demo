package com.example.redisscript.redisscriptdemo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.redisscript.redisscriptdemo.entity.OrderInfo;
import com.example.redisscript.redisscriptdemo.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class OrderInfoService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    public OrderInfo checkIsTestOrderInfoByChannelAgreementNo(String channelAgreementNo) {
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderInfoLambdaQueryWrapper
                .eq(OrderInfo::getChannelAgreementNo, channelAgreementNo)
                .eq(OrderInfo::getAgreementState, "NORMAL");
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(orderInfoLambdaQueryWrapper);
        if(CollectionUtils.isEmpty(orderInfoList)){
            return null;
        }
        return orderInfoList.get(0);
    }
}
