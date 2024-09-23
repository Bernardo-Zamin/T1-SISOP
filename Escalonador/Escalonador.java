import java.util.ArrayList;
import java.util.List;

public class Escalonador {
    private List<Processo> processos = new ArrayList<>();
    private int tempo = 0;
    private int limiteTempoTotal = 0;
    private Processo processoExecutando;

    // Adiciona processo à lista e atualiza o tempo total de CPU
    public void adicionarProcesso(Processo p) {
        processos.add(p);
        limiteTempoTotal += p.tempoTotalCPU;
    }

    // Executa o escalonamento até que todos os processos estejam finalizados
    public void executarEscalonamento() {
        while (tempo < limiteTempoTotal && processos.stream().anyMatch(p -> !p.estaTerminado())) {
            System.out.println("Tempo: " + tempo);

            // Seleciona o processo a ser executado
            if (processoExecutando == null || processoExecutando.estaTerminado() || processoExecutando.estaBloqueado() || processoExecutando.creditos == 0) {
                processoExecutando = selecionarProcesso();
            }

            if (processoExecutando == null) {
                System.out.println("Nenhum processo pronto para ser executado.");

            } else {
                System.out.println("Processo selecionado: " + processoExecutando.nome);
            }

            // Verifica se todos os processos estão sem créditos
            if (todosProcessosSemCredito()) {
                redistribuirCreditos(); // Redistribui créditos
                System.out.println("Todos os processos sem crédito. Redistribuindo créditos...");
                tempo++;
                continue;
            }

            // Executa o processo selecionado
            if (processoExecutando != null) {
                processoExecutando.executar();
                processoExecutando.bloquear(); // Verifica se o processo deve ser bloqueado após a execução
                processoExecutando.atualizarEstado(); // Atualiza o estado do processo
            }

            for (Processo p : processos) {
                if(p != processoExecutando) {
                    if(p.bloqueado) {
                        p.tempoBloqueado--;
                        if(p.tempoBloqueado == 0) {
                            p.desbloquear();
                        }
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

    // Seleciona o próximo processo a ser executado baseado nos créditos e prioridade
private Processo selecionarProcesso() {
    return processos.stream()
            .filter(p -> p.estaPronto() && p.creditos > 0)  // Garante que apenas processos com créditos > 0 sejam selecionados
            .sorted((p1, p2) -> {
                if (p1.creditos == p2.creditos) {
                    return Integer.compare(p2.prioridade, p1.prioridade);
                }
                return Integer.compare(p2.creditos, p1.creditos);
            })
            .findFirst()
            .orElse(null);
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