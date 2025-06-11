package casoft.mvc.view;

import casoft.mvc.controller.MovimentacaoBancariaController;

import casoft.mvc.model.MovimentacaoBancaria;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/movimentacao")
public class MovimentacaoBancariaView {
    @Autowired
    private MovimentacaoBancariaController movimentacaoController;

    @GetMapping()
    public List<Map<String, Object>> getMovimentacoes() {
        return movimentacaoController.getMovimentacoes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMovimentacao(@PathVariable("id") int id) {
        Map<String, Object> json;
        json = movimentacaoController.getMovimentacao(id);
        if (json != null)
            return ResponseEntity.ok(json);
        else
            return ResponseEntity.badRequest().body(new Mensagem("Necessário cadastrar a movimentação"));
    }

    @PostMapping
    public ResponseEntity<Object> addMovimentacao(@RequestBody MovimentacaoBancaria movimentacao) {
        Map<String, Object> json = movimentacaoController.addMovimentacao(movimentacao);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Movimentação cadastrada com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Object> updtMovimentacao(@PathVariable("id") int id, @RequestBody MovimentacaoBancaria movimentacao_atualizada) {
        Map<String, Object> json = movimentacaoController.updtMovimentacao(id, movimentacao_atualizada);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Movimentação alterada com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovimentacao(@PathVariable("id") int id) {
        Map<String, Object> json = movimentacaoController.deletarMovimentacao(id);

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Movimentação deletada com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }


}
