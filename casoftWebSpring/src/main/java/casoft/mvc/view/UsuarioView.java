package casoft.mvc.view;


import casoft.mvc.controller.UsuarioController;
import casoft.mvc.model.Usuario;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioView {

    @Autowired
    private UsuarioController usuarioController;

    @PostMapping("/cadastro")
    public ResponseEntity<Mensagem> cadastrar(@RequestBody Usuario usuario) {
        Map<String, Object> resultado = usuarioController.cadastrarUsuario(usuario);
        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
        }
        return ResponseEntity.ok(new Mensagem("Usuário criado com sucesso"));
    }

    @PutMapping("/{id}/desativar")
    public ResponseEntity<Mensagem> desativar(@PathVariable int id) {
        Map<String, Object> resultado = usuarioController.desativarUsuario(id);

        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest()
                    .body(new Mensagem(resultado.get("erro").toString()));
        }

        return ResponseEntity.ok()
                .body(new Mensagem(resultado.get("mensagem").toString()));
    }
}