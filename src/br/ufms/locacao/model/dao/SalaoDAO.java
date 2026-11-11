package br.ufms.locacao.model.dao;

import br.ufms.locacao.model.entity.SalaoDeFestas;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SalaoDAO {
    public List<SalaoDeFestas> listarTodos() throws Exception {
        List<SalaoDeFestas> saloes = new ArrayList<>();
        String sql = "SELECT id, codigo, nome, capacidade_maxima, valor_base, disponivel FROM saloes WHERE disponivel = 1 ORDER BY nome";
        try (Connection conexao = ConexaoBanco.abrir(); Statement comando = conexao.createStatement(); ResultSet resultado = comando.executeQuery(sql)) {
            while (resultado.next()) saloes.add(mapear(resultado));
        }
        return saloes;
    }

    private SalaoDeFestas mapear(ResultSet r) throws Exception {
        return new SalaoDeFestas(r.getInt("id"), r.getString("codigo"), r.getString("nome"), r.getInt("capacidade_maxima"),
                r.getBigDecimal("valor_base"), r.getInt("disponivel") == 1);
    }
}
