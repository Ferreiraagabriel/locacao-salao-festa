package br.ufms.locacao.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LeituraDeEnergia {
    private int id;
    private int locacaoId;
    private BigDecimal valorKwh;
    private LocalDateTime dataHora;
    private TipoLeitura tipo;
    private String funcionarioCpf;

    public LeituraDeEnergia(int id, int locacaoId, BigDecimal valorKwh, LocalDateTime dataHora, TipoLeitura tipo, String funcionarioCpf) {
        this.id = id; this.locacaoId = locacaoId; this.valorKwh = valorKwh;
        this.dataHora = dataHora; this.tipo = tipo; this.funcionarioCpf = funcionarioCpf;
    }

    public int getId() { return id; }
    public int getLocacaoId() { return locacaoId; }
    public BigDecimal getValorLeitura() { return valorKwh; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public TipoLeitura getTipo() { return tipo; }
    public String getFuncionarioCpf() { return funcionarioCpf; }
    public void setValorKwh(BigDecimal valorKwh) { this.valorKwh = valorKwh; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public void setTipo(TipoLeitura tipo) { this.tipo = tipo; }
    public void setLocacaoId(int locacaoId) { this.locacaoId = locacaoId; }
    public void setFuncionarioCpf(String funcionarioCpf) { this.funcionarioCpf = funcionarioCpf; }
}
