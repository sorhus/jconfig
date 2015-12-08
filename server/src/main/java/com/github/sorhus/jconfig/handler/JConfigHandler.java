package com.github.sorhus.jconfig.handler;

import com.github.sorhus.jconfig.dao.DAO;
import com.github.sorhus.jconfig.filter.JQFilter;
import com.github.sorhus.jconfig.filter.JsonFilter;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author anton.sorhus@gmail.com
 */
public class JConfigHandler extends AbstractHandler {

    private final DAO dao;
    private final boolean strictJson;

    private final JQFilter jqFilter = new JQFilter();
    private final JsonFilter jsonFilter = new JsonFilter();

    private static final String GET = "/api/v1/get";
    private static final String SET = "/api/v1/set";

    private final Logger log = LoggerFactory.getLogger(getClass());

    public JConfigHandler(DAO dao, boolean strictJson) {
        this.dao = dao;
        this.strictJson = strictJson;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug(target);
        switch (target) {
            case GET:
                handleGet(request, response);
                break;
            case SET:
                handlePost(request, response);
                break;
            default: response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        baseRequest.setHandled(true);
    }

    private void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String key = request.getParameter("key");
        log.debug("incoming get, key: {}", key);
        String value = dao.get(key);
        log.debug("dao returned value: {}", value);
        String filter = request.getParameter("filter");
        log.debug("incoming filter: {}", filter);
        if(null != filter) {
            value = jqFilter.filter(value, filter);
        }
        if(null != value) {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(value);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    private void handlePost(HttpServletRequest request, HttpServletResponse response) {
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        log.info("incoming post, key: {}, value: {}", key, value);
        if(strictJson && !jsonFilter.doFilter(value)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        try {
            dao.put(key, value);
            log.debug("stored value {} for key {}", value, key);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch(RuntimeException e) {
            log.error("could not store value {} for key {}, {}", key, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
