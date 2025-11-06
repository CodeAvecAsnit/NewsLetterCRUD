package com.news.lettercrud.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisTest {

    @Autowired
    @Qualifier("redisTemplateLimiter")
    private RedisTemplate<String,Integer> redisTemplate;

    @Test
    public void testRedis(){
        Integer num = 1;
        redisTemplate.opsForValue().set("email",num);

        Integer val = redisTemplate.opsForValue().get("email");
        assertEquals(num,val);
    }
}
