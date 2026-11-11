package br.ufms.locacao.view;

import br.ufms.locacao.model.entity.Comprovante;
import br.ufms.locacao.util.DataHoraUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ComprovanteDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    public ComprovanteDialog(JFrame pai, Comprovante comprovante) {
        super(pai, "Comprovante de encerramento", true);
        setSize(560, 580); setLocationRelativeTo(pai);
        JTextArea texto = new JTextArea(montarTexto(comprovante));
        texto.setEditable(false); texto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        texto.setBorder(new EmptyBorder(16,16,16,16));
        JButton imprimir = EstiloTela.botao("Imprimir comprovante");
        JButton fechar = EstiloTela.botao("Fechar");
        imprimir.addActionListener(e -> { try { texto.print(); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Não foi possível imprimir: " + ex.getMessage()); } });
        fechar.addActionListener(e -> dispose());
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT)); botoes.add(imprimir); botoes.add(fechar);
        add(new JScrollPane(texto), BorderLayout.CENTER); add(botoes, BorderLayout.SOUTH);
    }

    private String montarTexto(Comprovante c) {
        NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return "COMPROVANTE DE ENCERRAMENTO DA LOCAÇÃO\n" +
                "==============================================\n\n" +
                "Comprovante nº: " + c.getId() + "\n" +
                "Locação nº: " + c.getLocacaoId() + "\n" +
                "Morador responsável: " + c.getMorador() + "\n" +
                "Unidade: " + c.getUnidade() + "\n" +
                "Salão: " + c.getSalao() + "\n" +
                "Início da locação: " + DataHoraUtil.formatar(c.getInicio()) + "\n" +
                "Encerramento: " + DataHoraUtil.formatar(c.getFim()) + "\n\n" +
                "LEITURAS DE ENERGIA\n" +
                "Leitura inicial: " + c.getLeituraInicial().toPlainString() + " kWh\n" +
                "Leitura final:   " + c.getLeituraFinal().toPlainString() + " kWh\n" +
                "Consumo total:   " + c.getConsumoKwh().toPlainString() + " kWh\n\n" +
                "Tarifa aplicada: " + moeda.format(c.getTarifaKwh()) + " / kWh\n" +
                "VALOR TOTAL:     " + moeda.format(c.getValorTotal()) + "\n\n" +
                "Cobrança gerada automaticamente com status PENDENTE.\n" +
                "Data de emissão: " + DataHoraUtil.formatar(c.getDataEmissao()) + "\n";
    }
}
