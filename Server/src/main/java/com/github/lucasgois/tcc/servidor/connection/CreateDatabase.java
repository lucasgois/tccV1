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
        createFileLocationMap();
        createVersionsInfo();
        createFilesVersionMap();
    }

    private void createModulesTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS modules (" +
                     "module_id TEXT PRIMARY KEY, " +
                     "module_name TEXT NOT NULL, " +
                     "module_created_at TEXT, " +
                     "module_updated_at TEXT" +
                     ");";
        executarSQL(sql);
    }

    private void createEnvironmentsTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS environments (" +
                     "environment_id TEXT PRIMARY KEY, " +
                     "environment_name TEXT NOT NULL, " +
                     "environment_created_at TEXT, " +
                     "environment_updated_at TEXT" +
                     ");";
        executarSQL(sql);
    }

    private void createFileRegistries() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS file_registries (" +
                     "file_registry_hash TEXT PRIMARY KEY, " +
                     "file_registry_name TEXT NOT NULL, " +
                     "file_registry_created_at TEXT, " +
                     "file_registry_updated_at TEXT" +
                     ");";
        executarSQL(sql);
    }

    private void createFileLocationMap() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS file_locations_map (" +
                     "file_location_id TEXT PRIMARY KEY, " +
                     "file_location_hash_ref TEXT NOT NULL, " +
                     "file_location_path TEXT NOT NULL, " +
                     "file_location_created_at TEXT, " +
                     "file_location_updated_at TEXT, " +
                     "FOREIGN KEY(file_location_hash_ref) REFERENCES file_registries(file_registry_hash)" +
                     ");";
        executarSQL(sql);
    }

    private void createVersionsInfo() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS versions_info (" +
                     "version_info_id TEXT PRIMARY KEY, " +
                     "version_info_environment_ref INTEGER NOT NULL, " +
                     "version_info_module_ref INTEGER NOT NULL, " +
                     "version_info_created_at TEXT, " +
                     "version_info_updated_at TEXT, " +
                     "FOREIGN KEY(version_info_environment_ref) REFERENCES environments(environment_id)," +
                     "FOREIGN KEY(version_info_module_ref) REFERENCES modules(module_id)" +
                     ");";
        executarSQL(sql);
    }

    private void createFilesVersionMap() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS files_version_map (" +
                     "file_version_map_id TEXT PRIMARY KEY, " +
                     "file_version_map_version_ref TEXT NOT NULL, " +
                     "file_version_map_location_ref TEXT NOT NULL, " +
                     "file_version_map_created_at TEXT, " +
                     "file_version_map_updated_at TEXT, " +
                     "FOREIGN KEY(file_version_map_version_ref) REFERENCES versions_info(version_info_id)," +
                     "FOREIGN KEY(file_version_map_location_ref) REFERENCES file_locations_map(file_location_id)" +
                     ");";
        executarSQL(sql);
    }

    private void executarSQL(final String sql) throws SQLException {
        try (final Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
    }
}