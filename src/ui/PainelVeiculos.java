package ui;

import dao.VeiculoDAO;
import model.Veiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class PainelVeiculos extends JPanel {

    private final VeiculoDAO dao = new VeiculoDAO();
    private final DefaultTableModel modelo =
            new DefaultTableModel(new String[]{"ID", "Matrícula", "Marca", "Modelo", "Cor", "Tipo", "Dono ID"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
    private final JTable tabela = new JTable(modelo);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
    private final JTextField txtPesquisa = new JTextField(20);

    public PainelVeiculos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        topo.setBorder(BorderFactory.createTitledBorder("Pesquisar e Gerir Veículos"));
        topo.add(new JLabel("Pesquisar (matrícula/marca):"));
        topo.add(txtPesquisa);
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnEliminar  = new JButton("Eliminar Selecionado");
        topo.add(btnAtualizar);
        topo.add(btnEliminar);

        tabela.setRowSorter(sorter);
        add(topo, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        carregar();

        btnAtualizar.addActionListener(e -> carregar());
        btnEliminar.addActionListener(e -> eliminar());
        txtPesquisa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
    }

    private void filtrar() {
        String t = txtPesquisa.getText().trim();
        if (t.isEmpty()) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + t, 1, 2));
    }

    private void eliminar() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleciona um veículo.",
                    "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        int id = (int) modelo.getValueAt(tabela.convertRowIndexToModel(row), 0);
        int op = JOptionPane.showConfirmDialog(this,
                "Eliminar o veículo com id " + id + "?\nEsta ação não pode ser desfeita.",
                "Confirmar eliminação", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;
        if (dao.eliminar(id)) {
            JOptionPane.showMessageDialog(this, "Veículo eliminado.");
            carregar();
        } else {
            JOptionPane.showMessageDialog(this, "Não é possível eliminar (tem bilhetes).",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregar() {
        modelo.setRowCount(0);
        for (Veiculo v : dao.listar()) {
            modelo.addRow(new Object[]{v.getId(), v.getMatricula(), v.getMarca(),
                    v.getModelo(), v.getCor(), v.getTipo(), v.getProprietarioId()});
        }
    }
}