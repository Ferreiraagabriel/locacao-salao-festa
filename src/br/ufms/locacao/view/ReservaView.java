package br.ufms.locacao.view;

import br.ufms.locacao.model.entity.Morador;
import br.ufms.locacao.model.entity.Reserva;
import br.ufms.locacao.model.entity.SalaoDeFestas;
import br.ufms.locacao.util.DataHoraUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReservaView extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JComboBox<Morador> comboMorador = new JComboBox<>();
    private final JComboBox<SalaoDeFestas> comboSalao = new JComboBox<>();
    private final JTextField campoData = EstiloTela.campo(9);
    private final JTextField campoInicio = EstiloTela.campo(5);
    private final JTextField campoFim = EstiloTela.campo(5);
    private final JButton botaoReservar = EstiloTela.botao("Registrar reserva");
    private final JButton botaoCancelar = EstiloTela.botao("Cancelar reserva selecionada");
    private final DefaultTableModel modelo = EstiloTela.modeloTabela("Nº", "Morador", "Unidade", "Salão", "Início", "Término", "Situação");
    private final JTable tabela = new JTable(modelo);
    private final JLabel mensagem = new JLabel("Selecione os dados para registrar uma reserva.");

    public ReservaView() {
        setLayout(new BorderLayout(15, 15)); setBorder(new EmptyBorder(24, 28, 24, 28)); setBackground(EstiloTela.FUNDO);
        add(EstiloTela.cabecalho("Reservar salão", "Registre uma reserva para um morador e um período."), BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(10, 12)); centro.setOpaque(false);
        JPanel formulario = new JPanel(new GridBagLayout()); formulario.setBackground(Color.WHITE); formulario.setBorder(new EmptyBorder(10, 12, 10, 12));
        GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(6, 7, 6, 7); c.anchor = GridBagConstraints.LINE_START;
        c.gridx=0;c.gridy=0;formulario.add(new JLabel("Morador responsável"),c); c.gridx=1; c.fill=GridBagConstraints.HORIZONTAL; formulario.add(comboMorador,c);
        c.gridx=2;c.fill=GridBagConstraints.NONE;formulario.add(new JLabel("Salão"),c);c.gridx=3;c.fill=GridBagConstraints.HORIZONTAL;formulario.add(comboSalao,c);
        c.gridx=0;c.gridy=1;c.fill=GridBagConstraints.NONE;formulario.add(new JLabel("Data (DD/MM/AAAA)"),c);c.gridx=1;c.fill=GridBagConstraints.HORIZONTAL;formulario.add(campoData,c);
        c.gridx=2;c.fill=GridBagConstraints.NONE;formulario.add(new JLabel("Início (HH:MM)"),c);c.gridx=3;c.fill=GridBagConstraints.HORIZONTAL;formulario.add(campoInicio,c);
        c.gridx=0;c.gridy=2;formulario.add(new JLabel("Término (HH:MM)"),c);c.gridx=1;formulario.add(campoFim,c);
        c.gridx=2;c.gridwidth=2;c.fill=GridBagConstraints.HORIZONTAL;formulario.add(botaoReservar,c);
        centro.add(formulario, BorderLayout.NORTH); tabela.setRowHeight(26); tabela.setFillsViewportHeight(true);
        centro.add(new JScrollPane(tabela), BorderLayout.CENTER);
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT)); rodape.setOpaque(false); rodape.add(botaoCancelar); rodape.add(mensagem); centro.add(rodape, BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);
        campoData.setText(DataHoraUtil.formatarData(java.time.LocalDate.now())); campoInicio.setText("18:00"); campoFim.setText("23:00");
    }

    public void aoReservar(Runnable acao) { botaoReservar.addActionListener(e -> acao.run()); }
    public void aoCancelar(Runnable acao) { botaoCancelar.addActionListener(e -> acao.run()); }
    public Morador getMoradorSelecionado() { return (Morador) comboMorador.getSelectedItem(); }
    public SalaoDeFestas getSalaoSelecionado() { return (SalaoDeFestas) comboSalao.getSelectedItem(); }
    public String getData() { return campoData.getText(); }
    public String getHoraInicio() { return campoInicio.getText(); }
    public String getHoraFim() { return campoFim.getText(); }
    public int getReservaSelecionada() { int linha=tabela.getSelectedRow(); return linha < 0 ? -1 : (int) modelo.getValueAt(tabela.convertRowIndexToModel(linha),0); }
    public void setMoradores(List<Morador> moradores) { comboMorador.removeAllItems(); for (Morador m : moradores) comboMorador.addItem(m); }
    public void setSaloes(List<SalaoDeFestas> saloes) { comboSalao.removeAllItems(); for (SalaoDeFestas s : saloes) comboSalao.addItem(s); }
    public void mostrarReservas(List<Reserva> reservas) {
        modelo.setRowCount(0);
        for (Reserva r : reservas) modelo.addRow(new Object[]{r.getId(), r.getMorador().getNome(), r.getMorador().getUnidade(), r.getSalao().getNome(), DataHoraUtil.formatar(r.getDataInicio()), DataHoraUtil.formatar(r.getDataFim()), r.getStatus()});
    }
    public void mostrarMensagem(String texto, boolean erro) { mensagem.setForeground(erro ? new Color(170,45,45) : new Color(35,110,70)); mensagem.setText(texto); }
}
