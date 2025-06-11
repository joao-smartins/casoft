package casoft.mvc.view;

import casoft.mvc.controller.ReceitasController;
import casoft.mvc.util.JWTTokenProvider;
import casoft.mvc.util.Mensagem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin()

@RequestMapping("apis/receita")
public class ReceitasView {
    @Autowired
    private ReceitasController controller;

    @PostMapping("/cadastroReceita")
    public ResponseEntity<Object> create(
            @RequestParam double valor,
            @RequestParam boolean futura,
            @RequestParam String descricao,
            @RequestParam int eventoId,
            @RequestParam int categoriaId,
            @RequestParam String dataVencimento,
            @RequestParam boolean quitada,
            @RequestParam String statusConciliacao,
            @RequestParam int pagamento,
            @RequestParam int usuarioId,
            HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if(JWTTokenProvider.verifyToken(token)) {
            Map<String, Object> json = controller.addReceita(
                    valor, futura, descricao, eventoId, categoriaId,
                    dataVencimento, quitada, statusConciliacao, pagamento, usuarioId);

            if (json != null) {
                return ResponseEntity.ok(json);
            } else {
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao Cadastrar"));
            }
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuário não autenticado"));
    }

    @GetMapping
    public ResponseEntity<Object> getAll(HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        System.out.println("Token recebido: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Token inválido ou expirado");
            return ResponseEntity.status(401).body(new Mensagem("Token inválido ou expirado"));
        }

        // Remove "Bearer " e quaisquer espaços extras
        String token = authHeader.substring(7).trim();

        if(!JWTTokenProvider.verifyToken(token)) {
            System.out.println("Token inválido ou expirado");
            return ResponseEntity.status(401).body(new Mensagem("Token inválido ou expirado"));
        }

        List<Map<String, Object>> jsonList = controller.getAll();
        if (!jsonList.isEmpty()) {
            return ResponseEntity.ok(jsonList);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem("Nenhuma receita encontrada"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if(JWTTokenProvider.verifyToken(token)) {
            Map<String, Object> json = controller.delete(id);
            if (json != null) {
                return ResponseEntity.ok(json);
            } else {
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao remover"));
            }
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuário não autenticado"));
    }

}