package com.github.lucasgois.tcc.servidor.controller.modules;

import com.github.lucasgois.tcc.servidor.connection.SqliteConnection;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Repository
public class ModulesRepository extends SqliteConnection {

    private static final String QUERY_ALL = "SELECT module_id, module_name, module_created_at, module_updated_at FROM modules ORDER BY module_created_at";
    private static final String QUERY_ID = "SELECT module_id, module_name, module_created_at, module_updated_at FROM modules WHERE module_id = ?";
    private static final String QUERY_INSERT = "INSERT INTO modules (module_id, module_name, module_created_at, module_updated_at) VALUES (?, ?, ?, ?)";
    private static final String QUERY_UPDATE = "UPDATE modules SET module_name = ?, module_updated_at = ? WHERE module_id = ?";
    private static final String QUERY_DELETE = "DELETE FROM modules WHERE module_id = ?";


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


    public String create(@NotNull final ModuleDto body) throws SQLException {
        final String uuid = UUID.randomUUID().toString();
        final String dateTime = LocalDateTime.now().toString();

        final Connection connection = getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_INSERT)) {
            int i = 0;
            statement.setString(++i, uuid);
            statement.setString(++i, body.getModule_name());
            statement.setString(++i, dateTime);
            statement.setString(++i, dateTime);

            log.info("{}", statement);
            statement.executeUpdate();
        }

        return uuid;
    }

    public void update(final String uuid, @NotNull final ModuleDto body) throws SQLException {
        final String dateTime = LocalDateTime.now().toString();

        final Connection connection = getConnection();

        try (final PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE)) {
            int i = 0;
            statement.setString(++i, body.getModule_name());
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