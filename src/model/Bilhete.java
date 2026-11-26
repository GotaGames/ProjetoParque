package model;

public class Bilhete {
    private int id;
    private String codigo;
    private int veiculoId;
    private int vagaId;
    private String dataHoraEntrada;
    private String dataHoraSaida;
    private double valorPago;
    private String estado;

    public Bilhete() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public int getVeiculoId() { return veiculoId; }
    public void setVeiculoId(int veiculoId) { this.veiculoId = veiculoId; }
    public int getVagaId() { return vagaId; }
    public void setVagaId(int vagaId) { this.vagaId = vagaId; }
    public String getDataHoraEntrada() { return dataHoraEntrada; }
    public void setDataHoraEntrada(String d) { this.dataHoraEntrada = d; }
    public String getDataHoraSaida() { return dataHoraSaida; }
    public void setDataHoraSaida(String d) { this.dataHoraSaida = d; }
    public double getValorPago() { return valorPago; }
    public void setValorPago(double v) { this.valorPago = v; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}