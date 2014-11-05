package com.github.sorhus.jconfig.compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: anton.sorhus@gmail.com
 */
public class GZipCompressor implements Compressor {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String decompress(String input) {
        try {
            byte[] compressed = Base64.decodeBase64(input);
            ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
            GZIPInputStream gzis = new GZIPInputStream(bais);
            byte[] buffer = new byte[compressed.length];
            int size = gzis.read(buffer);
            byte[] decompressed = Arrays.copyOf(buffer, size);
            return new String(decompressed);
        } catch (IOException e) {
            log.warn("Could not decompress {}", input, e);
            return null;
        }

    }

    @Override
    public String compress(String input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(input.length());
            GZIPOutputStream gzos = new GZIPOutputStream(baos);
            gzos.write(input.getBytes());
            gzos.finish();
            gzos.close();
            byte[] compressed = baos.toByteArray();
            return Base64.encodeBase64String(compressed);
        } catch (IOException e) {
            log.warn("Could not decompress {}", input, e);
            return null;
        }
    }
}
