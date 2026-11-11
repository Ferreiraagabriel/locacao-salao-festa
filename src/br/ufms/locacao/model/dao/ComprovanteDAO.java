package br.ufms.locacao.model.dao;

import br.ufms.locacao.model.entity.Comprovante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class ComprovanteDAO {
    public int inserir(Connection conexao, Comprovante comprovante) throws Exception {
        String sql = "INSERT INTO comprovantes(locacao_id, morador, unidade, salao, inicio, fim, leitura_inicial, leitura_final, consumo_kwh, tarifa_kwh, valor_total, data_emissao) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setInt(1, comprovante.getLocacaoId()); comando.setString(2, comprovante.getMorador());
            comando.setString(3, comprovante.getUnidade()); comando.setString(4, comprovante.getSalao());
            comando.setString(5, comprovante.getInicio().toString()); comando.setString(6, comprovante.getFim().toString());
            comando.setBigDecimal(7, comprovante.getLeituraInicial()); comando.setBigDecimal(8, comprovante.getLeituraFinal());
            comando.setBigDecimal(9, comprovante.getConsumoKwh()); comando.setBigDecimal(10, comprovante.getTarifaKwh());
            comando.setBigDecimal(11, comprovante.getValorTotal()); comando.setString(12, comprovante.getDataEmissao().toString());
            comando.executeUpdate(); try (ResultSet r = comando.getGeneratedKeys()) { if (r.next()) return r.getInt(1); }
        }
        throw new java.sql.SQLException("Não foi possível gerar o comprovante.");
    }
}
