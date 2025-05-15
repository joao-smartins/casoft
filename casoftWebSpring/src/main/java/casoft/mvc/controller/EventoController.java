package casoft.mvc.controller;

import casoft.mvc.model.Evento;
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
    private Evento eventoModel;

    public List<Map<String,Object>> getAll(String filtro){
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar()){
            List<Evento> lista =  eventoModel.consultar(filtro, conexao);
            if(!lista.isEmpty()){
                List<Map<String,Object>> eventosList = new ArrayList<>();
                for(Evento evento:lista){
                    Map<String,Object> json = new HashMap<>();
                    json.put("id",evento.getId());
                    json.put("nome",evento.getNome());
                    json.put("descricao",evento.getDescricao());
                    json.put("data",evento.getData());
                    json.put("status",evento.isStatus());
                    eventosList.add(json);
                }
                conexao.Desconectar();
                return eventosList;
            }
            conexao.Desconectar();
            return null;
        }
        return null;
    }

    public Map<String,Object> getId(int id){
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar()){
            Evento e = eventoModel.consultar(id,conexao);
            if(e!=null){
                Map<String,Object> json = new HashMap<>();
                json.put("id",e.getId());
                json.put("nome",e.getNome());
                json.put("descricao",e.getDescricao());
                json.put("data",e.getData());
                json.put("status",e.isStatus());
                conexao.Desconectar();
                return json;
            }
            return null;
        }
        return null;
    }

    public boolean delete(int id){
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar()){
            Evento e = eventoModel.consultar(id,conexao);
            if(e!=null){
                return eventoModel.delete(e,conexao);
            }
        }
        return false;
    }

    public Map<String, Object> update(Evento evento){
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar()){
            Evento e = eventoModel.update(evento, conexao);
            if(e!=null){
                Map<String, Object> json = new HashMap<>();
                json.put("id",e.getId());
                json.put("nome",e.getNome());
                json.put("descricao",e.getDescricao());
                json.put("data",e.getData());
                json.put("status",e.isStatus());
                conexao.Desconectar();
                return json;
            }
            conexao.Desconectar();
        }
        return null;
    }

    public Map<String, Object> create(Evento evento){
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar()){
            Evento e = eventoModel.create(evento, conexao);
            if(e!=null){
                Map<String, Object> json = new HashMap<>();
                json.put("id",e.getId());
                json.put("nome",e.getNome());
                json.put("descricao",e.getDescricao());
                json.put("data",e.getData());
                json.put("status",e.isStatus());
                conexao.Desconectar();
                return json;
            }
            conexao.Desconectar();
        }
        return null;
    }
}
