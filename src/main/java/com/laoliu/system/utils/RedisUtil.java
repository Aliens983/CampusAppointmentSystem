package com.laoliu.system.utils;

import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * @author 25516
 */
@Component
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 存储验证码到Redis
     * @param key Redis键（可以是UUID或邮箱地址）
     * @param value 验证码值
     * @param timeout 过期时间（秒）
     */
    public void setVerificationCode(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取验证码
     * @param key Redis键
     * @return 验证码值
     */
    public String getVerificationCode(String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        return obj != null ? obj.toString() : null;
    }

    /**
     * 删除验证码
     * @param key Redis键
     */
    public void removeVerificationCode(String key) {
        redisTemplate.delete(key);
    }
}