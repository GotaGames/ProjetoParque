package service;

import dao.FilaEsperaDAO;
import model.Veiculo;
import java.util.ArrayList;
import java.util.List;

public class FilaEspera {
    private final FilaEsperaDAO dao = new FilaEsperaDAO();
    private final List<Veiculo> fila = new ArrayList<>();

    public FilaEspera() {
        fila.addAll(dao.listar());
    }

    public void enfileirar(Veiculo v) {
        if (dao.estaNaFila(v.getId())) return;
        dao.enfileirar(v.getId());
        fila.add(v);
    }

    public Veiculo removerCompativel(String tipoVaga) {
        for (int i = 0; i < fila.size(); i++) {
            Veiculo v = fila.get(i);
            boolean ok = tipoVaga.equals("TODOS") || v.getTipo().equals(tipoVaga);
            if (ok) {
                dao.remover(v.getId());
                fila.remove(i);
                return v;
            }
        }
        return null;
    }

    /** Cancela a espera de um veículo pela matrícula. Devolve true se removeu. */
    public boolean cancelar(String matricula) {
        for (int i = 0; i < fila.size(); i++) {
            if (fila.get(i).getMatricula().equalsIgnoreCase(matricula)) {
                dao.remover(fila.get(i).getId());
                fila.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean vazia() { return fila.isEmpty(); }
    public int tamanho()   { return fila.size(); }
    public List<Veiculo> listarLista() { return new ArrayList<>(fila); }

    public void listar() {
        if (fila.isEmpty()) { System.out.println("Fila vazia."); return; }
        for (int i = 0; i < fila.size(); i++)
            System.out.println((i + 1) + ". " + fila.get(i).getMatricula());
    }
}