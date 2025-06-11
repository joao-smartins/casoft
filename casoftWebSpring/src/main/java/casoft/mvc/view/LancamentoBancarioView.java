package casoft.mvc.view;

import casoft.mvc.controller.DespesasController;
import casoft.mvc.controller.LancamentoBancarioController;
import casoft.mvc.controller.MovimentacaoBancariaController;
import casoft.mvc.controller.ReceitasController;
import casoft.mvc.model.LancamentoBancario;
import casoft.mvc.model.MovimentacaoBancaria;
import casoft.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/lancamento-bancario")
public class LancamentoBancarioView {
    @Autowired
    private LancamentoBancarioController lancamentoController;

    @Autowired
    private MovimentacaoBancariaController movimentacaoController;

    @Autowired
    private ReceitasController receitasController;

    @Autowired
    private DespesasController despesasController;

    @GetMapping()
    public List<Map<String, Object>> getLancamentos() {
        return lancamentoController.getLancamentos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLancamento(@PathVariable("id") int id) {
        Map<String, Object> json;
        json = lancamentoController.getLancamento(id);
        if (json != null)
            return ResponseEntity.ok(json);
        else
            return ResponseEntity.badRequest().body(new Mensagem("Necessário cadastrar o lançamento"));
    }

    @PostMapping
    public ResponseEntity<Object> addLancamento(@RequestBody LancamentoBancario lancamento) {
        Map<String, Object> json = lancamentoController.addLancamento(lancamento);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Lançamento cadastrado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updtLancamento(@PathVariable("id") int id, @RequestBody LancamentoBancario lancamento_atualizado) {
        Map<String, Object> json = lancamentoController.updtLancamento(id, lancamento_atualizado);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Lançamento alterado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLancamento(@PathVariable("id") int id) {
        System.out.println("Deletando lançamento com ID: " + id);
        Map<String, Object> json = lancamentoController.deletarLancamento(id);

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Lançamento deletado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping("/recalcular-total/{movimentacaoBancariaId}")
    public ResponseEntity<Object> recalcularTotalMovimentacao(@PathVariable("movimentacaoBancariaId") int movimentacaoBancariaId) {
        try {
            // Busca todos os lançamentos bancários relacionados à movimentação

            List<Map<String, Object>> lancamentos =  lancamentoController.getLancamentosPorMovimentacaoId(movimentacaoBancariaId);
            double total = 0.0;

            for (Map<String, Object> lancamento : lancamentos) {
                Double valorReceita = null;
                Double valorDespesa = null;

                Integer receitaId = lancamento.get("receitaId") != null ? Integer.valueOf(lancamento.get("receitaId").toString()) : null;
                Integer despesaId = lancamento.get("despesaId") != null ? Integer.valueOf(lancamento.get("despesaId").toString()) : null;

                if (receitaId != null && receitaId != 0) {
                    Map<String, Object> receita = receitasController.get(receitaId);
                    if (receita.get("valor") != null)
                        valorReceita = Double.valueOf(receita.get("valor").toString());
                }
                if (despesaId != null && despesaId != 0) {
                    Map<String, Object> despesa = despesasController.getById(despesaId);
                    if (despesa.get("val") != null)
                        valorDespesa = Double.valueOf(despesa.get("val").toString());
                }

                if (valorReceita != null) total += valorReceita;
                if (valorDespesa != null) total -= valorDespesa;
            }

            Map<String, Object> movimentacao = movimentacaoController.getMovimentacao(movimentacaoBancariaId);
            movimentacao.put("movbancTotal", total);

            MovimentacaoBancaria movimentacaoObj = new MovimentacaoBancaria();
            movimentacaoObj.setMovbancId(movimentacao.get("id") != null ? Integer.valueOf(movimentacao.get("id").toString()) : null);
            movimentacaoObj.setMovbancTotal(movimentacao.get("movbancTotal") != null ? Double.valueOf(movimentacao.get("movbancTotal").toString()) : null);
            movimentacaoObj.setMovbancData(movimentacao.get("data") != null ? java.time.LocalDate.parse(movimentacao.get("data").toString()) : null);
            movimentacaoObj.setUsuarioUserId(movimentacao.get("usuarioId") != null ? Integer.valueOf(movimentacao.get("usuarioId").toString()) : null);
            movimentacaoObj.setContabancariaContabId(movimentacao.get("contaBancariaId") != null ? Integer.valueOf(movimentacao.get("contaBancariaId").toString()) : null);

            // Atualiza o total da movimentação bancária
            Map<String, Object> resultado = movimentacaoController.updtMovimentacao(movimentacaoBancariaId, movimentacaoObj);

            if (resultado.get("erro") == null) {
                return ResponseEntity.ok(new Mensagem("Total da movimentação bancária recalculado com sucesso! Novo total: " + total));
            } else {
                return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensagem("Erro ao recalcular o total da movimentação bancária: " + e.getMessage()));
        }
    }
}
