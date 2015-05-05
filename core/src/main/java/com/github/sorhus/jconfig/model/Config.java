package com.github.sorhus.jconfig.model;

/**
 * @author: anton.sorhus@gmail.com
 */
public class Config {

    private final String key;
    private final String value;

    public Config(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
