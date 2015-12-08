package com.github.sorhus.jconfig.filter;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author anton.sorhus@gmail.com
 */
public class JQFilter{

    private final Logger log = LoggerFactory.getLogger(getClass());

    public String filter(String value, String filter) {
        try {
            Process p = new ProcessBuilder("jq", filter).start();
            InputStream is = p.getInputStream();
            OutputStream os = p.getOutputStream();
            os.write(value.getBytes());
            os.close();
            p.waitFor();
            return new String(ByteStreams.toByteArray(is));
        } catch (Exception e) {
            log.info("", e);
        }
        return null;
    }
}
