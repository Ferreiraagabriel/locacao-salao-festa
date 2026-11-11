package br.ufms.locacao;

import br.ufms.locacao.controller.AutenticacaoController;
import br.ufms.locacao.controller.NavegacaoController;
import br.ufms.locacao.model.dao.DatabaseInitializer;
import br.ufms.locacao.model.dao.UsuarioDAO;
import br.ufms.locacao.model.service.AutenticacaoService;
import br.ufms.locacao.view.LoginView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> iniciarAplicacao());
    }

    private static void iniciarAplicacao() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new DatabaseInitializer().inicializar();

            LoginView telaLogin = new LoginView();
            NavegacaoController navegacao = new NavegacaoController();
            AutenticacaoService autenticacaoService = new AutenticacaoService(new UsuarioDAO());
            new AutenticacaoController(telaLogin, autenticacaoService, navegacao);
            telaLogin.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Não foi possível iniciar o sistema.\n\n" + e.getMessage() +
                    "\n\nVerifique se o SQLite JDBC está no classpath de execução.",
                    "Erro ao iniciar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
