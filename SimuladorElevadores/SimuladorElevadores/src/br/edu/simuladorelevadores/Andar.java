package br.edu.simuladorelevadores;

public class Andar implements EntidadeSimulavel {
    private int numero;
    private PainelElevador painel;
    private Fila<Pessoa> pessoasAguardando;

    public Andar(int numero, int tipoPainel) {
        this.numero = numero;
        this.painel = new PainelElevador(tipoPainel);
        this.pessoasAguardando = new Fila<>();
    }

    @Override
    public void atualizar(int minutoSimulado) {
        // Atualizações específicas do andar, se necessário
    }

    public void adicionarPessoa(Pessoa pessoa) {
        if (pessoa.temPrioridade()) {
            // Insere no início da fila se for pessoa com prioridade
            Fila<Pessoa> novaFila = new Fila<>();
            novaFila.inserirFim(pessoa);
            Ponteiro<Pessoa> p = pessoasAguardando.getInicio();
            while (p != null) {
                novaFila.inserirFim(p.getElemento());
                p = p.getProximo();
            }
            pessoasAguardando = novaFila;
        } else {
            // Insere no final da fila se não for prioritária
            pessoasAguardando.inserirFim(pessoa);
        }
    }

    public int getNumero() {
        return numero;
    }

    public PainelElevador getPainel() {
        return painel;
    }

    public Fila<Pessoa> getPessoasAguardando() {
        return pessoasAguardando;
    }
}