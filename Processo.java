public class Processo {
    private String nome;
    private int surtoCPU;
    private int tempoIO;
    private int tempoTotalCPU;
    private int ordem;
    private int prioridade;
    private int creditos;
    private String estado;
    private int tempoExecutado;
    private int tempoBloqueado;

    public Processo(String nome, int surtoCPU, int tempoIO, int tempoTotalCPU, int ordem, int prioridade) {
        this.nome = nome;
        this.surtoCPU = surtoCPU;
        this.tempoIO = tempoIO;
        this.tempoTotalCPU = tempoTotalCPU;
        this.ordem = ordem;
        this.prioridade = prioridade;
        this.creditos = prioridade;
        this.estado = "Ready";  // Inicialmente o processo está pronto
        this.tempoExecutado = 0;
        this.tempoBloqueado = 0;
    }

    // Getters e Setters para acessar os atributos
    public String getNome() {
        return nome;
    }

    public int getSurtoCPU() {
        return surtoCPU;
    }

    public int getTempoIO() {
        return tempoIO;
    }

    public int getTempoTotalCPU() {
        return tempoTotalCPU;
    }

    public int getOrdem() {
        return ordem;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void incrementarTempoExecutado() {
        tempoExecutado++;
    }

    public int getTempoExecutado() {
        return tempoExecutado;
    }

    public void incrementarTempoBloqueado() {
        tempoBloqueado++;
    }

    public int getTempoBloqueado(){
        return tempoBloqueado;
    }

    public boolean isTerminado() {
        return tempoExecutado >= tempoTotalCPU;
    }
}
