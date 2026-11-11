package br.ufms.locacao.model.service;

import br.ufms.locacao.model.dao.*;
import br.ufms.locacao.model.entity.*;
import br.ufms.locacao.util.ConexaoBanco;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public Comprovante encerrar(int locacaoId, BigDecimal leituraFinal, boolean chavesDevolvidas, String condicaoSalao, String cpfFuncionario) throws Exception {
        if (!chavesDevolvidas) throw new IllegalArgumentException("Confirme a devolução das chaves.");
        if (condicaoSalao == null || condicaoSalao.isBlank()) throw new IllegalArgumentException("Informe as condições do salão.");
        if (leituraFinal == null || leituraFinal.signum() < 0) throw new IllegalArgumentException("A leitura final deve ser igual ou maior que zero.");

        try (Connection conexao = ConexaoBanco.abrir()) {
            conexao.setAutoCommit(false);
            try {
                Locacao locacao = locacaoDAO.buscarEmAndamento(conexao, locacaoId);
                if (locacao == null) throw new IllegalArgumentException("A locação selecionada não está em andamento.");

                BigDecimal leituraInicial = leituraDAO.buscarInicial(conexao, locacaoId);
                int leituraInicialId = leituraDAO.buscarIdInicial(conexao, locacaoId);
                if (leituraInicial == null || leituraInicialId == 0) throw new IllegalStateException("A leitura inicial não foi encontrada.");
                if (leituraFinal.compareTo(leituraInicial) < 0) throw new IllegalArgumentException("A leitura final não pode ser menor que a leitura inicial.");

                TarifaDeEnergia tarifa = tarifaDAO.buscarVigente(conexao, LocalDate.now());
                if (tarifa == null) throw new IllegalStateException("Não existe tarifa de energia vigente para a data de hoje.");

                LocalDateTime agora = LocalDateTime.now();
                int leituraFinalId = leituraDAO.inserir(conexao, locacaoId, leituraFinal, agora, TipoLeitura.FINAL, cpfFuncionario);
                ConsumoDeEnergia consumo = ConsumoDeEnergia.calcular(locacaoId, leituraInicial, leituraFinal, tarifa);
                BigDecimal consumoKwh = consumo.getConsumoKwh();
                BigDecimal valorTotal = consumo.getValorTotal();

                consumoDAO.inserir(conexao, locacaoId, leituraInicialId, leituraFinalId, tarifa.getId(), consumoKwh, valorTotal);
                cobrancaDAO.inserir(conexao, locacaoId, locacao.getReserva().getMorador().getId(), consumoKwh, tarifa.getValorPorKwh(), valorTotal);
                locacaoDAO.encerrar(conexao, locacaoId, agora, condicaoSalao.trim());
                reservaDAO.atualizarStatus(conexao, locacao.getReserva().getId(), StatusReserva.ENCERRADA);

                Comprovante comprovante = new Comprovante(0, locacaoId, locacao.getReserva().getMorador().getNome(),
                        locacao.getReserva().getMorador().getUnidade(), locacao.getReserva().getSalao().getNome(),
                        locacao.getDataInicioReal(), agora, leituraInicial, leituraFinal,
                        consumoKwh, tarifa.getValorPorKwh(), valorTotal, agora);
                int comprovanteId = comprovanteDAO.inserir(conexao, comprovante);
                conexao.commit();
                return new Comprovante(comprovanteId, locacaoId, comprovante.getMorador(), comprovante.getUnidade(),
                        comprovante.getSalao(), comprovante.getInicio(), comprovante.getFim(), leituraInicial, leituraFinal,
                        consumoKwh, tarifa.getValorPorKwh(), valorTotal, agora);
            } catch (Exception e) { conexao.rollback(); throw e; }
        }
    }
}
