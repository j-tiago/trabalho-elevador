package br.edu.simuladorelevadores;

public class Ponteiro<T> {
    private T elemento;
    private Ponteiro<T> proximo;

    public Ponteiro(T elemento, Ponteiro<T> proximo) {
        this.elemento = elemento;
        this.proximo = proximo;
    }

    public T getElemento() {
        return elemento;
    }

    public Ponteiro<T> getProximo() {
        return proximo;
    }

    public void setProximo(Ponteiro<T> proximo) {
        this.proximo = proximo;
    }
}