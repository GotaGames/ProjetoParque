package dao;

import util.Conexao;
import model.Proprietario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProprietarioDAO {

    public int inserir(Proprietario p) {
        String sql = "INSERT INTO Proprietario (nome, bi, telefone, email) VALUES (?,?,?,?)";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getBi());
            ps.setString(3, p.getTelefone());
            ps.setString(4, p.getEmail());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    public List<Proprietario> listar() {
        List<Proprietario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Proprietario ORDER BY nome";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Proprietario(rs.getInt("id"), rs.getString("nome"),
                        rs.getString("bi"), rs.getString("telefone"), rs.getString("email")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Proprietario WHERE id=?";
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