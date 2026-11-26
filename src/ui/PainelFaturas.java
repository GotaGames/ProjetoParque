package ui;

import dao.FaturaDAO;
import model.Fatura;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PainelFaturas extends JPanel {

    private FaturaDAO dao = new FaturaDAO();
    private DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID","Nº Fatura","Bilhete ID","Horas","Preço/Hora","Total","Emitida em"}, 0);
    private JTable tabela = new JTable(modelo);

    public PainelFaturas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAtualizar = new JButton("Atualizar");
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(btnAtualizar);

        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        carregar();
        btnAtualizar.addActionListener(e -> carregar());
    }

    private void carregar() {
        modelo.setRowCount(0);
        for (Fatura f : dao.listar()) {
            modelo.addRow(new Object[]{f.getId(), f.getNumero(), f.getBilheteId(),
                    String.format("%.2f", f.getHoras()),
                    String.format("%.2f", f.getPrecoHora()),
                    String.format("%.2f", f.getTotal()),
                    f.getEmitidaEm()});
        }
    }
}