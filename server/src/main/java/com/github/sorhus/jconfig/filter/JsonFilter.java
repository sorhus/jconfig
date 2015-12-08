package com.github.sorhus.jconfig.filter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: anton.sorhus@gmail.com
 */
public class JsonFilter {

    private final Gson gson = new Gson();
    private final Logger log = LoggerFactory.getLogger(getClass());

    public boolean doFilter(String maybeJson) {
        log.debug("incoming value: {}", maybeJson);
        try {
            Object o = gson.fromJson(maybeJson, Object.class);
            log.debug("deserialized value: {}", o);
            return true;
        } catch(JsonParseException e) {
            log.info("could not parse json: {}", maybeJson);
            log.debug("{}", e);
            return false;
        }
    }
}
