package casoft.mvc.view;

import casoft.mvc.model.Receitas;
import casoft.mvc.model.Despesas;
import casoft.mvc.model.MovimentacaoBancaria;
import casoft.mvc.util.Mensagem;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping("apis/saldo-geral")
public class SaldoView {
    @Autowired
    private Receitas receitasModel;
    @Autowired
    private Despesas despesasModel;
    @Autowired
    private MovimentacaoBancaria movimentacaoBancariaModel;

    // Consulta saldo geral (total receitas - total despesas)
    @GetMapping()
    public ResponseEntity<Object> getSaldoGeral() {
        Singleton conexao = Singleton.getInstancia();
        double totalReceitas = 0.0;
        double totalDespesas = 0.0;
        try {
            if (conexao.conectar()) {
                List<Receitas> receitas = receitasModel.get("", conexao);
                List<Despesas> despesas = despesasModel.listar("", conexao);
                for (Receitas r : receitas) {
                    totalReceitas += r.getValor();
                }
                for (Despesas d : despesas) {
                    totalDespesas += d.getValor();
                }
                double saldoGeral = totalReceitas - totalDespesas;
                Map<String, Object> retorno = new HashMap<>();
                retorno.put("totalReceitas", totalReceitas);
                retorno.put("totalDespesas", totalDespesas);
                retorno.put("saldoGeral", saldoGeral);
                return ResponseEntity.ok(retorno);
            } else {
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao conectar ao banco de dados"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensagem("Erro ao calcular saldo: " + e.getMessage()));
        } finally {
            conexao.Desconectar();
        }
    }

    // Consulta saldo geral filtrando receitas/despesas por "status"
    @GetMapping("/por-status")
    public ResponseEntity<Object> getSaldoPorStatus(
            @RequestParam(required = false) String statusReceita,
            @RequestParam(required = false) String statusDespesa) {
        Singleton conexao = Singleton.getInstancia();
        double totalReceitas = 0.0;
        double totalDespesas = 0.0;
        try {
            if (conexao.conectar()) {
                List<Receitas> receitas = receitasModel.get("", conexao);
                List<Despesas> despesas = despesasModel.listar("", conexao);
                if (statusReceita != null && !statusReceita.isEmpty()) {
                    if (statusReceita.equalsIgnoreCase("Pendente") ||
                        statusReceita.equalsIgnoreCase("Conciliado") ||
                        statusReceita.equalsIgnoreCase("Aguardando")) {
                        String statusFiltro = statusReceita;
                        receitas = receitas.stream()
                                .filter(r -> statusFiltro.equalsIgnoreCase(r.isFutura()))
                                .toList();
                    }
                }
                if (statusDespesa != null && !statusDespesa.isEmpty()) {
                    if (statusDespesa.equalsIgnoreCase("Pendente") ||
                        statusDespesa.equalsIgnoreCase("Conciliado") ||
                        statusDespesa.equalsIgnoreCase("Aguardando")) {
                        String statusFiltro = statusDespesa;
                        despesas = despesas.stream()
                                .filter(d -> statusFiltro.equalsIgnoreCase(d.getStatus_conci()))
                                .toList();
                    }
                }
                for (Receitas r : receitas) {
                    totalReceitas += r.getValor();
                }
                for (Despesas d : despesas) {
                    totalDespesas += d.getValor();
                }
                double saldo = totalReceitas - totalDespesas;
                Map<String, Object> response = new HashMap<>();
                response.put("receitas", totalReceitas);
                response.put("despesas", totalDespesas);
                response.put("saldo", saldo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao conectar ao banco de dados"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensagem("Erro ao calcular saldo: " + e.getMessage()));
        } finally {
            conexao.Desconectar();
        }
    }

    /**
     * Consulta saldo total das movimentações bancárias por conta bancária.
     *
     * @param idConta ID da conta bancária a ser consultada.
     * @return ResponseEntity com o saldo total das movimentações ou mensagem de erro.
     */



    
    // Consulta saldo total das movimentações por conta bancária
    @GetMapping("/por-conta/{id}")
    public ResponseEntity<Object> getSaldoPorConta(@PathVariable("id") int idConta) {
        Singleton conexao = Singleton.getInstancia();
        double saldo = 0.0;
        try {
            if (conexao.conectar()) {
                // Buscar todas as movimentações da conta
                List<MovimentacaoBancaria> movimentacoes = movimentacaoBancariaModel.consultarComFiltro(
                    "contabancaria_contab_id = " + idConta, conexao);
                for (MovimentacaoBancaria m : movimentacoes) {
                    saldo += m.getMovbancTotal();
                }
                Map<String, Object> response = new HashMap<>();
                response.put("idConta", idConta);
                response.put("saldoMovimentacoes", saldo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(new Mensagem("Erro ao conectar ao banco de dados"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Mensagem("Erro ao calcular saldo: " + e.getMessage()));
        } finally {
            conexao.Desconectar();
        }
    }
    
}