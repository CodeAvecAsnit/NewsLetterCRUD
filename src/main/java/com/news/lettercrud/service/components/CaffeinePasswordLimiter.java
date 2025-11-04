package com.news.lettercrud.service.components;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : Asnit Bakhati
 */
@Component
public class CaffeinePasswordLimiter implements PasswordLimiter {

    private Cache<String,Integer> limiter;

    public CaffeinePasswordLimiter() {
    }

    @PostConstruct
    public void init() {
        this.limiter = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    @Override
    public Optional<Integer> getTries(String email){
        return Optional.ofNullable(limiter.getIfPresent(email));
    }

    @Override
    public void setTries(String email,int tryNumber){
        limiter.put(email,tryNumber);
    }

    @Override
    public void onSuccessRemove(String email,int tryNumber){
        if(tryNumber!=1){
            limiter.invalidate(email);
        }
    }
}
