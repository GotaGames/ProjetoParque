package ui;

import javax.swing.*;

public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal() {
        setTitle("Sistema de Gestão — Parque de Estacionamento");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Parque", new PainelParque());
        abas.addTab("Clientes", new PainelClientes());
        abas.addTab("Veículos", new PainelVeiculos());
        abas.addTab("Faturas", new PainelFaturas());
        abas.addTab("Financeiro", new PainelFinanceiro());

        add(abas);
    }
}