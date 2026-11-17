package br.ufms.locacao.model.service;

import br.ufms.locacao.model.dao.ReservaDAO;
import br.ufms.locacao.model.dao.SalaoDAO;
import br.ufms.locacao.model.entity.SalaoDeFestas;
import br.ufms.locacao.model.dao.ConexaoBanco;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DisponibilidadeService {
    private final SalaoDAO salaoDAO;
    private final ReservaDAO reservaDAO;

    public DisponibilidadeService(SalaoDAO salaoDAO, ReservaDAO reservaDAO) {
        this.salaoDAO = salaoDAO; this.reservaDAO = reservaDAO;
    }

    public List<SalaoDeFestas> consultar(LocalDateTime inicio, LocalDateTime fim) throws Exception {
        validarPeriodo(inicio, fim);
        List<SalaoDeFestas> disponiveis = new ArrayList<>();
        try (Connection conexao = ConexaoBanco.abrir()) {
            for (SalaoDeFestas salao : salaoDAO.listarTodos()) {
                if (!reservaDAO.temConflito(conexao, salao.getId(), inicio, fim)) disponiveis.add(salao);
            }
        }
        return disponiveis;
    }

    public void validarPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null || !inicio.isBefore(fim)) {
            throw new IllegalArgumentException("A data e hora de término devem ser posteriores ao início.");
        }
    }
}
