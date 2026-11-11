package br.ufms.locacao.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public final class EstiloTela {
    public static final Color AZUL = new Color(20, 55, 90);
    public static final Color AZUL_CLARO = new Color(235, 243, 250);
    public static final Color FUNDO = new Color(246, 248, 251);
    public static final Color TEXTO = new Color(45, 55, 65);

    private EstiloTela() { }

    public static JLabel titulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 22));
        label.setForeground(AZUL);
        return label;
    }

    public static JButton botao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFocusPainted(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 13));
        botao.setBackground(AZUL);
        botao.setForeground(Color.WHITE);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
        return botao;
    }

    public static JTextField campo(int colunas) {
        JTextField campo = new JTextField(colunas);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return campo;
    }

    public static JPanel painelFormulario() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 7));
        painel.setBackground(Color.WHITE);
        return painel;
    }

    public static JPanel cabecalho(String titulo, String descricao) {
        JPanel painel = new JPanel(new BorderLayout(4, 4));
        painel.setOpaque(false);
        painel.add(titulo(titulo), BorderLayout.NORTH);
        JLabel subtitulo = new JLabel(descricao);
        subtitulo.setForeground(TEXTO);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        painel.add(subtitulo, BorderLayout.CENTER);
        return painel;
    }

    public static DefaultTableModel modeloTabela(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int linha, int coluna) { return false; }
        };
    }
}
