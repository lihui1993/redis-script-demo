package com.example.redisscript.redisscriptdemo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import javax.annotation.Resource;

@Configuration
public class RedisConfig {

    @Resource
    private Environment environment;

    @Bean
    @Lazy
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setTransportMode(TransportMode.NIO);
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://"
                .concat(environment.getProperty("spring.redis.host", "127.0.0.1")).concat(":")
                .concat(environment.getProperty("spring.redis.port", "6379")));
        return Redisson.create(config);
    }

    @Bean
    @Lazy
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(
                environment.getProperty("spring.redis.host", "127.0.0.1"),
                environment.getProperty("spring.redis.port", Integer.class, 6379));
    }

    /**
     * RedisTemplate配置
     *
     * @param lettuceConnectionFactory
     * @return
     */
    @Bean
    @Lazy
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
        return new StringRedisTemplate(lettuceConnectionFactory);
    }

    @Bean(name = "lockDelRedisScript")
    @Lazy
    public DefaultRedisScript<Long> lockDelRedisScript() {
        DefaultRedisScript<Long> unLock = new DefaultRedisScript<>();
        unLock.setScriptSource(new ResourceScriptSource(new ClassPathResource("lockDel.lua")));
        unLock.setResultType(Long.class);
        return unLock;
    }

}
