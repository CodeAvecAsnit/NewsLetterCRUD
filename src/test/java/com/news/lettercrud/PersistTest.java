package com.news.lettercrud;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.news.lettercrud.Data.model.BaseAccount;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class PersistTest {

    private Cache<String, BaseAccount> cache;

    @PostConstruct
    public void init() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }


    public void store(String email, BaseAccount account) {
        cache.put(email, account);
    }

    public Optional<BaseAccount> find(String email) {
        return Optional.ofNullable(cache.getIfPresent(email));
    }

    public void remove(String email) {
        cache.invalidate(email);
    }
}

}
