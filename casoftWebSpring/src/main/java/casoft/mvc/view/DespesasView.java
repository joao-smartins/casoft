package casoft.mvc.view;

import casoft.mvc.controller.DespesasController;
import casoft.mvc.util.JWTTokenProvider;
import casoft.mvc.util.Mensagem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("apis/despesa")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DespesasView {
    @Autowired
    private DespesasController controller;

    @PostMapping
    public ResponseEntity<Object> create(@RequestParam String valor, @RequestParam String data_venc, @RequestParam String data_lanc, @RequestParam String pagamento, @RequestParam String descricao, @RequestParam String status_conci, @RequestParam String tipoDespesa_id, @RequestParam String usuario_id, @RequestParam String evento_id,HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            Map<String,Object> json;
            json=controller.addDespesa(valor,data_venc,data_lanc,pagamento,descricao,status_conci,tipoDespesa_id,usuario_id,evento_id);
            if (json!=null)
                return ResponseEntity.ok(json);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao Cadastrar"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuario nao autenticado"));
    }
    @GetMapping
    public ResponseEntity<Object> getAll(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            List<Map<String,Object>> jsonlist;
            jsonlist=controller.getAll();
            if (!jsonlist.isEmpty())
                return ResponseEntity.ok(jsonlist);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao Cadastrar"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuario nao autenticado"));
    }
    @GetMapping("/aguardando")
    public ResponseEntity<Object> getAguardando(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            List<Map<String,Object>> jsonlist;
            jsonlist=controller.getAguardando();
            if (!jsonlist.isEmpty())
                return ResponseEntity.ok(jsonlist);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao Cadastrar"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuario nao autenticado"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id,  HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            Map<String,Object> json;
            json=controller.delete(id);
            if (json!=null)
                return ResponseEntity.ok(json);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao Cadastrar"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuario nao autenticado"));
    }
    @PutMapping
    public ResponseEntity<Object> uptde(@RequestParam int id,@RequestParam String valor, @RequestParam String data_venc, @RequestParam String data_lanc, @RequestParam String pagamento, @RequestParam String descricao, @RequestParam String status_conci, @RequestParam String tipoDespesa_id, @RequestParam String usuario_id, @RequestParam String evento_id,HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            Map<String,Object> json;
            json=controller.uptDespesa(id,valor,data_venc,data_lanc,pagamento,descricao,status_conci,tipoDespesa_id,usuario_id,evento_id);
            if (json!=null)
                return ResponseEntity.ok(json);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao Cadastrar"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuario nao autenticado"));
    }
}
