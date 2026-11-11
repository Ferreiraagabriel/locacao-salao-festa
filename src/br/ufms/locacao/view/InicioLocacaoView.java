package br.ufms.locacao.view;

import br.ufms.locacao.model.entity.Reserva;
import br.ufms.locacao.util.DataHoraUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InicioLocacaoView extends JPanel {
    private static final long serialVersionUID = 1L;
    private final DefaultTableModel modelo = EstiloTela.modeloTabela("Reserva", "Morador", "Salão", "Início previsto", "Término previsto");
    private final JTable tabela = new JTable(modelo);
    private final JTextField campoLeitura = EstiloTela.campo(12);
    private final JCheckBox checkChaves = new JCheckBox("Confirmo a entrega das chaves ao responsável");
    private final JButton botaoIniciar = EstiloTela.botao("Iniciar locação");
    private final JLabel mensagem = new JLabel("Selecione uma reserva para iniciar a locação.");

    public InicioLocacaoView() {
        setLayout(new BorderLayout(15,15)); setBorder(new EmptyBorder(24,28,24,28)); setBackground(EstiloTela.FUNDO);
        add(EstiloTela.cabecalho("Iniciar locação", "Registre a leitura inicial do medidor e a entrega das chaves."), BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(10,12)); centro.setOpaque(false); tabela.setRowHeight(27); tabela.setFillsViewportHeight(true); centro.add(new JScrollPane(tabela), BorderLayout.CENTER);
        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT,10,8)); formulario.setBackground(Color.WHITE);
        formulario.add(new JLabel("Leitura inicial (kWh):")); formulario.add(campoLeitura); formulario.add(checkChaves); formulario.add(botaoIniciar);
        centro.add(formulario, BorderLayout.SOUTH); add(centro, BorderLayout.CENTER);
        add(mensagem, BorderLayout.PAGE_END);
    }

    public void aoIniciar(Runnable acao) { botaoIniciar.addActionListener(e -> acao.run()); }
    public int getReservaSelecionada() { int linha=tabela.getSelectedRow(); return linha < 0 ? -1 : (int) modelo.getValueAt(tabela.convertRowIndexToModel(linha),0); }
    public String getLeituraInicial() { return campoLeitura.getText().trim(); }
    public boolean isChavesEntregues() { return checkChaves.isSelected(); }
    public void limparFormulario() { campoLeitura.setText(""); checkChaves.setSelected(false); }
    public void mostrarReservas(List<Reserva> reservas) {
        modelo.setRowCount(0);
        for (Reserva r : reservas) modelo.addRow(new Object[]{r.getId(),r.getMorador().getNome(),r.getSalao().getNome(),DataHoraUtil.formatar(r.getDataInicio()),DataHoraUtil.formatar(r.getDataFim())});
    }
    public void mostrarMensagem(String texto, boolean erro) { mensagem.setForeground(erro ? new Color(170,45,45) : new Color(35,110,70)); mensagem.setText(texto); }
}
