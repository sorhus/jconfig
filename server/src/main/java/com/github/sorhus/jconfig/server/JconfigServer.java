package com.github.sorhus.jconfig.server;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.validators.PositiveInteger;
import com.github.sorhus.jconfig.dao.CompressedDao;
import com.github.sorhus.jconfig.dao.DAO;
import com.github.sorhus.jconfig.compression.FakeCompressor;
import com.github.sorhus.jconfig.filter.ParseJsonFilter;
import com.github.sorhus.jconfig.servlet.JConfigServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

class JconfigServer {

    @Parameter(names = {"-p", "--port"}, validateWith = PositiveInteger.class)
    Integer port = 8080;

    @Parameter(names = {"-b", "--backend"})
    String backend = "memory"; // mysql, redis

    @Parameter(names = {"-s", "--strict-json"})
    boolean strictJson = false;

    @Parameter(names = {"-c", "--compress"})
    boolean compress = false;

    public static void main(String[] args) throws Exception {
        JconfigServer app = new JconfigServer();
        new JCommander(app, args);
        app.init();
    }

    JconfigServer() {}

    void init() throws Exception {
        String contextFile = String.format("%s-context.xml", backend);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextFile);
        DAO dao = context.getBean("dao", DAO.class);
        if(compress) {
            dao = new CompressedDao(dao, new FakeCompressor());
        }

        final Server server = new Server(port);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        contextHandler.setAttribute("dao", dao);

        contextHandler.addServlet(JConfigServlet.class, "/api/get");
        contextHandler.addServlet(JConfigServlet.class, "/api/set");

        if(strictJson) {
            contextHandler.addFilter(ParseJsonFilter.class, "/api/set", EnumSet.of(DispatcherType.REQUEST));
        }

        server.start();
        server.join();
    }

}
