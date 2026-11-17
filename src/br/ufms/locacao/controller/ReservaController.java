package br.ufms.locacao.controller;

import br.ufms.locacao.model.dao.MoradorDAO;
import br.ufms.locacao.model.dao.ReservaDAO;
import br.ufms.locacao.model.dao.SalaoDAO;
import br.ufms.locacao.model.entity.Morador;
import br.ufms.locacao.model.entity.SalaoDeFestas;
import br.ufms.locacao.model.service.ReservaService;
import br.ufms.locacao.util.DataHoraUtil;
import br.ufms.locacao.view.ReservaView;
import java.util.List;

public class ReservaController {
    private final ReservaView view;
    private final MoradorDAO moradorDAO;
    private final SalaoDAO salaoDAO;
    private final ReservaDAO reservaDAO;
    private final ReservaService service;
    private final String cpfFuncionario;
    private final Runnable atualizarOutrasTelas;

    public ReservaController(ReservaView view, MoradorDAO moradorDAO, SalaoDAO salaoDAO, ReservaDAO reservaDAO,
            ReservaService service, String cpfFuncionario, Runnable atualizarOutrasTelas) {
        this.view=view; this.moradorDAO=moradorDAO; this.salaoDAO=salaoDAO; this.reservaDAO=reservaDAO;
        this.service=service; this.cpfFuncionario=cpfFuncionario; this.atualizarOutrasTelas=atualizarOutrasTelas;
        view.aoReservar(this::reservar); view.aoCancelar(this::cancelar);
        atualizar();
    }

    public void atualizar() {
        try {
            List<Morador> moradores = moradorDAO.listarTodos();
            List<SalaoDeFestas> saloes = salaoDAO.listarTodos();
            view.setMoradores(moradores); view.setSaloes(saloes);
            view.mostrarReservas(reservaDAO.listarTodas());
        } catch (Exception e) { view.mostrarMensagem("Erro ao carregar reservas: " + e.getMessage(), true); }
    }

    private void reservar() {
        try {
            Morador morador = view.getMoradorSelecionado();
            SalaoDeFestas salao = view.getSalaoSelecionado();
            if (morador == null || salao == null) throw new IllegalArgumentException("Cadastre dados iniciais válidos ou selecione o morador e o salão.");
            var inicio = DataHoraUtil.lerDataHora(view.getData(), view.getHoraInicio());
            var fim = DataHoraUtil.lerDataHora(view.getData(), view.getHoraFim());
            int id = service.reservar(morador.getId(), salao.getId(), inicio, fim, cpfFuncionario);
            view.mostrarMensagem("Reserva nº " + id + " registrada com sucesso.", false);
            atualizar(); atualizarOutrasTelas.run();
        } catch (Exception e) { view.mostrarMensagem(e.getMessage(), true); }
    }

    private void cancelar() {
        try {
            int id = view.getReservaSelecionada();
            if (id < 0) throw new IllegalArgumentException("Selecione uma reserva na tabela.");
            if (!view.confirmarCancelamento(id)) return;
            service.cancelar(id);
            view.mostrarMensagem("Reserva cancelada.", false);
            atualizar(); atualizarOutrasTelas.run();
        } catch (Exception e) { view.mostrarMensagem(e.getMessage(), true); }
    }
}
