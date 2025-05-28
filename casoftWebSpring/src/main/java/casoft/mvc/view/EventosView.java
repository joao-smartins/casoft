package casoft.mvc.view;

import casoft.mvc.controller.EventoController;
import casoft.mvc.model.Eventos;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/eventos")
public class EventosView {

    @Autowired
    private EventoController eventoController;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getId(@PathVariable int id){
        Map<String,Object> json = eventoController.getId(id);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<Object> getAll(){
        List<Map<String,Object>> listJson = eventoController.getAll("");
        if(!listJson.isEmpty()){
            return ResponseEntity.ok().body(listJson);
        }
        return ResponseEntity.badRequest().body(new Mensagem("Não há eventos registrados!"));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Eventos eventos){
        Map<String, Object> json = eventoController.update(eventos);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Eventos eventos){
        Map<String,Object> json = eventoController.create(eventos);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(json);
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Eventos eventos){
        Map<String,Object> json = new HashMap<>();
         if(eventoController.delete(eventos.getId())){
             return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body(json.put("erro","Tipo de Receita não deletado"));
    }
}
