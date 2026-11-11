package br.ufms.locacao.model.entity;

import java.math.BigDecimal;

public class SalaoDeFestas {
    private int id;
    private String codigo;
    private String nome;
    private int capacidadeMaxima;
    private BigDecimal valorBase;
    private boolean disponivel;

    public SalaoDeFestas(int id, String codigo, String nome, int capacidadeMaxima, BigDecimal valorBase, boolean disponivel) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.capacidadeMaxima = capacidadeMaxima;
        this.valorBase = valorBase;
        this.disponivel = disponivel;
    }

    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public BigDecimal getValorBase() { return valorBase; }
    public boolean isDisponivel() { return disponivel; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCapacidadeMaxima(int capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }
    public void setValorBase(BigDecimal valorBase) { this.valorBase = valorBase; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    @Override
    public String toString() { return codigo + " - " + nome; }
}
