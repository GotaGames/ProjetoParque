package dao;

import util.Conexao;
import model.Fatura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FaturaDAO {

    public void inserir(int bilheteId, double horas, double total) {
        String sql = "INSERT INTO Fatura (bilhete_id, numero, horas, preco_hora, total) " +
                "VALUES (?, CONCAT('FAT-', DATE_FORMAT(NOW(),'%Y%m%d'), '-', " +
                "LPAD(?, 6, '0')), ?, 100.00, ?)";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bilheteId);
            ps.setInt(2, bilheteId);
            ps.setDouble(3, horas);
            ps.setDouble(4, total);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<Fatura> listar() {
        List<Fatura> lista = new ArrayList<>();
        String sql = "SELECT * FROM Fatura ORDER BY emitida_em DESC";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public double totalHoje() {
        String sql = "SELECT COALESCE(SUM(total),0) AS t FROM Fatura WHERE DATE(emitida_em)=CURDATE()";
        return escalar(sql);
    }

    public double totalGeral() {
        return escalar("SELECT COALESCE(SUM(total),0) AS t FROM Fatura");
    }

    public int numeroFaturas() {
        String sql = "SELECT COUNT(*) AS t FROM Fatura";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("t");
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private double escalar(String sql) {
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("t");
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    private Fatura map(ResultSet rs) throws SQLException {
        Fatura f = new Fatura(rs.getInt("bilhete_id"), rs.getString("numero"),
                rs.getDouble("horas"), rs.getDouble("preco_hora"),
                rs.getDouble("total"), rs.getString("emitida_em"));
        f.setId(rs.getInt("id"));
        return f;
    }
}