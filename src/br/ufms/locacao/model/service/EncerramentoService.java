package br.ufms.locacao.model.service;

import br.ufms.locacao.model.dao.*;
import br.ufms.locacao.model.entity.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;

public class EncerramentoService {
    private final LocacaoDAO locacaoDAO;
    private final LeituraDAO leituraDAO;
    private final TarifaDAO tarifaDAO;
    private final ConsumoDAO consumoDAO;
    private final CobrancaDAO cobrancaDAO;
    private final ComprovanteDAO comprovanteDAO;
    private final ReservaDAO reservaDAO;

    public EncerramentoService(LocacaoDAO locacaoDAO, LeituraDAO leituraDAO, TarifaDAO tarifaDAO,
            ConsumoDAO consumoDAO, CobrancaDAO cobrancaDAO, ComprovanteDAO comprovanteDAO, ReservaDAO reservaDAO) {
        this.locacaoDAO = locacaoDAO; this.leituraDAO = leituraDAO; this.tarifaDAO = tarifaDAO;
        this.consumoDAO = consumoDAO; this.cobrancaDAO = cobrancaDAO; this.comprovanteDAO = comprovanteDAO; this.reservaDAO = reservaDAO;
    }

    public Comprovante encerrarLocacao(int locacaoId, boolean chavesDevolvidas, String condicaoSalao, BigDecimal leituraFinal, String cpfFuncionario) throws Exception {
        try (Connection conexao = ConexaoBanco.abrir()) {
            conexao.setAutoCommit(false);
            try {
                Locacao locacao = locacaoDAO.buscarEmAndamento(conexao, locacaoId);
                if (locacao == null) throw new IllegalArgumentException("A locação selecionada não está em andamento.");
                locacao.setLeituraInicial(leituraDAO.buscarPorTipo(conexao, locacaoId, TipoLeitura.INICIAL));

                TarifaDeEnergia tarifa = tarifaDAO.buscarTarifaVigente(conexao, LocalDate.now());
                ConsumoDeEnergia consumo = locacao.encerrar(chavesDevolvidas, condicaoSalao, leituraFinal, tarifa, cpfFuncionario);

                LeituraDeEnergia leituraFinalRegistrada = consumo.getLeituraFinal();
                leituraFinalRegistrada.setId(leituraDAO.inserir(conexao, leituraFinalRegistrada));
                consumoDAO.inserir(conexao, consumo);
                cobrancaDAO.inserir(conexao, locacaoId, locacao.getReserva().getMorador().getId(),
                        consumo.getConsumoKwh(), tarifa.getValorKwh(), consumo.getValorTotal());
                locacaoDAO.encerrar(conexao, locacaoId, locacao.getDataFimReal(), locacao.getCondicaoSalao());
                reservaDAO.atualizarStatus(conexao, locacao.getReserva().getId(), StatusReserva.ENCERRADA);

                Comprovante comprovante = consumo.gerarComprovante(locacao);
                comprovante.setId(comprovanteDAO.inserir(conexao, comprovante));
                conexao.commit();
                return comprovante;
            } catch (Exception e) { conexao.rollback(); throw e; }
        }
    }
}
