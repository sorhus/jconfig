package com.github.sorhus.jconfig.filter;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiFunction;

/**
 * @author anton.sorhus@gmail.com
 */
public class JQFilter implements BiFunction<String, String, String> {

    private static final String ENCODING = "utf-8";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String apply(String value, String filter) {
        try {
            log.debug("value: {}, jq filter: {}", value, filter);
            Process p = new ProcessBuilder("jq", filter).start();
            InputStream is = p.getInputStream();
            OutputStream os = p.getOutputStream();
            os.write(value.getBytes(ENCODING));
            os.close();
            p.waitFor();
            byte[] bytes = ByteStreams.toByteArray(is);
            if(null != bytes) {
                String result = new String(bytes, ENCODING).trim();
                log.debug("result: {}", result);
                return result;
            }
        } catch (Exception e) {
            log.info("", e);
        }
        return null;
    }
}
