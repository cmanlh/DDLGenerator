package com.lifeonwalden.generator4db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lifeonwalden.codeGenerator.DBSourceGenerateCodeMain;

public class DBSourceTest {
    protected static final String CONNECTION_STRING = "jdbc:hsqldb:mem:testdb;shutdown=false";
    protected static final String USER_NAME = "SA";
    protected static final String PASSWORD = "";

    @Before
    public void setup() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");

        Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD);
        connection.createStatement()
                .executeUpdate("create table \"User\" (\"id\" varchar(32) PRIMARY KEY, \"age\" int, \"income\" decimal(20,6), \"birthday\" datetime)");
        connection.createStatement()
                .executeUpdate("create table \"Book\" (\"id\" varchar(32), \"name\" varchar(32), \"publisher\" varchar(32), \"owner\" varchar(32))");
        connection.createStatement().executeUpdate(
                "create table \"APP_TEST\" (\"ID\" varchar(32), \"NAME\" varchar(32), \"PUBLISHER_address\" varchar(32), \"owner_address_HELLO\" varchar(32))");
        PreparedStatement ps = connection.prepareStatement("insert into \"User\" values(?,?,?,?)");
        ps.setString(1, "1111");
        ps.setInt(2, 50);
        ps.setDouble(3, 5.6);
        ps.setDate(4, new Date(System.currentTimeMillis()));
        ps.addBatch();
        ps.setString(1, "2222");
        ps.setInt(2, 35);
        ps.setDouble(3, 8.6);
        ps.setDate(4, new Date(System.currentTimeMillis()));
        ps.addBatch();
        ps.executeBatch();
        ps.setString(1, "3333");
        ps.setInt(2, 35);
        ps.setDouble(3, 8.6);
        ps.setDate(4, null);
        ps.addBatch();
        ps.executeBatch();
        ps.close();

        ps = connection.prepareStatement("insert into \"Book\" values(?,?,?,?)");
        ps.setString(1, "aaaa");
        ps.setString(2, "AAAAAA");
        ps.setString(3, "apublisher");
        ps.setString(4, "1111");
        ps.addBatch();
        ps.setString(1, "bbbbb");
        ps.setString(2, "BBBBBB");
        ps.setString(3, "bpublisher");
        ps.setString(4, "1111");
        ps.executeBatch();
        connection.close();
    }

    @Test
    public void generateCodeMainTest() {
        String root_path = "D:\\ideaProjects\\DDLGenerator\\";
        String[] param = {root_path + "resources\\db2xml", root_path + "target"};

        DBSourceGenerateCodeMain.main(param);
    }

    @After
    public void clear() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");

        Connection connection = DriverManager.getConnection(CONNECTION_STRING, USER_NAME, PASSWORD);
        Statement stmt = connection.createStatement();
        stmt.execute("DROP SCHEMA PUBLIC CASCADE");
        connection.close();
    }

}
