package br.ufms.locacao.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cobranca {
    private int id;
    private int locacaoId;
    private BigDecimal valor;
    private StatusCobranca status;
    private LocalDate dataCriacao;

    public Cobranca(int id, int locacaoId, BigDecimal valor, StatusCobranca status, LocalDate dataCriacao) {
        this.id = id; this.locacaoId = locacaoId; this.valor = valor; this.status = status; this.dataCriacao = dataCriacao;
    }

    public int getId() { return id; }
    public int getLocacaoId() { return locacaoId; }
    public BigDecimal getValor() { return valor; }
    public StatusCobranca getStatus() { return status; }
    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public void setStatus(StatusCobranca status) { this.status = status; }
    public void setLocacaoId(int locacaoId) { this.locacaoId = locacaoId; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
}
