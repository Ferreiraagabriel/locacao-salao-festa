package br.ufms.locacao.model.service;

import br.ufms.locacao.model.dao.UsuarioDAO;
import br.ufms.locacao.model.entity.Usuario;

public class AutenticacaoService {
    private final UsuarioDAO usuarioDAO;

    public AutenticacaoService(UsuarioDAO usuarioDAO) { this.usuarioDAO = usuarioDAO; }

    public Usuario autenticar(String cpf, String senha) throws Exception {
        if (cpf == null || cpf.replaceAll("\\D", "").length() != 11) {
            throw new IllegalArgumentException("Informe um CPF com 11 números.");
        }
        if (senha == null || senha.isBlank()) throw new IllegalArgumentException("Informe a senha.");
        Usuario usuario = usuarioDAO.autenticar(cpf, senha);
        if (usuario == null) throw new IllegalArgumentException("CPF ou senha inválidos.");
        return usuario;
    }
}
