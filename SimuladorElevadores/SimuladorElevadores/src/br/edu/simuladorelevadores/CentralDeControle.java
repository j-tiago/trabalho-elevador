package br.edu.simuladorelevadores;

public class CentralDeControle implements EntidadeSimulavel {
    private Lista<Elevador> elevadores;
    private Predio predio;
    private int heuristica;
    private int totalPessoasAtendidas;
    private int totalPrioritariasAtendidas;
    private Lista<Integer> temposEspera;
    private int chamadasAtendidas;
    private float consumoTotalEnergia;
    private int totalPessoasPorChamada;

    public CentralDeControle(Simulador simulador, int quantidadeElevadores, Predio predio, int capacidadeElevador) {
        this.elevadores = new Lista<>();
        this.predio = predio;
        this.heuristica = 2;
        this.totalPessoasAtendidas = 0;
        this.totalPrioritariasAtendidas = 0;
        this.temposEspera = new Lista<>();
        this.chamadasAtendidas = 0;
        this.consumoTotalEnergia = 0;
        this.totalPessoasPorChamada = 0;
        for (int i = 0; i < quantidadeElevadores; i++) {
            elevadores.inserirFim(new Elevador(simulador, i + 1, capacidadeElevador));
        }
    }

    @Override
    public void atualizar(int minutoSimulado) {
        Ponteiro<Elevador> p = elevadores.getInicio();
        while (p != null) {
            Elevador e = p.getElemento();
            e.atualizar(minutoSimulado);
            p = p.getProximo();
        }

        if (heuristica == 1) {
            atribuirElevadoresOrdemChegada(minutoSimulado);
        } else if (heuristica == 2) {
            atribuirElevadoresOtimizandoTempo(minutoSimulado);
        } else if (heuristica == 3) {
            atribuirElevadoresOtimizandoEnergia(minutoSimulado);
        }

        atualizarEstatisticas(minutoSimulado);
    }

    private void atribuirElevadoresOrdemChegada(int minutoSimulado) {
        Lista<Andar> andaresComChamadas = new Lista<>();
        Ponteiro<Andar> pa = predio.getAndares().getInicio();
        while (pa != null) {
            Andar andar = pa.getElemento();
            if (andar.getPainel().temChamada() && !andar.getPessoasAguardando().estaVazia()) {
                andaresComChamadas.inserirFim(andar);
            }
            pa = pa.getProximo();
        }

        Ponteiro<Elevador> pe = elevadores.getInicio();
        while (pe != null && !andaresComChamadas.estaVazia()) {
            Elevador elevador = pe.getElemento();
            if (!elevador.isEmMovimento()) {
                Andar andar = andaresComChamadas.obterElemento(0);
                if (andar != null) {
                    int tempoEspera = calcularTempoEspera(andar, minutoSimulado);
                    temposEspera.inserirFim(tempoEspera);
                    chamadasAtendidas++;
                    totalPessoasPorChamada += andar.getPessoasAguardando().tamanho();
                    elevador.moverPara(andar.getNumero(), minutoSimulado);
                    andaresComChamadas = removerAndar(andaresComChamadas, andar);
                }
            }
            pe = pe.getProximo();
        }
    }

