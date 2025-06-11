package casoft.mvc.dto;

import java.util.List;

public class EmailDTO {
    private List<String> destinatarios;
    private String assunto;
    private String mensagem;

    public List<String> getDestinatarios() {
        return destinatarios;
    }
    public void setDestinatarios(List<String> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getAssunto() {
        return assunto;
    }
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
