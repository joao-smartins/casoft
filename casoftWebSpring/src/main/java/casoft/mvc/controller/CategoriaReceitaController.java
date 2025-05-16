package casoft.mvc.controller;

import casoft.mvc.model.CategoriaReceita;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoriaReceitaController {
    @Autowired
    CategoriaReceita categoriaReceitaModel;

    public List<Map<String,Object>> getAll(String filtro){
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json = new HashMap<>();
        List<Map<String,Object>> tipoReceitaList = new ArrayList<>();
        if(conexao.conectar()){
            List<CategoriaReceita> lista =  categoriaReceitaModel.consultar(filtro, conexao);
            if(!lista.isEmpty()){
                for(CategoriaReceita categoriaReceita :lista){
                    json.put("id", categoriaReceita.getId());
                    json.put("nome", categoriaReceita.getNome());
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
            CategoriaReceita tr = categoriaReceitaModel.consultar(id,conexao);
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
            CategoriaReceita tr = categoriaReceitaModel.consultar(id,conexao);
            if(tr!=null){
                return categoriaReceitaModel.delete(tr,conexao);
            }
        }
        return false;
    }

    public Map<String, Object> update(CategoriaReceita categoriaReceita){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            CategoriaReceita tr = categoriaReceitaModel.update(categoriaReceita, conexao);
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

    public Map<String, Object> create(CategoriaReceita categoriaReceita){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            CategoriaReceita tr = categoriaReceitaModel.create(categoriaReceita, conexao);
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

