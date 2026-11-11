package br.ufms.locacao.model.dao;

import br.ufms.locacao.model.entity.TarifaDeEnergia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class TarifaDAO {
    public TarifaDeEnergia buscarVigente(Connection conexao, LocalDate data) throws Exception {
        String sql = "SELECT id, valor_kwh, inicio_vigencia, fim_vigencia FROM tarifas WHERE inicio_vigencia <= ? AND (fim_vigencia IS NULL OR fim_vigencia >= ?) ORDER BY inicio_vigencia DESC LIMIT 1";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, data.toString()); comando.setString(2, data.toString());
            try (ResultSet r = comando.executeQuery()) {
                if (!r.next()) return null;
                return new TarifaDeEnergia(r.getInt("id"), r.getBigDecimal("valor_kwh"), LocalDate.parse(r.getString("inicio_vigencia")),
                        r.getString("fim_vigencia") == null ? null : LocalDate.parse(r.getString("fim_vigencia")));
            }
        }
    }
}
