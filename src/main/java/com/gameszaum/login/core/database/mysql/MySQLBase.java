package com.gameszaum.login.core.database.mysql;

import com.gameszaum.login.core.database.DatabaseCredentials;

import java.sql.Connection;

/*
    @author gameszaum
 */
public interface MySQLBase {

    MySQLBase createConnection(DatabaseCredentials credentials);

    void closeConnection();

    boolean isConnected();

    Connection getConnection();

    void executeQuery(String sql);

    boolean contains(String table, String where, String whereResult);

    void update(String table, String where, String whereResult, String update, Object updateResult);

    void delete(String table, String where, String whereResult);

    String getString(String table, String where, String whereResult, String column);

    Integer getInteger(String table, String where, String whereResult, String column);

    Long getLong(String table, String where, String whereResult, String column);

}