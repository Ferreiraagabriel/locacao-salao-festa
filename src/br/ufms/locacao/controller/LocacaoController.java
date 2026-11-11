package br.ufms.locacao.controller;

import br.ufms.locacao.model.dao.ReservaDAO;
import br.ufms.locacao.model.dao.LocacaoDAO;
import br.ufms.locacao.model.service.LocacaoService;
import br.ufms.locacao.view.InicioLocacaoView;
import java.math.BigDecimal;

public class LocacaoController {
    private final InicioLocacaoView view;
    private final ReservaDAO reservaDAO;
    private final LocacaoDAO locacaoDAO;
    private final LocacaoService service;
    private final String cpfFuncionario;
    private final Runnable atualizarOutrasTelas;

    public LocacaoController(InicioLocacaoView view, ReservaDAO reservaDAO, LocacaoDAO locacaoDAO, LocacaoService service, String cpfFuncionario, Runnable atualizarOutrasTelas) {
        this.view=view; this.reservaDAO=reservaDAO; this.locacaoDAO=locacaoDAO; this.service=service;
        this.cpfFuncionario=cpfFuncionario; this.atualizarOutrasTelas=atualizarOutrasTelas;
        view.aoIniciar(this::iniciar); atualizar();
    }

    public void atualizar() {
        try { view.mostrarReservas(reservaDAO.listarAbertas()); }
        catch (Exception e) { view.mostrarMensagem("Erro ao listar reservas: " + e.getMessage(), true); }
    }

    private void iniciar() {
        try {
            int reservaId = view.getReservaSelecionada();
            if (reservaId < 0) throw new IllegalArgumentException("Selecione uma reserva.");
            BigDecimal leitura = new BigDecimal(view.getLeituraInicial().replace(',', '.'));
            int locacaoId = service.iniciar(reservaId, leitura, view.isChavesEntregues(), cpfFuncionario);
            view.limparFormulario(); view.mostrarMensagem("Locação nº " + locacaoId + " iniciada.", false);
            atualizar(); atualizarOutrasTelas.run();
        } catch (NumberFormatException e) { view.mostrarMensagem("Informe uma leitura inicial numérica. Use ponto ou vírgula para os decimais.", true); }
        catch (Exception e) { view.mostrarMensagem(e.getMessage(), true); }
    }
}
