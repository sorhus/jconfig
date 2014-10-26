package com.github.sorhus.jconfig.dao;

/**
 * @author: anton.sorhus@gmail.com
 */
public interface DAO {

    public String get(String key);
    public void put(String key, String value);

}
