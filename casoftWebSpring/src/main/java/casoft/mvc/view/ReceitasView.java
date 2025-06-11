package casoft.mvc.view;

import casoft.mvc.controller.ReceitasController;
import casoft.mvc.model.Receitas;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("apis/receitas")
public class ReceitasView {

    @Autowired
    private ReceitasController receitasController;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        try {
            List<Map<String, Object>> lista = receitasController.getAll("");
            if (!lista.isEmpty() && !lista.get(0).containsKey("erro")) {
                return ResponseEntity.ok().body(lista);
            }
            return ResponseEntity.badRequest().body(new Mensagem(lista.isEmpty() ? "Nenhuma receita encontrada" : lista.get(0).get("erro").toString()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Mensagem("Erro interno: " + e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        Map<String, Object> json = receitasController.get(id);
        if (json.get("erro") == null) {
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Receitas receita) {
        Map<String, Object> json = receitasController.create(receita);
        if (json.get("erro") == null) {
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Receitas receita) {
        Map<String, Object> json = receitasController.update(receita);
        if (json.get("erro") == null) {
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}