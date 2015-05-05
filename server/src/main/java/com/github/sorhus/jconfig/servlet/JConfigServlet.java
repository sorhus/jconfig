package com.github.sorhus.jconfig.servlet;

import com.github.sorhus.jconfig.dao.DAO;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: anton.sorhus@gmail.com
 */
public class JConfigServlet extends HttpServlet {

    private DAO dao;
    private boolean strictJson;

    private final Gson gson = new Gson();
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void init(ServletConfig config) throws ServletException {
        dao = (DAO) config.getServletContext().getAttribute("dao");
        strictJson = (boolean) config.getServletContext().getAttribute("strictJson");
        log.info("init Servlet with DAO: {}", dao);
    }

    @Override
    protected void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        String key = servletRequest.getParameter("key");
        log.info("incoming get, key: {}", key);
        String value = dao.get(key);
        log.debug("dao returned value: {}", value);
        if(null != value) {
            servletResponse.setContentType("application/json");
            servletResponse.setCharacterEncoding("utf-8");
            servletResponse.setStatus(HttpServletResponse.SC_OK);
            servletResponse.getWriter().print(value);
        } else {
            servletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        String key = servletRequest.getParameter("key");
        String value = servletRequest.getParameter("value");
        log.info("incoming post, key: {}, value: {}", key, value);
        try {
            if(strictJson) {
                value = gson.toJson(gson.fromJson(value, Object.class));
            }
            dao.put(key, value);
            log.debug("stored value {} for key {}", value, key);
            servletResponse.setStatus(HttpServletResponse.SC_OK);
        } catch(RuntimeException e) {
            log.error("could not store value {} for key {}, {}", key, e);
            servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}