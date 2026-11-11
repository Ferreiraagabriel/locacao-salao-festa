package br.ufms.locacao.model.entity;

public class Usuario {
    private String cpf;
    private String nome;
    private String senhaHash;
    private PerfilUsuario perfil;

    public Usuario(String cpf, String nome, String senhaHash, PerfilUsuario perfil) {
        this.cpf = cpf;
        this.nome = nome;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }

    @Override
    public String toString() { return nome + " (" + perfil + ")"; }
}
