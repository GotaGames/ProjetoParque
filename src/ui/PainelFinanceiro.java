package ui;

import dao.FaturaDAO;

import javax.swing.*;
import java.awt.*;

public class PainelFinanceiro extends JPanel {

    private final FaturaDAO dao = new FaturaDAO();
    private final JLabel lblHoje = new JLabel("—");
    private final JLabel lblGeral = new JLabel("—");
    private final JLabel lblNum = new JLabel("—");

    public PainelFinanceiro() {
        setLayout(new GridLayout(3, 2, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font grande = new Font("SansSerif", Font.BOLD, 22);
        Font rotulo = new Font("SansSerif", Font.PLAIN, 16);

        lblHoje.setFont(grande); lblGeral.setFont(grande); lblNum.setFont(grande);

        add(new JLabel("Receita de hoje:"));     add(lblHoje);
        add(new JLabel("Receita total:"));       add(lblGeral);
        add(new JLabel("Faturas emitidas:"));    add(lblNum);

        // Atualiza a cada 2s
        Timer t = new Timer(2000, e -> atualizar());
        t.start();
        atualizar();
    }

    private void atualizar() {
        lblHoje.setText(String.format("%.2f Mts", dao.totalHoje()));
        lblGeral.setText(String.format("%.2f Mts", dao.totalGeral()));
        lblNum.setText(String.valueOf(dao.numeroFaturas()));
    }
}