package dao;

import util.Conexao;
import model.Operacao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperacaoDAO {
    public List<Operacao> ultimas(int limite) {
        List<Operacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM Operacao ORDER BY ocorrido_em DESC, id DESC LIMIT ?";
        try (Connection c = Conexao.abrir();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Operacao(rs.getInt("id"), rs.getString("tipo"),
                        rs.getString("descricao"), rs.getString("matricula"),
                        rs.getString("ocorrido_em")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }
}