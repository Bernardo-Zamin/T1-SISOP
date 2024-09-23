import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Lista de processos para teste
        List<Processo> processos = new ArrayList<>();

        // Processo A: Surto de 2ms, Tempo de E/S 5ms, Tempo total 6ms, Ordem 1, Prioridade 3
        processos.add(new Processo("A", 2, 5, 6, 1, 3));

        // Processo B: Surto de 3ms, Tempo de E/S 10ms, Tempo total 6ms, Ordem 2, Prioridade 3
        processos.add(new Processo("B", 3, 10, 6, 2, 3));

        // Processo C: Processo CPU bound, Tempo total 14ms, Ordem 3, Prioridade 3
        processos.add(new Processo("C", 0, 0, 14, 3, 3));

        // Processo D: Processo CPU bound, Tempo total 10ms, Ordem 4, Prioridade 3
        processos.add(new Processo("D", 0, 0, 10, 4, 3));

        // Inicializa o escalonador com a lista de processos
        Escalonador escalonador = new Escalonador(processos);
        escalonador.executar();
    }
}
