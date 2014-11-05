package com.github.sorhus.jconfig.dao;

import com.github.sorhus.jconfig.model.Config;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: anton.sorhus@gmail.com
 */
@RegisterMapper(MySQLDAO.ConfigMapper.class)
public interface MySQLDAO extends DAO {

    @Override
    @SqlQuery("SELECT json FROM configs where id = :id")
    public String get(@Bind("id") String id);

    @Override
    @SqlUpdate("INSERT INTO configs (id, json) VALUES (:id, :json) ON DUPLICATE KEY UPDATE json = :json")
    public void put(@Bind("id") String id, @Bind("json") String value);

    @Override
    @SqlQuery("SELECT id, json FROM configs")
    public Iterable<Config> getAll();

    @Override
    @SqlBatch("INSERT INTO configs (id, json) VALUES (:id, :json) ON DUPLICATE KEY UPDATE json = :json")
    @BatchChunkSize(1000)
    public void putAll(@BindBean Iterable<Config> configs);

    void close();

    public class ConfigMapper implements ResultSetMapper<Config> {
        @Override
        public Config map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
            return new Config(resultSet.getString("id"), resultSet.getString("json"));
        }
    }

}