package br.ufms.locacao.model.entity;

import java.math.BigDecimal;

public class ConsumoDeEnergia {
    private int locacaoId;
    private BigDecimal consumoKwh;
    private BigDecimal valorTotal;
    private TarifaDeEnergia tarifa;

    public ConsumoDeEnergia(int locacaoId, BigDecimal consumoKwh, BigDecimal valorTotal, TarifaDeEnergia tarifa) {
        this.locacaoId = locacaoId; this.consumoKwh = consumoKwh; this.valorTotal = valorTotal; this.tarifa = tarifa;
    }

    public int getLocacaoId() { return locacaoId; }
    public BigDecimal getConsumoKwh() { return consumoKwh; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public TarifaDeEnergia getTarifa() { return tarifa; }
    public void setConsumoKwh(BigDecimal consumoKwh) { this.consumoKwh = consumoKwh; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public void setTarifa(TarifaDeEnergia tarifa) { this.tarifa = tarifa; }
    public void setLocacaoId(int locacaoId) { this.locacaoId = locacaoId; }

    public static ConsumoDeEnergia calcular(int locacaoId, BigDecimal leituraInicial, BigDecimal leituraFinal, TarifaDeEnergia tarifa) {
        if (leituraInicial == null || leituraFinal == null || tarifa == null) {
            throw new IllegalArgumentException("Leituras e tarifa são obrigatórias para calcular o consumo.");
        }
        if (leituraFinal.compareTo(leituraInicial) < 0) {
            throw new IllegalArgumentException("A leitura final não pode ser menor que a inicial.");
        }
        BigDecimal consumo = leituraFinal.subtract(leituraInicial).setScale(0, java.math.RoundingMode.HALF_UP);
        BigDecimal valor = consumo.multiply(tarifa.getValorPorKwh()).setScale(2, java.math.RoundingMode.HALF_UP);
        return new ConsumoDeEnergia(locacaoId, consumo, valor, tarifa);
    }
}
