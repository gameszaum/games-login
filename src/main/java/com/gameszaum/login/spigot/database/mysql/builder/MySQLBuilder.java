package com.gameszaum.login.spigot.database.mysql.builder;

import com.gameszaum.login.spigot.Bukkit;
import com.gameszaum.login.spigot.database.DatabaseCredentials;
import com.gameszaum.login.spigot.database.mysql.MySQLBase;
import java.sql.*;

/*
    @author gameszaum
 */
public class MySQLBuilder implements MySQLBase {

    private Connection connection;

    @Override
    public MySQLBase createConnection(DatabaseCredentials credentials) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + credentials.getHost() + ":"
                            + credentials.getPort() + "/" + credentials.getDb(),
                    credentials.getUser(), credentials.getPass());
            System.out.println("[" + Bukkit.getPlugin().getDescription().getName() + "] MySQL connected.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
            System.out.println("[" + Bukkit.getPlugin().getDescription().getName() + "] MySQL closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void executeQuery(String sql) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean contains(String table, String where, String whereResult) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("SELECT * FROM `" + table + "` WHERE `" + where + "` = '" + whereResult + "';");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return false;

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void update(String table, String where, String whereResult, String update, Object updateResult) {
        PreparedStatement ps;
        try {
            String query = "UPDATE `" + table + "` SET `" + update + "` = '" + updateResult + "' WHERE `" + where + "` = '" + whereResult + "';";

            ps = connection.prepareStatement(query);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String table, String where, String whereResult) {
        PreparedStatement ps;
        try {
            String query = "DELETE FROM `" + table + "` WHERE `" + where + "` = '" + whereResult + "';";
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getString(String table, String where, String whereResult, String column) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("SELECT * FROM `" + table + "` WHERE `" + where + "` = '" + whereResult + "';");
            ResultSet rs = ps.executeQuery();
            rs.next();
            String s = rs.getString(column);
            rs.close();
            ps.close();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public Integer getInteger(String table, String where, String whereResult, String column) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("SELECT * FROM `" + table + "` WHERE `" + where + "` = '" + whereResult + "';");
            ResultSet rs = ps.executeQuery();
            rs.next();
            int i = rs.getInt(column);
            rs.close();
            ps.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Long getLong(String table, String where, String whereResult, String column) {
        PreparedStatement ps;
        try {
            ps = connection.prepareStatement("SELECT * FROM `" + table + "` WHERE `" + where + "` = '" + whereResult + "';");
            ResultSet rs = ps.executeQuery();
            rs.next();
            long l = rs.getLong(column);
            rs.close();
            ps.close();
            return l;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }
}