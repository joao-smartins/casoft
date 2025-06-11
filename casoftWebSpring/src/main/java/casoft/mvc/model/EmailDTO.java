package casoft.mvc.dto;

import casoft.mvc.dao.EmailDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class EmailDTO {
    private List<String> destinatarios;
    private String assunto;
    private String mensagem;

    @Autowired
    EmailDAO emailDAO;
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
    public void gravar(String destinatario, String assunto, String mensagem, Singleton conexao) {
        emailDAO.gravar(destinatario,assunto,mensagem,conexao);
    }
}
