package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.compression.Compressor;
import com.github.sorhus.jconfig.dao.DAO;
import com.github.sorhus.jconfig.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: anton.sorhus@gmail.com
 */
public class CompressedDAO implements DAO {

    private final DAO dao;
    private final Compressor compressor;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public CompressedDAO(DAO dao, Compressor compressor) {
        this.dao = dao;
        this.compressor = compressor;
    }

    @Override
    public String get(String key) {
        String compressed = dao.get(key);
        String result = compressor.decompress(compressed);
        log.info("decompressing {} into {}", compressed, result);
        return result;
    }

    @Override
    public void put(String key, String value) {
        String compressed = compressor.compress(value);
        log.info("compressing {} into {}", value, compressed);
        dao.put(key, compressed);
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
