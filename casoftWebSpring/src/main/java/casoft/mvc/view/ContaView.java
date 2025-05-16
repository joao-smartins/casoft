package casoft.mvc.view;


import casoft.mvc.controller.ContaController;
import casoft.mvc.model.Conta;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("apis/conta")
public class ContaView {

    @Autowired
    private ContaController contaController;

    @GetMapping()
    public List<Map<String, Object>> getContas() {
        return contaController.getContas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getConta(@PathVariable("id") int id) {
        Map<String,Object> json;
        json=contaController.getConta(id);
        if (json!=null)
            return ResponseEntity.ok(json);
        else
            return ResponseEntity.badRequest().body(new Mensagem("Necessário cadastrar a conta"));
    }
    @PostMapping
    public ResponseEntity<Object> addConta(@RequestBody Conta conta) {
        Map<String,Object> json =contaController.addConta(conta);
        if(json.get("erro")==null)
            return ResponseEntity.ok(new Mensagem("Conta do " + conta.getBanco()+" cadastrada com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> updtConta(@RequestParam("id_conta") int id_conta, @RequestBody Conta conta_atualizada) {
        Map<String,Object> json =contaController.updtConta(id_conta,conta_atualizada);
        if(json.get("erro")==null)
            return ResponseEntity.ok(new Mensagem("Conta do " + conta_atualizada.getBanco()+" alterada com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteConta(@PathVariable("id") int id) {
        Map<String, Object> json = contaController.deletarConta(id);

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Conta deletada com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

}