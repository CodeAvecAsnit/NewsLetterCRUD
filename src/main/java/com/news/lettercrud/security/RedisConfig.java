package com.news.lettercrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean(name = "redisTemplateLimiter")
    public RedisTemplate<String,Integer> redisTemplateLimiter(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
