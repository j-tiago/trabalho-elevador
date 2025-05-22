package br.edu.simuladorelevadores;

public class Elevador implements EntidadeSimulavel {
    private int id;
    private int andarAtual;
    private Fila<Pessoa> pessoasDentro;
    private boolean emMovimento;
    private int andarDestino;
    private int capacidade;
    private PainelInternoElevador painelInterno;
    private float tempoAcumulado;
    private Simulador simulador;
    private float consumoEnergia;
    private boolean aguardandoEmbarque;
    private Andar andarChamado;

    public Elevador(Simulador simulador, int id, int capacidade) {
        this.simulador = simulador;
        this.id = id;
        this.andarAtual = 0;
        this.pessoasDentro = new Fila<>();
        this.emMovimento = false;
        this.andarDestino = 0;
        this.capacidade = capacidade;
        this.painelInterno = new PainelInternoElevador();
        this.tempoAcumulado = 0;
        this.consumoEnergia = 0;
        this.aguardandoEmbarque = false;
        this.andarChamado = null;
    }

    @Override
    public void atualizar(int minutoSimulado) {
        if (emMovimento) {
            float tempoPorAndar = simulador.getTempoPorAndar();
            tempoAcumulado += 1.0f / 60.0f; // 1 segundo = 1/60 minutos
            if (tempoAcumulado >= tempoPorAndar) {
                if (andarAtual < andarDestino) {
                    andarAtual++;
                } else if (andarAtual > andarDestino) {
                    andarAtual--;
                }
                tempoAcumulado = 0;
            }

            if (andarAtual == andarDestino) {
                emMovimento = false;
                // Desembarque
                Fila<Pessoa> temp = new Fila<>();
                Ponteiro<Pessoa> p = pessoasDentro.getInicio();
                while (p != null) {
                    Pessoa pessoa = p.getElemento();
                    if (pessoa.getAndarDestino() != andarAtual) {
                        temp.inserirFim(pessoa);
                    } else {
                        String mensagem = "[" + String.format("%02d:%02d:%02d", minutoSimulado / 3600, (minutoSimulado / 60) % 60, minutoSimulado % 60) + "] Pessoa " + pessoa.getId() + " desembarcou no andar " + andarAtual;
                        simulador.getGui().adicionarLog(mensagem);
                        simulador.getPredio().getCentral().incrementarPessoasAtendidas(pessoa.temPrioridade());
                        float consumo = Math.abs(pessoa.getAndarOrigem() - pessoa.getAndarDestino()) * 0.5f;
                        simulador.getPredio().getCentral().incrementarConsumoEnergia(consumo);
                        atualizarConsumoEnergia(consumo);
                    }
                    p = p.getProximo();
                }
                pessoasDentro = temp;
                painelInterno.removerDestino(andarDestino);

                // Embarque (se estiver aguardando)
                if (aguardandoEmbarque && andarChamado != null && andarChamado.getNumero() == andarAtual) {
                    while (podeReceberMaisPessoas() && !andarChamado.getPessoasAguardando().estaVazia()) {
                        Pessoa pessoa = andarChamado.getPessoasAguardando().removerInicio();
                        embarcarPessoa(pessoa, minutoSimulado);
                        moverPara(pessoa.getAndarDestino(), minutoSimulado);
                    }
                    if (andarChamado.getPessoasAguardando().estaVazia()) {
                        andarChamado.getPainel().resetar();
                        aguardandoEmbarque = false;
                        andarChamado = null;
                    } else {
                        // Se ainda houver pessoas, manter a chamada ativa
                        aguardandoEmbarque = true;
                        if (simulador.getTipoPainel() == 1) {
                            Ponteiro<Pessoa> pp = andarChamado.getPessoasAguardando().getInicio();
                            if (pp != null) {
                                Pessoa proximaPessoa = pp.getElemento();
                                if (proximaPessoa.getAndarDestino() > andarChamado.getNumero()) {
                                    andarChamado.getPainel().pressionarSubir();
                                } else if (proximaPessoa.getAndarDestino() < andarChamado.getNumero()) {
                                    andarChamado.getPainel().pressionarDescer();
                                }
                            }
                        } else if (simulador.getTipoPainel() == 2) {
                            Ponteiro<Pessoa> pp = andarChamado.getPessoasAguardando().getInicio();
                            if (pp != null) {
                                Pessoa proximaPessoa = pp.getElemento();
                                andarChamado.getPainel().chamar(proximaPessoa.getAndarDestino(), andarChamado.getNumero());
                            }
                        }
                    }
                }
            }
        }
    }

    public int getAndarAtual() {
        return andarAtual;
    }

    public void moverPara(int andarDestino, int minutoSimulado) {
        this.andarDestino = andarDestino;
        this.emMovimento = true;
        painelInterno.adicionarDestino(andarDestino);
        String mensagem = "[" + String.format("%02d:%02d:%02d", minutoSimulado / 3600, (minutoSimulado / 60) % 60, minutoSimulado % 60) + "] Elevador " + id + " movendo-se para o andar " + andarDestino;
        simulador.getGui().adicionarLog(mensagem);

        // Verifica se o movimento é para um andar com chamada pendente
        Andar andar = simulador.getPredio().getAndares().obterElemento(andarDestino);
        if (andar != null && andar.getPainel().temChamada() && !andar.getPessoasAguardando().estaVazia()) {
            aguardandoEmbarque = true;
            andarChamado = andar;
        }
    }

    public boolean isEmMovimento() {
        return emMovimento;
    }

    public boolean podeReceberMaisPessoas() {
        return pessoasDentro.tamanho() < capacidade;
    }

    public void embarcarPessoa(Pessoa pessoa, int minutoSimulado) {
        pessoasDentro.inserirFim(pessoa);
        painelInterno.adicionarDestino(pessoa.getAndarDestino());
        String mensagem = "[" + String.format("%02d:%02d:%02d", minutoSimulado / 3600, (minutoSimulado / 60) % 60, minutoSimulado % 60) + "] Pessoa " + pessoa.getId() + " embarcou no elevador " + id;
        simulador.getGui().adicionarLog(mensagem);
    }

    public Fila<Pessoa> getPessoasDentro() {
        return pessoasDentro;
    }

    public int getId() {
        return id;
    }

    public PainelInternoElevador getPainelInterno() {
        return painelInterno;
    }

    public void atualizarConsumoEnergia(float consumo) {
        this.consumoEnergia += consumo;
    }

    public float getConsumoEnergia() {
        return consumoEnergia;
    }
}