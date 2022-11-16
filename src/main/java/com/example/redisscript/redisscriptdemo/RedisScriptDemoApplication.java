package com.example.redisscript.redisscriptdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.redisscript.redisscriptdemo.mapper")
public class RedisScriptDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisScriptDemoApplication.class, args);
    }

}
