package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.model.Config;

import java.util.Map;

/**
 * @author: anton.sorhus@gmail.com
 */
public interface DAO {

    public String get(String key);
    public void put(String key, String value);
    public void putAll(Iterable<Config> configs);
    public Iterable<Config> getAll();
}