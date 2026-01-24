package com.news.lettercrud.security;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.news.lettercrud.data.dto.LoginAttempt;
import com.news.lettercrud.data.model.BaseAccount;
import com.news.lettercrud.util.CustomRedisMapper;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis configuration for various use cases including rate limiting,
 * session management, and caching.
 *
 * @author Asnit Bakhati
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;


    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return mapper;
    }

    /**
     * Primary Redis connection factory using Lettuce.
     */
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
        return factory;
    }

    /**
     * Redis template for password rate limiting.
     */
    @Bean(name = "redisTemplateLimiter")
    public CustomRedisMapper<String, Integer> redisTemplateLimiter(RedisConnectionFactory redisConnectionFactory) {
        CustomRedisMapper<String, Integer> template = new CustomRedisMapper<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Redis template for pending account storage.
     */
    @Bean(name = "redisTemplatePending")
    public CustomRedisMapper<String, BaseAccount> redisTemplatePending(
            RedisConnectionFactory redisConnectionFactory,
            ObjectMapper redisObjectMapper) {
        CustomRedisMapper<String, BaseAccount> template = new CustomRedisMapper<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(redisObjectMapper);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Redis template for login verification attempts.
     */
    @Bean(name = "redisTemplateVerification")
    public CustomRedisMapper<String, LoginAttempt> redisTemplateVerification(
            RedisConnectionFactory redisConnectionFactory,
            ObjectMapper redisObjectMapper) {
        CustomRedisMapper<String, LoginAttempt> template = new CustomRedisMapper<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(redisObjectMapper);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Redis template for filter operations.
     */
    @Bean(name = "redisTemplateFilter")
    public CustomRedisMapper<String, String> redisTemplateFilter(RedisConnectionFactory redisConnectionFactory) {
        CustomRedisMapper<String, String> template = new CustomRedisMapper<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Lettuce Redis client for Bucket-4j rate limiting.
     */
    @Bean(name = "redisClient")
    public RedisClient redisClient() {
        String redisUrl = String.format("redis://%s:%d", redisHost, redisPort);
        return RedisClient.create(redisUrl);
    }

    /**
     * Stateful connection for Bucket-4j proxy manager.
     */
    @Bean
    public StatefulRedisConnection<String, byte[]> redisConnection(RedisClient redisClient) {
        return redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    /**
     * Bucket-4j proxy manager for distributed rate limiting.
     */
    @Bean
    public ProxyManager<String> proxyManager(StatefulRedisConnection<String, byte[]> connection) {
        return LettuceBasedProxyManager.builderFor(connection).build();
    }
}