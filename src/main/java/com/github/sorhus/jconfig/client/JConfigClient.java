package com.github.sorhus.jconfig.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author: anton.sorhus@gmail.com
 */
public class JConfigClient {

    private final String host;
    private final String port;

    public JConfigClient() throws IOException {
        this("/etc/jconfig/config");
    }

    public JConfigClient(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(path));
        this.host = properties.getProperty("HOST");
        this.port = properties.getProperty("PORT");
    }

    public String get(String key) {
        try {
            URL url = new URL(String.format("http://%s:%s/%s", host, port, key));
            return url.getContent().toString();
        } catch (IOException e) {
            return null;
        }
    }

}