package com.github.lucasgois.tcc.servidor.connection;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@AllArgsConstructor
public class CreateDatabase {

    private final Connection conn;

    public void createTables() throws SQLException {
        createModulesTable();
        createEnvironmentsTable();
        createFileRegistries();
        criarFileLocationMap();
        criarVersionInfo();
        criarVersionFileMapping();
    }

    private void createModulesTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS modules (" +
                     "module_id TEXT PRIMARY KEY, " +
                     "module_name TEXT NOT NULL, " +
                     "created_at TEXT, " +
                     "updated_at TEXT" +
                     ");";
        executarSQL(sql);
    }

    private void createEnvironmentsTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS environments (" +
                     "environment_id TEXT PRIMARY KEY, " +
                     "environment_name TEXT NOT NULL," +
                     "created_at TEXT, " +
                     "updated_at TEXT" +
                     ");";
        executarSQL(sql);
    }

    private void createFileRegistries() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS file_registries (" +
                     "file_hash TEXT PRIMARY KEY," +
                     "file_name TEXT NOT NULL," +
                     "created_at TEXT, " +
                     "updated_at TEXT" +
                     ");";
        executarSQL(sql);
    }

    private void criarFileLocationMap() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS FileLocationMap (" +
                     "LocationID INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "FileHash_Ref TEXT NOT NULL," +
                     "FileLocationPath TEXT NOT NULL," +
                     "CreationDate TEXT DEFAULT CURRENT_TIMESTAMP," +
                     "LastModifiedDate TEXT DEFAULT CURRENT_TIMESTAMP," +
                     "FOREIGN KEY(FileHash_Ref) REFERENCES FileRegistry(FileHash)" +
                     ");";
        executarSQL(sql);
    }

    private void criarVersionInfo() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS VersionInfo (" +
                     "VersionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "EnvironmentID_Ref INTEGER NOT NULL," +
                     "ModuleID_Ref INTEGER NOT NULL," +
                     "CreationDate TEXT DEFAULT CURRENT_TIMESTAMP," +
                     "LastModifiedDate TEXT DEFAULT CURRENT_TIMESTAMP," +
                     "FOREIGN KEY(EnvironmentID_Ref) REFERENCES DeploymentEnvironment(EnvironmentID)," +
                     "FOREIGN KEY(ModuleID_Ref) REFERENCES ModuleInfo(ModuleID)" +
                     ");";
        executarSQL(sql);
    }

    private void criarVersionFileMapping() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS VersionFileMapping (" +
                     "MappingID INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "VersionID_Ref INTEGER NOT NULL," +
                     "LocationID_Ref INTEGER NOT NULL," +
                     "CreationDate TEXT DEFAULT CURRENT_TIMESTAMP," +
                     "LastModifiedDate TEXT DEFAULT CURRENT_TIMESTAMP," +
                     "FOREIGN KEY(VersionID_Ref) REFERENCES VersionInfo(VersionID)," +
                     "FOREIGN KEY(LocationID_Ref) REFERENCES FileLocationMap(LocationID)" +
                     ");";
        executarSQL(sql);
    }

    private void executarSQL(String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}