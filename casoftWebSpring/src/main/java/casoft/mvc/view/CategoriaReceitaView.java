package casoft.mvc.view;

import casoft.mvc.controller.CategoriaReceitaController;
import casoft.mvc.model.CategoriaReceita;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("apis/tiporeceita")
public class CategoriaReceitaView {

    @Autowired
    private CategoriaReceitaController categoriaReceitaController;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getId(@PathVariable int id){
        Map<String,Object> json = categoriaReceitaController.getId(id);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<Object> getAll(){
        List<Map<String,Object>> listJson = categoriaReceitaController.getAll("");
        if(listJson.getFirst().get("erro")==null){
            return ResponseEntity.ok().body(listJson);
        }
        return ResponseEntity.badRequest().body(new Mensagem(listJson.getFirst().get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody CategoriaReceita categoriaReceita){
        Map<String, Object> json = categoriaReceitaController.update(categoriaReceita);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CategoriaReceita categoriaReceita){
        Map<String,Object> json = categoriaReceitaController.create(categoriaReceita);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id){
        if(categoriaReceitaController.delete(id)){
            return ResponseEntity.ok().body(new Mensagem("Categoria receita com sucesso"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Falha ao deletar!"));
    }
}
