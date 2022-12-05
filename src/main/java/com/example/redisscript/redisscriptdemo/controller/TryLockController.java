package com.example.redisscript.redisscriptdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.redisscript.redisscriptdemo.dto.BaseResponse;
import com.example.redisscript.redisscriptdemo.dto.CommonResponse;
import com.example.redisscript.redisscriptdemo.dto.ErrorResponse;
import com.example.redisscript.redisscriptdemo.respEnum.ResponseEnum;
import com.example.redisscript.redisscriptdemo.service.TryLockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Tag(name = "redis锁操作")
@Slf4j
@RestController
public class TryLockController {

    @Resource
    @Lazy
    private TryLockService tryLockService;

    @Operation(
            summary = "redis可重入锁测试接口"
            , parameters = {@Parameter(name = "key", description = "锁的键", in = ParameterIn.PATH)}
            , responses = {@ApiResponse(content = {@Content(schema = @Schema(oneOf = {BaseResponse.class, CommonResponse.class, ErrorResponse.class}))})}
    )
    @GetMapping("tryLockByKey/{key}")
    public ResponseEntity<BaseResponse> tryLock(@PathVariable("key") String key) {
        ResponseEnum.LOCK_KEY_HAS_NOT_TEXT.assertHasText(key, ResponseEnum.LOCK_KEY_HAS_NOT_TEXT.getMessage());
        boolean lockResult = tryLockService.tryLockByKey(key);
        return new ResponseEntity<>(new CommonResponse<>(JSONObject.parse("{\"lockResult\":\"" + lockResult + "\"}")), HttpStatus.OK);
    }
}
