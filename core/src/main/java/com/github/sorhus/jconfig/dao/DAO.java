package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.model.Config;

import java.util.Map;

/**
 * @author: anton.sorhus@gmail.com
 */
public interface DAO {

    public String get(String id);
    public void put(String id, String json);
    public void putAll(Iterable<Config> configs);
    public Iterable<Config> getAll();
}