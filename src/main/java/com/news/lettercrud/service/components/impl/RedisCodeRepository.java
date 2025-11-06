package com.news.lettercrud.service.components.impl;

import com.news.lettercrud.data.DTOs.LoginAttempt;
import com.news.lettercrud.service.components.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCodeRepository implements VerificationCodeRepository {

    private static final int EXPIRY_TIME;

    private final RedisTemplate<String,LoginAttempt> template;

    static {
        EXPIRY_TIME=5;
    }

    @Autowired
    public RedisCodeRepository(@Qualifier("redisTemplateVerification")RedisTemplate<String,LoginAttempt> template){
        this.template=template;
    }

    @Override
    public void store(String email, LoginAttempt attempt) {
        template.opsForValue().set(email,attempt,EXPIRY_TIME, TimeUnit.MINUTES);
    }

    @Override
    public Optional<LoginAttempt> find(String email) {
        return Optional.ofNullable(template.opsForValue().get(email));
    }

    @Override
    public void remove(String email) {
        template.delete(email);
    }

    @Override
    public boolean exists(String email) {
        return find(email).isPresent();
    }
}
