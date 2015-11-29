package com.github.sorhus.jconfig.server;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.validators.PositiveInteger;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jetty9.InstrumentedHandler;
import com.github.sorhus.jconfig.dao.CompressedDao;
import com.github.sorhus.jconfig.dao.DAO;
import com.github.sorhus.jconfig.compression.FakeCompressor;
import com.github.sorhus.jconfig.filter.ParseJsonFilter;
import com.github.sorhus.jconfig.servlet.JConfigServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

class JconfigServer {

    @Parameter(names = {"-p", "--port"}, validateWith = PositiveInteger.class)
    Integer port = 8080;

    @Parameter(names = {"-b", "--backend"})
    String backend = "memory"; // mysql, redis

    @Parameter(names = {"-j", "--json-strict"}, arity = 1)
    boolean strictJson = true;

    @Parameter(names = {"-c", "--compress"})
    boolean compress = false;

    @Parameter(names = {"-g", "--graphite"})
    boolean graphite = false;

    public static void main(String[] args) throws Exception {
        JconfigServer app = new JconfigServer();
        new JCommander(app, args);
        app.init();
    }

    JconfigServer() {}

    void init() throws Exception {
        DAO dao = new ClassPathXmlApplicationContext(String.format("%s-context.xml", backend))
                .getBean("dao", DAO.class);

        if(compress) {
            dao = new CompressedDao(dao, new FakeCompressor());
        }

        Server server = new Server(port);
        Handler handler;

        ServletContextHandler servletHandler = new ServletContextHandler();
        servletHandler.setContextPath("/");
        servletHandler.setAttribute("dao", dao);
        servletHandler.setAttribute("strictJson", strictJson);
        servletHandler.addServlet(JConfigServlet.class, "/api/v1/get");
        servletHandler.addServlet(JConfigServlet.class, "/api/v1/set");

        handler = servletHandler;

        if(strictJson) {
            servletHandler.addFilter(ParseJsonFilter.class, "/api/set", EnumSet.of(DispatcherType.REQUEST));
        }

        if(graphite) {
            MetricRegistry registry = new MetricRegistry();

            InstrumentedHandler instrumentedHandler = new InstrumentedHandler(registry);
            instrumentedHandler.setHandler(servletHandler);
            handler = instrumentedHandler;

            Graphite graphite = new ClassPathXmlApplicationContext("graphite-context.xml")
                    .getBean("graphite", Graphite.class);
            GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .build(graphite);

            reporter.start(1, TimeUnit.MINUTES);
        }

        server.setHandler(handler);
        server.start();
        server.join();
    }

}
