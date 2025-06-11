package casoft.mvc.controller;

import casoft.mvc.dao.LancamentoBancarioDAO;
import casoft.mvc.model.LancamentoBancario;
import casoft.mvc.model.MovimentacaoBancaria;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LancamentoBancarioController {
    @Autowired
    private LancamentoBancarioDAO dao;

    @Autowired
    private MovimentacaoBancariaController movimentacaoController;

    @Autowired
    private ReceitasController receitasController;

    @Autowired
    private DespesasController despesasController;

    public Map<String, Object> getLancamento(int id) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                LancamentoBancario lancamento = dao.get(id, conexao);
                if (lancamento != null) {
                    json.put("id", lancamento.getLancamentoId());
                    json.put("dataLancamento", lancamento.getDataLancamento());
                    json.put("descricao", lancamento.getDescricao());
                    json.put("origem", lancamento.getOrigem());
                    json.put("destino", lancamento.getDestino());
                    json.put("contaBancariaId", lancamento.getContaBancariaId());
                    json.put("movimentacaoBancariaId", lancamento.getMovimentacaoBancariaId());
                    json.put("receitaId", lancamento.getReceitaId());
                    json.put("despesaId", lancamento.getDespesaId());
                } else {
                    json.put("erro", "Lançamento não encontrado");
                }
            } else {
                json.put("erro", "Erro ao conectar ao banco de dados");
            }
        } finally {
            conexao.Desconectar();
        }

        return json;
    }

    public List<Map<String, Object>> getLancamentos() {
        List<Map<String, Object>> lancamentosList = new ArrayList<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                List<LancamentoBancario> lancamentos = dao.get("", conexao);

                for (LancamentoBancario lancamento : lancamentos) {
                    Map<String, Object> json = new HashMap<>();
                    json.put("id", lancamento.getLancamentoId());
                    json.put("dataLancamento", lancamento.getDataLancamento());
                    json.put("descricao", lancamento.getDescricao());
                    json.put("origem", lancamento.getOrigem());
                    json.put("destino", lancamento.getDestino());
                    json.put("contaBancariaId", lancamento.getContaBancariaId());
                    json.put("movimentacaoBancariaId", lancamento.getMovimentacaoBancariaId());
                    json.put("receitaId", lancamento.getReceitaId());
                    json.put("despesaId", lancamento.getDespesaId());

                    lancamentosList.add(json);
                }
            } else {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Erro ao conectar ao banco de dados");
                lancamentosList.add(erro);
            }
        } finally {
            conexao.Desconectar();
        }

        return lancamentosList;
    }

    public List<Map<String, Object>> getLancamentosPorMovimentacaoId(int movimentacaoBancariaId) {
        List<Map<String, Object>> lancamentosList = new ArrayList<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                List<LancamentoBancario> lancamentos = dao.get("movimentacaobancaria_movbanc_id = " + movimentacaoBancariaId, conexao);
                System.out.println("Total de lançamentos encontrados: " + lancamentos.size());

                for (LancamentoBancario lancamento : lancamentos) {
                    Map<String, Object> json = new HashMap<>();
                    json.put("id", lancamento.getLancamentoId());
                    json.put("dataLancamento", lancamento.getDataLancamento());
                    json.put("descricao", lancamento.getDescricao());
                    json.put("origem", lancamento.getOrigem());
                    json.put("destino", lancamento.getDestino());
                    json.put("contaBancariaId", lancamento.getContaBancariaId());
                    json.put("movimentacaoBancariaId", lancamento.getMovimentacaoBancariaId());
                    json.put("receitaId", lancamento.getReceitaId());
                    json.put("despesaId", lancamento.getDespesaId());

                    lancamentosList.add(json);
                }
            } else {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Erro ao conectar ao banco de dados");
                lancamentosList.add(erro);
            }
        } finally {
            conexao.Desconectar();
        }

        return lancamentosList;
    }

    public Map<String, Object> addLancamento(LancamentoBancario lancamento) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                if (dao.gravar(lancamento, conexao) != null) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Lançamento cadastrado com sucesso");
                    json.put("id", lancamento.getLancamentoId());
                    int id = lancamento.getMovimentacaoBancariaId();

                    System.out.println(recalcularTotalMovimentacao(id));
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao cadastrar o lançamento");
                }
            } else {
                json.put("erro", "Erro ao conectar com o banco de dados");
            }
        } catch (Exception e) {
            try {
                conexao.getConexao().rollback();
            } catch (Exception ignored) {
            }
            json.put("erro", "Erro ao armazenar o conteúdo: " + e.getMessage());
        } finally {
            conexao.Desconectar();
        }

        return json;
    }

    public Map<String, Object> updtLancamento(int id, LancamentoBancario lancamentoAtualizado) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                lancamentoAtualizado.setLancamentoId(id);
                if (dao.alterar(lancamentoAtualizado, conexao) != null) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Lançamento atualizado com sucesso");
                    int mov_id = lancamentoAtualizado.getMovimentacaoBancariaId();
                    System.out.println(recalcularTotalMovimentacao(mov_id));
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao atualizar o lançamento");
                }
            } else {
                json.put("erro", "Erro ao conectar com o banco de dados");
            }
        } catch (Exception e) {
            conexao.getConexao().rollback();
            json.put("erro", "Erro ao atualizar o lançamento: " + e.getMessage());
        } finally {
            conexao.Desconectar();
        }

        return json;
    }


    public Map<String, Object> deletarLancamento(int id) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                LancamentoBancario lancamento = new LancamentoBancario();
                lancamento.setLancamentoId(id);

                // Buscar o lançamento antes de deletar para obter o movimentacaoBancariaId
                LancamentoBancario lancamentoExistente = dao.get(id, conexao);
                Integer movimentacaoBancariaId = null;
                if (lancamentoExistente != null) {
                    movimentacaoBancariaId = lancamentoExistente.getMovimentacaoBancariaId();
                }

                if (dao.apagar(lancamento, conexao)) {
                    System.out.println("Lançamento deletado com sucesso: " + id);
                    conexao.getConexao().commit();
                    json.put("mensagem", "Lançamento deletado com sucesso");

                    // Recalcular total da movimentação bancária, se aplicável
                    if (movimentacaoBancariaId != null) {
                        Map<String, Object> resultado = recalcularTotalMovimentacao(movimentacaoBancariaId);
                        if (resultado.get("erro") != null) {
                            json.put("erroRecalculo", resultado.get("erro"));
                        } else {
                            json.put("mensagemRecalculo", resultado.get("mensagem"));
                        }
                    }

                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao deletar o lançamento");
                }
            } else {
                json.put("erro", "Erro ao conectar com o banco de dados");
            }
        } catch (Exception e) {
            conexao.getConexao().rollback();
            json.put("erro", "Erro ao excluir o lançamento: " + e.getMessage());
        } finally {
            conexao.Desconectar();
        }

        return json;
    }



    public Map<String, Object> recalcularTotalMovimentacao(int movimentacaoBancariaId) {
        Map<String, Object> json = new HashMap<>();
        try {
            List<Map<String, Object>> lancamentos = getLancamentosPorMovimentacaoId(movimentacaoBancariaId);
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

            Map<String, Object> resultado = movimentacaoController.updtMovimentacao(movimentacaoBancariaId, movimentacaoObj);

            if (resultado.get("erro") == null) {
                json.put("mensagem", "Total da movimentação bancária recalculado com sucesso! Novo total: " + total);
            } else {
                json.put("erro", resultado.get("erro").toString());
            }
        } catch (Exception e) {
            json.put("erro", "Erro ao recalcular o total da movimentação bancária: " + e.getMessage());
        }
        return json;
    }

}
