package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.model.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: anton.sorhus@gmail.com
 */
public class RedisDAO implements DAO {

    private final JedisPool jedisPool;
    private final String jc;

    public RedisDAO(JedisPool jedisPool, String jc) {
        this.jedisPool = jedisPool;
        this.jc = jc;
    }

    @Override
    public String get(String id) {
        try(Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(jc, id);
        }
    }

    @Override
    public void put(String id, String json) {
        try(Jedis jedis = jedisPool.getResource()) {
            jedis.hset(jc, id, json);
        }
    }

    @Override
    public void putAll(Iterable<Config> configs) {
        try(Jedis jedis = jedisPool.getResource()) {
            Pipeline pipeline = jedis.pipelined();
            for(Config config : configs) {
                pipeline.hset(jc, config.getId(), config.getJson());
            }
            pipeline.sync();
        }
    }
    @Override
    // TODO implement with scan
    public Iterable<Config> getAll() {
        try(Jedis jedis = jedisPool.getResource()) {
            Map<String,String> content = jedis.hgetAll(jc);
            List<Config> result = new LinkedList<>();
            for(Map.Entry<String,String> entry : content.entrySet()) {
                result.add(new Config(entry.getKey(), entry.getValue()));
            }
            return result;
        }
    }

}