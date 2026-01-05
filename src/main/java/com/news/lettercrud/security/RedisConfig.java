package com.news.lettercrud.security;

import com.news.lettercrud.data.dto.LoginAttempt;
import com.news.lettercrud.data.model.BaseAccount;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author : Asnit Bakhati
 */

@Configuration
public class RedisConfig {

    @Bean(name = "redisTemplateLimiter")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RedisTemplate<String,Integer> redisTemplateLimiter(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


    @Bean(name="redisTemplatePending")
    public RedisTemplate<String, BaseAccount> redisTemplatePending(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,BaseAccount> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean(name = "redisTemplateVerification")
    public RedisTemplate<String, LoginAttempt> redisTemplateVerification(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,LoginAttempt> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean(name ="redisTemplateFilter")
    public RedisTemplate<String,String> redisTemplateFilter(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean (name="redisClient")
    public RedisClient redisClient(){
        return RedisClient.create("redis://localhost:6379");
    }

    @Bean
    public StatefulRedisConnection<String, byte[]> redisConnection(RedisClient redisClient) {
        return redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    @Bean
    public ProxyManager<String> proxyManager(StatefulRedisConnection<String, byte[]> connection) {
        return LettuceBasedProxyManager.builderFor(connection)
                .build();
    }
}
