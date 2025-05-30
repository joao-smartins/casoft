package casoft.mvc.controller;

import casoft.mvc.model.Evento;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.sql.SQLOutput;
import java.time.LocalDate;
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
        Map<String,Object> jsonE = new HashMap<>();
        List<Map<String,Object>> eventosList = new ArrayList<>();
        if(conexao.conectar()){
            List<Evento> lista =  eventoModel.consultar(filtro, conexao);
            if(!lista.isEmpty()){
                for(Evento evento :lista){
                    Map<String,Object> json = new HashMap<>();
                    json.put("id", evento.getId());
                    json.put("nome", evento.getNome());
                    json.put("descricao", evento.getDescricao());
                    json.put("data", evento.getData());
                    json.put("status", evento.isStatus());
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
            Evento e = eventoModel.consultar(id,conexao);
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
            Evento e = eventoModel.consultar(id,conexao);
            if(e!=null){
                return eventoModel.delete(e,conexao);
            }
        }
        return false;
    }

    public Map<String, Object> update(Evento evento){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            Evento e = eventoModel.update(evento, conexao);
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

    public Map<String, Object> create(Evento evento){
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()){
            Evento e = eventoModel.create(evento, conexao);
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

    @Scheduled(initialDelay = 10000, fixedRate = 60000)
    public void inativarEventos(){
        LocalDate hoje = LocalDate.now();
        Singleton conexao = Singleton.getInstancia();
        if(conexao.conectar()) {
            eventoModel.inativarEventos(hoje, conexao);
            conexao.Desconectar();
        }
    }
}
