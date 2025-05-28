package casoft.mvc.controller;

import casoft.mvc.model.Eventos;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventoController {
    @Autowired
    private Eventos eventosModel;

    public List<Map<String,Object>> getAll(String filtro){
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> jsonE = new HashMap<>();
        List<Map<String,Object>> eventosList = new ArrayList<>();
        if(conexao.conectar()){
            List<Eventos> lista =  eventosModel.consultar(filtro, conexao);
            if(!lista.isEmpty()){
                for(Eventos eventos :lista){
                    Map<String,Object> json = new HashMap<>();
                    json.put("id", eventos.getId());
                    json.put("nome", eventos.getNome());
                    json.put("descricao", eventos.getDescricao());
                    json.put("data", eventos.getData());
                    json.put("status", eventos.isStatus());
                    eventosList.add(json);
                }
                conexao.Desconectar();
                return eventosList;
            }
            conexao.Desconectar();
            jsonE.put("erro", "Eventos não encontrado");
            eventosList.add(jsonE);
            return eventosList;
        }
        jsonE.put("erro", "Erro ao conectar ao banco de dados");
        eventosList.add(jsonE);
        return eventosList;
    }

    public Map<String,Object> getId(int id){
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json = new HashMap<>();
        if(conexao.conectar()){
            Eventos e = eventosModel.consultar(id,conexao);
            if(e!=null){
                json.put("id",e.getId());
                json.put("nome",e.getNome());
                json.put("descricao",e.getDescricao());
                json.put("data",e.getData());
                json.put("status",e.isStatus());
                conexao.Desconectar();
                return json;
            }
            else
                json.put("erro","Evento não encontrado");
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar ao banco de dados");
        return json;
    }

    public boolean delete(int id){
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar()){
            Eventos e = eventosModel.consultar(id,conexao);
            if(e!=null){
                return eventosModel.delete(e,conexao);
            }
        }
        return false;
    }

    public Map<String, Object> update(Eventos eventos){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            Eventos e = eventosModel.update(eventos, conexao);
            if(e!=null){
                json.put("id",e.getId());
                json.put("nome",e.getNome());
                json.put("descricao",e.getDescricao());
                json.put("data",e.getData());
                json.put("status",e.isStatus());
                conexao.Desconectar();
                return json;
            }
            else
                json.put("erro","Evento não encontrado");
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar ao banco de dados");
        return json;
    }

    public Map<String, Object> create(Eventos eventos){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            Eventos e = eventosModel.create(eventos, conexao);
            if(e!=null){

                json.put("id",e.getId());
                json.put("nome",e.getNome());
                json.put("descricao",e.getDescricao());
                if(e.getData()!=null){
                    json.put("data",e.getData());
                    }
                json.put("status",e.isStatus());
                conexao.Desconectar();
                return json;
            }
            else
                json.put("erro","Evento não cadastrado");
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar ao banco de dados");
        return json;
    }
}
