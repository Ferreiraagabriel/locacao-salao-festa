package br.ufms.locacao.controller;

import br.ufms.locacao.model.dao.LocacaoDAO;
import br.ufms.locacao.model.entity.Comprovante;
import br.ufms.locacao.model.service.EncerramentoService;
import br.ufms.locacao.view.ComprovanteDialog;
import br.ufms.locacao.view.EncerramentoView;
import javax.swing.JFrame;
import java.math.BigDecimal;

public class EncerramentoController {
    private final EncerramentoView view;
    private final LocacaoDAO locacaoDAO;
    private final EncerramentoService service;
    private final String cpfFuncionario;
    private final JFrame janelaPrincipal;
    private final Runnable atualizarOutrasTelas;

    public EncerramentoController(EncerramentoView view, LocacaoDAO locacaoDAO, EncerramentoService service, String cpfFuncionario, JFrame janelaPrincipal, Runnable atualizarOutrasTelas) {
        this.view=view; this.locacaoDAO=locacaoDAO; this.service=service; this.cpfFuncionario=cpfFuncionario;
        this.janelaPrincipal=janelaPrincipal; this.atualizarOutrasTelas=atualizarOutrasTelas;
        view.aoEncerrar(this::encerrar); atualizar();
    }

    public void atualizar() {
        try { view.mostrarLocacoes(locacaoDAO.listarEmAndamento()); }
        catch (Exception e) { view.mostrarMensagem("Erro ao listar locações: " + e.getMessage(), true); }
    }

    private void encerrar() {
        try {
            int locacaoId = view.getLocacaoSelecionada();
            if (locacaoId < 0) throw new IllegalArgumentException("Selecione uma locação em andamento.");
            BigDecimal leitura = new BigDecimal(view.getLeituraFinal().replace(',', '.'));
            Comprovante comprovante = service.encerrarLocacao(locacaoId, view.isChavesDevolvidas(), view.getCondicaoSalao(), leitura, cpfFuncionario);
            view.limparFormulario(); view.mostrarMensagem("Locação encerrada. Cobrança pendente e comprovante gerados.", false);
            atualizarOutrasTelas.run();
            new ComprovanteDialog(janelaPrincipal, comprovante).setVisible(true);
        } catch (NumberFormatException e) { view.mostrarMensagem("Informe uma leitura final numérica. Use ponto ou vírgula para os decimais.", true); }
        catch (Exception e) { view.mostrarMensagem(e.getMessage(), true); }
    }
}
