package model;

public class Vaga {
    private int id;
    private String numero;
    private String zona;
    private String tipoAceite;
    private String estado;
    private int veiculoId;

    public Vaga() {}

    public Vaga(int id, String numero, String zona, String tipoAceite,
                String estado, int veiculoId) {
        this.id = id;
        this.numero = numero;
        this.zona = zona;
        this.tipoAceite = tipoAceite;
        this.estado = estado;
        this.veiculoId = veiculoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }
    public String getTipoAceite() { return tipoAceite; }
    public void setTipoAceite(String tipoAceite) { this.tipoAceite = tipoAceite; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getVeiculoId() { return veiculoId; }
    public void setVeiculoId(int veiculoId) { this.veiculoId = veiculoId; }
}