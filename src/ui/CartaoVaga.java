package ui;

import dao.VagaDAO;
import model.Bilhete;
import model.Vaga;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CartaoVaga extends JPanel {

    private final Vaga vaga;
    private final Bilhete bilhete;
    private final Runnable onUpdate;

    public CartaoVaga(Vaga vaga, Bilhete bilhete, Runnable onUpdate) {
        this.vaga = vaga;
        this.bilhete = bilhete;
        this.onUpdate = onUpdate;
        setPreferredSize(new Dimension(160, 110));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        atualizarEstilo();

        // Clique direito: alternar manutenção
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    alternarManutencao();
                }
            }
        });
    }

    private void atualizarEstilo() {
        if ("MANUTENCAO".equals(vaga.getEstado())) {
            setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 2));
            setBackground(new Color(230, 230, 230));
            setToolTipText("Vaga em manutenção — clique direito para ativar");
        } else if (bilhete != null) {
            setBorder(BorderFactory.createLineBorder(new Color(200, 60, 60), 2));
            setBackground(new Color(255, 235, 235));
            setToolTipText("Clique para ver detalhes");
        } else {
            setBorder(BorderFactory.createLineBorder(new Color(60, 160, 90), 2));
            setBackground(new Color(235, 255, 240));
            setToolTipText("Vaga livre");
        }
    }

    private boolean ocupada() { return bilhete != null; }

    private void alternarManutencao() {
        VagaDAO dao = new VagaDAO();
        if ("MANUTENCAO".equals(vaga.getEstado())) {
            dao.ativar(vaga.getId());
            vaga.setEstado("LIVRE");
        } else if (!ocupada()) {
            dao.marcarManutencao(vaga.getId());
            vaga.setEstado("MANUTENCAO");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Não podes pôr em manutenção uma vaga ocupada.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        atualizarEstilo();
        repaint();
        if (onUpdate != null) onUpdate.run();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString(vaga.getNumero(), 10, 20);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.drawString(vaga.getTipoAceite(), 10, 36);

        if ("MANUTENCAO".equals(vaga.getEstado())) {
            g2.setColor(new Color(90, 90, 90));
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("MANUTENÇÃO", 40, 70);
        } else if (ocupada()) {
            desenharVeiculo(g2, 65, 25);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.setColor(new Color(150, 30, 30));
            String mat = new uihelpers.FachadaVeiculos()
                    .porId(bilhete.getVeiculoId()).getMatricula();
            g2.drawString(mat, 10, 100);
        } else {
            g2.setColor(new Color(100, 160, 120));
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("LIVRE", 60, 65);
        }
    }

    private void desenharVeiculo(Graphics2D g2, int x, int y) {
        g2.setColor(new Color(60, 90, 180));
        g2.fillRoundRect(x, y, 60, 24, 8, 8);
        g2.fillRoundRect(x + 12, y - 8, 36, 14, 6, 6);
        g2.setColor(Color.BLACK);
        g2.fillOval(x + 8, y + 20, 14, 14);
        g2.fillOval(x + 40, y + 20, 14, 14);
        g2.setColor(new Color(180, 220, 255));
        g2.fillRect(x + 16, y - 6, 12, 10);
        g2.fillRect(x + 32, y - 6, 12, 10);
    }
}