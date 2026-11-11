package br.ufms.locacao.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Comprovante {
    private int id;
    private int locacaoId;
    private String morador;
    private String unidade;
    private String salao;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private BigDecimal leituraInicial;
    private BigDecimal leituraFinal;
    private BigDecimal consumoKwh;
    private BigDecimal tarifaKwh;
    private BigDecimal valorTotal;
    private LocalDateTime dataEmissao;

    public Comprovante(int id, int locacaoId, String morador, String unidade, String salao, LocalDateTime inicio, LocalDateTime fim, BigDecimal leituraInicial, BigDecimal leituraFinal, BigDecimal consumoKwh, BigDecimal tarifaKwh, BigDecimal valorTotal, LocalDateTime dataEmissao) {
        this.id = id; this.locacaoId = locacaoId; this.morador = morador; this.unidade = unidade; this.salao = salao;
        this.inicio = inicio; this.fim = fim; this.leituraInicial = leituraInicial; this.leituraFinal = leituraFinal;
        this.consumoKwh = consumoKwh; this.tarifaKwh = tarifaKwh; this.valorTotal = valorTotal; this.dataEmissao = dataEmissao;
    }

    public int getId() { return id; }
    public int getLocacaoId() { return locacaoId; }
    public String getMorador() { return morador; }
    public String getUnidade() { return unidade; }
    public String getSalao() { return salao; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }
    public BigDecimal getLeituraInicial() { return leituraInicial; }
    public BigDecimal getLeituraFinal() { return leituraFinal; }
    public BigDecimal getConsumoKwh() { return consumoKwh; }
    public BigDecimal getTarifaKwh() { return tarifaKwh; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDateTime getDataEmissao() { return dataEmissao; }
    public void setLeituraInicial(BigDecimal leituraInicial) { this.leituraInicial = leituraInicial; }
    public void setLeituraFinal(BigDecimal leituraFinal) { this.leituraFinal = leituraFinal; }
    public void setConsumoKwh(BigDecimal consumoKwh) { this.consumoKwh = consumoKwh; }
    public void setTarifaKwh(BigDecimal tarifaKwh) { this.tarifaKwh = tarifaKwh; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public void setMorador(String morador) { this.morador = morador; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public void setSalao(String salao) { this.salao = salao; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
    public void setDataEmissao(LocalDateTime dataEmissao) { this.dataEmissao = dataEmissao; }
}
