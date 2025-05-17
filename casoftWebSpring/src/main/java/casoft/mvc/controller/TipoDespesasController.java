package casoft.mvc.controller;

import casoft.mvc.model.TipoDespesas;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class TipoDespesasController
{
    @Autowired
    private TipoDespesas despeModel;

    public List<Map<String,Object>> getDespesa(String filtro)
    {
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar())
        {
            List<TipoDespesas> lista = despeModel.consultar(filtro,conexao);
            if(!lista.isEmpty())
            {
                List<Map<String,Object>> despeList = new ArrayList<>();
                for(TipoDespesas tipoDespesa: lista)
                {
                    Map<String,Object> json = new HashMap<>();
                    json.put("id",tipoDespesa.getId());
                    json.put("nome",tipoDespesa.getNome());
                    despeList.add(json);
                }
                conexao.Desconectar();
                return despeList;
            }
            conexao.Desconectar();
            return null;
        }
        return null;
    }

    public Map<String,Object> getDespesaID(int id)
    {
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar())
        {
            TipoDespesas tipo = despeModel.consultarID(id,conexao);

            if(tipo != null)
            {
                Map<String,Object> json = new HashMap<>();
                json.put("id",tipo.getId());
                json.put("nome",tipo.getNome());
                conexao.Desconectar();
                return json;
            }
        }
        conexao.Desconectar();
        return null;
    }

    public Map<String,Object> addTipoDespesa(TipoDespesas tipoDespesa)
    {
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json = new HashMap<>();

        if(tipoDespesa.getNome() == null || tipoDespesa.getNome().trim().isEmpty())
        {
            json.put("erro!", "Campo esta vazio");
        }
        else
        {
            if(conexao.conectar())
            {
                TipoDespesas tipo = new TipoDespesas(tipoDespesa.getId(), tipoDespesa.getNome());
                if(despeModel.gravar(tipo,conexao) != null)
                {
                    json.put("id",tipo.getId());
                    json.put("nome",tipo.getNome());
                    conexao.getConexao().commit();
                }
                else
                {
                    json.put("erro", "Erro ao gravar tipo de despesa");
                    conexao.getConexao().rollback();
                }
                conexao.Desconectar();
            }
            else
            {
                json.put("erro","Erro ao conectar com o BD");
            }
        }
        return json;
    }

    public Map<String,Object> updateTipoDespesa(TipoDespesas tipoDespesa)
    {
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json= new HashMap<>();
        if(conexao.conectar())
        {
            TipoDespesas tipo = new TipoDespesas(tipoDespesa.getId(),tipoDespesa.getNome());
            if(despeModel.alterar(tipo,conexao) != null)
            {
                json.put("id",tipo.getId());
                json.put("nome",tipo.getNome());
                conexao.getConexao().commit();
            }
            else
            {
                json.put("erro", "Erro ao gravar tipo de despesa");
                conexao.getConexao().rollback();
            }
            conexao.Desconectar();
        }
        else
        {
            json.put("erro","Erro ao conectar com o BD");
        }
        return json;
    }

    public Map<String, Object> deleteTipoDespesa(int id)
    {
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json= new HashMap<>();

        if(conexao.conectar())
        {
            TipoDespesas tipo = despeModel.consultar(id,conexao);
            if(tipo != null)
            {
                if(despeModel.apagar(tipo,conexao))
                {
                    conexao.getConexao().commit();
                    json.put("ok","Tipo de despesa removido com sucesso");
                }
                else
                {
                    conexao.getConexao().rollback();
                    json.put("erro","Erro ao remover tipo de despesa");
                }
            }
            else
            {
                json.put("erro","Tipo de despesa não encontrada!");
            }
            conexao.Desconectar();
        }
        else
        {
            json.put("erro","Erro ao conectar com o BD");
        }
        return json;
    }



}
