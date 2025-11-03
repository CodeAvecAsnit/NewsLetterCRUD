package com.news.lettercrud.service.components;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.news.lettercrud.data.DTOs.LoginDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : Asnit Bakhati
 */
@Component
public class PasswordLimiter {

    private final PasswordEncoder passwordEncoder;

    private Cache<String,Integer> limiter;

    @Autowired
    public PasswordLimiter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        this.limiter = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
    }


    public static void main(String[] args) {
        Cache<String,Integer> data = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();
        String k = "random@gmail.com";
        Integer m = data.getIfPresent(k);
        if(m==null){
            System.out.println(" m is null");
        }

    }



}
