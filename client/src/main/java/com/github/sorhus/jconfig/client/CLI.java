package com.github.sorhus.jconfig.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class CLI {

    @Parameter(names = {"-h", "--host"})
    protected String host = "localhost";

    @Parameter(names = {"-p", "--port"})
    protected int port = 8080;

    @Parameter(names = {"-i", "--id"}, required = true)
    private String id;

    @Parameter(names = {"-j", "--json"})
    private String json;

    @Parameter(names = {"-c", "--compress"})
    protected boolean compress = false;

    private ConfigClientImpl client;

    public static void main(String[] args) {
        CLI cli = new CLI();
        new JCommander(cli, args);
        cli.init();
        cli.process();
    }

    private void init() {
        this.client = new ConfigClientImpl(this.host, this.port, this.compress);
    }

    private void process() {
        if(json == null) {
            String result = client.get(id);
            if(null != result) {
                System.out.println(result);
            }
        } else {
            client.put(id, json);
        }
    }
}
