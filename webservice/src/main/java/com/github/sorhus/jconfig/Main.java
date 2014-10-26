package com.github.sorhus.jconfig;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.validators.PositiveInteger;
import com.github.sorhus.jconfig.dao.DAO;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Main {

    @Parameter(names = "-port", validateWith = PositiveInteger.class)
    private Integer port = 8080;

    @Parameter(names = {"-mysql", "-redis", "-memory"})
    private String backend = "-memory";

    @Parameter(names = "-strict-json")
    private boolean strictJson = false;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        new JCommander(main, args);
        main.init();
    }

    private Main() {}

    private void init() throws Exception {
        String contextFile = String.format("%s-context.xml", backend.substring(1));
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextFile);
        DAO dao = context.getBean("dao", DAO.class);

        Server server = new Server(port);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        contextHandler.setAttribute("dao",dao);
        contextHandler.addServlet(JConfigServlet.class, "/api");
        if(strictJson) {
            contextHandler.addFilter(ParseJsonFilter.class, "/api", EnumSet.of(DispatcherType.REQUEST));
        }
        server.start();
        server.join();
    }
}
