package com.github.lucasgois.tcc.servidor.torevise;

import com.github.lucasgois.tcc.servidor.connection.ConexaoSqlite;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class ArquivoRepository extends ConexaoSqlite {

    public void insert(@NotNull ArquivoEntity arquivo) throws SQLException {

        if (selectHash(arquivo.getHash()).isPresent()) {
            return;
        }

        final String sql = "INSERT INTO arquivos (hash, nome) VALUES (?, ?)";

        try (final Connection connection = createConnection(); final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, arquivo.getHash());
            statement.setString(2, arquivo.getNome());

            statement.execute();
        }
    }

    public Optional<ArquivoEntity> selectHash(@NotNull String hash) throws SQLException {
        final String sql = "SELECT hash, nome FROM arquivos WHERE hash = ?";

        try (final Connection connection = createConnection(); final PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hash);

            try (final ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    final ArquivoEntity arquivo = new ArquivoEntity();

                    arquivo.setHash(resultSet.getString("hash"));
                    arquivo.setHash(resultSet.getString("nome"));

                    return Optional.of(arquivo);

                } else {
                    return Optional.empty();
                }
            }
        }
    }
}