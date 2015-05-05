package com.github.sorhus.jconfig.dao;

import org.junit.Test;
import redis.clients.jedis.JedisPool;

public class IT {

    @Test
    public void test() {
        DAO dao = new RedisDAO(new JedisPool("localhost",6379), "foo");
        dao.put("kye", "bar");
        String res = dao.get("kye");
        System.out.println(res);
    }
}
