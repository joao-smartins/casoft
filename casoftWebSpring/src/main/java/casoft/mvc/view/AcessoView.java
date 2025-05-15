package casoft.mvc.view;

import casoft.mvc.controller.AcessoController;
import casoft.mvc.model.Usuario;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("autenticar")
public class AcessoView {
    @Autowired
    private AcessoController controller;

    @GetMapping("{login}/{senha}")
    public ResponseEntity<Object> autenticar(@PathVariable String login, @PathVariable String senha){
        String token=controller.autenticar(login, senha);
        if(token!=null){
            String nivel=controller.getNivel(login);
            Map<String,Object> json= new HashMap<>();
            json.put("token",token);
            json.put("nivel",nivel);
            if(json.get("erro")==null){
                System.out.println("Token gerado: "+json.get("token").toString());
                System.out.println("Nivel: "+json.get("nivel").toString());
                return ResponseEntity.ok(json);
            }
            else
                return ResponseEntity.badRequest().body(new Mensagem("Usuário não cadastrado"));
        }
        else
            return ResponseEntity.badRequest().body(new Mensagem("Usuário não cadastrado"));
    }
}
