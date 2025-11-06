package com.news.lettercrud.security;

import com.news.lettercrud.data.DTOs.LoginAttempt;
import com.news.lettercrud.data.model.BaseAccount;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author : Asnit Bakhati
 */

@Configuration
public class RedisConfig {

    @Bean(name = "redisTemplateLimiter")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RedisTemplate<String,Integer> redisTemplateLimiter(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


    @Bean(name="redisTemplatePending")
    public RedisTemplate<String, BaseAccount> redisTemplatePending(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,BaseAccount> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean(name = "redisTemplateVerification")
    public RedisTemplate<String, LoginAttempt> redisTemplateVerification(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,LoginAttempt> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
