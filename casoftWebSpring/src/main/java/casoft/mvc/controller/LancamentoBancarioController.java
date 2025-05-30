package casoft.mvc.controller;

import casoft.mvc.dao.LancamentoBancarioDAO;
import casoft.mvc.model.LancamentoBancario;
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

    public Map<String, Object> addLancamento(LancamentoBancario lancamento) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                if (dao.gravar(lancamento, conexao) != null) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Lançamento cadastrado com sucesso");
                    json.put("id", lancamento.getLancamentoId());
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

                if (dao.apagar(lancamento, conexao)) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Lançamento deletado com sucesso");
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

}
