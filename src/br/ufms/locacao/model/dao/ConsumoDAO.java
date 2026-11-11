package br.ufms.locacao.model.dao;

import br.ufms.locacao.model.entity.ConsumoDeEnergia;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ConsumoDAO {
    public void inserir(Connection conexao, ConsumoDeEnergia consumo) throws Exception {
        String sql = "INSERT INTO consumos(locacao_id, leitura_inicial_id, leitura_final_id, tarifa_id, consumo_kwh, valor_total) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, consumo.getLocacaoId());
            comando.setInt(2, consumo.getLeituraInicial().getId());
            comando.setInt(3, consumo.getLeituraFinal().getId());
            comando.setInt(4, consumo.getTarifa().getId());
            comando.setBigDecimal(5, consumo.getConsumoKwh());
            comando.setBigDecimal(6, consumo.getValorTotal());
            comando.executeUpdate();
        }
    }
}
