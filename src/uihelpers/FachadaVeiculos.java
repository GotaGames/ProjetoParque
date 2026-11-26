package uihelpers;

import dao.VeiculoDAO;
import model.Veiculo;
import java.util.List;

public class FachadaVeiculos {
    private final VeiculoDAO dao = new VeiculoDAO();
    public Veiculo porId(int id) {
        for (Veiculo v : dao.listar()) if (v.getId() == id) return v;
        return null;
    }
}