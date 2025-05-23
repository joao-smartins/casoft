package casoft.mvc.dao;

import casoft.mvc.model.TipoDespesas;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TipoDespesasDAO
{
    //@Override
    public TipoDespesas gravar(TipoDespesas entidade, Singleton conexao)
    {
        String sql = """
                    INSERT INTO categoriadesp(catdesp_nome)
                    VALUES ('#1');
                """;
        sql = sql.replace("#1", entidade.getNome());
        if(conexao.getConexao().manipular(sql))
        {
            return entidade;
        }
        else
        {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    //@Override
    public TipoDespesas alterar(TipoDespesas entidade, Singleton conexao)
    {
        String sql = """
                    UPDATE categoriadesp
                    SET catdesp_nome = '#1' 
                    WHERE catdesp_id = #2;
                """;
        sql = sql.replace("#1", entidade.getNome());
        sql = sql.replace("#2", String.valueOf(entidade.getId()));
        if(conexao.getConexao().manipular(sql))
        {
            return entidade;
        }
        else
        {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    //@Override
    public boolean apagar(TipoDespesas entidade, Singleton conexao)
    {
        String sql = "DELETE FROM categoriadesp WHERE catdesp_id = " + entidade.getId();
        return conexao.getConexao().manipular(sql);
    }

    //@Override
    public TipoDespesas get(int id, Singleton conexao)
    {
        String sql = "SELECT * FROM categoriadesp WHERE catdesp_id = " + id;
        ResultSet rs = conexao.getConexao().consultar(sql);
        try
        {
            if (rs.next())
            {
                TipoDespesas desp = new TipoDespesas();
                desp.setId(rs.getInt("catdesp_id"));
                desp.setNome(rs.getString("catdesp_nome"));
                return desp;
            }
        }
        catch (Exception e)
        {
            System.out.println("Erro ao listar tipos de despesas: " + e.getMessage());
        }
        return null;
    }

    //@Override
    public List<TipoDespesas> get(String filtro, Singleton conexao)
    {
        List<TipoDespesas> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoriadesp";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                TipoDespesas desp = new TipoDespesas();
                desp.setId(rs.getInt("catdesp_id"));
                desp.setNome(rs.getString("catdesp_nome"));
                lista.add(desp);
            }
        } catch (Exception e) {
            return null;
        }
        return lista;
    }


    public boolean isEmpty(Singleton conexao)
    {
        String sql = "SELECT * FROM nome";
        ResultSet rs = conexao.getConexao().consultar(sql);
        try
        {
            return !rs.next();
        }
        catch (Exception e)
        {
            return true;
        }
    }



}
