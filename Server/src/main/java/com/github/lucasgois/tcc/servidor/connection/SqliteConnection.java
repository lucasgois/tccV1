package com.github.lucasgois.tcc.servidor.connection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class SqliteConnection {

    @Value("${datasource.url}")
    private String datasourceUrl;
    @Value("${datasource.database}")
    private String datasourceDatabase;


    private static Connection connection;
    private static boolean createDatabaseFlag;

    public SqliteConnection() {
        setCreateDatabaseFlag(true);
    }


    protected Connection getConnection() throws SQLException {
        if (!connected()) {
            connect();
        }
        return connection;
    }

    private static void setConnection(Connection connection) {
        SqliteConnection.connection = connection;
    }

    private boolean connected() throws SQLException {
        if (connection == null) return false;
        return connection.isValid(2);
    }

    private void connect() throws SQLException {
        final String url = datasourceUrl + System.getProperty("user.home") + '\\' + datasourceDatabase;
        log.info("url={}", url);

        setConnection(DriverManager.getConnection(url));

        if (createDatabaseFlag) {
            setCreateDatabaseFlag(false);
            new CreateDatabase(connection).createTables();
        }
    }

    private static void setCreateDatabaseFlag(boolean createDatabaseFlag) {
        SqliteConnection.createDatabaseFlag = createDatabaseFlag;
    }
}