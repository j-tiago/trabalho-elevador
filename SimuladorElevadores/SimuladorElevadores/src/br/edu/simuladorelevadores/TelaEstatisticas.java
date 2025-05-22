package br.edu.simuladorelevadores;

import javax.swing.*;
import java.awt.*;

public class TelaEstatisticas extends JFrame {
    private Simulador simulador;

    public TelaEstatisticas(Simulador simulador) {
        this.simulador = simulador;

        setTitle("Estatísticas do Simulador");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel principal com borda
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel Geral
        JPanel geralPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        geralPanel.setBorder(BorderFactory.createTitledBorder("Dados Gerais"));
        CentralDeControle central = simulador.getPredio().getCentral();
        geralPanel.add(new JLabel("Total de Pessoas Atendidas:"));
        geralPanel.add(new JLabel(String.valueOf(central.getTotalPessoasAtendidas())));
        geralPanel.add(new JLabel("Total de Pessoas Prioritárias Atendidas:"));
        geralPanel.add(new JLabel(String.valueOf(central.getTotalPrioritariasAtendidas())));
        geralPanel.add(new JLabel("Média de Pessoas por Chamada:"));
        geralPanel.add(new JLabel(String.format("%.2f", central.getMediaPessoasPorChamada())));
        geralPanel.add(new JLabel("Heurística Atual:"));
        geralPanel.add(new JLabel("Modelo " + central.getHeuristica()));
        mainPanel.add(geralPanel);

        // Painel Tempos de Espera
        JPanel temposPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        temposPanel.setBorder(BorderFactory.createTitledBorder("Tempos de Espera (min)"));
        temposPanel.add(new JLabel("Tempo Médio de Espera:"));
        temposPanel.add(new JLabel(String.format("%.2f", central.getTempoMedioEspera())));
        temposPanel.add(new JLabel("Tempo Máximo de Espera:"));
        temposPanel.add(new JLabel(String.valueOf(central.getTempoMaximoEspera())));
        mainPanel.add(temposPanel);

        // Painel Chamadas
        JPanel chamadasPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chamadasPanel.setBorder(BorderFactory.createTitledBorder("Chamadas"));
        chamadasPanel.add(new JLabel("Chamadas Atendidas:"));
        chamadasPanel.add(new JLabel(String.valueOf(central.getChamadasAtendidas())));
        mainPanel.add(chamadasPanel);

        // Painel Consumo de Energia
        JPanel consumoPanel = new JPanel(new GridLayout(central.getElevadores().tamanho() + 1, 2, 10, 10));
        consumoPanel.setBorder(BorderFactory.createTitledBorder("Consumo de Energia (unidades)"));
        consumoPanel.add(new JLabel("Consumo Total:"));
        consumoPanel.add(new JLabel(String.format("%.2f", central.getConsumoTotalEnergia())));
        Ponteiro<Elevador> p = central.getElevadores().getInicio();
        int i = 0;
        while (p != null && i < central.getElevadores().tamanho()) {
            Elevador elevador = central.getElevadores().obterElemento(i);
            consumoPanel.add(new JLabel("Consumo Elevador " + (char) ('A' + i) + ":"));
            consumoPanel.add(new JLabel(String.format("%.2f", elevador.getConsumoEnergia())));
            p = p.getProximo();
            i++;
        }
        mainPanel.add(consumoPanel);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}