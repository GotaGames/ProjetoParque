package uihelpers;

import dao.ProprietarioDAO;
import model.Proprietario;

public class FachadaProprietarios {
    private final ProprietarioDAO dao = new ProprietarioDAO();
    public String nomeDe(int id) {
        for (Proprietario p : dao.listar()) if (p.getId() == id) return p.getNome();
        return "?";
    }
}