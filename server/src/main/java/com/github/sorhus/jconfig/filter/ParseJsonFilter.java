package com.github.sorhus.jconfig.filter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: anton.sorhus@gmail.com
 */
public class ParseJsonFilter implements Filter {

    private final Gson gson = new Gson();
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("applying filter {}", getClass());
        String input = servletRequest.getParameter("json");
        log.debug("incoming value: {}", input);
        try {
            Object o = gson.fromJson(input, Object.class);
            log.debug("deserialized value: {}", o);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch(JsonParseException e) {
            log.info("could not parse json: {}", input);
            log.debug("{}", e);
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void destroy() {}
}
