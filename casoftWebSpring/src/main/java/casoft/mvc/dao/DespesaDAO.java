package casoft.mvc.dao;

import casoft.mvc.model.Despesa;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

@Repository
public class DespesaDAO {

    public Despesa gravar(Despesa entidade, Singleton conexao) {
        String sql;
        if(entidade.getEvento_id()==0)
            sql = """
            INSERT INTO despesa (
                despesa_val, 
                despesa_dt_venc, 
                despesa_dt_lanc, 
                despesa_pagamento, 
                despesa_desc, 
                despesa_statusconci, 
                categoriadesp_catdesp_id, 
                usuario_user_id
            ) VALUES (
                #2, '#3', '#4', '#5', '#6', '#7', #8, #9
            );
            """;
        else
            sql = """
            INSERT INTO despesa (
                despesa_val, 
                despesa_dt_venc, 
                despesa_dt_lanc, 
                despesa_pagamento, 
                despesa_desc, 
                despesa_statusconci, 
                categoriadesp_catdesp_id, 
                usuario_user_id, 
                evento_evento_id
            ) VALUES (
                #2, '#3', '#4', '#5', '#6', '#7', #8, #9, #A
            );
            """;

        sql = sql.replace("#2", String.valueOf(entidade.getValor()));
        sql = sql.replace("#3", entidade.getData_venc());
        sql = sql.replace("#4", entidade.getData_lanc());
        sql = sql.replace("#5", ""+entidade.getPagamento());
        sql = sql.replace("#6", entidade.getDescricao());
        sql = sql.replace("#7", entidade.getStatus_conci());
        sql = sql.replace("#8", String.valueOf(entidade.getTipoDespesa_id()));
        sql = sql.replace("#9", String.valueOf(entidade.getUsuario_id()));
        sql = sql.replace("#A", String.valueOf(entidade.getEvento_id()));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

}
