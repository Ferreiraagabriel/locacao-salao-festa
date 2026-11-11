package br.ufms.locacao.controller;

import br.ufms.locacao.model.entity.Usuario;
import br.ufms.locacao.model.service.AutenticacaoService;
import br.ufms.locacao.view.LoginView;

public class AutenticacaoController {
    private final LoginView view;
    private final AutenticacaoService service;
    private final NavegacaoController navegacao;

    public AutenticacaoController(LoginView view, AutenticacaoService service, NavegacaoController navegacao) {
        this.view = view; this.service = service; this.navegacao = navegacao;
        this.view.aoEntrar(this::entrar);
    }

    private void entrar() {
        try {
            Usuario usuario = service.autenticar(view.getCpf(), new String(view.getSenha()));
            view.dispose();
            navegacao.abrirSistema(usuario);
        } catch (Exception e) {
            view.limparSenha();
            view.mostrarErro(e.getMessage());
        }
    }
}
