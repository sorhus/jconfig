package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.model.Config;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: anton.sorhus@gmail.com
 */
public class MemoryDAO implements DAO {

    private final Map<String, String> map;

    public MemoryDAO(final int size) {
        this.map = new LinkedHashMap<String, String>(size + 1, 1.0f, true) {
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

    @Override
    public void putAll(Iterable<Config> configs) {
        for(Config config : configs) {
            put(config.getKey(), config.getValue());
        }
    }

    @Override
    public Iterable<Config> getAll() {
        return new MemoryIterable();
    }

    public class MemoryIterable implements Iterable<Config>{
        Iterator<Map.Entry<String,String>> it = map.entrySet().iterator();
        @Override
        public Iterator<Config> iterator() {
            return new Iterator<Config>() {
                @Override
                public Config next() {
                    Map.Entry<String, String> next = it.next();
                    return new Config(next.getKey(), next.getValue());
                }
                @Override
                public void remove() { it.remove(); }
                @Override
                public boolean hasNext() { return it.hasNext(); }
            };
        }
    }
}
