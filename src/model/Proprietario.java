package model;

public class Proprietario {
    private int id;
    private String nome;
    private String bi;
    private String telefone;
    private String email;

    public Proprietario() {}

    public Proprietario(int id, String nome, String bi, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.bi = bi;
        this.telefone = telefone;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getBi() { return bi; }
    public void setBi(String bi) { this.bi = bi; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return nome + " (" + bi + ")";
    }
}