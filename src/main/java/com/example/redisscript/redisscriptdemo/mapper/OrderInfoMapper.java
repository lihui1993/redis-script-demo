package com.example.redisscript.redisscriptdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.redisscript.redisscriptdemo.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
}
