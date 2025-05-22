package br.edu.simuladorelevadores;

public class PainelInternoElevador {
    private Lista<Integer> destinos;

    public PainelInternoElevador() {
        this.destinos = new Lista<>();
    }

    public void adicionarDestino(int andar) {
        if (!destinosContem(andar)) {
            destinos.inserirFim(andar);
        }
    }

    public void removerDestino(int andar) {
        Lista<Integer> novaLista = new Lista<>();
        Ponteiro<Integer> p = destinos.getInicio();
        while (p != null) {
            int destino = p.getElemento();
            if (destino != andar) {
                novaLista.inserirFim(destino);
            }
            p = p.getProximo();
        }
        destinos = novaLista;
    }

    public Lista<Integer> getDestinos() {
        return destinos;
    }

    private boolean destinosContem(int andar) {
        Ponteiro<Integer> p = destinos.getInicio();
        while (p != null) {
            if (p.getElemento() == andar) return true;
            p = p.getProximo();
        }
        return false;
    }
}