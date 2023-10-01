package com.github.lucasgois.tcc.servidor.connection;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConexaoSqlite {

    @Value("${datasource.url}")
    private String datasourceUrl;
    @Value("${datasource.database}")
    private String datasourceDatabase;

    private Connection connection;

    public Connection createConnection() throws SQLException {

        if (connection == null || !connection.isValid(2)) {
            final String url = datasourceUrl + System.getProperty("user.home") + '\\' + datasourceDatabase;
            log.info("url={}", url);

            connection = DriverManager.getConnection(url);

            new CreateDatabase(connection).createTables();
        }
        return connection;
    }
}