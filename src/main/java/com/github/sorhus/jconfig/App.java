package com.github.sorhus.jconfig;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class App {
    public static void main(String[] args) throws Exception {
        int port = args.length == 1 ? Integer.parseInt(args[0]) : 8080;
        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(JConfigServlet.class, "/hello");
        server.setHandler(handler);
        server.start();
        server.join();
    }
}
