import java.util.*;

public class Escalonador {
    private List<Processo> listaProcessos;
    private Queue<Processo> filaProntos;
    private int tempo;

    public Escalonador(List<Processo> processos) {
        this.listaProcessos = processos;
        this.filaProntos = new LinkedList<>();
        this.tempo = 0;
        inicializarFilaProntos();
    }

    private void inicializarFilaProntos() {
        for (Processo p : listaProcessos) {
            if (p.getEstado().equals("Ready")) {
                filaProntos.add(p);
            }
        }
    }

    public void executar() {
        while (!todosProcessosFinalizados()) {
            Processo processoAtual = selecionarProcesso();

            if (processoAtual == null) {
                System.out.println("Nenhum processo pronto ou com créditos disponíveis. Encerrando a execução.");
                break;  // Encerra o loop se nenhum processo estiver pronto
            }

            System.out.println("Tempo: " + tempo);
            System.out.println("Processo selecionado: " + processoAtual.getNome());
            
            executarProcesso(processoAtual);

            // Imprimir o estado atual de todos os processos
            imprimirEstados();

            tempo++;
        }

        System.out.println("Todos os processos foram finalizados ou o sistema não tem mais processos prontos.");
    }

    private Processo selecionarProcesso() {
        return filaProntos.stream()
                .filter(p -> p.getCreditos() > 0 && p.getEstado().equals("Ready"))
                .max(Comparator.comparing(Processo::getCreditos).thenComparing(Processo::getOrdem))
                .orElse(null);
    }

    private void executarProcesso(Processo processo) {
        // Verifica se o processo tem operações de E/S (surto de CPU > 0)
        if (processo.getSurtoCPU() > 0 && processo.getSurtoCPU() > processo.getTempoExecutado()) {
            processo.incrementarTempoExecutado();
            processo.setCreditos(processo.getCreditos() - 1);
            System.out.println(processo.getNome() + " - " + processo.getCreditos() + " créditos - Running");
    
            if (processo.getSurtoCPU() == processo.getTempoExecutado()) {
                // Bloqueia o processo se ele atingir o surto de CPU e possui E/S
                processo.setEstado("Blocked");
                processo.incrementarTempoBloqueado();
                System.out.println(processo.getNome() + " está bloqueado por " + processo.getTempoIO() + " unidades de tempo.");
            }
        } else if (processo.getSurtoCPU() == 0) {
            // Processo CPU-bound (sem E/S)
            processo.incrementarTempoExecutado();
            processo.setCreditos(processo.getCreditos() - 1);
            System.out.println(processo.getNome() + " - " + processo.getCreditos() + " créditos - Running");
    
            // Se o processo atingiu o tempo total de CPU, finalize-o
            if (processo.isTerminado()) {
                processo.setEstado("Exit");
                System.out.println("Processo " + processo.getNome() + " terminou.");
            }
        }
    
        // Se o processo atingiu o tempo total de CPU, finalize-o
        if (processo.isTerminado() && processo.getEstado().equals("Ready")) {
            processo.setEstado("Exit");
            System.out.println("Processo " + processo.getNome() + " terminou.");
        }
    }
    

    private void imprimirEstados() {
        for (Processo p : listaProcessos) {
            if (!p.getEstado().equals("Exit")) {
                System.out.println(p.getNome() + " - " + p.getCreditos() + " créditos - " + p.getEstado());
            } else {
                System.out.println(p.getNome() + " - " + p.getCreditos() + " créditos - Exit");
            }
        }
        System.out.println();
    }

    private boolean todosProcessosFinalizados() {
        return listaProcessos.stream().allMatch(Processo::isTerminado);
    }
}
