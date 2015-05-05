package com.github.sorhus.jconfig.client;

import com.github.sorhus.jconfig.model.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author: anton.sorhus@gmail.com
 */
public class JConfigClient {

    private final ConfigClientImpl client;

    public JConfigClient(String path) throws IOException {
        Properties properties = new Properties();
        FileInputStream file = new FileInputStream(path);
        properties.load(file);
        file.close();
        String host = properties.getProperty("jconfig.host");
        int port = Integer.parseInt(properties.getProperty("jconfig.port"));
        boolean compress = Boolean.parseBoolean(properties.getProperty("jconfig.compress", "false"));
        this.client = new ConfigClientImpl(host, port, compress);
    }

    public JConfigClient(Properties properties) {
        String host = properties.getProperty("jconfig.host");
        int port = Integer.parseInt(properties.getProperty("jconfig.port"));
        boolean compress = Boolean.parseBoolean(properties.getProperty("jconfig.compress", "false"));
        this.client = new ConfigClientImpl(host, port, compress);
    }

    public JConfigClient() {
        this.client = new ConfigClientImpl("localhost", 8080, false);
    }

    public String get(String id) {
        return client.get(id);
    }

    public boolean put(String id, String json) {
        return client.put(id, json);
    }

    public boolean put(Config config) {
        return client.put(config);
    }

}