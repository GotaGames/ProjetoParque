package model;

public class Fatura {
    private int id;
    private int bilheteId;
    private String numero;
    private double horas;
    private double precoHora;
    private double total;
    private String emitidaEm;

    public Fatura() {}
    public Fatura(int bilheteId, String numero, double horas,
                  double precoHora, double total, String emitidaEm) {
        this.bilheteId = bilheteId;
        this.numero = numero;
        this.horas = horas;
        this.precoHora = precoHora;
        this.total = total;
        this.emitidaEm = emitidaEm;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBilheteId() { return bilheteId; }
    public void setBilheteId(int v) { this.bilheteId = v; }
    public String getNumero() { return numero; }
    public void setNumero(String v) { this.numero = v; }
    public double getHoras() { return horas; }
    public void setHoras(double v) { this.horas = v; }
    public double getPrecoHora() { return precoHora; }
    public void setPrecoHora(double v) { this.precoHora = v; }
    public double getTotal() { return total; }
    public void setTotal(double v) { this.total = v; }
    public String getEmitidaEm() { return emitidaEm; }
    public void setEmitidaEm(String v) { this.emitidaEm = v; }
}