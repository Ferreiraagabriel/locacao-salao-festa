package br.ufms.locacao.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class ConsumoDeEnergia {
    private final int locacaoId;
    private final LeituraDeEnergia leituraInicial;
    private final LeituraDeEnergia leituraFinal;
    private final TarifaDeEnergia tarifa;
    private final BigDecimal consumoKwh;
    private final BigDecimal valorTotal;

    public ConsumoDeEnergia(int locacaoId, LeituraDeEnergia leituraInicial, LeituraDeEnergia leituraFinal, TarifaDeEnergia tarifa) {
        if (leituraInicial == null || leituraFinal == null || tarifa == null) {
            throw new IllegalArgumentException("Leituras e tarifa são obrigatórias para calcular o consumo.");
        }
        BigDecimal valorInicial = leituraInicial.getValorLeitura();
        BigDecimal valorFinal = leituraFinal.getValorLeitura();
        if (valorFinal.compareTo(valorInicial) < 0) {
            throw new IllegalArgumentException("A leitura final não pode ser menor que a leitura inicial.");
        }
        this.locacaoId = locacaoId;
        this.leituraInicial = leituraInicial;
        this.leituraFinal = leituraFinal;
        this.tarifa = tarifa;
        // RN01: consumo = leitura final - leitura inicial (sem arredondar o kWh)
        this.consumoKwh = valorFinal.subtract(valorInicial);
        // RN02: valor = consumo x valor do kWh da tarifa vigente, arredondado em centavos
        this.valorTotal = consumoKwh.multiply(tarifa.getValorKwh()).setScale(2, RoundingMode.HALF_UP);
    }

    public Comprovante gerarComprovante(Locacao locacao) {
        Reserva reserva = locacao.getReserva();
        return new Comprovante(0, locacaoId, reserva.getMorador().getNome(), reserva.getMorador().getUnidade(),
                reserva.getSalao().getNome(), locacao.getDataInicioReal(), locacao.getDataFimReal(),
                leituraInicial.getValorLeitura(), leituraFinal.getValorLeitura(), consumoKwh,
                tarifa.getValorKwh(), valorTotal, LocalDateTime.now());
    }

    public int getLocacaoId() { return locacaoId; }
    public LeituraDeEnergia getLeituraInicial() { return leituraInicial; }
    public LeituraDeEnergia getLeituraFinal() { return leituraFinal; }
    public TarifaDeEnergia getTarifa() { return tarifa; }
    public BigDecimal getConsumoKwh() { return consumoKwh; }
    public BigDecimal getValorTotal() { return valorTotal; }
}
