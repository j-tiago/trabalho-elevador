package br.edu.simuladorelevadores;

public class PainelElevador {
    private int tipoPainel; // 0: Único botão, 1: Dois botões, 2: Numérico
    private boolean botaoChamada;
    private boolean botaoSubirAtivado;
    private boolean botaoDescerAtivado;
    private Lista<Integer> destinos;

    public PainelElevador(int tipoPainel) {
        this.tipoPainel = tipoPainel;
        this.botaoChamada = false;
        this.botaoSubirAtivado = false;
        this.botaoDescerAtivado = false;
        this.destinos = new Lista<>();
    }

    public void chamar(int andarDestino, int andarAtual) {
        if (tipoPainel == 0) {
            botaoChamada = true;
        } else if (tipoPainel == 1) {
            if (andarDestino > andarAtual) {
                botaoSubirAtivado = true;
            } else if (andarDestino < andarAtual) {
                botaoDescerAtivado = true;
            }
        } else if (tipoPainel == 2) {
            destinos.inserirFim(andarDestino);
        }
    }

    public void pressionarSubir() {
        if (tipoPainel == 1) {
            botaoSubirAtivado = true;
        }
    }

    public void pressionarDescer() {
        if (tipoPainel == 1) {
            botaoDescerAtivado = true;
        }
    }

    public void resetar() {
        botaoChamada = false;
        botaoSubirAtivado = false;
        botaoDescerAtivado = false;
        destinos = new Lista<>();
    }

    public boolean temChamada() {
        if (tipoPainel == 0) return botaoChamada;
        if (tipoPainel == 1) return botaoSubirAtivado || botaoDescerAtivado;
        return !destinos.estaVazia();
    }

    public boolean isBotaoSubirAtivado() {
        return botaoSubirAtivado;
    }

    public boolean isBotaoDescerAtivado() {
        return botaoDescerAtivado;
    }

    public Lista<Integer> getDestinos() {
        return destinos;
    }
}