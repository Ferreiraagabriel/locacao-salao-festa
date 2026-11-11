package br.ufms.locacao.model.entity;

public class Funcionario extends Usuario {
    private String matricula;

    public Funcionario(String cpf, String nome, String senhaHash, PerfilUsuario perfil, String matricula) {
        super(cpf, nome, senhaHash, perfil);
        this.matricula = matricula;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
}
