package br.ufms.locacao.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Locacao {
    private int id;
    private Reserva reserva;
    private StatusLocacao status;
    private LocalDateTime dataInicioReal;
    private LocalDateTime dataFimReal;
    private boolean chavesEntregues;
    private boolean chavesDevolvidas;
    private String condicaoSalao;
    private LeituraDeEnergia leituraInicial;
    private LeituraDeEnergia leituraFinal;

    public Locacao(int id, Reserva reserva, StatusLocacao status, LocalDateTime dataInicioReal, LocalDateTime dataFimReal, boolean chavesEntregues, boolean chavesDevolvidas, String condicaoSalao) {
        this.id = id; this.reserva = reserva; this.status = status; this.dataInicioReal = dataInicioReal;
        this.dataFimReal = dataFimReal; this.chavesEntregues = chavesEntregues;
        this.chavesDevolvidas = chavesDevolvidas; this.condicaoSalao = condicaoSalao;
    }

    public int getId() { return id; }
    public Reserva getReserva() { return reserva; }
    public StatusLocacao getStatus() { return status; }
    public LocalDateTime getDataInicioReal() { return dataInicioReal; }
    public LocalDateTime getDataFimReal() { return dataFimReal; }
    public boolean isChavesEntregues() { return chavesEntregues; }
    public boolean isChavesDevolvidas() { return chavesDevolvidas; }
    public String getCondicaoSalao() { return condicaoSalao; }
    public void setStatus(StatusLocacao status) { this.status = status; }
    public void setDataFimReal(LocalDateTime dataFimReal) { this.dataFimReal = dataFimReal; }
    public void setChavesDevolvidas(boolean chavesDevolvidas) { this.chavesDevolvidas = chavesDevolvidas; }
    public void setCondicaoSalao(String condicaoSalao) { this.condicaoSalao = condicaoSalao; }
    public boolean estaEmAndamento() { return status == StatusLocacao.EM_ANDAMENTO; }
    public LeituraDeEnergia getLeituraInicial() { return leituraInicial; }
    public void setLeituraInicial(LeituraDeEnergia leituraInicial) { this.leituraInicial = leituraInicial; }
    public LeituraDeEnergia getLeituraFinal() { return leituraFinal; }

    public ConsumoDeEnergia encerrar(boolean chavesDevolvidas, String condicaoSalao, BigDecimal valorLeituraFinal,
            TarifaDeEnergia tarifa, String cpfFuncionario) {
        if (!estaEmAndamento()) throw new IllegalStateException("A locação selecionada não está em andamento.");
        if (!chavesDevolvidas) throw new IllegalArgumentException("Confirme a devolução das chaves.");
        if (condicaoSalao == null || condicaoSalao.isBlank()) throw new IllegalArgumentException("Informe as condições do salão.");
        if (leituraInicial == null) throw new IllegalStateException("A leitura inicial não foi encontrada.");
        if (valorLeituraFinal == null || valorLeituraFinal.signum() < 0) {
            throw new IllegalArgumentException("A leitura final deve ser igual ou maior que zero.");
        }
        if (valorLeituraFinal.compareTo(leituraInicial.getValorLeitura()) < 0) {
            throw new IllegalArgumentException("A leitura final não pode ser menor que a leitura inicial.");
        }
        if (tarifa == null) {
            throw new IllegalStateException("Não é possível encerrar: não existe tarifa de energia vigente.");
        }

        LocalDateTime agora = LocalDateTime.now();
        this.leituraFinal = new LeituraDeEnergia(0, id, valorLeituraFinal, agora, TipoLeitura.FINAL, cpfFuncionario);
        ConsumoDeEnergia consumo = new ConsumoDeEnergia(id, leituraInicial, leituraFinal, tarifa);
        this.chavesDevolvidas = true;
        this.condicaoSalao = condicaoSalao.trim();
        this.dataFimReal = agora;
        this.status = StatusLocacao.ENCERRADA;
        return consumo;
    }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }
    public void setDataInicioReal(LocalDateTime dataInicioReal) { this.dataInicioReal = dataInicioReal; }
    public void setChavesEntregues(boolean chavesEntregues) { this.chavesEntregues = chavesEntregues; }
}
