package com.github.sorhus.jconfig.filter;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * @author: anton.sorhus@gmail.com
 */
public class JsonFilter implements Function<String, String> {

    private final Gson gson = new Gson();
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String apply(String maybeJson) {
        log.debug("incoming value: {}", maybeJson);
        try {
            return gson.toJson(gson.fromJson(maybeJson, Object.class));
        } catch(JsonParseException e) {
            log.debug("could not parse json: {}", maybeJson, e);
            return null;
        }
    }
}
