package com.github.sorhus.jconfig;

import com.github.sorhus.jconfig.dao.DAO;
import com.google.gson.JsonParseException;
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
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void init(ServletConfig config) throws ServletException {
        dao = (DAO) config.getServletContext().getAttribute("dao");
        log.info("init Servlet with DAO: {}", dao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        log.info("incoming get, id: {}", id);
        String result = dao.get(id);
        log.debug("dao returned, json: {}", result);
        if(null != result) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(result);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        String id = servletRequest.getParameter("id");
        String value = servletRequest.getParameter("json");
        log.info("incoming post, id: {}, json: {}", id, value);
        try {
            dao.put(id, value);
            log.debug("stored json for id {}", id);
            servletResponse.setStatus(HttpServletResponse.SC_OK);
        } catch(RuntimeException e) {
            log.error("could not store json for id {}, {}", id, e);
            servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}