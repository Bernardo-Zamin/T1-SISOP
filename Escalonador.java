import java.util.ArrayList;
import java.util.List;

public class Escalonador {
    private List<Processo> processos = new ArrayList<>();
    private int tempo = 1;
    private int limiteTempoTotal = 0;
    private Processo processoExecutando;
    private int indiceAtual = -1;

    public void adicionarProcesso(Processo p) {
        processos.add(p);
        limiteTempoTotal += p.tempoTotalCPU;
    }

    public void executarEscalonamento() {

        while (tempo < limiteTempoTotal && processos.stream().anyMatch(p -> !p.estaTerminado())) {

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Tempo: " + tempo);

            if (todosProcessosSemCredito()) {
                redistribuirCreditos();
                System.out.println("Todos os processos sem crédito. Redistribuindo créditos...");
                processoExecutando = selecionarProximoProcesso();

                continue;
            }

            if (processoExecutando == null || processoExecutando.estaTerminado() || processoExecutando.estaBloqueado()
                    || processoExecutando.creditos == 0) {
                processoExecutando = selecionarProximoProcesso();
            }

            if (processoExecutando == null) {
                System.out.println("Nenhum processo pronto para ser executado.");
            } else {
                System.out.println("Processo selecionado: " + processoExecutando.nome);
            }

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

            for (Processo p : processos) {
                p.imprimirEstado(p == processoExecutando);
            }

            tempo++;
            System.out.println();
        }
    }

    private Processo selecionarProximoProcesso() {
        int numProcessos = processos.size();
        int tentativas = 0;

        while (tentativas < numProcessos) {
            indiceAtual = (indiceAtual + 1) % numProcessos;
            Processo candidato = processos.get(indiceAtual);

            if (candidato.estaPronto() && candidato.creditos > 0) {
                return candidato;
            }

            tentativas++;
        }

        return null;
    }

    private boolean todosProcessosSemCredito() {
        return processos.stream().noneMatch(p -> p.creditos > 0 && !p.bloqueado && !p.estaTerminado());
    }

    private void redistribuirCreditos() {
        for (Processo p : processos) {
            if (!p.estaTerminado()) {
                p.creditos = p.creditos / 2 + p.prioridade;
            }
        }
    }
}
