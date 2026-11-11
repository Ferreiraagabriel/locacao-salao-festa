package br.ufms.locacao.model.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class CobrancaDAO {
    public int inserir(Connection conexao, int locacaoId, int moradorId, BigDecimal consumo, BigDecimal tarifa, BigDecimal valor) throws Exception {
        String sql = "INSERT INTO cobrancas(locacao_id, morador_id, consumo_kwh, tarifa_kwh, valor_total, status, data_criacao) VALUES(?,?,?,?,?,'PENDENTE',?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setInt(1, locacaoId); comando.setInt(2, moradorId); comando.setBigDecimal(3, consumo); comando.setBigDecimal(4, tarifa); comando.setBigDecimal(5, valor); comando.setString(6, LocalDate.now().toString());
            comando.executeUpdate(); try (ResultSet r = comando.getGeneratedKeys()) { if (r.next()) return r.getInt(1); }
        }
        throw new java.sql.SQLException("Não foi possível gerar a cobrança.");
    }
}
