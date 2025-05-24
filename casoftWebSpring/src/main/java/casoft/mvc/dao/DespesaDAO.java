package casoft.mvc.dao;

import casoft.mvc.model.Despesa;
import casoft.mvc.model.Evento;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
                catdesp_id, 
                user_id
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
                catdesp_id, 
                user_id, 
                evento_id
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
    public List<Despesa> consultar(String filtro, Singleton conexao) {
        List<Despesa> despesas = new ArrayList<>();
        String sql = "SELECT * FROM despesa";

        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }

        var rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Despesa d = new Despesa();
                d.setId(rs.getInt("despesa_id"));
                d.setValor(rs.getDouble("despesa_val"));
                d.setData_venc(rs.getString("despesa_dt_venc"));
                d.setData_lanc(rs.getString("despesa_dt_lanc"));
                d.setPagamento(rs.getDouble("despesa_pagamento"));
                d.setDescricao(rs.getString("despesa_desc"));
                d.setStatus_conci(rs.getString("despesa_statusconci"));
                d.setTipoDespesa_id(rs.getInt("catdesp_id"));
                d.setUsuario_id(rs.getInt("user_id"));
                String evento_id = rs.getString("evento_id");
                if (evento_id != null) {
                    d.setEvento_id(rs.getInt("evento_id"));
                }
                else{
                    d.setEvento_id(0);
                }
                despesas.add(d);
            }
        } catch(Exception er) {
            System.out.println("Erro ao carregar despesas: " + er.getMessage());
        }
        return despesas;
    }

    public boolean apagar(int id, Singleton conexao) {
        String sql = "DELETE FROM despesa WHERE despesa_id = "+id;
        if(conexao.getConexao().manipular(sql)) {
            return true;
        }
        return false;
    }
}
