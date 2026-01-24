package com.news.lettercrud.service.components.impl;

import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.service.components.PendingAccountRepository;
import com.news.lettercrud.util.CustomRedisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redis-based repository for managing pending account registrations.
 * Stores temporary account data with automatic expiration for email verification workflow.
 *
 * @author Asnit Bakhati
 */
@Repository
public class RedisPendingAccountRepository implements PendingAccountRepository {

    private static final Logger logger = LoggerFactory.getLogger(RedisPendingAccountRepository.class);
    private static final String KEY_PREFIX = "pending:account:";

    private final CustomRedisMapper<String, BaseAccount> redisMapper;
    private final Duration expiryDuration;

    @Autowired
    public RedisPendingAccountRepository(
            @Qualifier("redisTemplatePending") CustomRedisMapper<String, BaseAccount> redisMapper,
            @Value("${app.pending-account.expiry-minutes:5}") int expiryMinutes) {
        this.redisMapper = redisMapper;
        this.expiryDuration = Duration.ofMinutes(expiryMinutes);
        logger.info("Initialized RedisPendingAccountRepository with expiry duration: {} minutes", expiryMinutes);
    }

    /**
     * Generate Redis key for email.
     */
    private String getKey(String email) {
        return KEY_PREFIX + email.toLowerCase();
    }

    /**
     * Store pending account with expiration.
     */
    @Override
    public void store(String email, BaseAccount account) {
        String key = getKey(email);
        redisMapper.set(key, account, expiryDuration);
        logger.debug("Stored pending account for email: {}", email);
    }

    /**
     * Store pending account with custom expiration duration.
     */
    public void store(String email, BaseAccount account, Duration duration) {
        String key = getKey(email);
        redisMapper.set(key, account, duration);
        logger.debug("Stored pending account for email: {} with custom expiry: {}", email, duration);
    }

    /**
     * Find pending account by email.
     */
    @Override
    public Optional<BaseAccount> find(String email) {
        String key = getKey(email);
        Optional<BaseAccount> account = redisMapper.getOptional(key);

        if (account.isPresent()) logger.debug("Found pending account for email: {}", email);
        else logger.debug("No pending account found for email: {}", email);

        return account;
    }


    /**
     * Remove pending account by email.
     */
    @Override
    public void remove(String email) {
        String key = getKey(email);
        Boolean removed = redisMapper.remove(key);

        if (Boolean.TRUE.equals(removed)) {
            logger.debug("Removed pending account for email: {}", email);
        } else {
            logger.debug("No pending account to remove for email: {}", email);
        }
    }

    /**
     * Check if pending account exists for email.
     */
    public boolean exists(String email) {
        String key = getKey(email);
        Boolean exists = redisMapper.exists(key);
        return Boolean.TRUE.equals(exists);
    }

    /**
     * Get remaining time until account expires.
     */
    public long getRemainingTimeSeconds(String email) {
        String key = getKey(email);
        Long ttl = redisMapper.getExpire(key, java.util.concurrent.TimeUnit.SECONDS);
        return ttl != null ? ttl : -2L;
    }


    /**
     * Extend expiration time for existing pending account.
     */
    public boolean extendExpiration(String email, Duration additionalTime) {
        String key = getKey(email);
        if (Boolean.TRUE.equals(redisMapper.exists(key))) {
            Boolean extended = redisMapper.setExpire(key, additionalTime);
            if (Boolean.TRUE.equals(extended)) {
                logger.debug("Extended expiration for pending account: {} by {}", email, additionalTime);
                return true;
            }
        }
        return false;
    }

    /**
     * Get all pending account keys for admin and monitoring purpose.
     */
    public Set<String> getAllPendingEmails() {
        Set<String> keys = redisMapper.keys(KEY_PREFIX + "*");
        return keys.stream()
                .map(key -> key.substring(KEY_PREFIX.length()))
                .collect(Collectors.toSet());
    }

    /**
     * Count total pending accounts.
     */
    public long count() {
        Set<String> keys = redisMapper.keys(KEY_PREFIX + "*");
        return keys.size();
    }

    /**
     * Clear all pending accounts
     */
    public long clearAll() {
        Set<String> keys = redisMapper.keys(KEY_PREFIX + "*");
        if (!keys.isEmpty()) {
            Long deleted = redisMapper.remove(keys);
            logger.warn("Cleared all pending accounts. Total deleted: {}", deleted);
            return deleted != null ? deleted : 0L;
        }
        return 0L;
    }
}
