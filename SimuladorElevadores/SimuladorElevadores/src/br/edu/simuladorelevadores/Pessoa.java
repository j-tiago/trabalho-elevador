package br.edu.simuladorelevadores;

public class Pessoa {
    private int id;
    private int andarOrigem;
    private int andarDestino;
    private boolean prioridade;
    private int tempoEntrada;

    public Pessoa(int id, int andarOrigem, int andarDestino, boolean prioridade, int tempoEntrada) {
        this.id = id;
        this.andarOrigem = andarOrigem;
        this.andarDestino = andarDestino;
        this.prioridade = prioridade;
        this.tempoEntrada = tempoEntrada;
    }

    public int getId() {
        return id;
    }

    public int getAndarOrigem() {
        return andarOrigem;
    }

    public int getAndarDestino() {
        return andarDestino;
    }

    public boolean temPrioridade() {
        return prioridade;
    }

    public int getTempoEntrada() {
        return tempoEntrada;
    }
}