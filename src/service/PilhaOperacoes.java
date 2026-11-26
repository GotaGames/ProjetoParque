package service;

import model.Operacao;

public class PilhaOperacoes {
    private Operacao[] pilha = new Operacao[50];
    private int topo = -1;

    public void empilhar(Operacao op) {
        if (topo < pilha.length - 1) {
            pilha[++topo] = op;
        } else {
            for (int i = 1; i < pilha.length; i++) pilha[i - 1] = pilha[i];
            pilha[pilha.length - 1] = op;
        }
    }

    public Operacao topo() { return topo >= 0 ? pilha[topo] : null; }

    public void mostrar() {
        if (topo < 0) { System.out.println("Pilha vazia."); return; }
        System.out.println("--- Últimas operações (mais recente no topo) ---");
        for (int i = topo; i >= 0; i--) {
            Operacao o = pilha[i];
            System.out.println("[" + o.getOcorridoEm() + "] " + o.getTipo()
                    + " - " + o.getDescricao() + " (" + o.getMatricula() + ")");
        }
    }
}