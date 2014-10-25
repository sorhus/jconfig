package com.github.sorhus.jconfig.service;

/**
 * @author: anton.sorhus@gmail.com
 */
public interface Service {

    public String get(String key);
    public void put(String key, String value);

}
