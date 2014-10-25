package com.github.sorhus.jconfig.service;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.StringMapper;

/**
 * @author: anton.sorhus@gmail.com
 */
public class MySQLService implements Service {

    DBI dbi;

    public MySQLService(DBI dbi) {
        this.dbi = dbi;
    }

    @Override
    public String get(String id) {
        Handle handle = dbi.open();
        String result = handle.createQuery("SELECT json FROM configs where id = :id")
            .bind("id", id)
            .map(StringMapper.FIRST)
            .first();
        handle.close();
        return result;
    }

    @Override
    public void put(String key, String value) {
        Handle handle = dbi.open();
        String insert = "INSERT INTO configs (id, json) VALUES (:id, :value) ON DUPLICATE KEY UPDATE json = :value";
        handle.createStatement(insert)
            .bind("id", key)
            .bind("value", value)
            .execute();
        handle.close();
    }

}
