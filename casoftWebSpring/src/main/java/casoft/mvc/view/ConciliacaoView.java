    package casoft.mvc.view;

    import casoft.mvc.controller.ConciliacaoController;
    import casoft.mvc.model.Conciliacao;
    import casoft.mvc.util.Mensagem;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDate;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @CrossOrigin
    @RestController
    @RequestMapping("apis/conciliacao")
    public class ConciliacaoView
    {

        @Autowired
        private ConciliacaoController conciliacaoController;

        @GetMapping("/nao-conciliados")
        public ResponseEntity<Object> getItensNaoConciliados() {
            List<Map<String, Object>> listItens = conciliacaoController.getItensNaoConciliados();
            if (listItens != null) {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("ok", listItens);
                return ResponseEntity.ok().body(responseBody);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Mensagem("Erro ao carregar itens para conciliação."));
            }
        }

        @PostMapping("/problemas")
        public ResponseEntity<Object> addConciliacaoProblemas(@RequestBody List<Conciliacao> conciliacoes)
        {
            Map<String, Object> json = conciliacaoController.addConciliacaoProblemas(conciliacoes);
            if (json.get("erro") == null)
            {
                return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
            } else
            {
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
            }
        }

        @PutMapping("/despesa/{id}/status")
        public ResponseEntity<Object> updateDespesaStatus(@PathVariable("id") int id)
        {
            Map<String, Object> json = conciliacaoController.marcarDespesaConciliada(id);
            if (json.get("erro") == null)
            {
                return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
            } else
            {
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
            }
        }

        @PutMapping("/receita/{id}/status")
        public ResponseEntity<Object> updateReceitaStatus(@PathVariable("id") int id)
        {
            Map<String, Object> json = conciliacaoController.marcarReceitaConciliada(id);
            if (json.get("erro") == null) {
                return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
            } else {
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
            }
        }

        @GetMapping("/{id}")
        public ResponseEntity<Object> getConciliacaoID(@PathVariable("id") int id) {
            Map<String, Object> conciliacao = conciliacaoController.getConciliacaoID(id);
            if (conciliacao != null && conciliacao.get("erro") == null) {
                return ResponseEntity.ok().body(conciliacao);
            } else {
                return ResponseEntity.badRequest().body(new Mensagem(conciliacao != null ? conciliacao.get("erro").toString() : "Conciliação não encontrada ou erro desconhecido."));
            }
        }

        @PostMapping
        public ResponseEntity<Object> addConciliacao(@RequestBody Conciliacao conciliacao) {
            Map<String, Object> json = conciliacaoController.addConciliacao(conciliacao);
            if (json.get("erro") == null) {
                return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
            } else {
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
            }
        }

        @PutMapping
        public ResponseEntity<Object> updtConciliacao(@RequestBody Conciliacao conciliacao) {
            Map<String, Object> json = conciliacaoController.updateConciliacao(conciliacao);
            if (json.get("erro") == null) {
                return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
            } else {
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Object> deleteConciliacao(@PathVariable("id") int id) {
            Map<String, Object> json = conciliacaoController.deleteConciliacao(id);
            if (json.get("erro") == null) {
                return ResponseEntity.ok(new Mensagem(json.get("ok").toString()));
            } else {
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
            }
        }
    }
