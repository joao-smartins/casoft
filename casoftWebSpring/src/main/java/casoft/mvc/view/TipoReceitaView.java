package casoft.mvc.view;

import casoft.mvc.controller.TipoReceitasController;
import casoft.mvc.model.TipoReceitas;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("apis/tiporeceita")
public class TipoReceitaView {

    @Autowired
    private TipoReceitasController tipoReceitasController;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getId(@PathVariable int id){
        Map<String,Object> json = tipoReceitasController.getId(id);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<Object> getAll(){
        List<Map<String,Object>> listJson = tipoReceitasController.getAll("");
        if(listJson.getFirst().get("erro")==null){
            return ResponseEntity.ok().body(listJson);
        }
        return ResponseEntity.badRequest().body(new Mensagem(listJson.getFirst().get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody TipoReceitas tipoReceitas){
        Map<String, Object> json = tipoReceitasController.update(tipoReceitas);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TipoReceitas tipoReceitas){
        Map<String,Object> json = tipoReceitasController.create(tipoReceitas);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody TipoReceitas tipoReceitas){
        if(tipoReceitasController.delete(tipoReceitas.getId())){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body(new Mensagem("Evento não deletado!"));
    }
}
