package casoft.mvc.controller;

import casoft.mvc.model.Receitas;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReceitasController {
    @Autowired
    private Receitas receitasModel;

    public Map<String, Object> addReceita(double valor, boolean futura, String descricao, int eventoId,
                                          int categoriaId, String dataVencimento, boolean quitada,
                                          String statusConciliacao, int pagamento, int usuarioId) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = null;

        try {
            conexao = Singleton.getInstancia();
            if (!conexao.conectar()) {
                System.err.println("Falha ao conectar no banco"); // Log de erro

                json.put("erro", "Falha na conexão com o banco");
                return json;
            }



            Receitas novaReceita = new Receitas();
            novaReceita.setValor(valor);
            novaReceita.setFutura(futura);
            novaReceita.setDescricao(descricao);
            novaReceita.setEventoId(eventoId);
            novaReceita.setDatavencimento(LocalDate.parse(dataVencimento));
            novaReceita.setQuitada(quitada);
            novaReceita.setStatusConciliacao(statusConciliacao);
            novaReceita.setPagamento(pagamento);
            novaReceita.setUsuario_id(usuarioId);
            novaReceita.setCategoria(categoriaId);

            novaReceita = receitasModel.gravar(novaReceita, conexao);

            if (novaReceita != null) {
                json.put("id", novaReceita.getId());
                json.put("mensagem", "Receita cadastrada com sucesso");
                conexao.getConexao().commit();
            } else {
                json.put("erro", "Falha ao gravar receita");
                conexao.getConexao().rollback();
            }
            conexao.getConexao().commit();
            System.out.println("Transação confirmada!"); // Log de sucesso
        } catch (Exception e) {
            if (conexao != null) {
                conexao.getConexao().rollback();
            }
            json.put("erro", "Erro interno: " + e.getMessage());
        } finally {
            if (conexao != null) {
                conexao.Desconectar();
            }
        }
        return json;
    }

    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> jsonList = new ArrayList<>();
        Singleton conexao = Singleton.getInstancia();

        if(conexao.conectar()) {
            try {
                List<Receitas> receitasList = receitasModel.consultar("", conexao);

                for (Receitas receita : receitasList) {
                    Map<String, Object> json = new HashMap<>();
                    json.put("receita_id", receita.getId());
                    json.put("receita_val", receita.getValor());
                    json.put("receita_futura", receita.isFutura());
                    json.put("receita_desc", receita.getDescricao());
                    json.put("evento_id", receita.getEventoId());
                    json.put("catrec_id", receita.getCategoria());
                    json.put("receita_datavencimento", receita.getDatavencimento());
                    json.put("receita_quitada", receita.isQuitada());
                    json.put("receita_statusconciliacao", receita.getStatusConciliacao());
                    json.put("receita_pagamento", receita.getPagamento());
                    json.put("id", receita.getUsuario_id());

                    jsonList.add(json);
                    conexao.getConexao().commit();
                }
            } finally {
                conexao.Desconectar();
            }
        }
        return jsonList;
    }

    public Map<String, Object> delete(int id) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        if(conexao.conectar()) {
            if(receitasModel.apagar(id, conexao)) {
                json.put("mensagem", "Receita removida com sucesso");
                return json;
            }
        }
        return json;
    }
}