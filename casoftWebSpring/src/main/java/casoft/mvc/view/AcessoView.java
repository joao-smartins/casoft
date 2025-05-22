package casoft.mvc.view;

import casoft.mvc.controller.AcessoController;
import casoft.mvc.util.JWTTokenProvider;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AcessoView {
    @Autowired
    private AcessoController controller;

    @GetMapping("{login}/{senha}")
    public ResponseEntity<Object> autenticar(@PathVariable String login, @PathVariable String senha){
        String token=controller.autenticar(login, senha);
        if(token!=null){
            Map<String,Object> json=controller.getUsuario(login,token);
            if(json.get("erro")==null){
                return ResponseEntity.ok(json);
            }
            else
                return ResponseEntity.badRequest().body(new Mensagem("Usuário não cadastrado"));
        }
        else
            return ResponseEntity.badRequest().body(new Mensagem("Usuário não cadastrado"));
    }
    @GetMapping("/verificar-token")
    public ResponseEntity<?> verificarToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            boolean valido = JWTTokenProvider.verifyToken(token);
            if (valido) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(401).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
