package casoft.mvc.controller;


import casoft.mvc.dao.MovimentacaoBancariaDAO;
import casoft.mvc.model.MovimentacaoBancaria;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovimentacaoBancariaController {
    @Autowired
    private MovimentacaoBancariaDAO dao;

    public Map<String, Object> getMovimentacao(int id) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                MovimentacaoBancaria movimentacao = dao.get(id, conexao);
                if (movimentacao != null) {
                    json.put("id", movimentacao.getMovbancId());
                    json.put("data", movimentacao.getMovbancData());
                    json.put("total", movimentacao.getMovbancTotal());
                    json.put("usuarioId", movimentacao.getUsuarioUserId());
                    json.put("contaBancariaId", movimentacao.getContabancariaContabId());
                } else {
                    json.put("erro", "Movimentação não encontrada");
                }
            } else {
                json.put("erro", "Erro ao conectar ao banco de dados");
            }
        } finally {
            conexao.Desconectar();
        }

        return json;
    }

    public List<Map<String, Object>> getMovimentacoes() {
        List<Map<String, Object>> movimentacoesList = new ArrayList<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                List<MovimentacaoBancaria> movimentacoes = dao.get("", conexao);

                for (MovimentacaoBancaria movimentacao : movimentacoes) {
                    Map<String, Object> json = new HashMap<>();
                    json.put("id", movimentacao.getMovbancId());
                    json.put("data", movimentacao.getMovbancData());
                    json.put("total", movimentacao.getMovbancTotal());
                    json.put("usuarioId", movimentacao.getUsuarioUserId());
                    json.put("contaBancariaId", movimentacao.getContabancariaContabId());

                    movimentacoesList.add(json);
                }
            } else {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Erro ao conectar ao banco de dados");
                movimentacoesList.add(erro);
            }
        } finally {
            conexao.Desconectar();
        }

        return movimentacoesList;
    }

    public Map<String, Object> addMovimentacao(MovimentacaoBancaria movimentacao) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                if (dao.gravar(movimentacao, conexao) != null) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Movimentação cadastrada com sucesso");
                    json.put("id", movimentacao.getMovbancId());
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao cadastrar a movimentação");
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

    public Map<String, Object> updtMovimentacao(int id, MovimentacaoBancaria movimentacaoAtualizada) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                movimentacaoAtualizada.setMovbancId(id);
                if (dao.alterar(movimentacaoAtualizada, conexao) != null) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Movimentação atualizada com sucesso");
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao atualizar a movimentação");
                }
            } else {
                json.put("erro", "Erro ao conectar com o banco de dados");
            }
        } catch (Exception e) {
            conexao.getConexao().rollback();
            json.put("erro", "Erro ao atualizar a movimentação: " + e.getMessage());
        } finally {
            conexao.Desconectar();
        }

        return json;
    }

    public Map<String, Object> deletarMovimentacao(int id) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                MovimentacaoBancaria movimentacao = new MovimentacaoBancaria();
                movimentacao.setMovbancId(id);

                if (dao.apagar(movimentacao, conexao)) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Movimentação deletada com sucesso");
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao deletar a movimentação");
                }
            } else {
                json.put("erro", "Erro ao conectar com o banco de dados");
            }
        } catch (Exception e) {
            conexao.getConexao().rollback();
            json.put("erro", "Erro ao excluir a movimentação: " + e.getMessage());
        } finally {
            conexao.Desconectar();
        }

        return json;
    }
}
