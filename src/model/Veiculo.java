package model;

public class Veiculo {
    private int id;
    private String matricula;
    private String marca;
    private String modelo;
    private String cor;
    private String tipo;
    private int proprietarioId;

    public Veiculo() {}

    public Veiculo(int id, String matricula, String marca, String modelo,
                   String cor, String tipo, int proprietarioId) {
        this.id = id;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.tipo = tipo;
        this.proprietarioId = proprietarioId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getProprietarioId() { return proprietarioId; }
    public void setProprietarioId(int proprietarioId) { this.proprietarioId = proprietarioId; }

    @Override
    public String toString() {
        return matricula + " (" + tipo + ")";
    }
}