package com.github.sorhus.jconfig.server;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.validators.PositiveInteger;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jetty9.InstrumentedHandler;
import com.github.sorhus.jconfig.dao.CachedDAO;
import com.github.sorhus.jconfig.dao.CompressedDAO;
import com.github.sorhus.jconfig.dao.DAO;
import com.github.sorhus.jconfig.compression.FakeCompressor;
import com.github.sorhus.jconfig.handler.JConfigHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

class JconfigServer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Parameter(names = {"-p", "--port"}, validateWith = PositiveInteger.class)
    Integer port = 8080;

    @Parameter(names = {"-b", "--backend"})
    String backend = "memory"; // mysql, redis

    @Parameter(names = {"-j", "--json-strict"}, arity = 1)
    boolean strictJson = true;

    @Parameter(names = {"--compress"})
    boolean compress = false;

    @Parameter(names = {"--graphite"})
    boolean graphite = false;

    @Parameter(names = {"--cache"}, validateWith = PositiveInteger.class)
    Integer cache = 0;

    public static void main(String[] args) throws Exception {
        JconfigServer app = new JconfigServer();
        new JCommander(app, args);
        app.init();
    }

    JconfigServer() {}

    void init() throws Exception {
        log.info("Initialising DAO: {}", backend);
        DAO dao = new ClassPathXmlApplicationContext(String.format("%s-context.xml", backend))
                .getBean("dao", DAO.class);

        if(cache > 0) {
            log.info("Initialising CachedDAO, size {}", cache);
            dao = new CachedDAO(dao, cache);
        }

        if(compress) {
            log.info("Initialising CompressedDAO");
            dao = new CompressedDAO(dao, new FakeCompressor());
        }

        Handler handler = new JConfigHandler(dao, strictJson);

        if(graphite) {
            handler = graphiteWrapHandler(handler);
        }

        log.info("Handler: {}", handler);

        log.info("Initialising Server on port {}", port);
        Server server = new Server(port);
        server.setHandler(handler);
        server.start();
        server.join();
    }

    private static Handler graphiteWrapHandler(Handler handler) {
        MetricRegistry registry = new MetricRegistry();

        InstrumentedHandler instrumentedHandler = new InstrumentedHandler(registry);
        instrumentedHandler.setHandler(handler);

        Graphite graphite = new ClassPathXmlApplicationContext("graphite-context.xml")
                .getBean("graphite", Graphite.class);

        GraphiteReporter reporter = GraphiteReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build(graphite);

        reporter.start(1, TimeUnit.MINUTES);
        return instrumentedHandler;
    }
 }
