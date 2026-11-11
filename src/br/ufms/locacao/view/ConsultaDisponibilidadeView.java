package br.ufms.locacao.view;

import br.ufms.locacao.model.entity.SalaoDeFestas;
import br.ufms.locacao.util.DataHoraUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ConsultaDisponibilidadeView extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JTextField campoData = EstiloTela.campo(10);
    private final JTextField campoInicio = EstiloTela.campo(5);
    private final JTextField campoFim = EstiloTela.campo(5);
    private final JButton botaoConsultar = EstiloTela.botao("Consultar disponibilidade");
    private final DefaultTableModel modelo = EstiloTela.modeloTabela("Código", "Salão", "Capacidade");
    private final JTable tabela = new JTable(modelo);
    private final JLabel mensagem = new JLabel("Informe o período para consultar os salões livres.");

    public ConsultaDisponibilidadeView() {
        setLayout(new BorderLayout(15, 15)); setBorder(new EmptyBorder(24, 28, 24, 28)); setBackground(EstiloTela.FUNDO);
        add(EstiloTela.cabecalho("Consultar disponibilidade", "Verifique se existe um salão livre na data e no horário desejados."), BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(10, 12)); centro.setOpaque(false);
        JPanel formulario = EstiloTela.painelFormulario();
        formulario.add(new JLabel("Data (DD/MM/AAAA):")); formulario.add(campoData);
        formulario.add(new JLabel("Início (HH:MM):")); formulario.add(campoInicio);
        formulario.add(new JLabel("Término (HH:MM):")); formulario.add(campoFim); formulario.add(botaoConsultar);
        centro.add(formulario, BorderLayout.NORTH);
        tabela.setRowHeight(28); tabela.setFillsViewportHeight(true);
        centro.add(new JScrollPane(tabela), BorderLayout.CENTER);
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT)); rodape.setOpaque(false); rodape.add(mensagem); centro.add(rodape, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);
        campoData.setText(DataHoraUtil.formatarData(java.time.LocalDate.now())); campoInicio.setText("18:00"); campoFim.setText("23:00");
    }

    public void aoConsultar(Runnable acao) { botaoConsultar.addActionListener(e -> acao.run()); }
    public String getData() { return campoData.getText(); }
    public String getHoraInicio() { return campoInicio.getText(); }
    public String getHoraFim() { return campoFim.getText(); }
    public void mostrarSaloes(List<SalaoDeFestas> saloes) {
        modelo.setRowCount(0);
        for (SalaoDeFestas salao : saloes) modelo.addRow(new Object[]{salao.getCodigo(), salao.getNome(), salao.getCapacidadeMaxima()});
        mensagem.setText(saloes.isEmpty() ? "Nenhum salão disponível para esse período." : saloes.size() + " salão(ões) disponível(is).");
    }
    public void mostrarMensagem(String texto, boolean erro) { mensagem.setForeground(erro ? new Color(170, 45, 45) : new Color(35, 110, 70)); mensagem.setText(texto); }
}
