package dao;

import util.Conexao;
import model.Bilhete;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BilheteDAO {

    public int inserir(int veiculoId, int vagaId) {
        String sql = "INSERT INTO Bilhete (veiculo_id, vaga_id, data_hora_entrada, estado) " +
                "VALUES (?,?,NOW(),'ABERTO')";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, veiculoId);
            ps.setInt(2, vagaId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public Bilhete procurarAberto(int veiculoId) {
        String sql = "SELECT * FROM Bilhete WHERE veiculo_id=? AND estado='ABERTO'";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, veiculoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public Bilhete procurarPorId(int id) {
        String sql = "SELECT * FROM Bilhete WHERE id=?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Bilhete> listarAbertos() {
        List<Bilhete> lista = new ArrayList<>();
        String sql = "SELECT * FROM Bilhete WHERE estado='ABERTO'";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public void fechar(int bilheteId, double valor) {
        String sql = "UPDATE Bilhete SET estado='FECHADO', data_hora_saida=NOW(), valor_pago=? WHERE id=?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, valor);
            ps.setInt(2, bilheteId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Horas exatas: segundos totais convertidos. Nunca arredonda mal.
    public double horasDesdeEntrada(int bilheteId) {
        String sql = "SELECT TIMESTAMPDIFF(SECOND, data_hora_entrada, NOW()) / 3600.0 AS h " +
                "FROM Bilhete WHERE id=?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, bilheteId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double h = rs.getDouble("h");
                return h < 0.01 ? 0.01 : h;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0.01;
    }

    private Bilhete map(ResultSet rs) throws SQLException {
        Bilhete b = new Bilhete();
        b.setId(rs.getInt("id"));
        b.setCodigo(rs.getString("codigo"));
        b.setVeiculoId(rs.getInt("veiculo_id"));
        b.setVagaId(rs.getInt("vaga_id"));
        b.setDataHoraEntrada(rs.getString("data_hora_entrada"));
        b.setEstado(rs.getString("estado"));
        return b;
    }
}