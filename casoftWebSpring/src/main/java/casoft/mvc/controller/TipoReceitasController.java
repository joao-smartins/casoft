package casoft.mvc.controller;

import casoft.mvc.model.Evento;
import casoft.mvc.model.TipoReceitas;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TipoReceitasController {
    @Autowired
    TipoReceitas tipoReceitasModel;

    public List<Map<String,Object>> getAll(String filtro){
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json = new HashMap<>();
        List<Map<String,Object>> tipoReceitaList = new ArrayList<>();
        if(conexao.conectar()){
            List<TipoReceitas> lista =  tipoReceitasModel.consultar(filtro, conexao);
            if(!lista.isEmpty()){
                for(TipoReceitas tipoReceitas:lista){
                    json.put("id",tipoReceitas.getId());
                    json.put("nome",tipoReceitas.getNome());
                    tipoReceitaList.add(json);
                }
                conexao.Desconectar();
                return tipoReceitaList;
            }
            conexao.Desconectar();
            json.put("erro", "Tipos de Receita não encontrados");
            tipoReceitaList.add(json);
            return tipoReceitaList;
        }
        json.put("erro", "Erro ao conectar ao banco de dados");
        tipoReceitaList.add(json);
        return tipoReceitaList;
    }

    public Map<String,Object> getId(int id){
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json = new HashMap<>();
        if(conexao.conectar()){
            TipoReceitas tr = tipoReceitasModel.consultar(id,conexao);
            if(tr!=null){
                json.put("id",tr.getId());
                json.put("nome",tr.getNome());
                conexao.Desconectar();
                return json;
            }
            else
                json.put("erro","Tipo de Receita não encontrado");
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar ao banco de dados");
        return json;
    }

    public boolean delete(int id){
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar()){
            TipoReceitas tr = tipoReceitasModel.consultar(id,conexao);
            if(tr!=null){
                return tipoReceitasModel.delete(tr,conexao);
            }
        }
        return false;
    }

    public Map<String, Object> update(TipoReceitas tipoReceitas){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            TipoReceitas tr = tipoReceitasModel.update(tipoReceitas, conexao);
            if(tr!=null){
                json.put("id",tr.getId());
                json.put("nome",tr.getNome());
                conexao.Desconectar();
                return json;
            }
            else
                json.put("erro","Tipo de Receita não encontrado");
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar ao banco de dados");
        return json;
    }

    public Map<String, Object> create(TipoReceitas tipoReceitas){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            TipoReceitas tr = tipoReceitasModel.create(tipoReceitas, conexao);
            if(tr!=null){

                json.put("id",tr.getId());
                json.put("nome",tr.getNome());
                conexao.Desconectar();
                return json;
            }
            else
                json.put("erro","Tipo de Receita não cadastrado");
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar ao banco de dados");
        return json;
    }
}

