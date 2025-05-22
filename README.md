
# 🛗 Simulador de Elevadores 

Este projeto em Java simula um sistema de elevadores em funcionamento dentro de um edifício, com foco em modelagem orientada a objetos e uso de estruturas de dados dinâmicas. A arquitetura do projeto promove o entendimento de conceitos fundamentais como herança, polimorfismo, encapsulamento e serialização.

> 📚 Projeto desenvolvido para a disciplina de **Estrutura de Dados**.

---

## 🎯 Objetivo

Desenvolver uma aplicação modular que:
- Modele o comportamento realista de elevadores;
- Reaja a eventos com base no tempo simulado;
- Utilize estruturas de dados dinâmicas (listas e filas);
- Permita expansão com novas funcionalidades;
- Sirva como base para estudos acadêmicos e testes com algoritmos de controle.

---

## 🧩 Funcionalidades Principais

✅ Gerenciamento simultâneo de múltiplos elevadores  
✅ Simulação em tempo real de chamadas, movimentação e paradas  
✅ Interface de controle para chamadas e painéis internos  
✅ Capacidade de gravar e carregar o estado da simulação (`serialização`)  
✅ Estruturas de dados personalizadas: `Lista`, `Fila`, etc.  
✅ Painéis simulando botões de chamada por andar e por elevador  
✅ Controle de fluxo de passageiros (origem, destino, status)

---

## 📂 Estrutura do Projeto

| Classe                | Descrição |
|------------------------|------------|
| `Simulador`            | Controla o tempo da simulação e faz a serialização do estado. |
| `EntidadeSimulavel`    | Interface para objetos que respondem ao tempo (método `atualizar`). |
| `Predio`               | Contém os andares e elevadores. Responsável por atualizações globais. |
| `CentralDeControle`    | Coordena os elevadores com base nas chamadas pendentes. |
| `Elevador`             | Implementa a lógica de movimentação e controle de passageiros. |
| `Andar`                | Contém as filas de espera e painéis de chamada. |
| `Pessoa`               | Passageiro com dados de origem, destino e status. |
| `PainelElevador`, `PainelInternoElevador`, `PainelConfig` | Interfaces gráficas (em Swing) para interagir com a simulação. |
| `Lista`, `Fila`        | Estruturas de dados próprias para organizar elementos. |
| `Main`                 | Classe principal para inicialização e execução da simulação. |

---

## ▶️ Como Executar

### Pré-requisitos:
- Java JDK 8 ou superior
- IDE como IntelliJ, Eclipse ou terminal com suporte à compilação Java

### Passos:

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/simulador-elevadores.git

# Acesse o diretório
cd simulador-elevadores

# Compile os arquivos
javac src/*.java

# Execute o programa
java Main
```

---

## 💡 Exemplos de Uso

- Criar um prédio com 5 andares e 3 elevadores.
- Chamar elevadores de andares aleatórios.
- Observar os passageiros entrando e saindo.
- Gravar o estado da simulação, encerrar e retomar depois.

---

## 📈 Ideias de Expansão

- ✔️ Algoritmos inteligentes de despacho (prioridade, menor distância etc.)
- ✔️ Limite de peso e capacidade
- ✔️ Logs detalhados de movimentação
- ✔️ Simulação com filas dinâmicas e tempo variável
- ✔️ Interface gráfica aprimorada com JavaFX

---

## 🛠️ Tecnologias Utilizadas

- Java 8+
- Paradigma de Programação Orientada a Objetos (POO)
- Estruturas de dados personalizadas
- Serialização de objetos Java
- Interface Swing (GUI)

---

## 📄 Licença

Projeto de uso acadêmico. Modificações e contribuições são bem-vindas.  
Distribuído sob a licença MIT.

---

Feito com 💡 por [Seu Nome](https://github.com/seu-usuario)
