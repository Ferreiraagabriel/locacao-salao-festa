package br.ufms.locacao.model.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ConsumoDAO {
    public void inserir(Connection conexao, int locacaoId, int leituraInicialId, int leituraFinalId, int tarifaId, BigDecimal consumo, BigDecimal valor) throws Exception {
        String sql = "INSERT INTO consumos(locacao_id, leitura_inicial_id, leitura_final_id, tarifa_id, consumo_kwh, valor_total) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, locacaoId); comando.setInt(2, leituraInicialId); comando.setInt(3, leituraFinalId);
            comando.setInt(4, tarifaId); comando.setBigDecimal(5, consumo); comando.setBigDecimal(6, valor); comando.executeUpdate();
        }
    }
}
