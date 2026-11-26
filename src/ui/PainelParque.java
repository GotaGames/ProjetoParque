package ui;

import dao.BilheteDAO;
import dao.VeiculoDAO;
import model.Bilhete;
import model.Vaga;
import model.Veiculo;
import service.ParqueService;
import service.ParqueService.ResultadoEntrada;
import service.ParqueService.ResultadoSaida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PainelParque extends JPanel {

    private final ParqueService parque = new ParqueService();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final BilheteDAO bilheteDAO = new BilheteDAO();

    private final JTextField campoMatricula = new JTextField(15);
    private final JLabel lblLotacao = new JLabel("—");
    private final JLabel lblLivres  = new JLabel("—");
    private final JPanel grade = new JPanel();
    private final JTextArea log = new JTextArea(4, 80);

    public PainelParque() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Topo =====
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        topo.setBorder(BorderFactory.createTitledBorder("Operações"));
        topo.add(new JLabel("Matrícula:"));
        topo.add(campoMatricula);

        JButton btnEntrada  = botaoCor("▶ Entrada", new Color(40, 150, 80));
        JButton btnSaida    = botaoCor("■ Saída", new Color(200, 60, 60));
        JButton btnFila     = new JButton("Ver Fila");
        JButton btnCancelar = botaoCor("Cancelar Fila", new Color(200, 140, 30));
        JButton btnLotacao  = new JButton("Lotação");
        topo.add(btnEntrada);
        topo.add(btnSaida);
        topo.add(btnFila);
        topo.add(btnCancelar);
        topo.add(btnLotacao);

        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        painelInfo.setBorder(BorderFactory.createTitledBorder("Estado em Tempo Real"));
        painelInfo.add(new JLabel("Ocupação:"));
        lblLotacao.setFont(new Font("SansSerif", Font.BOLD, 14));
        painelInfo.add(lblLotacao);
        painelInfo.add(new JLabel("Vagas Livres:"));
        lblLivres.setFont(new Font("SansSerif", Font.BOLD, 14));
        painelInfo.add(lblLivres);

        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.add(topo, BorderLayout.NORTH);
        cabecalho.add(painelInfo, BorderLayout.SOUTH);

        // ===== Grade =====
        grade.setLayout(new GridLayout(0, 5, 8, 8));
        JScrollPane scrollGrade = new JScrollPane(grade);

        // ===== Log =====
        log.setEditable(false);
        log.setBackground(new Color(245, 245, 245));
        log.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollLog = new JScrollPane(log);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Registo de Operações (nesta sessão)"));
        scrollLog.setPreferredSize(new Dimension(900, 90));

        JButton btnLimparLog = new JButton("Limpar Registo");
        btnLimparLog.addActionListener(e -> log.setText(""));
        JPanel painelLog = new JPanel(new BorderLayout());
        painelLog.add(scrollLog, BorderLayout.CENTER);
        JPanel botoesLog = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesLog.add(btnLimparLog);
        painelLog.add(botoesLog, BorderLayout.SOUTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(scrollGrade, BorderLayout.CENTER);
        centro.add(painelLog, BorderLayout.SOUTH);

        add(cabecalho, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        // Timer 1s
        new Timer(1000, e -> atualizarTudo()).start();

        // Ações
        btnEntrada.addActionListener(e -> fazerEntrada());
        btnSaida.addActionListener(e -> fazerSaida());
        btnFila.addActionListener(e -> mostrarFila());
        btnCancelar.addActionListener(e -> cancelarFila());
        btnLotacao.addActionListener(e -> mostrarLotacaoDetalhada());

        atualizarTudo();
    }

    private JButton botaoCor(String texto, Color cor) {
        JButton b = new JButton(texto);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        return b;
    }

    private void atualizarTudo() {
        int total = parque.totalVagas();
        int livres = parque.vagasLivres();
        int ocupadas = total - livres;
        lblLotacao.setText(ocupadas + "/" + total);
        lblLivres.setText(String.valueOf(livres));
        desenharGrade();
    }

    private void desenharGrade() {
        grade.removeAll();
        List<Vaga> vagas = parque.listarVagas();
        List<Bilhete> abertos = bilheteDAO.listarAbertos();

        for (Vaga vg : vagas) {
            Bilhete b = abertos.stream()
                    .filter(x -> x.getVagaId() == vg.getId()).findFirst().orElse(null);

            CartaoVaga card = new CartaoVaga(vg, b, this::atualizarTudo);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e) && b != null) {
                        abrirDetalhe(b, vg);
                    }
                }
            });
            grade.add(card);
        }
        grade.revalidate();
        grade.repaint();
    }

    private void log(String msg) {
        String hora = java.time.LocalTime.now().withNano(0).toString();
        log.append("[" + hora + "] " + msg + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }

    private void fazerEntrada() {
        String mat = campoMatricula.getText().trim().toUpperCase();
        if (mat.isEmpty()) { aviso("Escreve a matrícula."); return; }

        ResultadoEntrada r = parque.entrada(mat);
        switch (r.status) {
            case "OK":
                log("Entrada: " + mat + " → vaga " + r.vagaNumero + " (" + r.codigoBilhete + ")");
                sucesso("Entrada registada!\nBilhete: " + r.codigoBilhete + "\nVaga: " + r.vagaNumero);
                campoMatricula.setText("");
                break;
            case "FILA":
                log("Fila: " + mat + " aguarda vaga");
                info("Parque cheio.\n" + r.mensagem);
                campoMatricula.setText("");
                break;
            case "JA_DENTRO":
                aviso(r.mensagem);
                break;
            default:
                aviso(r.mensagem);
        }
        atualizarTudo();
    }

    private void fazerSaida() {
        String mat = campoMatricula.getText().trim().toUpperCase();
        if (mat.isEmpty()) { aviso("Escreve a matrícula."); return; }

        Veiculo v = veiculoDAO.procurarPorMatricula(mat);
        if (v == null) { aviso("Veículo não encontrado."); return; }
        Bilhete b = bilheteDAO.procurarAberto(v.getId());
        if (b == null) { aviso("Veículo não está dentro do parque."); return; }

        double horas = bilheteDAO.horasDesdeEntrada(b.getId());
        double valor = Math.round(horas * 100.0 * 100.0) / 100.0;

        int op = JOptionPane.showConfirmDialog(this,
                "Fatura a emitir:\n\n" +
                        "Matrícula: " + mat + "\n" +
                        "Bilhete: " + b.getCodigo() + "\n" +
                        "Entrada: " + b.getDataHoraEntrada() + "\n" +
                        "Horas: " + String.format("%.2f", horas) + " h\n" +
                        "Taxa: 100,00 Mts/h\n" +
                        "────────────────\n" +
                        "TOTAL: " + String.format("%.2f", valor) + " Mts\n\n" +
                        "Confirmar pagamento?",
                "Confirmar Pagamento", JOptionPane.OK_CANCEL_OPTION);

        if (op != JOptionPane.OK_OPTION) return;

        ResultadoSaida r = parque.saida(mat);
        if ("OK".equals(r.status)) {
            log("Saída: " + mat + " | " + String.format("%.2f", r.valor) + " Mts | vaga " + r.vagaFaturada + " libertada");
            StringBuilder msg = new StringBuilder("Pagamento confirmado!\nTotal: ")
                    .append(String.format("%.2f", r.valor)).append(" Mts\nVaga ")
                    .append(r.vagaFaturada).append(" libertada.");
            if (r.promovido != null) {
                log("Fila promovida: " + r.promovido + " → vaga " + r.vagaPromovida);
                msg.append("\n\n✔ Fila promovida: ").append(r.promovido)
                        .append(" → vaga ").append(r.vagaPromovida);
            } else {
                msg.append("\n\nSem veículos compatíveis na fila.");
            }
            sucesso(msg.toString());
            campoMatricula.setText("");
        } else {
            aviso(r.mensagem);
        }
        atualizarTudo();
    }

    private void mostrarFila() {
        StringBuilder sb = new StringBuilder("Fila de Espera:\n\n");
        if (parque.getFila().vazia()) sb.append("(vazia)");
        else {
            int pos = 1;
            for (Veiculo v : parque.getFila().listarLista()) {
                sb.append(pos++).append(". ").append(v.getMatricula())
                        .append(" (").append(v.getTipo()).append(")\n");
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Fila", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelarFila() {
        String mat = JOptionPane.showInputDialog(this, "Matrícula do veículo a cancelar da fila:");
        if (mat == null || mat.trim().isEmpty()) return;
        if (parque.cancelarFila(mat.trim().toUpperCase())) {
            log("Cancelamento de fila: " + mat.toUpperCase());
            sucesso("Veículo removido da fila.");
        } else {
            aviso("Veículo não está na fila.");
        }
        atualizarTudo();
    }

    private void abrirDetalhe(Bilhete b, Vaga vg) {
        Veiculo v = new uihelpers.FachadaVeiculos().porId(b.getVeiculoId());
        String nomeDono = new uihelpers.FachadaProprietarios().nomeDe(v.getProprietarioId());
        double valorAtual = parque.valorAtual(b.getId());
        double horas = bilheteDAO.horasDesdeEntrada(b.getId());

        String msg = "Vaga: " + vg.getNumero() + "\n" +
                "Matrícula: " + v.getMatricula() + "\n" +
                "Tipo: " + v.getTipo() + "\n" +
                "Marca: " + v.getMarca() + " " + v.getModelo() + "\n" +
                "Proprietário: " + nomeDono + "\n" +
                "Entrada: " + b.getDataHoraEntrada() + "\n" +
                "Tempo: " + String.format("%.2f", horas) + " h\n" +
                "Valor atual: " + String.format("%.2f", valorAtual) + " Mts";

        JOptionPane.showMessageDialog(this, msg, "Detalhes da Vaga", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarLotacaoDetalhada() {
        int total = parque.totalVagas();
        int livres = parque.vagasLivres();
        int ocupadas = total - livres;
        double percent = total == 0 ? 0 : (ocupadas * 100.0 / total);
        String msg = "Total de vagas: " + total + "\n" +
                "Ocupadas: " + ocupadas + "\n" +
                "Livres: " + livres + "\n" +
                "Ocupação: " + String.format("%.1f", percent) + "%";
        JOptionPane.showMessageDialog(this, msg, "Lotação Detalhada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void aviso(String m)   { JOptionPane.showMessageDialog(this, m, "Aviso", JOptionPane.WARNING_MESSAGE); }
    private void info(String m)    { JOptionPane.showMessageDialog(this, m, "Informação", JOptionPane.INFORMATION_MESSAGE); }
    private void sucesso(String m) { JOptionPane.showMessageDialog(this, m, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
}