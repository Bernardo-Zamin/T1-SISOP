
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
        this.tempoExecutando = 0;
        this.ordem = ordem;
        this.prioridade = prioridade;
        this.creditos = prioridade;
        this.tempoBloqueado = 0;
        this.bloqueado = false;
        this.terminou = false;
    }

    public void executar() {
        if (creditos > 0 && !bloqueado) {
            creditos--;
            tempoTotalCPU--;
            tempoExecutando++;
        }
    }

    public void bloquear() {
        if (tempoExecutando >= surtoCPU && !bloqueado && tempoES > 0) {
            bloqueado = true;
            tempoBloqueado = tempoES;
            System.out.println(nome + " está bloqueado por " + tempoBloqueado + " unidades de tempo.");
            tempoExecutando = 0;
        }
    }

    public void desbloquear() {
        bloqueado = false;

    }

    public boolean estaPronto() {
        return !bloqueado && tempoTotalCPU > 0;
    }

    public boolean estaTerminado() {
        return tempoTotalCPU <= 0;
    }

    public void atualizarEstado() {
        if (tempoTotalCPU <= 0) {
            terminou = true;
        }
    }

    public boolean estaBloqueado() {
        return bloqueado;
    }

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