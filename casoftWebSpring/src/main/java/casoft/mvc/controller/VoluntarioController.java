package casoft.mvc.controller;

import casoft.mvc.model.Voluntario;
import casoft.mvc.model.Voluntario;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoluntarioController {

    @Autowired
    private Voluntario voluntarioModel;

    public List<Map<String,Object>> getAll(String filtro){
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> jsonE = new HashMap<>();
        List<Map<String,Object>> voluntarioList = new ArrayList<>();
        if(conexao.conectar()){
            List<Voluntario> lista =  voluntarioModel.consultar(filtro, conexao);
            if(!lista.isEmpty()){
                for(Voluntario voluntario :lista){
                    Map<String,Object> json = new HashMap<>();
                    json.put("id", voluntario.getId());
                    json.put("nome", voluntario.getNome());
                    json.put("cpf", voluntario.getCpf());
                    json.put("email", voluntario.getEmail());
                    json.put("logradouro", voluntario.getLogradouro());
                    json.put("bairro", voluntario.getBairro());
                    json.put("cep", voluntario.getCep());
                    json.put("cell", voluntario.getCell());
                    json.put("comp", voluntario.getComp());
                    voluntarioList.add(json);
                }
                conexao.Desconectar();
                return voluntarioList;
            }
            conexao.Desconectar();
            jsonE.put("erro", "Voluntarios não encontrados");
            voluntarioList.add(jsonE);
            return voluntarioList;
        }
        jsonE.put("erro", "Erro ao conectar ao banco de dados");
        voluntarioList.add(jsonE);
        return voluntarioList;
    }

    public Map<String,Object> getId(int id){
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json = new HashMap<>();
        if(conexao.conectar()){
            Voluntario v = voluntarioModel.consultar(id,conexao);
            if(v!=null){
                json.put("id", v.getId());
                json.put("nome", v.getNome());
                json.put("cpf", v.getCpf());
                json.put("email", v.getEmail());
                json.put("logradouro", v.getLogradouro());
                json.put("bairro", v.getBairro());
                json.put("cep", v.getCep());
                json.put("cell", v.getCell());
                json.put("comp", v.getComp());
                conexao.Desconectar();
                return json;
            }
            else
                json.put("erro","Voluntario não encontrado");
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar ao banco de dados");
        return json;
    }

    public boolean delete(int id){
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar()){
            Voluntario v = voluntarioModel.consultar(id,conexao);
            if(v!=null){
                return voluntarioModel.delete(v,conexao);
            }
        }
        return false;
    }

    public Map<String, Object> update(Voluntario voluntario){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            Voluntario v = voluntarioModel.update(voluntario, conexao);
            if(v!=null){
                json.put("id", v.getId());
                json.put("nome", v.getNome());
                json.put("cpf", v.getCpf());
                json.put("email", v.getEmail());
                json.put("logradouro", v.getLogradouro());
                json.put("bairro", v.getBairro());
                json.put("cep", v.getCep());
                json.put("cell", v.getCell());
                json.put("comp", v.getComp());
                conexao.Desconectar();
                return json;
            }
            else
                json.put("erro","Voluntario não encontrado");
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar ao banco de dados");
        return json;
    }

    public Map<String, Object> create(Voluntario voluntario){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            Voluntario v = voluntarioModel.create(voluntario, conexao);
            if(v!=null){

                json.put("id", v.getId());
                json.put("nome", v.getNome());
                json.put("cpf", v.getCpf());
                json.put("email", v.getEmail());
                json.put("logradouro", v.getLogradouro());
                json.put("bairro", v.getBairro());
                json.put("cep", v.getCep());
                json.put("cell", v.getCell());
                json.put("comp", v.getComp());
                conexao.Desconectar();
                return json;
            }
            else
                json.put("erro","Voluntario não cadastrado");
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar ao banco de dados");
        return json;
    }
}
