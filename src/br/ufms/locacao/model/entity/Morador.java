package br.ufms.locacao.model.entity;

public class Morador {
    private int id;
    private String nome;
    private String unidade;
    private String contato;

    public Morador(int id, String nome, String unidade, String contato) {
        this.id = id;
        this.nome = nome;
        this.unidade = unidade;
        this.contato = contato;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getUnidade() { return unidade; }
    public String getContato() { return contato; }
    public void setNome(String nome) { this.nome = nome; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public void setContato(String contato) { this.contato = contato; }

    @Override
    public String toString() { return nome + " - " + unidade; }
}
