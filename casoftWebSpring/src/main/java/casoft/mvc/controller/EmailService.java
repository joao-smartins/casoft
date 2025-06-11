package casoft.mvc.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailMultiplo(List<String> destinatarios, String assunto, String mensagem) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        // Define todos os destinatários
        String[] destinatariosArray = destinatarios.toArray(new String[0]);
        helper.setTo(destinatariosArray);

        helper.setSubject(assunto);
        helper.setText(mensagem, true); // true para HTML, false para texto simples

        mailSender.send(mimeMessage);
    }
}
