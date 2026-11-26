package service;

import dao.*;
import model.*;
import java.util.List;

public class ParqueService {
    private VeiculoDAO veiculoDAO = new VeiculoDAO();
    private VagaDAO vagaDAO = new VagaDAO();
    private BilheteDAO bilheteDAO = new BilheteDAO();
    private OperacaoDAO operacaoDAO = new OperacaoDAO();
    private FaturaDAO faturaDAO = new FaturaDAO();

    private FilaEspera fila = new FilaEspera();
    private PilhaOperacoes pilha = new PilhaOperacoes();

    public static class ResultadoEntrada {
        public String status;   // "OK", "FILA", "ERRO", "JA_DENTRO"
        public String mensagem;
        public String codigoBilhete;
        public String vagaNumero;
    }

    public ResultadoEntrada entrada(String matricula) {
        ResultadoEntrada r = new ResultadoEntrada();
        Veiculo v = veiculoDAO.procurarPorMatricula(matricula);
        if (v == null) { r.status = "ERRO"; r.mensagem = "Veículo não encontrado."; return r; }

        if (bilheteDAO.procurarAberto(v.getId()) != null) {
            r.status = "JA_DENTRO";
            r.mensagem = "Veículo já está dentro do parque.";
            return r;
        }

        Vaga vaga = vagaDAO.procurarLivre(v.getTipo());
        if (vaga == null) {
            fila.enfileirar(v);
            r.status = "FILA";
            r.mensagem = "Parque cheio. " + matricula +
                    " entrou na fila (posição " + fila.tamanho() + ").";
            return r;
        }

        // Se estava na fila (raro, mas possível se houve promoção), sai da fila
        fila.removerCompativel(v.getTipo());

        int id = bilheteDAO.inserir(v.getId(), vaga.getId());
        Bilhete b = bilheteDAO.procurarPorId(id);
        r.status = "OK";
        r.codigoBilhete = (b != null) ? b.getCodigo() : ("ID " + id);
        r.vagaNumero = vaga.getNumero();
        r.mensagem = "Entrada OK. Bilhete: " + r.codigoBilhete +
                " | Vaga: " + r.vagaNumero;
        pilha.empilhar(new Operacao(0, "ENTRADA",
                "Entrada na vaga " + vaga.getNumero(), matricula, "agora"));
        return r;
    }


    public static class ResultadoSaida {
        public String status;   // "OK", "ERRO"
        public String mensagem;
        public double horas;
        public double valor;
        public String matriculaFaturada;
        public String vagaFaturada;
        public String promovido;   // matrícula promovida da fila, ou null
        public String vagaPromovida;
    }

    public ResultadoSaida saida(String matricula) {
        ResultadoSaida r = new ResultadoSaida();
        Veiculo v = veiculoDAO.procurarPorMatricula(matricula);
        if (v == null) { r.status = "ERRO"; r.mensagem = "Veículo não encontrado."; return r; }

        Bilhete b = bilheteDAO.procurarAberto(v.getId());
        if (b == null) { r.status = "ERRO"; r.mensagem = "Veículo não está dentro do parque."; return r; }

        // Guardar a vaga que vai ser libertada e o seu tipo (antes de fechar)
        int vagaLibertadaId = b.getVagaId();
        String tipoVagaLibertada = "TODOS";
        for (Vaga vg : vagaDAO.listar()) {
            if (vg.getId() == vagaLibertadaId) {
                tipoVagaLibertada = vg.getTipoAceite();
                break;
            }
        }

        // Fechar bilhete + emitir fatura
        double horas = bilheteDAO.horasDesdeEntrada(b.getId());
        double valor = Math.round(horas * 100.0 * 100.0) / 100.0;
        bilheteDAO.fechar(b.getId(), valor);
        faturaDAO.inserir(b.getId(), horas, valor);

        r.status = "OK";
        r.horas = horas;
        r.valor = valor;
        r.matriculaFaturada = matricula;
        r.vagaFaturada = vagaNumero(vagaLibertadaId);
        r.mensagem = "Saída registada. Total a pagar: " + String.format("%.2f", valor) + " Mts";

        pilha.empilhar(new Operacao(0, "SAIDA",
                "Saída. Valor pago: " + String.format("%.2f", valor), matricula, "agora"));

        // ===== Promoção: procura na fila o primeiro veículo COMPATÍVEL =====
        Veiculo proximo = fila.removerCompativel(tipoVagaLibertada);

        if (proximo != null) {
            // Ocupa a vaga específica que acabou de ser libertada
            bilheteDAO.inserir(proximo.getId(), vagaLibertadaId);
            r.promovido = proximo.getMatricula();
            r.vagaPromovida = r.vagaFaturada;

            pilha.empilhar(new Operacao(0, "FILA_SAIDA",
                    "Promovido da fila para vaga " + r.vagaPromovida,
                    proximo.getMatricula(), "agora"));
        }
        // Se proximo == null → ninguém compatível na fila, vaga fica LIVRE

        return r;
    }


    public double valorAtual(int bilheteId) {
        double h = bilheteDAO.horasDesdeEntrada(bilheteId);
        return Math.round(h * 100.0 * 100.0) / 100.0;
    }

    public String vagaNumero(int vagaId) {
        for (Vaga vg : vagaDAO.listar()) if (vg.getId() == vagaId) return vg.getNumero();
        return "?";
    }

    public List<Operacao> historicoBD(int n) { return operacaoDAO.ultimas(n); }
    public int vagasLivres() { return vagaDAO.vagasLivres(); }
    public int totalVagas()  { return vagaDAO.totalVagas(); }
    public List<Vaga> listarVagas() { return vagaDAO.listar(); }
    public FilaEspera getFila() { return fila; }
    public PilhaOperacoes getPilha() { return pilha; }


    public boolean cancelarFila(String matricula) {
        boolean ok = fila.cancelar(matricula);
        if (ok) pilha.empilhar(new Operacao(0, "FILA_SAIDA",
                "Cliente cancelou a espera na fila", matricula, "agora"));
        return ok;
    }
}