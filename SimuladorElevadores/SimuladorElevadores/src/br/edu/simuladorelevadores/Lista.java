package br.edu.simuladorelevadores;

public class Lista<T> {
    private Ponteiro<T> inicio;
    private int tamanho;

    public Lista() {
        this.inicio = null;
        this.tamanho = 0;
    }

    public void inserirFim(T elemento) {
        Ponteiro<T> novo = new Ponteiro<>(elemento, null);
        if (inicio == null) {
            inicio = novo;
        } else {
            Ponteiro<T> atual = inicio;
            while (atual.getProximo() != null) {
                atual = atual.getProximo();
            }
            atual.setProximo(novo);
        }
        tamanho++;
    }

    public T obterElemento(int indice) {
        if (indice < 0 || indice >= tamanho) return null;
        Ponteiro<T> atual = inicio;
        for (int i = 0; i < indice; i++) {
            atual = atual.getProximo();
        }
        return atual.getElemento();
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int tamanho() {
        return tamanho;
    }

    public Ponteiro<T> getInicio() {
        return inicio;
    }
}