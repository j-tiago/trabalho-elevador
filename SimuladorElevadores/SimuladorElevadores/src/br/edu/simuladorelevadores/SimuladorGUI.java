package br.edu.simuladorelevadores;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimuladorGUI extends JFrame {
    private Simulador simulador;
    private int numAndares;
    private int numElevadores;
    private JLabel statusLabel;
    private JTextArea logArea;
    private List<List<JLabel>> elevadorLabels;
    private List<JLabel> andarLabels;
    private JTextField quantidadeField;
    private JComboBox<String> andarEntradaCombo;
    private JComboBox<String> andarDestinoCombo;
    private JTextField andarDestinoNumericoField;
    private JCheckBox prioridadeCheckBox;
    private JLabel horarioPicoLabel;
    private JLabel tempoLabel;
    private JButton configuracoesButton;
    private JButton estatisticasButton;

    public SimuladorGUI(Simulador simulador, int numAndares, int numElevadores) {
        this.simulador = simulador;
        this.numAndares = numAndares;
        this.numElevadores = numElevadores;
        this.elevadorLabels = new ArrayList<>();
        this.andarLabels = new ArrayList<>();

        setTitle("Simulador de Elevador Inteligente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel superior
        JPanel topPanel = new JPanel(new FlowLayout());
        statusLabel = new JLabel("Simulação parada");
        topPanel.add(statusLabel);
        horarioPicoLabel = new JLabel("Fora de Pico");
        topPanel.add(horarioPicoLabel);
        tempoLabel = new JLabel("Tempo: 00:00:00");
        topPanel.add(tempoLabel);
        configuracoesButton = new JButton("Configurações");
        configuracoesButton.addActionListener(e -> abrirTelaConfiguracoes());
        topPanel.add(configuracoesButton);
        estatisticasButton = new JButton("Estatísticas");
        estatisticasButton.addActionListener(e -> abrirTelaEstatisticas());
        topPanel.add(estatisticasButton);
        add(topPanel, BorderLayout.NORTH);

        // Painel central
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Adicionar pessoas
        JPanel adicionarPanel = new JPanel(new FlowLayout());
        adicionarPanel.add(new JLabel("Quantidade:"));
        quantidadeField = new JTextField("1", 5);
        adicionarPanel.add(quantidadeField);
        adicionarPanel.add(new JLabel("Andar de Entrada:"));
        String[] andares = new String[numAndares];
        for (int i = 0; i < numAndares; i++) {
            andares[i] = i == 0 ? "Térreo" : String.valueOf(i);
        }
        andarEntradaCombo = new JComboBox<>(andares);
        adicionarPanel.add(andarEntradaCombo);
        adicionarPanel.add(new JLabel("Andar de Destino:"));
        andarDestinoCombo = new JComboBox<>(andares);
        adicionarPanel.add(andarDestinoCombo);
        andarDestinoNumericoField = new JTextField("0", 5);
        adicionarPanel.add(andarDestinoNumericoField);
        prioridadeCheckBox = new JCheckBox("Prioridade (Idoso/Cadeirante)");
        adicionarPanel.add(prioridadeCheckBox);
        JButton adicionarButton = new JButton("Adicionar");
        adicionarButton.addActionListener(e -> adicionarPessoas());
        adicionarPanel.add(adicionarButton);
        centerPanel.add(adicionarPanel, BorderLayout.NORTH);

        // Visualização dos andares e elevadores
        JPanel elevadoresPanel = new JPanel(new GridLayout(numAndares + 1, numElevadores + 2));
        for (int i = 0; i <= numAndares; i++) {
            if (i == 0) {
                elevadoresPanel.add(new JLabel(""));
                for (int j = 0; j < numElevadores; j++) {
                    elevadoresPanel.add(new JLabel("Elev " + (char) ('A' + j), SwingConstants.CENTER));
                }
                elevadoresPanel.add(new JLabel("Fila"));
            } else {
                int andar = numAndares - i;
                JLabel andarLabel = new JLabel(andar == 0 ? "T" : String.valueOf(andar), SwingConstants.CENTER);
                elevadoresPanel.add(andarLabel);
                List<JLabel> labelsAndar = new ArrayList<>();
                for (int j = 0; j < numElevadores; j++) {
                    JLabel elevLabel = new JLabel("", SwingConstants.CENTER);
                    elevLabel.setOpaque(true);
                    elevLabel.setBackground(Color.LIGHT_GRAY);
                    elevadoresPanel.add(elevLabel);
                    labelsAndar.add(elevLabel);
                }
                elevadorLabels.add(labelsAndar);
                JLabel filaLabel = new JLabel("0", SwingConstants.CENTER);
                elevadoresPanel.add(filaLabel);
                andarLabels.add(filaLabel);
            }
        }
        centerPanel.add(elevadoresPanel, BorderLayout.CENTER);

        // Logs
        logArea = new JTextArea(5, 50);
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        centerPanel.add(logScroll, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Painel inferior
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton iniciarButton = new JButton("Iniciar");
        iniciarButton.addActionListener(e -> simulador.iniciar());
        bottomPanel.add(iniciarButton);
        JButton pausarButton = new JButton("Pausar");
        pausarButton.addActionListener(e -> simulador.pausar());
        bottomPanel.add(pausarButton);
        JButton continuarButton = new JButton("Reiniciar");
        continuarButton.addActionListener(e -> simulador.continuar());
        bottomPanel.add(continuarButton);
        add(bottomPanel, BorderLayout.SOUTH);

        atualizarCamposPainel();
        setVisible(true);
    }

    private void adicionarPessoas() {
        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int andarEntrada = andarEntradaCombo.getSelectedIndex();
        int andarDestino;
        if (simulador.getTipoPainel() == 2) {
            try {
                andarDestino = Integer.parseInt(andarDestinoNumericoField.getText());
                if (andarDestino < 0 || andarDestino >= numAndares) {
                    JOptionPane.showMessageDialog(this, "Andar de destino inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Andar de destino inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            andarDestino = andarDestinoCombo.getSelectedIndex();
        }
        boolean prioridade = prioridadeCheckBox.isSelected();

        Andar andar = simulador.getPredio().getAndares().obterElemento(andarEntrada);
        if (andar == null) {
            JOptionPane.showMessageDialog(this, "Erro: Andar inválido selecionado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < quantidade; i++) {
            int id = simulador.getProximoIdPessoa();
            Pessoa pessoa = new Pessoa(id, andarEntrada, andarDestino, prioridade, simulador.getMinutoSimulado());
            andar.adicionarPessoa(pessoa);
            if (simulador.getTipoPainel() == 1) {
                if (andarDestino > andarEntrada) {
                    andar.getPainel().pressionarSubir();
                } else if (andarDestino < andarEntrada) {
                    andar.getPainel().pressionarDescer();
                }
            } else if (simulador.getTipoPainel() == 2) {
                andar.getPainel().chamar(andarDestino, andarEntrada);
            }
            adicionarLog("[" + String.format("%02d:%02d:%02d", simulador.getMinutoSimulado() / 3600, (simulador.getMinutoSimulado() / 60) % 60, simulador.getMinutoSimulado() % 60) + "] Pessoa " + id + (prioridade ? " (Prioridade)" : "") + " entrou na fila do andar " + (andarEntrada == 0 ? "Térreo" : andarEntrada));
        }
    }

    public void atualizarInterface(int minutoSimulado) {
        statusLabel.setText(simulador.isEmExecucao() ? "Simulação em execução" : "Simulação parada");
        tempoLabel.setText("Tempo: " + String.format("%02d:%02d:%02d", minutoSimulado / 3600, (minutoSimulado / 60) % 60, minutoSimulado % 60));

        for (int i = 0; i < numAndares; i++) {
            for (int j = 0; j < numElevadores; j++) {
                elevadorLabels.get(i).get(j).setText("");
                elevadorLabels.get(i).get(j).setBackground(Color.LIGHT_GRAY);
            }
        }

        for (int i = 0; i < numElevadores; i++) {
            Elevador elevador = simulador.getPredio().getCentral().getElevadores().obterElemento(i);
            int posicao = numAndares - 1 - elevador.getAndarAtual();
            JLabel labelAtPos = elevadorLabels.get(posicao).get(i);
            labelAtPos.setText((char) ('A' + i) + " " + elevador.getPessoasDentro().tamanho() + "/" + simulador.getCapacidadeElevador());
            labelAtPos.setBackground(Color.BLUE);
            labelAtPos.setForeground(Color.WHITE);
        }

        for (int i = 0; i < numAndares; i++) {
            Andar andar = simulador.getPredio().getAndares().obterElemento(i);
            andarLabels.get(i).setText(String.valueOf(andar.getPessoasAguardando().tamanho()));
        }

        atualizarCamposPainel();
    }

    private void atualizarCamposPainel() {
        boolean isPainelNumerico = simulador.getTipoPainel() == 2;
        andarDestinoCombo.setVisible(!isPainelNumerico);
        andarDestinoNumericoField.setVisible(isPainelNumerico);
    }

    public void adicionarLog(String mensagem) {
        logArea.append(mensagem + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void limparLog() {
        logArea.setText("");
    }

    public void atualizarHorarioPico(boolean horarioPico) {
        horarioPicoLabel.setText(horarioPico ? "Horário de Pico" : "Fora de Pico");
    }

    private void abrirTelaConfiguracoes() {
        new TelaConfiguracoes(simulador);
    }

    private void abrirTelaEstatisticas() {
        new TelaEstatisticas(simulador);
    }
}