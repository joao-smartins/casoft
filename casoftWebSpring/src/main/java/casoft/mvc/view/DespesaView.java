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
    public ResponseEntity<Object> create(@RequestParam double valor, @RequestParam String data_venc, @RequestParam String data_lanc, @RequestParam double pagamento, @RequestParam String descricao, @RequestParam String status_conci, @RequestParam int tipoDespesa_id, @RequestParam int usuario_id, @RequestParam int evento_id,HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            Map<String,Object> novaDespesa;
            novaDespesa=controller.addDespesa(new Despesa(valor,data_venc,data_lanc,pagamento,descricao,status_conci,tipoDespesa_id,usuario_id,evento_id));
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
