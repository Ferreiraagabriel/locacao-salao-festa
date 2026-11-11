package br.ufms.locacao.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    private static final Path PASTA_BANCO = Path.of(System.getProperty("user.home"), ".locacao-salao");
    private static final Path ARQUIVO_BANCO = PASTA_BANCO.resolve("locacao_salao_iteracao1.db");

    private ConexaoBanco() { }

    public static void carregarDriver() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    public static Connection abrir() throws SQLException {
        try {
            Files.createDirectories(PASTA_BANCO);
        } catch (Exception e) {
            throw new SQLException("Não foi possível criar a pasta do banco de dados.", e);
        }
        Connection conexao = DriverManager.getConnection("jdbc:sqlite:" + ARQUIVO_BANCO);
        try (var comando = conexao.createStatement()) {
            comando.execute("PRAGMA foreign_keys = ON");
        }
        return conexao;
    }

    public static String getCaminhoBanco() { return ARQUIVO_BANCO.toString(); }
}
