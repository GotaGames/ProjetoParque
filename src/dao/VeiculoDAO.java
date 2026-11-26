package dao;

import util.Conexao;
import model.Veiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public int inserir(Veiculo v) {
        String sql = "INSERT INTO Veiculo (matricula, marca, modelo, cor, tipo, proprietario_id) " +
                "VALUES (?,?,?,?,?,?)";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getMatricula());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getCor());
            ps.setString(5, v.getTipo());
            ps.setInt(6, v.getProprietarioId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public Veiculo procurarPorMatricula(String matricula) {
        String sql = "SELECT * FROM Veiculo WHERE matricula=?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Veiculo(rs.getInt("id"), rs.getString("matricula"),
                        rs.getString("marca"), rs.getString("modelo"),
                        rs.getString("cor"), rs.getString("tipo"),
                        rs.getInt("proprietario_id"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Veiculo> listar() {
        List<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Veiculo ORDER BY matricula";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Veiculo(rs.getInt("id"), rs.getString("matricula"),
                        rs.getString("marca"), rs.getString("modelo"),
                        rs.getString("cor"), rs.getString("tipo"),
                        rs.getInt("proprietario_id")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Veiculo WHERE id=?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Não é possível eliminar: " + e.getMessage());
            return false;
        }
    }
}