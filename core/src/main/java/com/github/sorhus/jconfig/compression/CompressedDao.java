package com.github.sorhus.jconfig.compression;

import com.github.sorhus.jconfig.dao.DAO;
import com.github.sorhus.jconfig.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: anton.sorhus@gmail.com
 */
public class CompressedDao implements DAO {

    private final DAO dao;
    private final Compressor compressor;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public CompressedDao(DAO dao, Compressor compressor) {
        this.dao = dao;
        this.compressor = compressor;
    }

    @Override
    public String get(String id) {
        String compressed = dao.get(id);
        String result = compressor.decompress(compressed);
        log.info("decompressing {} into {}", compressed, result);
        return result;
    }

    @Override
    public void put(String id, String value) {
        String compressed = compressor.compress(value);
        log.info("compressing {} into {}", value, compressed);
        dao.put(id, compressed);
    }

    @Override
    public void putAll(Iterable<Config> configs) {
        throw new RuntimeException();
    }

    @Override
    public Iterable<Config> getAll() {
        throw new RuntimeException();
    }

    @Override
    public String toString() {
        return String.format("%s with underlying %s", getClass(), dao.toString());
    }
}
