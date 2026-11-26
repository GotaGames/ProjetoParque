package ui;

import dao.ProprietarioDAO;
import dao.VeiculoDAO;
import model.Proprietario;
import model.Veiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class PainelClientes extends JPanel {

    private final ProprietarioDAO propDAO = new ProprietarioDAO();
    private final VeiculoDAO veicDAO = new VeiculoDAO();

    private final JTextField txtNome = new JTextField(15);
    private final JTextField txtBi = new JTextField(10);
    private final JTextField txtTelefone = new JTextField(10);
    private final JTextField txtEmail = new JTextField(15);

    private final JTextField txtMatricula = new JTextField(10);
    private final JTextField txtMarca = new JTextField(10);
    private final JTextField txtModelo = new JTextField(10);
    private final JTextField txtCor = new JTextField(8);
    private final JComboBox<String> cbTipo =
            new JComboBox<>(new String[]{"MOTO", "LIGEIRO", "PESADO"});
    private final JComboBox<Proprietario> cbDono = new JComboBox<>();

    private final DefaultTableModel modeloClientes =
            new DefaultTableModel(new String[]{"ID", "Nome", "BI", "Telefone", "Email"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
    private final JTable tabelaClientes = new JTable(modeloClientes);
    private final TableRowSorter<DefaultTableModel> sorterClientes = new TableRowSorter<>(modeloClientes);
    private final JTextField txtPesquisa = new JTextField(20);

    public PainelClientes() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Formulário cliente
        JPanel formCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        formCliente.setBorder(BorderFactory.createTitledBorder("Novo Cliente"));
        formCliente.add(new JLabel("Nome:")); formCliente.add(txtNome);
        formCliente.add(new JLabel("BI:"));   formCliente.add(txtBi);
        formCliente.add(new JLabel("Tel:"));  formCliente.add(txtTelefone);
        formCliente.add(new JLabel("Email:")); formCliente.add(txtEmail);
        JButton btnAddCliente = botaoDestaque("+ Adicionar Cliente", new Color(30, 120, 200));
        JButton btnDelCliente = new JButton("Eliminar Selecionado");
        formCliente.add(btnAddCliente);
        formCliente.add(btnDelCliente);

        // 2. Pesquisa + tabela
        JPanel painelPesq = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        painelPesq.setBorder(BorderFactory.createTitledBorder("Clientes Registados"));
        painelPesq.add(new JLabel("Pesquisar (nome/BI):"));
        painelPesq.add(txtPesquisa);

        tabelaClientes.setRowSorter(sorterClientes);
        JScrollPane scrollTabela = new JScrollPane(tabelaClientes);
        scrollTabela.setPreferredSize(new Dimension(900, 220));

        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.add(painelPesq, BorderLayout.NORTH);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        // 3. Formulário veículo
        JPanel formVeiculo = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        formVeiculo.setBorder(BorderFactory.createTitledBorder("Novo Veículo"));
        formVeiculo.add(new JLabel("Matrícula:")); formVeiculo.add(txtMatricula);
        formVeiculo.add(new JLabel("Marca:"));     formVeiculo.add(txtMarca);
        formVeiculo.add(new JLabel("Modelo:"));    formVeiculo.add(txtModelo);
        formVeiculo.add(new JLabel("Cor:"));       formVeiculo.add(txtCor);
        formVeiculo.add(new JLabel("Tipo:"));      formVeiculo.add(cbTipo);
        formVeiculo.add(new JLabel("Dono:"));
        cbDono.setPreferredSize(new Dimension(180, 25));
        formVeiculo.add(cbDono);
        JButton btnAddVeic = botaoDestaque("+ Adicionar Veículo", new Color(40, 160, 80));
        formVeiculo.add(btnAddVeic);

        add(formCliente);
        add(Box.createVerticalStrut(5));
        add(painelTabela);
        add(Box.createVerticalStrut(5));
        add(formVeiculo);

        carregarClientes();

        // Ações
        btnAddCliente.addActionListener(e -> adicionarCliente());
        btnDelCliente.addActionListener(e -> eliminarCliente());
        btnAddVeic.addActionListener(e -> adicionarVeiculo());
        txtPesquisa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
    }

    private void filtrar() {
        String t = txtPesquisa.getText().trim();
        if (t.isEmpty()) sorterClientes.setRowFilter(null);
        else sorterClientes.setRowFilter(RowFilter.regexFilter("(?i)" + t, 1, 2));
    }

    private JButton botaoDestaque(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(180, 32));
        return b;
    }

    // Validação de matrícula: AAA-000-AA
    private boolean matriculaValida(String m) {
        return m.matches("[A-Z]{3}-\\d{3}-[A-Z]{2}");
    }

    private void adicionarCliente() {
        if (txtNome.getText().trim().isEmpty() || txtBi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e BI são obrigatórios.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Proprietario p = new Proprietario(0, txtNome.getText().trim(), txtBi.getText().trim(),
                txtTelefone.getText().trim(), txtEmail.getText().trim());
        if (propDAO.inserir(p) > 0) {
            JOptionPane.showMessageDialog(this, "Cliente adicionado com sucesso.");
            txtNome.setText(""); txtBi.setText(""); txtTelefone.setText(""); txtEmail.setText("");
            carregarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Erro — BI duplicado?",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        int row = tabelaClientes.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleciona um cliente.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloClientes.getValueAt(tabelaClientes.convertRowIndexToModel(row), 0);
        int op = JOptionPane.showConfirmDialog(this,
                "Eliminar o cliente com id " + id + "?\nEsta ação não pode ser desfeita.",
                "Confirmar eliminação", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;
        if (propDAO.eliminar(id)) {
            JOptionPane.showMessageDialog(this, "Cliente eliminado.");
            carregarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Não é possível eliminar (tem veículos).",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarVeiculo() {
        String mat = txtMatricula.getText().trim().toUpperCase();
        if (mat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Matrícula é obrigatória.",
                    "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        if (!matriculaValida(mat)) {
            JOptionPane.showMessageDialog(this,
                    "Formato inválido.\nUsa: AAA-000-AA (3 letras, 3 números, 2 letras).",
                    "Matrícula inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Proprietario dono = (Proprietario) cbDono.getSelectedItem();
        if (dono == null) {
            JOptionPane.showMessageDialog(this, "Seleciona um dono.",
                    "Aviso", JOptionPane.WARNING_MESSAGE); return;
        }
        Veiculo v = new Veiculo(0, mat, txtMarca.getText().trim(), txtModelo.getText().trim(),
                txtCor.getText().trim(), cbTipo.getSelectedItem().toString(), dono.getId());
        if (veicDAO.inserir(v) > 0) {
            JOptionPane.showMessageDialog(this, "Veículo adicionado com sucesso.");
            txtMatricula.setText(""); txtMarca.setText(""); txtModelo.setText(""); txtCor.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Erro — matrícula duplicada?",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarClientes() {
        modeloClientes.setRowCount(0);
        cbDono.removeAllItems();
        for (Proprietario p : propDAO.listar()) {
            modeloClientes.addRow(new Object[]{p.getId(), p.getNome(), p.getBi(),
                    p.getTelefone(), p.getEmail()});
            cbDono.addItem(p);
        }
    }
}