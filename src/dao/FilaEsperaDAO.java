package dao;

import util.Conexao;
import model.Veiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilaEsperaDAO {

    /** Insere no fim da fila (posicao = max+1). */
    public void enfileirar(int veiculoId) {
        int proximaPosicao = 1;
        // 1) Ler a próxima posição
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT COALESCE(MAX(posicao),0)+1 AS prox FROM FilaEspera");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) proximaPosicao = rs.getInt("prox");
        } catch (Exception e) { e.printStackTrace(); return; }

        // 2) Inserir com o valor já calculado
        String sql = "INSERT INTO FilaEspera (veiculo_id, posicao, chegou_em) VALUES (?, ?, NOW())";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, veiculoId);
            ps.setInt(2, proximaPosicao);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }


    /** Remove um veículo da fila pelo id e reorganiza posições. */
    public void remover(int veiculoId) {
        String sql = "DELETE FROM FilaEspera WHERE veiculo_id = ?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, veiculoId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        reorganizarPosicoes();
    }

    /** Reordena posicao = 1,2,3,... pela ordem de chegada. */
    private void reorganizarPosicoes() {
        String sel = "SELECT id FROM FilaEspera ORDER BY chegou_em, id";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sel);
             ResultSet rs = ps.executeQuery()) {
            int pos = 1;
            List<Integer> ids = new ArrayList<>();
            while (rs.next()) ids.add(rs.getInt("id"));
            for (int id : ids) {
                try (PreparedStatement upd = c.prepareStatement(
                        "UPDATE FilaEspera SET posicao=? WHERE id=?")) {
                    upd.setInt(1, pos++);
                    upd.setInt(2, id);
                    upd.executeUpdate();
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    /** Lista a fila inteira ordenada por posição. */
    public List<Veiculo> listar() {
        List<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT v.* FROM FilaEspera f " +
                "JOIN Veiculo v ON v.id = f.veiculo_id ORDER BY f.posicao";
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

    public boolean estaNaFila(int veiculoId) {
        String sql = "SELECT 1 FROM FilaEspera WHERE veiculo_id = ?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, veiculoId);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}