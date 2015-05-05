package com.github.sorhus.jconfig.client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Scanner;

public class CLI {

    @Parameter(names = {"--help"})
    private boolean help = false;

    @Parameter(names = {"-h", "--host"}, description = "Host of the server you want to connect to")
    private String host = "localhost";

    @Parameter(names = {"-p", "--port"}, description = "Port of the server you want to connect to")
    private int port = 8080;

    @Parameter(names = {"-k", "--key"}, required = true, description = "The key of the element you want to access")
    private String key;

    @Parameter(names = {"-v", "--value"}, description = "Specify the new value associated to the key inline")
    private String value;

    @Parameter(names = {"-s", "--stdin"}, description = "Specify the new value associated to the key via stdin")
    private boolean stdin;

    @Parameter(names = {"-c", "--compress"}, description = "Compress the value before sending it to the server")
    private boolean compress = false;

    @Parameter(names = {"-pp", "--pretty-print"}, description = "Pretty print the json")
    private boolean pretty = false;

    private ConfigClientImpl client;

    public static void main(String[] args) {
        CLI cli = new CLI();
        JCommander jCommander = new JCommander(cli);
        jCommander.setProgramName("java -jar jconfig-client.jar");
        try {
            jCommander.parse(args);
            if(cli.help) {
                CLI.usage(jCommander, true);
            }
            cli.init();
            boolean success = cli.process();
            System.exit(success ? 0 : 1);
        } catch(ParameterException e) {
            CLI.usage(jCommander, false);
        }
    }

    private void init() {
        if(stdin && value != null) {
            throw new ParameterException("Conflicting flags. Specify --value or --stdin, not both.");
        }
        this.client = new ConfigClientImpl(host, port, compress);
    }

    private boolean process() {
        if(stdin) {
            value = read();
        }
        if(value != null) {
            return client.put(key, value);
        } else {
            String result = client.get(key);
            if(result != null) {
                if(pretty) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    result = gson.toJson(gson.fromJson(result, Object.class));
                }
                System.out.println(result);
                return true;
            } else {
                System.err.println("No value associated to the key " + key);
                return false;
            }
        }
    }

    private String read() {
        try(Scanner scanner = new Scanner(System.in)) {
            StringBuilder sb = new StringBuilder();
            while(scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            return sb.toString();
        }
    }

    private static void usage(JCommander jCommander, boolean success) {
        StringBuilder sb = new StringBuilder();
        jCommander.usage(sb);
        if(success) {
            System.out.println(sb.toString());
            System.exit(0);
        } else {
            System.err.println(sb.toString());
            System.exit(1);
        }

    }
}
