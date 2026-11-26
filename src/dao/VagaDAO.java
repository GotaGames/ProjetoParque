package dao;

import util.Conexao;
import model.Vaga;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VagaDAO {

    public Vaga procurarLivre(String tipoVeiculo) {
        String sql = "SELECT * FROM Vaga WHERE estado='LIVRE' " +
                "AND (tipo_aceite=? OR tipo_aceite='TODOS') ORDER BY numero LIMIT 1";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tipoVeiculo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Vaga(rs.getInt("id"), rs.getString("numero"),
                        rs.getString("zona"), rs.getString("tipo_aceite"),
                        rs.getString("estado"), rs.getInt("veiculo_id"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Vaga> listar() {
        List<Vaga> lista = new ArrayList<>();
        String sql = "SELECT * FROM Vaga ORDER BY numero";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Vaga(rs.getInt("id"), rs.getString("numero"),
                        rs.getString("zona"), rs.getString("tipo_aceite"),
                        rs.getString("estado"), rs.getInt("veiculo_id")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    public int vagasLivres() {
        String sql = "SELECT COUNT(*) AS total FROM Vaga WHERE estado='LIVRE'";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int totalVagas() {
        String sql = "SELECT COUNT(*) AS total FROM Vaga";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }


    public void marcarManutencao(int vagaId) {
        String sql = "UPDATE Vaga SET estado='MANUTENCAO', veiculo_id=NULL WHERE id=?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vagaId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void ativar(int vagaId) {
        String sql = "UPDATE Vaga SET estado='LIVRE', veiculo_id=NULL WHERE id=?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, vagaId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}