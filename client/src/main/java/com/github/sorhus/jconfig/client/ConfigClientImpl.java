package com.github.sorhus.jconfig.client;

import com.github.sorhus.jconfig.compression.GZipCompressor;
import com.github.sorhus.jconfig.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

class ConfigClientImpl {

    private final boolean compress;
    private final String baseGetURL;
    private final String baseSetURL;

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final GZipCompressor compressor = new GZipCompressor();

    ConfigClientImpl(String host, int port, boolean compress) {
        this.compress = compress;
        this.baseGetURL = String.format("http://%s:%d/api/get?id=", host, port);
        this.baseSetURL = String.format("http://%s:%d/api/set", host, port);
    }

    String get(String id) {
        try {
            URL url = new URL(baseGetURL + id);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return compress ? compressor.decompress(response.toString()) : response.toString();
        } catch (FileNotFoundException e) {
            log.info("Get failed:", e);
        } catch (IOException e) {
            log.warn("Get failed:", e);
        }
        return null;
    }

    boolean put(String id, String json) {
        try {
            URL url = new URL(baseSetURL);
            String output = String.format("id=%s&json=%s", id, compress ? compressor.compress(json) : json);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(output);
            outputStream.close();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            log.info("Put failed:", e);
            return false;
        }
    }

    boolean put(Config config) {
        return put(config.getId(), config.getJson());
    }
}
