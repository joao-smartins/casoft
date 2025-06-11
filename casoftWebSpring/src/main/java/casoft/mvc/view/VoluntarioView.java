package casoft.mvc.view;

import casoft.mvc.controller.VoluntarioController;
import casoft.mvc.model.CategoriaReceita;
import casoft.mvc.model.Voluntario;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("apis/voluntario")
public class VoluntarioView {
    @Autowired
    private VoluntarioController voluntarioController;

    @PostMapping
    public ResponseEntity<Mensagem> cadastrar(@RequestBody Voluntario voluntario) {
        Map<String, Object> resultado = voluntarioController.create(voluntario);
        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
        }
        return ResponseEntity.ok(new Mensagem("Voluntário criado com sucesso"));
    }

    @GetMapping
    public ResponseEntity<Object> getAll(){
        List<Map<String,Object>> listJson = voluntarioController.getAll("");
        if(!listJson.isEmpty()){
            return ResponseEntity.ok().body(listJson);
        }
        return ResponseEntity.badRequest().body(new Mensagem("Não há voluntários registrados!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getId(@PathVariable("id") int id){
        Map<String,Object> listJson = voluntarioController.getId(id);
        if(!listJson.isEmpty()){
            return ResponseEntity.ok().body(listJson);
        }
        return ResponseEntity.badRequest().body(new Mensagem("Não há voluntários registrados!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id){
        if(voluntarioController.delete(id)){
            return ResponseEntity.ok().body(new Mensagem("Voluntário deletado com sucesso"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Falha ao deletar!"));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Voluntario voluntario){
        Map<String, Object> json = voluntarioController.update(voluntario);
        if(json.get("erro")==null){
            return ResponseEntity.ok().body(json);
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}
