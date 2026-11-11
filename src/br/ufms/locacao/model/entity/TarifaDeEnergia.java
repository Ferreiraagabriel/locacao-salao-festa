package br.ufms.locacao.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TarifaDeEnergia {
    private int id;
    private BigDecimal valorPorKwh;
    private LocalDate inicioVigencia;
    private LocalDate fimVigencia;

    public TarifaDeEnergia(int id, BigDecimal valorPorKwh, LocalDate inicioVigencia, LocalDate fimVigencia) {
        this.id = id; this.valorPorKwh = valorPorKwh; this.inicioVigencia = inicioVigencia; this.fimVigencia = fimVigencia;
    }

    public int getId() { return id; }
    public BigDecimal getValorPorKwh() { return valorPorKwh; }
    public LocalDate getInicioVigencia() { return inicioVigencia; }
    public LocalDate getFimVigencia() { return fimVigencia; }
    public void setValorPorKwh(BigDecimal valorPorKwh) { this.valorPorKwh = valorPorKwh; }
    public void setInicioVigencia(LocalDate inicioVigencia) { this.inicioVigencia = inicioVigencia; }
    public void setFimVigencia(LocalDate fimVigencia) { this.fimVigencia = fimVigencia; }
    public boolean estaVigente(LocalDate data) {
        return !data.isBefore(inicioVigencia) && (fimVigencia == null || !data.isAfter(fimVigencia));
    }
}
