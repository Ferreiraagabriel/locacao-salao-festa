package br.ufms.locacao.view;

import br.ufms.locacao.model.entity.Locacao;
import br.ufms.locacao.util.DataHoraUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EncerramentoView extends JPanel {
    private static final long serialVersionUID = 1L;
    private final DefaultTableModel modelo = EstiloTela.modeloTabela("Locação", "Morador", "Salão", "Início real");
    private final JTable tabela = new JTable(modelo);
    private final JTextField campoLeitura = EstiloTela.campo(12);
    private final JTextField campoCondicao = EstiloTela.campo(24);
    private final JCheckBox checkChaves = new JCheckBox("Confirmo a devolução das chaves");
    private final JButton botaoEncerrar = EstiloTela.botao("Encerrar locação e gerar comprovante");
    private final JLabel mensagem = new JLabel("Selecione uma locação em andamento.");

    public EncerramentoView() {
        setLayout(new BorderLayout(15,15)); setBorder(new EmptyBorder(24,28,24,28)); setBackground(EstiloTela.FUNDO);
        add(EstiloTela.cabecalho("Encerrar locação", "Registre a vistoria, a devolução das chaves e a leitura final."), BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(10,12)); centro.setOpaque(false); tabela.setRowHeight(27); tabela.setFillsViewportHeight(true); centro.add(new JScrollPane(tabela), BorderLayout.CENTER);
        JPanel formulario = new JPanel(new GridBagLayout()); formulario.setBackground(Color.WHITE); formulario.setBorder(new EmptyBorder(10,12,10,12));
        GridBagConstraints c = new GridBagConstraints(); c.insets=new Insets(6,7,6,7); c.anchor=GridBagConstraints.LINE_START;
        c.gridx=0;c.gridy=0;formulario.add(new JLabel("Leitura final (kWh):"),c);c.gridx=1;formulario.add(campoLeitura,c);
        c.gridx=0;c.gridy=1;formulario.add(new JLabel("Condições do salão:"),c);c.gridx=1;c.fill=GridBagConstraints.HORIZONTAL;formulario.add(campoCondicao,c);
        c.gridx=0;c.gridy=2;c.gridwidth=2;c.fill=GridBagConstraints.NONE;formulario.add(checkChaves,c);
        c.gridy=3;c.fill=GridBagConstraints.HORIZONTAL;formulario.add(botaoEncerrar,c);
        centro.add(formulario,BorderLayout.SOUTH); add(centro,BorderLayout.CENTER); add(mensagem,BorderLayout.PAGE_END);
    }

    public void aoEncerrar(Runnable acao) { botaoEncerrar.addActionListener(e -> acao.run()); }
    public int getLocacaoSelecionada() { int linha=tabela.getSelectedRow(); return linha < 0 ? -1 : (int) modelo.getValueAt(tabela.convertRowIndexToModel(linha),0); }
    public String getLeituraFinal() { return campoLeitura.getText().trim(); }
    public String getCondicaoSalao() { return campoCondicao.getText().trim(); }
    public boolean isChavesDevolvidas() { return checkChaves.isSelected(); }
    public void limparFormulario() { campoLeitura.setText(""); campoCondicao.setText(""); checkChaves.setSelected(false); }
    public void mostrarLocacoes(List<Locacao> locacoes) {
        modelo.setRowCount(0);
        for (Locacao l : locacoes) modelo.addRow(new Object[]{l.getId(),l.getReserva().getMorador().getNome(),l.getReserva().getSalao().getNome(),DataHoraUtil.formatar(l.getDataInicioReal())});
    }
    public void mostrarMensagem(String texto, boolean erro) { mensagem.setForeground(erro ? new Color(170,45,45) : new Color(35,110,70)); mensagem.setText(texto); }
}
