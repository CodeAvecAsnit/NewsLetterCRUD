package com.news.lettercrud.Services.components;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.news.lettercrud.Data.model.BaseAccount;
import com.news.lettercrud.Repositories.PendingAccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
    public class CaffeinePendingAccountRepository implements PendingAccountRepository {
        private Cache<String, BaseAccount> cache;

        @PostConstruct
        public void init() {
            this.cache = Caffeine.newBuilder()
                    .expireAfterWrite(5, TimeUnit.MINUTES)
                    .maximumSize(1000)
                    .build();
        }

        @Override
        public void store(String email, BaseAccount account) {
            cache.put(email, account);
        }

        @Override
        public Optional<BaseAccount> find(String email) {
            return Optional.ofNullable(cache.getIfPresent(email));
        }

        @Override
        public void remove(String email) {
            cache.invalidate(email);
        }
    }
