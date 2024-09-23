public class Main {
    public static void main(String[] args) {
        Escalonador escalonador = new Escalonador();
        escalonador.adicionarProcesso(new Processo("A", 2, 5, 6, 1, 3));
        escalonador.adicionarProcesso(new Processo("B", 3, 10, 6, 2, 3));
        escalonador.adicionarProcesso(new Processo("C", 0, 0, 14, 3, 3));
        escalonador.adicionarProcesso(new Processo("D", 0, 0, 10, 4, 3));
        
        escalonador.executarEscalonamento();
    }
}
