package com.github.sorhus.jconfig.service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author: anton.sorhus@gmail.com
 */
public class RedisService implements Service {

    JedisPool jedisPool;

    public RedisService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    @Override
    public void put(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
        jedis.close();
    }
}
