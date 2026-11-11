package br.ufms.locacao.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {
    private static final long serialVersionUID = 1L;
    private final JTextField campoCpf = EstiloTela.campo(18);
    private final JPasswordField campoSenha = new JPasswordField(18);
    private final JButton botaoEntrar = EstiloTela.botao("Entrar");

    public LoginView() {
        setTitle("Locação de Salão de Festas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 330);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel fundo = new JPanel(new GridBagLayout());
        fundo.setBackground(EstiloTela.FUNDO);

        JPanel cartao = new JPanel(new GridBagLayout());
        cartao.setBackground(Color.WHITE);
        cartao.setBorder(new EmptyBorder(28, 34, 28, 34));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.insets = new Insets(0, 0, 8, 0);

        JLabel titulo = EstiloTela.titulo("Acesso ao sistema");
        cartao.add(titulo, c);
        c.gridy++; 
        
        JLabel descricao = new JLabel("Entre com seu CPF e senha de acesso.");
        descricao.setForeground(EstiloTela.TEXTO); cartao.add(descricao, c);

        c.gridwidth = 1; c.gridy++; c.gridx = 0; c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(20, 0, 6, 10); cartao.add(new JLabel("CPF"), c);
        c.gridx = 1; c.insets = new Insets(20, 0, 6, 0); cartao.add(campoCpf, c);
        c.gridx = 0; c.gridy++; c.insets = new Insets(8, 0, 6, 10); cartao.add(new JLabel("Senha"), c);
        c.gridx = 1; c.insets = new Insets(8, 0, 6, 0); cartao.add(campoSenha, c);
        c.gridx = 0; c.gridy++; c.gridwidth = 2; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(20, 0, 0, 0);
        
        cartao.add(botaoEntrar, c);
        fundo.add(cartao);
        setContentPane(fundo);
        getRootPane().setDefaultButton(botaoEntrar);
    }

    public void aoEntrar(Runnable acao) { botaoEntrar.addActionListener(e -> acao.run()); }
    public String getCpf() { return campoCpf.getText().trim(); }
    public char[] getSenha() { return campoSenha.getPassword(); }
    public void limparSenha() { campoSenha.setText(""); }
    public void mostrarErro(String mensagem) { JOptionPane.showMessageDialog(this, mensagem, "Não foi possível entrar", JOptionPane.WARNING_MESSAGE); }
}
