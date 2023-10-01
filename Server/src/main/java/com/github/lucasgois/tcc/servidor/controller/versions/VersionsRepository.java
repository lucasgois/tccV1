package com.github.lucasgois.tcc.servidor.controller.versions;

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
public class VersionsRepository extends SqliteConnection {

    private static final String QUERY_ALL = "SELECT version_id, version_name, version_environment_ref AS environment_id, version_module_ref AS module_id, version_created_at, version_updated_at FROM versions ORDER BY version_created_at";
    private static final String QUERY_ID = "SELECT version_id, version_name, version_environment_ref AS environment_id, version_module_ref AS module_id, version_created_at, version_updated_at FROM versions WHERE version_id = ?";
    private static final String QUERY_INSERT = "INSERT INTO versions (version_id, version_name, version_environment_ref, version_module_ref, version_created_at, version_updated_at) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String QUERY_UPDATE = "UPDATE versions SET version_name = ?, version_environment_ref = ?, version_module_ref = ?, version_updated_at = ? WHERE version_id = ?";
    private static final String QUERY_DELETE = "DELETE FROM versions WHERE version_id = ?";


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


    public String create(@NotNull final VersionDto body) throws SQLException {
        final String uuid = UUID.randomUUID().toString();
        final String dateTime = LocalDateTime.now().toString();

        final Connection connection = getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            int i = 0;
            statement.setString(++i, uuid);
            statement.setString(++i, body.getVersion_name());
            statement.setString(++i, body.getEnvironment_id());
            statement.setString(++i, body.getModule_id());
            statement.setString(++i, dateTime);
            statement.setString(++i, dateTime);

            log.info("{}", statement);
            statement.executeUpdate();
        }

        return uuid;
    }

    public void update(final String uuid, @NotNull final VersionDto body) throws SQLException {
        final String dateTime = LocalDateTime.now().toString();

        final Connection connection = getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            int i = 0;
            statement.setString(++i, body.getVersion_name());
            statement.setString(++i, body.getEnvironment_id());
            statement.setString(++i, body.getModule_id());
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