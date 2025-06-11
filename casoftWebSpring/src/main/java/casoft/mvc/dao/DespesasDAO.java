package casoft.mvc.dao;

import casoft.mvc.model.Despesas;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DespesasDAO {

    public Despesas gravar(Despesas entidade, Singleton conexao) {
        String sql;

        sql = """
    INSERT INTO despesa (
        despesa_val,
        despesa_dt_venc,
        despesa_dt_lanc,
        despesa_pagamento,
        despesa_desc,
        despesa_statusconci,
        catdesp_id,
        user_id"""; // Não fecha ainda para adicionar os opcionais

        // Verifica campos opcionais para incluir no INSERT
        if (entidade.getEvento_id() != 0) {
            sql += ", evento_id";
        }
        if (entidade.getData_pag() != null) {
            sql += ", despesa_dt_pag";
        }
        if (entidade.getObs() != null && !entidade.getObs().isEmpty()) {
            sql += ", despesa_obs";
        }
        if (entidade.getPai_id() != null) {
            sql += ", despesa_pai_id";
        }

        sql += ") VALUES ("; // Começa os valores

        sql += String.valueOf(entidade.getValor()) + ", ";
        sql += "'" + entidade.getData_venc() + "', ";
        sql += "'" + entidade.getData_lanc() + "', ";
        sql += "'" + entidade.getPagamento() + "', ";
        sql += "'" + entidade.getDescricao() + "', ";
        sql += "'" + entidade.getStatus_conci() + "', ";
        sql += String.valueOf(entidade.getTipoDespesa_id()) + ", ";
        sql += String.valueOf(entidade.getUsuario_id());

        // Adiciona os valores dos campos opcionais
        if (entidade.getEvento_id() != 0) {
            sql += ", " + entidade.getEvento_id();
        }
        if (entidade.getData_pag() != null) {
            sql += ", '" + entidade.getData_pag() + "'";
        }
        if (entidade.getObs() != null && !entidade.getObs().isEmpty()) {
            sql += ", '" + entidade.getObs() + "'";
        }
        if (entidade.getPai_id() != null) {
            sql += ", " + entidade.getPai_id();
        }

        sql += ")";

        System.out.println(sql);
        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }
    public List<Despesas> consultar(String filtro, Singleton conexao) {
        List<Despesas> despesas = new ArrayList<>();
        String sql = "SELECT * FROM despesa";

        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }

        var rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Despesas d = new Despesas();
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
    public Despesas alterar(Despesas entidade, Singleton conexao) {
        String sql;

        sql = """
        UPDATE despesa  set
            despesa_val = #1, 
            despesa_dt_venc = '#2', 
            despesa_dt_lanc = '#3', 
            despesa_pagamento = '#4', 
            despesa_desc = '#5', 
            despesa_statusconci = '#6', 
            catdesp_id = #7, 
            user_id = #8
           
        """;

        sql = sql.replace("#1", String.valueOf(entidade.getValor()));
        sql = sql.replace("#2", entidade.getData_venc());
        sql = sql.replace("#3", entidade.getData_lanc());
        sql = sql.replace("#4", ""+entidade.getPagamento());
        sql = sql.replace("#5", entidade.getDescricao());
        sql = sql.replace("#6", entidade.getStatus_conci());
        sql = sql.replace("#7", String.valueOf(entidade.getTipoDespesa_id()));
        sql = sql.replace("#8", String.valueOf(entidade.getUsuario_id()));
        if(entidade.getEvento_id()!=0){
            sql+=",evento_id = "+entidade.getEvento_id();
        }
        if(entidade.getData_pag()!=null){
            sql+=",despesa_dt_pag = '"+entidade.getData_pag()+"'";
        }
        if(entidade.getObs()!=null && !entidade.getObs().isEmpty()){
            sql+=",despesa_obs = '"+entidade.getObs()+"'";
        }
        if(entidade.getPai_id() != null){
            sql+=",despesa_pai_id = "+entidade.getPai_id();
        }


        sql+=" WHERE despesa_id = "+entidade.getId();

        System.out.println(sql);
        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

}
