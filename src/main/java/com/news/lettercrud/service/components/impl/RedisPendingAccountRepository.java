package com.news.lettercrud.service.components.impl;

import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.service.components.PendingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : Asnit Bakhati
 */
@Component
public class RedisPendingAccountRepository implements PendingAccountRepository {

    private static final int EXPIRY_TIME;

    private final RedisTemplate<String,BaseAccount> redisTemplate;

    static {
        EXPIRY_TIME=5;
    }

    @Autowired
    public RedisPendingAccountRepository(@Qualifier("redisTemplatePending") RedisTemplate<String, BaseAccount> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void store(String email, BaseAccount account) {
        redisTemplate.opsForValue().set(email,account,EXPIRY_TIME, TimeUnit.MINUTES);
    }

    @Override
    public Optional<BaseAccount> find(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(email));
    }

    @Override
    public void remove(String email) {
        redisTemplate.delete(email);
    }
}
