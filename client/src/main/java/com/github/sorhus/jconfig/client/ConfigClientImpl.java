package com.github.sorhus.jconfig.client;

import com.github.sorhus.jconfig.compression.GZipCompressor;
import com.github.sorhus.jconfig.model.Config;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class ConfigClientImpl {

    private final boolean compress;
    private final String baseURL;

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Gson gson = new Gson();
    private final GZipCompressor compressor = new GZipCompressor();

    ConfigClientImpl(String host, int port, boolean compress) {
        this.compress = compress;
        this.baseURL = String.format("http://%s:%d/api/v1/", host, port);
    }

    String get(String key) {
        try {
            InputStream input = new URL(baseURL + "get?key=" + key).openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return compress ? compressor.decompress(response.toString()) : response.toString();
        } catch (FileNotFoundException e) {
            log.info("Get failed:", e);
        } catch (IOException e) {
            log.warn("Get failed:", e);
        }
        return null;
    }

    boolean put(String key, String value) {
        try {
            if(compress) {
                value = compressor.compress(value);
            }
            String output = "key=" + key + "&value=" + value;
            HttpURLConnection connection = (HttpURLConnection) new URL(baseURL + "set").openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(output);
            outputStream.close();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            log.warn("Put failed:", e);
            return false;
        }
    }

    boolean put(Config config) {
        return put(config.getKey(), config.getValue());
    }
}
