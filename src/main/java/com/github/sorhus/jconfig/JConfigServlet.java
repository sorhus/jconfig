package com.github.sorhus.jconfig;

import com.github.sorhus.jconfig.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

    private Service service;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void init(ServletConfig config) throws ServletException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        service = context.getBean("service", Service.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        log.info("incoming get, id: {}", id);
        String result = service.get(id);
        log.info("service returned, value: {}", result);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String value = req.getParameter("value");
        log.info("incoming post, id: {}, value: {}", id, value);
        try {
            service.put(id, value);
            log.info("stored data for id {}", id);
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        } catch(RuntimeException e) {
            log.error("could not store data for id {}, {}", id, e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}