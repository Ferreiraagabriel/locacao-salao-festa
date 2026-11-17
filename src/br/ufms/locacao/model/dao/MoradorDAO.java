package br.ufms.locacao.model.dao;

import br.ufms.locacao.model.entity.Morador;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MoradorDAO {
    public List<Morador> listarTodos() throws Exception {
        List<Morador> moradores = new ArrayList<>();
        String sql = "SELECT id, nome, unidade, contato FROM moradores ORDER BY nome";
        try (Connection conexao = ConexaoBanco.abrir(); Statement comando = conexao.createStatement(); ResultSet resultado = comando.executeQuery(sql)) {
            while (resultado.next()) moradores.add(new Morador(resultado.getInt("id"), resultado.getString("nome"), resultado.getString("unidade"), resultado.getString("contato")));
        }
        return moradores;
    }
}
