package br.edu.simuladorelevadores;

public class Simulador {
    private SimuladorGUI gui;
    private Predio predio;
    private int minutoSimulado;
    private boolean emExecucao;
    private int proximoIdPessoa;
    private float tempoPorAndarPico;
    private float tempoPorAndarForaPico;
    private int capacidadeElevador;
    private int tipoPainel;

    public Simulador() {
        minutoSimulado = 0;
        emExecucao = false;
        proximoIdPessoa = 1;
        tempoPorAndarPico = 1.5f;
        tempoPorAndarForaPico = 1.0f;
        capacidadeElevador = 8;
        tipoPainel = 1; // Dois botões por padrão
        predio = new Predio(this, 6, 3, capacidadeElevador, tipoPainel);
        gui = new SimuladorGUI(this, 6, 3);
    }

    public void iniciar() {
        if (!emExecucao) {
            emExecucao = true;
            new Thread(() -> {
                while (emExecucao) {
                    atualizar();
                    try {
                        Thread.sleep(1000); // 1 segundo por "minuto simulado"
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void pausar() {
        emExecucao = false;
    }

    public void continuar() {
        minutoSimulado = 0;
        proximoIdPessoa = 1; // Reinicia o contador de IDs das pessoas
        predio = new Predio(this, predio.getAndares().tamanho(), predio.getCentral().getElevadores().tamanho(), capacidadeElevador, tipoPainel);
        emExecucao = false;
        gui.atualizarInterface(minutoSimulado);
        gui.atualizarHorarioPico(false);
        gui.limparLog(); // Zera o log ao reiniciar
        iniciar();
    }

    public void atualizarConfiguracao(int numAndares, int numElevadores, int capacidade, float tempoPico, float tempoForaPico, int tipoPainel, int heuristica) {
        this.tempoPorAndarPico = tempoPico;
        this.tempoPorAndarForaPico = tempoForaPico;
        this.capacidadeElevador = capacidade;
        this.tipoPainel = tipoPainel;
        predio = new Predio(this, numAndares, numElevadores, capacidade, tipoPainel);
        predio.getCentral().setHeuristica(heuristica);
        gui.dispose();
        gui = new SimuladorGUI(this, numAndares, numElevadores);
        minutoSimulado = 0;
        emExecucao = false;
        proximoIdPessoa = 1; // Reinicia o contador de IDs ao reconfigurar
        iniciar();
    }

    public void atualizar() {
        minutoSimulado++;
        boolean horarioPico = isHorarioPico();
        gui.atualizarHorarioPico(horarioPico);

        // Atualizar os andares
        for (int i = 0; i < predio.getAndares().tamanho(); i++) {
            Andar andar = predio.getAndares().obterElemento(i);
            andar.atualizar(minutoSimulado);
        }

        // Atualizar a central de controle
        predio.getCentral().atualizar(minutoSimulado);

        // Atualizar a interface
        gui.atualizarInterface(minutoSimulado);
    }

    private boolean isHorarioPico() {
        int hora = (minutoSimulado / 60) % 24;
        return (hora >= 7 && hora <= 9) || (hora >= 17 && hora <= 19);
    }

    public Predio getPredio() {
        return predio;
    }

    public int getMinutoSimulado() {
        return minutoSimulado;
    }

    public boolean isEmExecucao() {
        return emExecucao;
    }

    public int getProximoIdPessoa() {
        return proximoIdPessoa++;
    }

    public float getTempoPorAndar() {
        return isHorarioPico() ? tempoPorAndarPico : tempoPorAndarForaPico;
    }

    public int getCapacidadeElevador() {
        return capacidadeElevador;
    }

    public int getTipoPainel() {
        return tipoPainel;
    }

    public float getTempoPorAndarPico() {
        return tempoPorAndarPico;
    }

    public float getTempoPorAndarForaPico() {
        return tempoPorAndarForaPico;
    }

    public SimuladorGUI getGui() {
        return gui;
    }
}