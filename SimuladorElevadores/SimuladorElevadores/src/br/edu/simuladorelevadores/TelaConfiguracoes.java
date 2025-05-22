package br.edu.simuladorelevadores;

import javax.swing.*;
import java.awt.*;

public class TelaConfiguracoes extends JFrame {
    private Simulador simulador;
    private JTextField numAndaresField;
    private JTextField numElevadoresField;
    private JTextField capacidadeField;
    private JTextField tempoPicoField;
    private JTextField tempoForaPicoField;
    private JComboBox<String> heuristicaCombo;
    private JComboBox<String> tipoPainelCombo;

    public TelaConfiguracoes(Simulador simulador) {
        this.simulador = simulador;

        setTitle("Configurações do Simulador");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel principal com borda
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel Geral
        JPanel geralPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        geralPanel.setBorder(BorderFactory.createTitledBorder("Configurações Gerais"));
        geralPanel.add(new JLabel("Número de Andares:"));
        numAndaresField = new JTextField(String.valueOf(simulador.getPredio().getAndares().tamanho()), 5);
        geralPanel.add(numAndaresField);
        geralPanel.add(new JLabel("Número de Elevadores:"));
        numElevadoresField = new JTextField(String.valueOf(simulador.getPredio().getCentral().getElevadores().tamanho()), 5);
        geralPanel.add(numElevadoresField);
        geralPanel.add(new JLabel("Capacidade por Elevador:"));
        capacidadeField = new JTextField(String.valueOf(simulador.getCapacidadeElevador()), 5);
        geralPanel.add(capacidadeField);
        mainPanel.add(geralPanel);

        // Painel Tempos
        JPanel temposPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        temposPanel.setBorder(BorderFactory.createTitledBorder("Tempos (min por andar)"));
        temposPanel.add(new JLabel("Tempo em Horário de Pico:"));
        tempoPicoField = new JTextField(String.valueOf(simulador.getTempoPorAndarPico()), 5);
        temposPanel.add(tempoPicoField);
        temposPanel.add(new JLabel("Tempo Fora de Horário de Pico:"));
        tempoForaPicoField = new JTextField(String.valueOf(simulador.getTempoPorAndarForaPico()), 5);
        temposPanel.add(tempoForaPicoField);
        mainPanel.add(temposPanel);

        // Painel Heurística
        JPanel heuristicaPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        heuristicaPanel.setBorder(BorderFactory.createTitledBorder("Heurística"));
        heuristicaPanel.add(new JLabel("Modelo:"));
        String[] heuristicas = {"Ordem de Chegada (1)", "Otimizando Tempo (2)", "Otimizando Energia (3)"};
        heuristicaCombo = new JComboBox<>(heuristicas);
        int heuristicaAtual = simulador.getPredio().getCentral().getHeuristica();
        heuristicaCombo.setSelectedIndex(heuristicaAtual - 1);
        heuristicaPanel.add(heuristicaCombo);
        mainPanel.add(heuristicaPanel);

        // Painel Painel
        JPanel painelPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        painelPanel.setBorder(BorderFactory.createTitledBorder("Tipo de Painel"));
        painelPanel.add(new JLabel("Tipo:"));
        String[] tiposPainel = {"Dois Botões (1)", "Numérico (2)"};
        tipoPainelCombo = new JComboBox<>(tiposPainel);
        tipoPainelCombo.setSelectedIndex(simulador.getTipoPainel() - 1);
        painelPanel.add(tipoPainelCombo);
        mainPanel.add(painelPanel);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> salvarConfiguracoes());
        buttonPanel.add(salvarButton);
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelarButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void salvarConfiguracoes() {
        try {
            int numAndares = Integer.parseInt(numAndaresField.getText());
            int numElevadores = Integer.parseInt(numElevadoresField.getText());
            int capacidade = Integer.parseInt(capacidadeField.getText());
            float tempoPico = Float.parseFloat(tempoPicoField.getText());
            float tempoForaPico = Float.parseFloat(tempoForaPicoField.getText());
            int heuristica = heuristicaCombo.getSelectedIndex() + 1;
            int tipoPainel = tipoPainelCombo.getSelectedIndex() + 1;

            if (numAndares <= 0 || numElevadores <= 0 || capacidade <= 0 || tempoPico <= 0 || tempoForaPico <= 0) {
                JOptionPane.showMessageDialog(this, "Valores inválidos. Todos devem ser positivos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            simulador.atualizarConfiguracao(numAndares, numElevadores, capacidade, tempoPico, tempoForaPico, tipoPainel, heuristica);
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira valores numéricos válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}