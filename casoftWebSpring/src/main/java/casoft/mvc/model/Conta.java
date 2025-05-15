package casoft.mvc.model;

public class Conta {
    private String agencia;
    private String identificador;

    public Conta(String agencia, String identificador) {
        this.agencia = agencia;
        this.identificador = identificador;
    }
    public Conta(){}

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
