package casoft.mvc.view;

import casoft.mvc.controller.DespesaController;
import casoft.mvc.model.Despesa;
import casoft.mvc.util.JWTTokenProvider;
import casoft.mvc.util.Mensagem;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("apis/despesa")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DespesaView {
    @Autowired
    private DespesaController controller;

    @PostMapping
    public ResponseEntity<Object> addDespesa(@RequestBody Despesa despesa, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            Map<String,Object> novaDespesa;
            novaDespesa=controller.addDespesa(despesa);
            if (novaDespesa!=null)
                return ResponseEntity.ok(novaDespesa);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao Cadastrar"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuario nao autenticado"));
    }

    @GetMapping
    public ResponseEntity<Object> getTipoDespesas(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            List<Map<String,Object>> tipoDespesas;
            tipoDespesas=controller.getTipoDespesa();
            if (!tipoDespesas.isEmpty())
                return ResponseEntity.ok(tipoDespesas);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Nenhum Tipo Despesa cadastrada"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuario nao autenticado"));
    }

}
