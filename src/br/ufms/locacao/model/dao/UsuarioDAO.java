package br.ufms.locacao.model.dao;

import br.ufms.locacao.model.entity.Funcionario;
import br.ufms.locacao.model.entity.Usuario;
import br.ufms.locacao.model.entity.PerfilUsuario;
import br.ufms.locacao.util.ConexaoBanco;
import br.ufms.locacao.util.SenhaUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {
    public Usuario autenticar(String cpf, String senha) throws Exception {
        String sql = "SELECT cpf, nome, senha_hash, perfil, matricula FROM usuarios WHERE cpf = ? AND ativo = 1";
        try (Connection conexao = ConexaoBanco.abrir(); PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, cpf.replaceAll("\\D", ""));
            try (ResultSet resultado = comando.executeQuery()) {
                if (!resultado.next() || !SenhaUtil.conferir(senha, resultado.getString("senha_hash"))) return null;
                PerfilUsuario perfil = PerfilUsuario.valueOf(resultado.getString("perfil"));
                if (perfil == PerfilUsuario.FUNCIONARIO) {
                    return new Funcionario(resultado.getString("cpf"), resultado.getString("nome"),
                            resultado.getString("senha_hash"), perfil, resultado.getString("matricula"));
                }
                return new Usuario(resultado.getString("cpf"), resultado.getString("nome"),
                        resultado.getString("senha_hash"), perfil);
            }
        }
    }
}
