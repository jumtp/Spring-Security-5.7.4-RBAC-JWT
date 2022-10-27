package com.example.demo_security.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class FastRedis {

    private final Logger log = LoggerFactory.getLogger(FastRedis.class);
    private final long ttl = 1000 * 10;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.MILLISECONDS);
        log.info("redis set key = {} , value = {} ", key, value);
    }

    public Object get(String key) {
        Object o = redisTemplate.opsForValue().get(key);
        log.info("redis get key = {} , value = {}", key, o);
        return o;
    }

    public boolean del(String key) {
        Boolean result = redisTemplate.delete(key);
        log.info("redis delete key = {} , result = {}", key, result);
        return Boolean.TRUE.equals(result);
    }

    public Long getExpireTime(String key) {
        return redisTemplate.opsForValue().getOperations().getExpire(key);
    }
}
