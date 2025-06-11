package casoft.mvc.dao;

import casoft.mvc.model.Receitas;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ReceitasDAO {

    public Receitas gravar(Receitas entidade, Singleton conexao) {
        String sql = """
        INSERT INTO receita (
            receita_val, 
            receita_futura, 
            receita_desc, 
            evento_id, 
            catrec_id, 
            receita_datavencimento, 
            receita_quitada, 
            receita_statusconciliacao, 
            receita_pagamento, 
            id
        ) VALUES (
            #1, #2, '#3', #4, #5, '#6', #7, '#8', #9, #A
        );
        """;

        sql = sql.replace("#1", String.valueOf(entidade.getValor()));
        sql = sql.replace("#2", entidade.isFutura() ? "true" : "false"); // Corrigido aqui
        sql = sql.replace("#3", entidade.getDescricao());
        sql = sql.replace("#4", String.valueOf(entidade.getEventoId()));
        sql = sql.replace("#5", String.valueOf(entidade.getCategoria()));
        sql = sql.replace("#6", entidade.getDatavencimento().toString());
        sql = sql.replace("#7", entidade.isQuitada() ? "true" : "false"); // Também corrigido
        sql = sql.replace("#8", entidade.getStatusConciliacao());
        sql = sql.replace("#9", String.valueOf(entidade.getPagamento()));
        sql = sql.replace("#A", String.valueOf(entidade.getUsuario_id()));

        System.out.println("SQL gerado: " + sql); // Adicione este log para verificação

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    public List<Receitas> consultar(String filtro, Singleton conexao) {
        List<Receitas> receitas = new ArrayList<>();
        String sql = "SELECT * FROM receita";

        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }

        var rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Receitas r = new Receitas();
                r.setId(rs.getInt("receita_id"));
                r.setValor(rs.getDouble("receita_val"));
                r.setFutura(rs.getBoolean("receita_futura"));
                r.setDescricao(rs.getString("receita_desc"));
                r.setEventoId(rs.getInt("evento_id"));
                r.setCategoria(rs.getInt("catrec_id"));
                r.setDatavencimento(rs.getDate("receita_datavencimento").toLocalDate());
                r.setQuitada(rs.getBoolean("receita_quitada"));
                r.setStatusConciliacao(rs.getString("receita_statusconciliacao"));
                r.setPagamento(rs.getInt("receita_pagamento"));
                r.setUsuario_id(rs.getInt("id"));
                receitas.add(r);
            }
        } catch(Exception er) {
            System.out.println("Erro ao carregar receitas: " + er.getMessage());
        }
        return receitas;
    }

    public boolean apagar(int id, Singleton conexao) {
        String sql = "DELETE FROM receita WHERE receita_id = "+id;
        return conexao.getConexao().manipular(sql);
    }

    public Receitas alterar(Receitas entidade, Singleton conexao) {
        String sql = """
            UPDATE receita SET
                receita_val = #1, 
                receita_futura = '#2', 
                receita_desc = '#3', 
                evento_id = #4, 
                catrec_id = #5, 
                receita_datavencimento = '#6', 
                receita_quitada = #7, 
                receita_statusconciliacao = '#8', 
                receita_pagamento = #9, 
                user_id = #A
            WHERE receita_id = #B
            """;

        sql = sql.replace("#1", String.valueOf(entidade.getValor()));
        sql = sql.replace("#2", entidade.isFutura() ? "true" : "false");
        sql = sql.replace("#3", entidade.getDescricao());
        sql = sql.replace("#4", String.valueOf(entidade.getEventoId()));
        sql = sql.replace("#5", String.valueOf(entidade.getCategoria()));
        sql = sql.replace("#6", entidade.getDatavencimento().toString());
        sql = sql.replace("#7", entidade.isQuitada() ? "true" : "false");
        sql = sql.replace("#8", entidade.getStatusConciliacao());
        sql = sql.replace("#9", String.valueOf(entidade.getPagamento()));
        sql = sql.replace("#A", String.valueOf(entidade.getUsuario_id()));
        sql = sql.replace("#B", String.valueOf(entidade.getId()));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }
}