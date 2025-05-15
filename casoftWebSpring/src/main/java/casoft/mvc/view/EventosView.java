package casoft.mvc.view;

import casoft.mvc.controller.EventoController;
import casoft.mvc.model.Evento;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if(json!=null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem("ID não encontrado!"));
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
        if(json!=null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem("Não alterado!"));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Evento evento){
        Map<String,Object> json = eventoController.create(evento);
        if(json!=null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem("Evento não criado!"));
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Evento evento){
         if(eventoController.delete(evento.getId())){
             return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body(new Mensagem("Evento não deletado!"));
    }
}
