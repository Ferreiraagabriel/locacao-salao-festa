package br.ufms.locacao.model.entity;

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
    public void setReserva(Reserva reserva) { this.reserva = reserva; }
    public void setDataInicioReal(LocalDateTime dataInicioReal) { this.dataInicioReal = dataInicioReal; }
    public void setChavesEntregues(boolean chavesEntregues) { this.chavesEntregues = chavesEntregues; }
}
