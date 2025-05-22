package br.edu.simuladorelevadores;

public class Fila<T> {
    private Ponteiro<T> inicio;
    private Ponteiro<T> fim;
    private int tamanho;

    public Fila() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    public void inserirFim(T elemento) {
        Ponteiro<T> novo = new Ponteiro<>(elemento, null);
        if (inicio == null) {
            inicio = novo;
            fim = novo;
        } else {
            fim.setProximo(novo);
            fim = novo;
        }
        tamanho++;
    }

    public T removerInicio() {
        if (inicio == null) return null;
        T elemento = inicio.getElemento();
        inicio = inicio.getProximo();
        if (inicio == null) fim = null;
        tamanho--;
        return elemento;
    }

    public Ponteiro<T> getInicio() {
        return inicio;
    }

    public int tamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }
}