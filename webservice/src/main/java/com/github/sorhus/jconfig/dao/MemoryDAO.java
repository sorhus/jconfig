package com.github.sorhus.jconfig.dao;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: anton.sorhus@gmail.com
 */
public class MemoryDAO implements DAO {

    private final Map<String, String> map;

    public MemoryDAO(final int size) {
        this.map = new LinkedHashMap<String, String>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> entry) {
                return size() > size;
            }
        };
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public void put(String key, String value) {
        map.put(key, value);
    }
}
