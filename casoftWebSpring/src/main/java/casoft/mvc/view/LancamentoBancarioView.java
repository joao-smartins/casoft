package casoft.mvc.view;

import casoft.mvc.controller.LancamentoBancarioController;
import casoft.mvc.model.LancamentoBancario;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/lancamento-bancario")
public class LancamentoBancarioView {
    @Autowired
    private LancamentoBancarioController lancamentoController;

    @GetMapping()
    public List<Map<String, Object>> getLancamentos() {
        return lancamentoController.getLancamentos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLancamento(@PathVariable("id") int id) {
        Map<String, Object> json;
        json = lancamentoController.getLancamento(id);
        if (json != null)
            return ResponseEntity.ok(json);
        else
            return ResponseEntity.badRequest().body(new Mensagem("Necessário cadastrar o lançamento"));
    }

    @PostMapping
    public ResponseEntity<Object> addLancamento(@RequestBody LancamentoBancario lancamento) {
        Map<String, Object> json = lancamentoController.addLancamento(lancamento);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Lançamento cadastrado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updtLancamento(@PathVariable("id") int id, @RequestBody LancamentoBancario lancamento_atualizado) {
        Map<String, Object> json = lancamentoController.updtLancamento(id, lancamento_atualizado);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Lançamento alterado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLancamento(@PathVariable("id") int id) {
        Map<String, Object> json = lancamentoController.deletarLancamento(id);

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Lançamento deletado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}
