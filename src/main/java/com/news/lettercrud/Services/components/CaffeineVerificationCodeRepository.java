package com.news.lettercrud.Services.components;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.news.lettercrud.Data.DTOs.LoginAttempt;
import com.news.lettercrud.Repositories.VerificationCodeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class CaffeineVerificationCodeRepository implements VerificationCodeRepository {
    private Cache<String, LoginAttempt> cache;

    @PostConstruct
    public void init() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }

    @Override
    public void store(String email, LoginAttempt attempt) {
        cache.put(email, attempt);
    }

    @Override
    public Optional<LoginAttempt> find(String email) {
        return Optional.ofNullable(cache.getIfPresent(email));
    }

    @Override
    public void remove(String email) {
        cache.invalidate(email);
    }

    @Override
    public boolean exists(String email) {
        return cache.getIfPresent(email) != null;
    }
}
