package uihelpers;

import dao.BilheteDAO;
import model.Bilhete;
import java.util.List;

public class FachadaBilhetes {
    private final BilheteDAO dao = new BilheteDAO();
    public List<Bilhete> listarAbertos() { return dao.listarAbertos(); }
    public Bilhete procurarAberto(int veiculoId) { return dao.procurarAberto(veiculoId); }
}