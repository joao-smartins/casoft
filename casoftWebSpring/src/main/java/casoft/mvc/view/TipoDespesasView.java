package casoft.mvc.view;

import casoft.mvc.controller.TipoDespesasController;
import casoft.mvc.model.TipoDespesas;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/despesas")

public class TipoDespesasView
{

    @Autowired
    private TipoDespesasController tipoDespesasController;

    @GetMapping
    public ResponseEntity<Object> getDespesa()
    {
        List<Map<String,Object>> listDesp;
        listDesp = tipoDespesasController.getDespesa("");
        if(listDesp != null)
        {
            return ResponseEntity.ok().body(listDesp);
        }
        else
        {
            return ResponseEntity.badRequest().body(new Mensagem("Necessario cadastrar a despesa"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDespesaID(@PathVariable("id") int id)
    {
        Map<String, Object> despesa = tipoDespesasController.getDespesaID(id);

        if(despesa != null)
        {
            return ResponseEntity.ok().body(despesa);
        }
        else
        {
            return ResponseEntity.badRequest().body(new Mensagem("Despesa com ID: "+ id +" nao encontrada"));
        }
    }

    @PostMapping
    public ResponseEntity<Object> addDespesa(@RequestBody TipoDespesas tipodespesa)
    {
        Map<String,Object> json = tipoDespesasController.addTipoDespesa(tipodespesa);
        if(json.get("erro") == null)
        {
            return ResponseEntity.ok(new Mensagem(tipodespesa + "cadastrada com sucesso!"));
        }
        else
        {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @PutMapping
    public ResponseEntity<Object> updtDespesa(@RequestBody TipoDespesas tipodespesa)
    {
        Map<String,Object> json = tipoDespesasController.updateTipoDespesa(tipodespesa);
        if(json.get("erro") == null)
        {
            return ResponseEntity.ok(new Mensagem("Tipo de despesa"+ tipodespesa.getDescricao() + "alterada com sucesso!"));
        }
        else
        {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDespesa(@PathVariable("id") int id)
    {
        Map<String, Object> json = tipoDespesasController.deleteTipoDespesa(id);
        if(json.get("erro") == null)
        {
            return ResponseEntity.ok(new Mensagem("Tipo de despesa deletada com sucesso!"));
        }
        else
        {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

}
