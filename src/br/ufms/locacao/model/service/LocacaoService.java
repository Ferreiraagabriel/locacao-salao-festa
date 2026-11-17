package br.ufms.locacao.model.service;

import br.ufms.locacao.model.dao.LeituraDAO;
import br.ufms.locacao.model.dao.LocacaoDAO;
import br.ufms.locacao.model.dao.ReservaDAO;
import br.ufms.locacao.model.entity.Reserva;
import br.ufms.locacao.model.entity.StatusReserva;
import br.ufms.locacao.model.entity.TipoLeitura;
import br.ufms.locacao.model.dao.ConexaoBanco;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;

public class LocacaoService {
    private final ReservaDAO reservaDAO;
    private final LocacaoDAO locacaoDAO;
    private final LeituraDAO leituraDAO;

    public LocacaoService(ReservaDAO reservaDAO, LocacaoDAO locacaoDAO, LeituraDAO leituraDAO) {
        this.reservaDAO = reservaDAO; this.locacaoDAO = locacaoDAO; this.leituraDAO = leituraDAO;
    }

    public int iniciar(int reservaId, BigDecimal leituraInicial, boolean chavesEntregues, String cpfFuncionario) throws Exception {
        if (!chavesEntregues) throw new IllegalArgumentException("Confirme a entrega das chaves antes de iniciar a locação.");
        if (leituraInicial == null || leituraInicial.signum() < 0) throw new IllegalArgumentException("A leitura inicial deve ser um valor igual ou maior que zero.");
        try (Connection conexao = ConexaoBanco.abrir()) {
            conexao.setAutoCommit(false);
            try {
                Reserva reserva = reservaDAO.buscarPorId(conexao, reservaId);
                if (reserva == null) throw new IllegalArgumentException("Reserva não encontrada.");
                if (reserva.getStatus() != StatusReserva.RESERVADA) throw new IllegalArgumentException("A reserva não está disponível para início.");
                LocalDateTime agora = LocalDateTime.now();
                if (agora.toLocalDate().isBefore(reserva.getDataInicio().toLocalDate())) {
                    throw new IllegalArgumentException("A locação só pode ser iniciada a partir do dia da reserva.");
                }
                if (agora.isAfter(reserva.getDataFim())) {
                    throw new IllegalArgumentException("O período desta reserva já terminou. Cancele a reserva ou registre uma nova.");
                }
                int locacaoId = locacaoDAO.inserir(conexao, reservaId, agora);
                leituraDAO.inserir(conexao, locacaoId, leituraInicial, agora, TipoLeitura.INICIAL, cpfFuncionario);
                reservaDAO.atualizarStatus(conexao, reservaId, StatusReserva.INICIADA);
                conexao.commit();
                return locacaoId;
            } catch (Exception e) { conexao.rollback(); throw e; }
        }
    }
}
