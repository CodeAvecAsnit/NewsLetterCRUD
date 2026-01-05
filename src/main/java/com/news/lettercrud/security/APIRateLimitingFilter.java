package com.news.lettercrud.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@Order(1)
public class APIRateLimitingFilter extends OncePerRequestFilter {

    private final ProxyManager<String> proxyManager;
    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_REQUESTS_PER_MINUTE = 20;
    private static final String PRISON_KEY_PREFIX = "rate_limit:prison:";
    private static final String BUCKET_KEY_PREFIX = "rate_limit:bucket:";
    private static final long PRISON_DURATION_HOURS = 1;

    @Autowired
    public APIRateLimitingFilter(ProxyManager<String> proxyManager, @Qualifier("redisTemplateFilter") RedisTemplate<String, String> redisTemplate) {
        this.proxyManager = proxyManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       log.warn("Triggered");
       String userIP = getClientIPAddress(request);
       String prisonKey = PRISON_KEY_PREFIX+userIP;
       if(redisTemplate.hasKey(prisonKey)){
           Long ttl = redisTemplate.getExpire(prisonKey, TimeUnit.MINUTES);
           response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
           response.setContentType("application/json");
           response.getWriter().write(String.format(
                   "{\"error\": \"Too many requests. IP blocked. Try again in %d minutes.\"}",
                   ttl != null ? ttl : PRISON_DURATION_HOURS * 60
           ));
           return;
       }
        String bucketKey = BUCKET_KEY_PREFIX + userIP;
        Bucket bucket = proxyManager.builder().build(bucketKey, bucketConfiguration());

        if(bucket.tryConsume(1))
            filterChain.doFilter(request, response);
        else{
            sendToPrison(userIP);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                    "{\"error\": \"Rate limit exceeded. IP blocked for %d hour(s).\"}",
                    PRISON_DURATION_HOURS
            ));
        }
    }

    private Supplier<BucketConfiguration> bucketConfiguration() {
        return () -> {
            Bandwidth limit = Bandwidth.builder()
                    .capacity(MAX_REQUESTS_PER_MINUTE)
                    .refillGreedy(MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1))
                    .build();

            return BucketConfiguration.builder()
                    .addLimit(limit)
                    .build();
        };
    }

    private void sendToPrison(String ipAddress) {
        String prisonKey = PRISON_KEY_PREFIX + ipAddress;
        redisTemplate.opsForValue().set(
                prisonKey,
                String.valueOf(System.currentTimeMillis()),
                PRISON_DURATION_HOURS,
                TimeUnit.HOURS
        );
    }

    private String getClientIPAddress(HttpServletRequest request){
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
