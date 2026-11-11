package br.ufms.locacao.model.service;

import br.ufms.locacao.model.dao.ReservaDAO;
import br.ufms.locacao.model.entity.Reserva;
import br.ufms.locacao.model.entity.StatusReserva;
import br.ufms.locacao.util.ConexaoBanco;
import java.sql.Connection;
import java.time.LocalDateTime;

public class ReservaService {
    private final ReservaDAO reservaDAO;
    private final DisponibilidadeService disponibilidadeService;

    public ReservaService(ReservaDAO reservaDAO, DisponibilidadeService disponibilidadeService) {
        this.reservaDAO = reservaDAO; this.disponibilidadeService = disponibilidadeService;
    }

    public int reservar(int moradorId, int salaoId, LocalDateTime inicio, LocalDateTime fim, String cpfFuncionario) throws Exception {
        disponibilidadeService.validarPeriodo(inicio, fim);
        if (moradorId <= 0 || salaoId <= 0) throw new IllegalArgumentException("Selecione o morador e o salão.");
        try (Connection conexao = ConexaoBanco.abrir()) {
            conexao.setAutoCommit(false);
            try {
                if (reservaDAO.temConflito(conexao, salaoId, inicio, fim)) {
                    throw new IllegalArgumentException("O salão já possui uma reserva nesse período.");
                }
                int id = reservaDAO.inserir(conexao, moradorId, salaoId, inicio, fim, cpfFuncionario);
                conexao.commit();
                return id;
            } catch (Exception e) { conexao.rollback(); throw e; }
        }
    }

    public void cancelar(int reservaId) throws Exception {
        try (Connection conexao = ConexaoBanco.abrir()) {
            conexao.setAutoCommit(false);
            try {
                Reserva reserva = reservaDAO.buscarPorId(conexao, reservaId);
                if (reserva == null) throw new IllegalArgumentException("Reserva não encontrada.");
                if (!reserva.podeCancelar()) {
                    throw new IllegalArgumentException("Só é possível cancelar uma reserva antes do início da locação.");
                }
                reservaDAO.atualizarStatus(conexao, reservaId, StatusReserva.CANCELADA);
                conexao.commit();
            } catch (Exception e) { conexao.rollback(); throw e; }
        }
    }
}
