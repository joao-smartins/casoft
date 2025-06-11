package casoft.mvc.view;

import casoft.mvc.controller.EventoController;
import casoft.mvc.model.Evento;
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

    @GetMapping("/{id}/voluntario")
    public ResponseEntity<Object> createIdVolu(@PathVariable int id){
        List<Map<String,Object>> json = eventoController.getIdVolu(id);
        if(!json.isEmpty()){
                return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem("Voluntario não deletado!"));
    }

    @PostMapping("/{id}/voluntario/{voluntarioId}")
    public ResponseEntity<Object> putIdVolu(@PathVariable int id, @PathVariable int voluntarioId){
        boolean mensagem = eventoController.createIdVolu(id, voluntarioId);
        if(mensagem == true){
            return ResponseEntity.ok().body(mensagem);
        }
        return ResponseEntity.badRequest().body(new Mensagem("Voluntario não adicionado!"));
    }

    @DeleteMapping("/{id}/voluntario/{voluntarioId}")
    public ResponseEntity<Object> deleteIdVolu(@PathVariable int id, @PathVariable int voluntarioId){
        boolean mensagem = eventoController.deleteIdVolu(id, voluntarioId);
        if(mensagem == true){
            return ResponseEntity.ok().body(mensagem);
        }
        return ResponseEntity.badRequest().body(new Mensagem("Voluntario não adicionado!"));
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
    public ResponseEntity<Object> update(@RequestBody Evento evento){
        Map<String, Object> json = eventoController.update(evento);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Evento evento){
        Map<String,Object> json = eventoController.create(evento);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(json);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id){
        Map<String,Object> json = new HashMap<>();
         if(eventoController.delete(id)){
             return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body(json.put("erro","Tipo de Receita não deletado"));
    }
}
