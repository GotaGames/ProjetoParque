package model;

public class Operacao {
    private int id;
    private String tipo;
    private String descricao;
    private String matricula;
    private String ocorridoEm;

    public Operacao() {}

    public Operacao(int id, String tipo, String descricao,
                    String matricula, String ocorridoEm) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.matricula = matricula;
        this.ocorridoEm = ocorridoEm;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getOcorridoEm() { return ocorridoEm; }
    public void setOcorridoEm(String ocorridoEm) { this.ocorridoEm = ocorridoEm; }
}