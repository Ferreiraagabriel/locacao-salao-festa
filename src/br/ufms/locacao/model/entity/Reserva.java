package br.ufms.locacao.model.entity;

import java.time.LocalDateTime;

public class Reserva {
    private int id;
    private Morador morador;
    private SalaoDeFestas salao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private StatusReserva status;

    public Reserva(int id, Morador morador, SalaoDeFestas salao, LocalDateTime dataInicio, LocalDateTime dataFim, StatusReserva status) {
        this.id = id;
        this.morador = morador;
        this.salao = salao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
    }

    public int getId() { return id; }
    public Morador getMorador() { return morador; }
    public SalaoDeFestas getSalao() { return salao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public StatusReserva getStatus() { return status; }
    public void setStatus(StatusReserva status) { this.status = status; }
    public void setMorador(Morador morador) { this.morador = morador; }
    public void setSalao(SalaoDeFestas salao) { this.salao = salao; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public boolean podeCancelar() { return status == StatusReserva.RESERVADA; }
}
