package com.example.redisscript.redisscriptdemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisLock {

    private static RedisTemplate<String, String> redisTemplate;


    private static DefaultRedisScript<Long> lockDelRedisScript;

    public static boolean tryLock(String key, String value, long timeOut, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeOut, timeUnit);
    }

    public static boolean tryLock(String key, String value, Duration duration) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, duration);
    }

    public static boolean unLock(String key, String value) {
        return  redisTemplate.execute(RedisLock.lockDelRedisScript, Collections.singletonList(key), value) != 0L;
    }

    @Resource(name = "lockDelRedisScript")
    public void setUnLockRedisScript(DefaultRedisScript<Long> lockDelRedisScript) {
        RedisLock.lockDelRedisScript = lockDelRedisScript;
    }

    @Resource
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        RedisLock.redisTemplate = redisTemplate;
    }
}
