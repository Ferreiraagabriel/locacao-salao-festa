package br.ufms.locacao.model.dao;

import br.ufms.locacao.model.entity.LeituraDeEnergia;
import br.ufms.locacao.model.entity.TipoLeitura;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

public class LeituraDAO {
    public int inserir(Connection conexao, int locacaoId, BigDecimal valor, LocalDateTime dataHora, TipoLeitura tipo, String cpf) throws Exception {
        String sql = "INSERT INTO leituras(locacao_id, valor_kwh, data_hora, tipo, funcionario_cpf) VALUES(?,?,?,?,?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setInt(1, locacaoId); comando.setBigDecimal(2, valor); comando.setString(3, dataHora.toString());
            comando.setString(4, tipo.name()); comando.setString(5, cpf); comando.executeUpdate();
            try (ResultSet r = comando.getGeneratedKeys()) { if (r.next()) return r.getInt(1); }
        }
        throw new java.sql.SQLException("Não foi possível salvar a leitura.");
    }

    public int inserir(Connection conexao, LeituraDeEnergia leitura) throws Exception {
        return inserir(conexao, leitura.getLocacaoId(), leitura.getValorLeitura(), leitura.getDataHora(),
                leitura.getTipo(), leitura.getFuncionarioCpf());
    }

    public LeituraDeEnergia buscarPorTipo(Connection conexao, int locacaoId, TipoLeitura tipo) throws Exception {
        String sql = "SELECT id, locacao_id, valor_kwh, data_hora, tipo, funcionario_cpf FROM leituras WHERE locacao_id = ? AND tipo = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, locacaoId); comando.setString(2, tipo.name());
            try (ResultSet r = comando.executeQuery()) {
                if (!r.next()) return null;
                return new LeituraDeEnergia(r.getInt("id"), r.getInt("locacao_id"), r.getBigDecimal("valor_kwh"),
                        LocalDateTime.parse(r.getString("data_hora")), TipoLeitura.valueOf(r.getString("tipo")),
                        r.getString("funcionario_cpf"));
            }
        }
    }
}
