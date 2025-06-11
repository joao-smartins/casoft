package casoft.mvc.service;

import casoft.mvc.util.Singleton;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private casoft.mvc.dto.EmailDTO emailDTO;

    public void enviarEmailMultiplo(List<String> destinatarios, String assunto, String mensagem) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Singleton conexao = Singleton.getInstancia();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        // Define todos os destinatários
        String[] destinatariosArray = destinatarios.toArray(new String[0]);
        if(conexao.conectar()) {
            for (String destinatario : destinatariosArray) {
                emailDTO.gravar(destinatario, assunto, mensagem, conexao);
            }
        }
        helper.setTo(destinatariosArray);

        helper.setSubject(assunto);
        helper.setText(mensagem, true); // true para HTML, false para texto simples

        mailSender.send(mimeMessage);
    }
}
