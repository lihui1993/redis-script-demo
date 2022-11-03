package com.example.redisscript.redisscriptdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TryLockService {

    @Resource
    private RedissonClient redissonClient;


    public boolean tryLockByKey(String key) {
        RLock lock = redissonClient.getLock(key);
        boolean lockResult0 = false;
        try {
            log.info("key:{},threadId:{},tryLock start", key, Thread.currentThread().getId());
            boolean lockResult = lock.tryLock(1000, 1000 * 20, TimeUnit.MILLISECONDS);
            log.info("key:{},lock:{},threadId:{}", key, lockResult, Thread.currentThread().getId());
            if (lockResult) {
                boolean lockResult2 = lock.tryLock(1000, 1000 * 20, TimeUnit.MILLISECONDS);
                log.info("key:{},lock2:{},threadId:{}", key, lockResult2, Thread.currentThread().getId());
                log.info("threadId:{},sleep", Thread.currentThread().getId());
                Thread.sleep(1000 * 10);
            }
        } catch (InterruptedException e) {
            log.error("锁异常", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                int holdCount = lock.getHoldCount();
                log.info("threadId:{},holdCount:{}", Thread.currentThread().getId(), holdCount);
                for (int j = 1; j <= holdCount; j++) {
                    lock.unlock();
                    log.info("key:{},threadId:{},释放锁{}", key, Thread.currentThread().getId(), j);
                }
                lockResult0 = true;
            }
        }
        return lockResult0;
    }
}
