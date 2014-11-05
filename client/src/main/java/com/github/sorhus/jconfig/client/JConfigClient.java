package com.github.sorhus.jconfig.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.github.sorhus.jconfig.compression.GZipCompressor;
import com.github.sorhus.jconfig.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * @author: anton.sorhus@gmail.com
 */
public class JConfigClient {

    @Parameter(names = {"-h", "--host"})
    private String host = "localhost";

    @Parameter(names = {"-p", "--port"})
    private int port = 8080;

    @Parameter(names = {"-i", "--id"}, required = true)
    private String id; // only used through cli

    @Parameter(names = {"-j", "--json"})
    private String json; // only used through cli

    @Parameter(names = {"-c", "--doCompress"})
    private boolean doCompress = false;

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final GZipCompressor compressor = new GZipCompressor();

    public JConfigClient() throws IOException {
        this("/etc/jconfig/config");
    }

    public JConfigClient(String path) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(path));
        this.host = properties.getProperty("HOST");
        this.port = Integer.parseInt(properties.getProperty("PORT"));
    }

    private JConfigClient(boolean dummy) {}

    public String get(String id) {
        try {
            URL url = new URL(String.format("http://%s:%d/api/get?id=%s", host, port, id));
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return doCompress ? compressor.decompress(response.toString()) : response.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            log.info("Get failed:", e);
        }
        return null;
    }

    public boolean put(String id, String json) {
        try {
            URL url = new URL(String.format("http://%s:%d/api/set", host, port));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            String output = String.format("id=%s&json=%s", id, doCompress ? compressor.compress(json) : json);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(output);
            outputStream.close();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            log.info("Put failed:", e);
            return false;
        }
    }

    public void put(Config config) {
        put(config.getId(), config.getJson());
    }

    private void process() {
        if(json == null) {
            String result = get(id);
            if(null != result) {
                System.out.println(result);
            }
        } else {
            put(id, json);
        }
    }

    public static void main(String[] args) {
        JConfigClient client = new JConfigClient(false);
        new JCommander(client, args);
        client.process();
    }

}