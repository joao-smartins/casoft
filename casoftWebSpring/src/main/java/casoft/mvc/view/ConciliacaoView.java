package casoft.mvc.view; // Certifique-se que o pacote 'view' é o correto para seus REST Controllers

import casoft.mvc.controller.ConciliacaoController;
import casoft.mvc.model.Conciliacao;
import casoft.mvc.util.Mensagem; // Certifique-se de que sua classe Mensagem está acessível
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin // Permite requisições de origens diferentes (do seu frontend)
@RestController // Marca como um controlador REST, como o TipoDespesasView
@RequestMapping("apis/conciliacao") // Define o caminho base para os endpoints
public class ConciliacaoView {

    @Autowired
    private ConciliacaoController conciliacaoController; // Injeta o Controller de negócio, como no TipoDespesasView

    // Endpoint para obter itens não conciliados
    @GetMapping("/nao-conciliados")
    public ResponseEntity<Object> getItensNaoConciliados() {
        List<Map<String, Object>> listItens = conciliacaoController.getItensNaoConciliados();
        if (listItens != null) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("ok", listItens); // Encapsula a lista em um Map com chave "ok"
            return ResponseEntity.ok().body(responseBody);
        } else {
            // Retorna um Map com a chave "erro" caso a lista seja nula
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensagem("Erro ao carregar itens para conciliação."));
        }
    }

    // Endpoint para registrar problemas de conciliação (lote)
    @PostMapping("/problemas")
    public ResponseEntity<Object> addConciliacaoProblemas(@RequestBody List<Conciliacao> conciliacoes) {
        Map<String, Object> json = conciliacaoController.addConciliacaoProblemas(conciliacoes);
        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    // Endpoint para marcar despesa como conciliada
    @PutMapping("/despesa/{id}/status")
    public ResponseEntity<Object> updateDespesaStatus(@PathVariable("id") int id) {
        // Não é necessário passar 'status' via RequestParam, já que o endpoint implica 'CONCILIADO'
        Map<String, Object> json = conciliacaoController.marcarDespesaConciliada(id);
        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    // Endpoint para marcar receita como conciliada
    @PutMapping("/receita/{id}/status")
    public ResponseEntity<Object> updateReceitaStatus(@PathVariable("id") int id) {
        // Não é necessário passar 'status' via RequestParam
        Map<String, Object> json = conciliacaoController.marcarReceitaConciliada(id);
        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    // Métodos CRUD para a tabela 'conciliacao' (se for necessário manipulá-la diretamente via API)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getConciliacaoID(@PathVariable("id") int id) {
        Map<String, Object> conciliacao = conciliacaoController.getConciliacaoID(id);
        if (conciliacao != null && conciliacao.get("erro") == null) {
            return ResponseEntity.ok().body(conciliacao);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(conciliacao != null ? conciliacao.get("erro").toString() : "Conciliação não encontrada ou erro desconhecido."));
        }
    }

    @PostMapping
    public ResponseEntity<Object> addConciliacao(@RequestBody Conciliacao conciliacao) {
        Map<String, Object> json = conciliacaoController.addConciliacao(conciliacao);
        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @PutMapping
    public ResponseEntity<Object> updtConciliacao(@RequestBody Conciliacao conciliacao) {
        Map<String, Object> json = conciliacaoController.updateConciliacao(conciliacao);
        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteConciliacao(@PathVariable("id") int id) {
        Map<String, Object> json = conciliacaoController.deleteConciliacao(id);
        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }
}
