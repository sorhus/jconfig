package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.model.Config;
import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author: anton.sorhus@gmail.com
 */
public class CachedDAO implements DAO {

    private final DAO dao;
    private final LoadingCache<String, String> cache;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public CachedDAO(final DAO dao, int size) {
        this.dao = dao;
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(size)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) {
                    return dao.get(s);
                }
            });
    }

    @Override
    public String get(String key) {
        try {
            log.debug("Cache lookup: {}", key);
            return cache.get(key);
        } catch (ExecutionException e) {
            log.debug("", e);
            return null;
        }
    }

    @Override
    public void put(String key, String value) {
        cache.invalidate(key);
        dao.put(key,value);
    }

    @Override
    public void putAll(Iterable<Config> configs) {
        cache.invalidateAll();
        dao.putAll(configs);
    }

    @Override
    public Iterable<Config> getAll() {
        return dao.getAll();
    }
}
