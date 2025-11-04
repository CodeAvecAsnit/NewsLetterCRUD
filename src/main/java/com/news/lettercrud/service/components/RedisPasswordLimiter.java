 package com.news.lettercrud.service.components;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.annotation.Qualifier;
 import org.springframework.data.redis.core.RedisTemplate;
 import org.springframework.stereotype.Component;

 import java.util.Optional;
 import java.util.concurrent.TimeUnit;

 /**
  * @author : Asnit Bakhati
  */
 @Component
public class RedisPasswordLimiter implements PasswordLimiter{

     private static final int EXPIRY_TIME;

     private final RedisTemplate<String,Integer> redisTemplate;

     static {
         EXPIRY_TIME=5;
     }

     @Autowired
     public RedisPasswordLimiter(@Qualifier("redisTemplateLimiter") RedisTemplate<String, Integer> redisTemplate) {
         this.redisTemplate = redisTemplate;
     }

     /**
      * Get the number of tries for a specific email.
      * @param email user email
      * @return Optional containing number of tries if present
      */
     @Override
     public Optional<Integer> getTries(String email) {
         Integer num = redisTemplate.opsForValue().get(email);
         if(num!=null){
             redisTemplate.expire(email,EXPIRY_TIME, TimeUnit.MINUTES);
         }
         return Optional.ofNullable(num);
     }

     /**
      * Set the number of tries for a specific email.
      * @param email user email
      * @param tryNumber number of tries
      */
     @Override
     public void setTries(String email, int tryNumber) {
         redisTemplate.opsForValue().set(email,tryNumber,EXPIRY_TIME,TimeUnit.MINUTES);
     }

     /**
      * Remove the key on successful login if tryNumber is not 1.
      * @param email user email
      * @param tryNumber number of tries (1 means first try and success on first try so no need to store in cache)
      */
     @Override
     public void onSuccessRemove(String email, int tryNumber) {
         if(tryNumber!=1){
             redisTemplate.delete(email);
         }

     }
 }