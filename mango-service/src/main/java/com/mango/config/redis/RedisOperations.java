package com.mango.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作类
 *
 */
@Component("redisOperations")
public class RedisOperations {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 默认超期时间
     */
    private static final Long TIMEOUT_SECONDS = 60 * 60 * 5L;

    /**
     * 向redis中设置值，不过期.
     *
     * @param key   key
     * @param value value
     */
    public void setNoExpire(String key, String value) {
        this.stringRedisTemplate.boundValueOps(key).set(value);
    }

    /**
     * 向redis中设置值，过期时间默认5小时.
     *
     * @param key   key
     * @param value value
     */
    public void set(String key, String value) {
        this.setEx(key, value, TIMEOUT_SECONDS);
    }

    /**
     * 向redis中设置值，并指定过期时间.
     *
     * @param key     key
     * @param value   value
     * @param seconds seconds
     */
    public void setEx(String key, String value, Long seconds) {
        this.stringRedisTemplate.boundValueOps(key).set(value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 向redis中设置值，如果key不存在则设置成功并返回true；如果key存在则设置失败并返回false.
     *
     * @param key   key
     * @param value value
     * @return 设置成功或失败
     */
    public Boolean setNx(String key, String value) {
        return this.stringRedisTemplate.boundValueOps(key).setIfAbsent(value);
    }

    /**
     * 获取redis中指定key的值.
     *
     * @param key key
     * @return 返回信息
     */
    public String get(String key) {
        return this.stringRedisTemplate.boundValueOps(key).get();
    }

    /**
     * 删除redis中指定key数据.
     *
     * @param key key
     */
    public void delete(String key) {
        this.stringRedisTemplate.delete(key);
    }

    /**
     * 判断redis中是否存在指定的key.
     *
     * @param key key
     * @return 如果key存在返回true，否则返回false
     */
    public Boolean exists(String key) {
        return this.stringRedisTemplate.hasKey(key);
    }

    /**
     * redis原子加1操作
     *
     * @param key key
     * @return 值
     */
    public Long incr(String key) {
        return this.incr(key, 1L);
    }

    /**
     * redis 原子加指定数值操作
     *
     * @param key   key
     * @param delta 指定数值
     * @return 新增后的数据
     */
    public Long incr(String key, Long delta) {
        return this.redisTemplate.boundValueOps(key).increment(delta);
    }

    /**
     * 向redis中设置Hash数据类型值.
     *
     * @param key       key
     * @param fieldName 字段名
     * @param value     返回值
     */
    public void setHashValue(String key, String fieldName, String value) {
        this.stringRedisTemplate.boundHashOps(key).put(fieldName, value);
    }

    /**
     * 获取redis中指定key的Hash值
     *
     * @param key       key
     * @param fieldName 字段名称
     * @return 字段值
     */
    public Object getHashValue(String key, String fieldName) {
        return this.stringRedisTemplate.boundHashOps(key).get(fieldName);
    }

}