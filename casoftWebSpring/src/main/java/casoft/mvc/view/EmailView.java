package casoft.mvc.view;

import casoft.mvc.model.EmailDTO;
import casoft.mvc.controller.EmailService;
import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis/email")
@CrossOrigin(origins = "*") // ajuste conforme necessário para CORS
public class EmailView{

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<String> enviarEmail(@RequestBody EmailDTO emailDTO) {
        try {
            emailService.enviarEmailMultiplo(
                    emailDTO.getDestinatarios(),
                    emailDTO.getAssunto(),
                    emailDTO.getMensagem()
            );
            return ResponseEntity.ok("E-mails enviados com sucesso!");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao enviar e-mails: " + e.getMessage());
        }
    }
}
