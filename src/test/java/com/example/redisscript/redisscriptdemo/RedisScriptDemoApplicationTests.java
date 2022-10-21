package com.example.redisscript.redisscriptdemo;

import com.example.redisscript.redisscriptdemo.utils.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@Slf4j
class RedisScriptDemoApplicationTests {

    @Resource
    private RedissonClient redissonClient;

    @Test
    void contextLoads() {
        String key = UUID.randomUUID().toString();
        String value = key.replace("-", "");
        boolean lock = RedisLock.tryLock(key, value, Duration.ofMinutes(2));
        log.info("key:{},value:{},lock:{}", key, value, lock);

        String value3 = value + "1";
        boolean unLockValue = RedisLock.unLock(key, value3);
        log.info("key:{},value:{},unLock:{}", key, value3, unLockValue);

        boolean unLock = RedisLock.unLock(key, value);
        log.info("key:{},value:{},unLock:{}", key, value, unLock);

        String key2 = UUID.randomUUID().toString();
        String value2 = key2.replace("-", "");
        boolean lock2 = RedisLock.tryLock(key2, value2, 2, TimeUnit.MINUTES);
        log.info("key:{},value:{},lock:{}", key2, value2, lock2);
        boolean unLock2 = RedisLock.unLock(key2, value2);
        log.info("key:{},value:{},unLock:{}", key2, value2, unLock2);

    }

    @Test
    public void testRedisson() throws InterruptedException {
        String key = UUID.randomUUID().toString();
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                RLock lock = redissonClient.getLock(key);
                try {
                    boolean lockResult = lock.tryLock(1000 * 11, 1000 * 20, TimeUnit.MILLISECONDS);
                    log.info("key:{},lock:{},threadId:{}", key, lockResult, Thread.currentThread().getId());
                    if (lockResult) {
                        boolean lockResult2 = lock.tryLock(1000 * 11, 1000 * 20, TimeUnit.MILLISECONDS);
                        log.info("key:{},lock2:{},threadId:{}", key, lockResult2, Thread.currentThread().getId());
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
                    }
                }
            }).start();
        }
        Thread.sleep(1000 * 50);
    }

}
