public class Processo {
    String nome;
    int tempoExecutando;
    int surtoCPU;
    int tempoES;
    int tempoTotalCPU;
    int ordem;
    int prioridade;
    int creditos;
    int tempoBloqueado;
    boolean bloqueado;
    boolean terminou;

    public Processo(String nome, int surtoCPU, int tempoES, int tempoTotalCPU, int ordem, int prioridade) {
        this.nome = nome;
        this.surtoCPU = surtoCPU;
        this.tempoES = tempoES;
        this.tempoTotalCPU = tempoTotalCPU;
        this.tempoExecutando = 0; // Inicializa em 0, já que o processo ainda não começou a execução
        this.ordem = ordem;
        this.prioridade = prioridade;
        this.creditos = prioridade; 
        this.tempoBloqueado = 0; // Inicializa com 0, bloqueio ocorrerá após a execução de um surto
        this.bloqueado = false;
        this.terminou = false;
    }

    // Executa o processo, decrementando créditos e tempo de CPU
    public void executar() {
        if (creditos > 0 && !bloqueado) {
            creditos--;
            tempoTotalCPU--;
            tempoExecutando++;
        }
    }

    // Bloqueia o processo após o término do surto de CPU ou se já atingiu o tempo de ES
    public void bloquear() {
        if (tempoExecutando >= surtoCPU && !bloqueado && tempoES > 0) {
            bloqueado = true;
            tempoBloqueado = tempoES;  
            System.out.println(nome + " está bloqueado por " + tempoBloqueado + " unidades de tempo.");
            tempoExecutando = 0; 
        }
    }

    // Desbloqueia o processo após o tempo de ES ter passado
    public void desbloquear() {
            bloqueado = false;

        
    }

    // Verifica se o processo está pronto para executar
    public boolean estaPronto() {
        return !bloqueado && tempoTotalCPU > 0;
    }

    // Verifica se o processo terminou a execução
    public boolean estaTerminado() {
        return tempoTotalCPU <= 0;
    }

    // Atualiza o estado do processo quando ele termina
    public void atualizarEstado() {
        if (tempoTotalCPU <= 0) {
            terminou = true;
        }
    }

    // Verifica se o processo está bloqueado
    public boolean estaBloqueado() {
        return bloqueado;
    }

    // Imprime o estado atual do processo
    public void imprimirEstado(boolean isRunning) {
        if (isRunning) {
            System.out.println(nome + " - " + creditos + " créditos - Running");
        } else if (bloqueado) {
            System.out.println(nome + " - " + creditos + " créditos - Blocked");
        } else if (terminou) {
            System.out.println(nome + " - " + creditos + " créditos - Exit");
        } else {
            System.out.println(nome + " - " + creditos + " créditos - Ready");
        }
    }
}