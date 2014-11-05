package com.github.sorhus.jconfig.model;

/**
 * @author: anton.sorhus@gmail.com
 */
public class Config {

    private final String id;
    private final String json;

    public Config(String id, String json) {
        this.id = id;
        this.json = json;
    }

    public String getId() {
        return id;
    }

    public String getJson() {
        return json;
    }

}
