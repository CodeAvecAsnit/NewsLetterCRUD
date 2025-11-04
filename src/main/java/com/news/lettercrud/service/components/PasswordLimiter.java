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
public class PasswordLimiter {


    private Cache<String,Integer> limiter;


    public PasswordLimiter() {
    }

    @PostConstruct
    public void init() {
        this.limiter = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }


    public Optional<Integer> getTries(String email){
        return Optional.ofNullable(limiter.getIfPresent(email));
    }

    public void setTries(String email,int tryNumber){
        limiter.put(email,tryNumber);
    }

    public void onSuccessRemove(String email,int tryNumber){
        if(tryNumber!=1){
            limiter.invalidate(email);
        }
    }


}
