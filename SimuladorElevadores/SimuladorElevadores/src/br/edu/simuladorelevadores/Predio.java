package br.edu.simuladorelevadores;

public class Predio {
    private Lista<Andar> andares;
    private CentralDeControle central;
    private int capacidadeElevador;
    private int tipoPainel;
    private Simulador simulador;

    public Predio(Simulador simulador, int numAndares, int numElevadores, int capacidadeElevador, int tipoPainel) {
        this.simulador = simulador;
        this.capacidadeElevador = capacidadeElevador;
        this.tipoPainel = tipoPainel;
        andares = new Lista<>();
        for (int i = 0; i < numAndares; i++) {
            andares.inserirFim(new Andar(i, tipoPainel));
        }
        central = new CentralDeControle(simulador, numElevadores, this, capacidadeElevador);
    }

    public Lista<Andar> getAndares() {
        return andares;
    }

    public CentralDeControle getCentral() {
        return central;
    }

    public int getCapacidadeElevador() {
        return capacidadeElevador;
    }

    public int getTipoPainel() {
        return tipoPainel;
    }

    public Simulador getSimulador() {
        return simulador;
    }
}