package com.github.lucasgois.tcc.servidor.controller.files;

import com.github.lucasgois.tcc.common.Util;
import com.github.lucasgois.tcc.servidor.connection.SqliteConnection;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Repository
public class FilesRepository extends SqliteConnection {

    private static final String QUERY_ALL = "SELECT file_registry_hash, file_registry_name, file_registry_created_at, file_registry_updated_at FROM file_registries ORDER BY file_registry_created_at";
    private static final String QUERY_ID = "SELECT file_registry_hash, file_registry_name, file_registry_created_at, file_registry_updated_at FROM file_registries WHERE file_registry_hash = ?";
    private static final String QUERY_INSERT = "INSERT INTO file_registries (file_registry_hash, file_registry_name, file_registry_created_at, file_registry_updated_at) VALUES (?, ?, ?, ?)";
    private static final String QUERY_UPDATE = "UPDATE file_registries SET file_registry_name = ?, file_registry_updated_at = ? WHERE file_registry_hash = ?";
    private static final String QUERY_DELETE = "DELETE FROM file_registries WHERE file_registry_hash = ?";


    public List<Map<String, Object>> all() throws SQLException {
        final List<Map<String, Object>> lista = new ArrayList<>();

        final Connection connection = getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_ALL)) {
            log.info("{}", statement);

            try (final ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    final Map<String, Object> item = new LinkedHashMap<>();

                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        item.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                    }

                    lista.add(item);
                }
            }
        }

        return lista;
    }

    public Optional<Map<String, Object>> id(final String uuid) throws SQLException {

        final Connection connection = getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_ID)) {
            statement.setString(1, uuid);
            log.info("{}", statement);

            try (final ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    final Map<String, Object> item = new LinkedHashMap<>();

                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        item.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                    }

                    return Optional.of(item);
                }
            }
        }

        return Optional.empty();
    }


    public void create(@NotNull final FilesDto body) throws SQLException {
        final String dateTime = LocalDateTime.now().toString();

        final Connection connection = getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            int i = 0;
            statement.setString(++i, body.getFile_hash());
            statement.setString(++i, body.getFile_name());
            statement.setString(++i, dateTime);
            statement.setString(++i, dateTime);

            log.info("{}", statement);
            statement.executeUpdate();
        }
    }

    public void update(final String uuid, @NotNull final FilesDto body) throws SQLException {
        final String dateTime = LocalDateTime.now().toString();

        final Connection connection = getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            int i = 0;
            statement.setString(++i, body.getFile_hash());
            statement.setString(++i, body.getFile_name());
            statement.setString(++i, dateTime);
            statement.setString(++i, uuid);

            log.info("{}", statement);
            statement.executeUpdate();
        }
    }

    public void delete(final String uuid) throws SQLException {
        final Connection connection = getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_DELETE)) {
            int i = 0;
            statement.setString(++i, uuid);

            log.info("{}", statement);
            statement.executeUpdate();
        }
    }
}