    private void atribuirElevadoresOtimizandoTempo(int minutoSimulado) {
        Lista<Andar> andaresComChamadas = new Lista<>();
        Ponteiro<Andar> pa = predio.getAndares().getInicio();
        while (pa != null) {
            Andar andar = pa.getElemento();
            if (andar.getPainel().temChamada() && !andar.getPessoasAguardando().estaVazia()) {
                andaresComChamadas.inserirFim(andar);
            }
            pa = pa.getProximo();
        }

        Lista<Elevador> elevadoresDisponiveis = new Lista<>();
        Ponteiro<Elevador> pe = elevadores.getInicio();
        while (pe != null) {
            Elevador elevador = pe.getElemento();
            if (!elevador.isEmMovimento()) {
                elevadoresDisponiveis.inserirFim(elevador);
            }
            pe = pe.getProximo();
        }

        Ponteiro<Andar> pc = andaresComChamadas.getInicio();
        while (pc != null && !elevadoresDisponiveis.estaVazia()) {
            Andar andarMaisPrioritario = null;
            int maxPessoasPrioritarias = -1;
            int menorDistancia = Integer.MAX_VALUE;
            Elevador elevadorEscolhido = null;

            Ponteiro<Elevador> peDisp = elevadoresDisponiveis.getInicio();
            while (peDisp != null) {
                Elevador elevador = peDisp.getElemento();
                Andar a = pc.getElemento();
                int prioritarias = contarPessoasPrioritarias(a.getPessoasAguardando());
                int distancia = Math.abs(a.getNumero() - elevador.getAndarAtual());
                if (prioritarias > maxPessoasPrioritarias ||
                        (prioritarias == maxPessoasPrioritarias && distancia < menorDistancia)) {
                    maxPessoasPrioritarias = prioritarias;
                    andarMaisPrioritario = a;
                    menorDistancia = distancia;
                    elevadorEscolhido = elevador;
                }
                peDisp = peDisp.getProximo();
            }

            if (andarMaisPrioritario != null && elevadorEscolhido != null) {
                int tempoEspera = calcularTempoEspera(andarMaisPrioritario, minutoSimulado);
                temposEspera.inserirFim(tempoEspera);
                chamadasAtendidas++;
                totalPessoasPorChamada += andarMaisPrioritario.getPessoasAguardando().tamanho();
                elevadorEscolhido.moverPara(andarMaisPrioritario.getNumero(), minutoSimulado);
                andaresComChamadas = removerAndar(andaresComChamadas, andarMaisPrioritario);

                Lista<Elevador> novaListaElevadores = new Lista<>();
                peDisp = elevadoresDisponiveis.getInicio();
                while (peDisp != null) {
                    Elevador e = peDisp.getElemento();
                    if (e != elevadorEscolhido) {
                        novaListaElevadores.inserirFim(e);
                    }
                    peDisp = peDisp.getProximo();
                }
                elevadoresDisponiveis = novaListaElevadores;
            }
            pc = pc.getProximo();
        }
    }

