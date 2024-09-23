import java.util.ArrayList;
import java.util.List;

public class Escalonador {
    private List<Processo> processos = new ArrayList<>();
    private int tempo = 1;
    private int limiteTempoTotal = 0;
    private Processo processoExecutando;
    private int indiceAtual = -1; // Controla o índice atual na lista de processos

    // Adiciona processo à lista e atualiza o tempo total de CPU
    public void adicionarProcesso(Processo p) {
        processos.add(p);
        limiteTempoTotal += p.tempoTotalCPU;
    }

    // Executa o escalonamento até que todos os processos estejam finalizados
    public void executarEscalonamento() {
        while (tempo < limiteTempoTotal && processos.stream().anyMatch(p -> !p.estaTerminado())) {
            System.out.println("Tempo: " + tempo);

            // Verifica se todos os processos estão sem créditos
            if (todosProcessosSemCredito()) {
                redistribuirCreditos(); // Redistribui créditos
                System.out.println("Todos os processos sem crédito. Redistribuindo créditos...");
                processoExecutando = selecionarProximoProcesso(); // Seleciona o próximo processo na fila
                
                continue;
            }

            // Seleciona o processo a ser executado
            if (processoExecutando == null || processoExecutando.estaTerminado() || processoExecutando.estaBloqueado() || processoExecutando.creditos == 0) {
                processoExecutando = selecionarProximoProcesso(); // Seleciona o próximo processo na fila
            }

            if (processoExecutando == null) {
                System.out.println("Nenhum processo pronto para ser executado.");
            } else {
                System.out.println("Processo selecionado: " + processoExecutando.nome);
            }

            // Executa o processo selecionado
            if (processoExecutando != null) {
                processoExecutando.executar();
                processoExecutando.bloquear();
                processoExecutando.atualizarEstado();
            }

            for (Processo p : processos) {
                if (p != processoExecutando && p.bloqueado) {
                    p.tempoBloqueado--;
                    if (p.tempoBloqueado == 0) {
                        p.desbloquear();
                    }
                }
            }

            // Imprime o estado de cada processo
            for (Processo p : processos) {
                p.imprimirEstado(p == processoExecutando);
            }

            // Incrementa o tempo do escalonador
            tempo++;
            System.out.println();
        }
    }

    // Seleciona o próximo processo na fila, respeitando a ordem circular
    private Processo selecionarProximoProcesso() {
        int numProcessos = processos.size();
        int tentativas = 0;

        // Percorre os processos a partir do próximo índice
        while (tentativas < numProcessos) {
            indiceAtual = (indiceAtual + 1) % numProcessos; // Avança na lista de processos circularmente
            Processo candidato = processos.get(indiceAtual);

            if (candidato.estaPronto() && candidato.creditos > 0) {
                return candidato;
            }

            tentativas++; // Garante que não entre em loop infinito
        }

        return null; // Nenhum processo pronto foi encontrado
    }

    // Verifica se todos os processos prontos estão sem crédito
    private boolean todosProcessosSemCredito() {
        return processos.stream().noneMatch(p -> p.creditos > 0 && !p.bloqueado && !p.estaTerminado());
    }

    // Redistribui créditos de acordo com a fórmula cred = cred / 2 + prioridade
    private void redistribuirCreditos() {
        for (Processo p : processos) {
            if (!p.estaTerminado()) {
                p.creditos = p.creditos / 2 + p.prioridade;
            }
        }
    }
}