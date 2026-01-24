package com.news.lettercrud.service.components.impl;

import com.news.lettercrud.service.components.PasswordLimiter;
import com.news.lettercrud.util.CustomRedisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

/**
 * Redis-based password attempt rate limiter.
 * Tracks failed login attempts per email address with automatic expiration.
 *
 * @author Asnit Bakhati
 */
@Component
public class RedisPasswordLimiter implements PasswordLimiter {

    private static final Duration EXPIRY_DURATION = Duration.ofMinutes(5);
    private static final String KEY_PREFIX = "password:attempt:";

    private final CustomRedisMapper<String, Integer> redisMapper;

    @Autowired
    public RedisPasswordLimiter(@Qualifier("redisTemplateLimiter") CustomRedisMapper<String, Integer> redisMapper) {
        this.redisMapper = redisMapper;
    }

    /**
     * Generate Redis key for email.
     */
    private String getKey(String email) {
        return KEY_PREFIX + email.toLowerCase();
    }

    /**
     * Get the number of tries for a specific email.
     */
    @Override
    public Optional<Integer> getTries(String email) {
        String key = getKey(email);
        return redisMapper.getAndRefreshOptional(key, EXPIRY_DURATION);
    }

    /**
     * Set the number of tries for a specific email.
     */
    @Override
    public void setTries(String email, int tryNumber) {
        String key = getKey(email);
        redisMapper.set(key, tryNumber, EXPIRY_DURATION);
    }

    /**
     * Increment tries counter.
     */
    public int incrementTries(String email) {
        String key = getKey(email);
        Long count = redisMapper.increment(key);
        redisMapper.setExpire(key, EXPIRY_DURATION);
        return count != null ? count.intValue() : 1;
    }

    /**
     * Remove the key on successful login if tryNumber is not 1.
     */
    @Override
    public void onSuccessRemove(String email, int tryNumber) {
        if (tryNumber != 1) {
            String key = getKey(email);
            redisMapper.remove(key);
        }
    }

    /**
     * Clear all attempts for an email.
     */
    public void clearTries(String email) {
        String key = getKey(email);
        redisMapper.remove(key);
    }

    /**
     * Check if email is locked out when exceeded max attempts.
     */
    public boolean isLockedOut(String email, int maxAttempts) {
        return getTries(email).map(tries -> tries >= maxAttempts).orElse(false);
    }
}