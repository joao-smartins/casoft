package casoft.mvc.view;

import casoft.mvc.controller.VoluntarioController;
import casoft.mvc.model.Voluntario;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("apis/voluntario")
public class VoluntarioView {
    @Autowired
    private VoluntarioController voluntarioController;

    @PostMapping("/cadastro")
    public ResponseEntity<Mensagem> cadastrar(@RequestBody Voluntario voluntario) {
        Map<String, Object> resultado = voluntarioController.create(voluntario);
        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
        }
        return ResponseEntity.ok(new Mensagem("Voluntário criado com sucesso"));
    }

}
