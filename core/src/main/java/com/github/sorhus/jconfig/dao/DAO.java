package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.model.Config;

import java.util.Map;

/**
 * @author: anton.sorhus@gmail.com
 */
public interface DAO {

    String get(String key);
    void put(String key, String value);
    void putAll(Iterable<Config> configs);
    Iterable<Config> getAll();
}