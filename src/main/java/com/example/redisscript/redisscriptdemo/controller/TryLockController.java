package com.example.redisscript.redisscriptdemo.controller;

import com.example.redisscript.redisscriptdemo.respEnum.ResponseEnum;
import com.example.redisscript.redisscriptdemo.service.TryLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class TryLockController {

    @Resource
    private TryLockService tryLockService;

    @RequestMapping("tryLockByKey/{key}")
    public void tryLock(@PathVariable("key")String key){
        ResponseEnum.LOCK_KEY_HAS_NOT_TEXT.assertHasText(key, ResponseEnum.LOCK_KEY_HAS_NOT_TEXT.getMessage());
        tryLockService.tryLockByKey(key);
    }
}