    private void atribuirElevadoresOtimizandoEnergia(int minutoSimulado) {
        Lista<Andar> andaresComChamadas = new Lista<>();
        Ponteiro<Andar> pa = predio.getAndares().getInicio();
        while (pa != null) {
            Andar andar = pa.getElemento();
            if (andar.getPainel().temChamada() && !andar.getPessoasAguardando().estaVazia()) {
                andaresComChamadas.inserirFim(andar);
            }
            pa = pa.getProximo();
        }

        Lista<Elevador> elevadoresDisponiveis = new Lista<>();
        Ponteiro<Elevador> pe = elevadores.getInicio();
        while (pe != null) {
            Elevador elevador = pe.getElemento();
            if (!elevador.isEmMovimento()) {
                elevadoresDisponiveis.inserirFim(elevador);
            }
            pe = pe.getProximo();
        }

        Ponteiro<Andar> pc = andaresComChamadas.getInicio();
        while (pc != null && !elevadoresDisponiveis.estaVazia()) {
            Andar andarMaisProximo = null;
            int menorDistancia = Integer.MAX_VALUE;
            Elevador elevadorEscolhido = null;

            Ponteiro<Elevador> peDisp = elevadoresDisponiveis.getInicio();
            while (peDisp != null) {
                Elevador elevador = peDisp.getElemento();
                Andar a = pc.getElemento();
                int distancia = Math.abs(a.getNumero() - elevador.getAndarAtual());
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    andarMaisProximo = a;
                    elevadorEscolhido = elevador;
                }
                peDisp = peDisp.getProximo();
            }

            if (andarMaisProximo != null && elevadorEscolhido != null) {
                int tempoEspera = calcularTempoEspera(andarMaisProximo, minutoSimulado);
                temposEspera.inserirFim(tempoEspera);
                chamadasAtendidas++;
                totalPessoasPorChamada += andarMaisProximo.getPessoasAguardando().tamanho();
                elevadorEscolhido.moverPara(andarMaisProximo.getNumero(), minutoSimulado);
                andaresComChamadas = removerAndar(andaresComChamadas, andarMaisProximo);

                Lista<Elevador> novaListaElevadores = new Lista<>();
                peDisp = elevadoresDisponiveis.getInicio();
                while (peDisp != null) {
                    Elevador e = peDisp.getElemento();
                    if (e != elevadorEscolhido) {
                        novaListaElevadores.inserirFim(e);
                    }
                    peDisp = peDisp.getProximo();
                }
                elevadoresDisponiveis = novaListaElevadores;
            }
            pc = pc.getProximo();
        }
    }

    private int calcularTempoEspera(Andar andar, int minutoSimulado) {
        Ponteiro<Pessoa> p = andar.getPessoasAguardando().getInicio();
        while (p != null) {
            Pessoa pessoa = p.getElemento();
            return minutoSimulado - pessoa.getTempoEntrada(); // Simplificado, assume tempo de entrada
        }
        return 0;
    }

    private float calcularConsumoEnergia(int andarAtual, int andarDestino) {
        int distancia = Math.abs(andarDestino - andarAtual);
        return distancia * 0.5f; // Consumo estimado: 0.5 unidades por andar
    }

    private void atualizarEstatisticas(int minutoSimulado) {
        Ponteiro<Elevador> p = elevadores.getInicio();
        while (p != null) {
            Elevador e = p.getElemento();
            p = p.getProximo();
        }
    }

    private Lista<Andar> removerAndar(Lista<Andar> lista, Andar andar) {
        Lista<Andar> novaLista = new Lista<>();
        Ponteiro<Andar> temp = lista.getInicio();
        while (temp != null) {
            Andar a = temp.getElemento();
            if (a != andar) novaLista.inserirFim(a);
            temp = temp.getProximo();
        }
        return novaLista;
    }

    private int contarPessoasPrioritarias(Fila<Pessoa> fila) {
        int count = 0;
        Ponteiro<Pessoa> p = fila.getInicio();
        while (p != null) {
            Pessoa pessoa = p.getElemento();
            if (pessoa.temPrioridade()) count++;
            p = p.getProximo();
        }
        return count;
    }

    public Lista<Elevador> getElevadores() {
        return elevadores;
    }

    public void setHeuristica(int heuristica) {
        this.heuristica = heuristica;
    }

    public int getHeuristica() {
        return heuristica;
    }

    public int getTotalPessoasAtendidas() {
        return totalPessoasAtendidas;
    }

    public int getTotalPrioritariasAtendidas() {
        return totalPrioritariasAtendidas;
    }

    public float getTempoMedioEspera() {
        if (temposEspera.tamanho() == 0) return 0;
        int soma = 0;
        Ponteiro<Integer> p = temposEspera.getInicio();
        while (p != null) {
            soma += p.getElemento();
            p = p.getProximo();
        }
        return (float) soma / temposEspera.tamanho();
    }

    public int getTempoMaximoEspera() {
        int max = 0;
        Ponteiro<Integer> p = temposEspera.getInicio();
        while (p != null) {
            max = Math.max(max, p.getElemento());
            p = p.getProximo();
        }
        return max;
    }

    public int getChamadasAtendidas() {
        return chamadasAtendidas;
    }

    public float getConsumoTotalEnergia() {
        return consumoTotalEnergia;
    }

    public void incrementarPessoasAtendidas(boolean temPrioridade) {
        totalPessoasAtendidas++;
        if (temPrioridade) totalPrioritariasAtendidas++;
    }

    public void incrementarConsumoEnergia(float consumo) {
        consumoTotalEnergia += consumo;
    }

    public float getMediaPessoasPorChamada() {
        if (chamadasAtendidas == 0) return 0;
        return (float) totalPessoasPorChamada / chamadasAtendidas;
    }
}