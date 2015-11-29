package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.model.Config;
import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;

/**
 * Created by anton on 27/11/15.
 */
public class RocksdbDAO implements DAO {

    private final RocksDB db;
//    if (db != null) db.close();
//    options.dispose();

    public RocksdbDAO(String path) throws RocksDBException {
        RocksDB.loadLibrary();
        Options options = new Options().setCreateIfMissing(true);
        db = RocksDB.open(options, path);
    }


    @Override
    public String get(String key) {
        try {
            byte[] bytes = db.get(key.getBytes());
            return bytes == null ? null : new String(bytes);
        } catch (RocksDBException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void put(String key, String value) {
        try {
            db.put(key.getBytes(), value.getBytes());
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putAll(Iterable<Config> configs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterable<Config> getAll() {
        throw new UnsupportedOperationException();
    }
}
