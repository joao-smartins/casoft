package casoft.mvc.controller;

import casoft.mvc.model.Despesas;
import casoft.mvc.model.Evento;
import casoft.mvc.model.TipoDespesas;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DespesasController {
    @Autowired
    private Despesas despesasModel;

    @Autowired
    private TipoDespesas tipoDespesasModel;

    @Autowired
    private Evento eventoModel;

    public Map<String,Object> addDespesa(String valor, String data_venc, String data_lanc, String pagamento, String descricao, String status_conci, String tipoDespesa_id, String usuario_id, String evento_id){
        Map<String,Object> json = new HashMap<>();
        System.out.println("Tipo Despesa: "+usuario_id);
        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            int evento;
            if(!Objects.equals(evento_id, ""))
                evento=Integer.parseInt(evento_id);
            else
                evento=0;
            Double pag;
            if(!Objects.equals(pagamento, ""))
                pag=Double.parseDouble(pagamento);
            else
                pag=0.0;
            Despesas novaDespesas =new Despesas(Double.parseDouble(valor),data_venc,data_lanc,pag,descricao,status_conci,Integer.parseInt(tipoDespesa_id),Integer.parseInt(usuario_id),evento);
            novaDespesas = despesasModel.add(novaDespesas,conexao);
            if(novaDespesas !=null){
                json.put("id", novaDespesas.getId());
                json.put("val", novaDespesas.getValor());
                json.put("data_venc", novaDespesas.getData_venc());
                json.put("data_lanc", novaDespesas.getData_lanc());
                json.put("pagamento", novaDespesas.getPagamento());
                json.put("descricao", novaDespesas.getDescricao());
                json.put("status", novaDespesas.getStatus_conci());
                json.put("tipo_id", novaDespesas.getTipoDespesa_id());
                json.put("usuario_id", novaDespesas.getUsuario_id());
                json.put("evento_id", novaDespesas.getEvento_id());
                conexao.getConexao().commit();
                return json;
            }
            else{
                json.put("erro","Erro ao cadastrar a Empresa");
                conexao.getConexao().rollback();
            }
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar com o BD");
        return json;
    }
    public Map<String,Object> uptDespesa(int id,String valor, String data_venc, String data_lanc, String pagamento, String descricao, String status_conci, String tipoDespesa_id, String usuario_id, String evento_id){
        Map<String,Object> json = new HashMap<>();
        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            int evento;
            if(!Objects.equals(evento_id, "null"))
                evento=Integer.parseInt(evento_id);
            else
                evento=0;
            Double pag;
            if(!Objects.equals(pagamento, ""))
                pag=Double.parseDouble(pagamento);
            else
                pag=0.0;
            Despesas novaDespesas =new Despesas(id,Double.parseDouble(valor),data_venc,data_lanc,pag,descricao,status_conci,Integer.parseInt(tipoDespesa_id),Integer.parseInt(usuario_id),evento);
            novaDespesas = despesasModel.alterar(novaDespesas,conexao);
            if(novaDespesas !=null){
                json.put("id", novaDespesas.getId());
                json.put("val", novaDespesas.getValor());
                json.put("data_venc", novaDespesas.getData_venc());
                json.put("data_lanc", novaDespesas.getData_lanc());
                json.put("pagamento", novaDespesas.getPagamento());
                json.put("descricao", novaDespesas.getDescricao());
                json.put("status", novaDespesas.getStatus_conci());
                json.put("tipo_id", novaDespesas.getTipoDespesa_id());
                json.put("usuario_id", novaDespesas.getUsuario_id());
                json.put("evento_id", novaDespesas.getEvento_id());
                conexao.getConexao().commit();
                return json;
            }
            else{
                json.put("erro","Erro ao cadastrar a Empresa");
                conexao.getConexao().rollback();
            }
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar com o BD");
        return json;
    }
    public List<Map<String,Object>> getAll() {
        List<Map<String,Object>> jsonlist = new ArrayList<>();

        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            List<Despesas> despesasList = despesasModel.listar("",conexao);

            for (Despesas despesas : despesasList){
                Evento evento =null;

                if(despesas.getEvento_id()!=0)
                    evento = eventoModel.consultar(despesas.getEvento_id(),conexao);
                TipoDespesas tipoDespesas=tipoDespesasModel.consultar(despesas.getTipoDespesa_id(),conexao);
                Map<String, Object> eventoJson = new HashMap<>();
                if (evento != null) {
                    eventoJson.put("id", evento.getId());
                    eventoJson.put("nome", evento.getNome());
                }
                Map<String, Object> tipoDespesaJson = new HashMap<>();
                if (tipoDespesas != null) {
                    tipoDespesaJson.put("id", tipoDespesas.getId());
                    tipoDespesaJson.put("nome", tipoDespesas.getNome());
                }
                Map<String,Object> json= new HashMap<>();
                json.put("id", despesas.getId());
                json.put("val", despesas.getValor());
                json.put("data_venc", despesas.getData_venc());
                json.put("data_lanc", despesas.getData_lanc());
                json.put("pagamento", despesas.getPagamento());
                json.put("descricao", despesas.getDescricao());
                json.put("status_conci", despesas.getStatus_conci());
                json.put("categoria",tipoDespesaJson);
                json.put("evento",eventoJson);
                jsonlist.add(json);
                conexao.getConexao().commit();
            }
            conexao.Desconectar();

        }
        return jsonlist;
    }
    public Map<String,Object> delete(int id){
        Map<String,Object> json=new HashMap<>();
        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            if(despesasModel.remover(id,conexao)){
                json.put("mesagem","Despesa removida com sucesso");
                return json;
            }
        }
        return json;
    }
    public List<Map<String,Object>> getAguardando() {
        List<Map<String,Object>> jsonlist = new ArrayList<>();

        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            List<Despesas> despesasList = despesasModel.listar("despesa_statusconci = 'Aguardando'",conexao);

            for (Despesas despesas : despesasList){
                Evento evento =null;

                if(despesas.getEvento_id()!=0)
                    evento = eventoModel.consultar(despesas.getEvento_id(),conexao);
                TipoDespesas tipoDespesas=tipoDespesasModel.consultar(despesas.getTipoDespesa_id(),conexao);
                Map<String, Object> eventoJson = new HashMap<>();
                if (evento != null) {
                    eventoJson.put("id", evento.getId());
                    eventoJson.put("nome", evento.getNome());
                }
                Map<String, Object> tipoDespesaJson = new HashMap<>();
                if (tipoDespesas != null) {
                    tipoDespesaJson.put("id", tipoDespesas.getId());
                    tipoDespesaJson.put("nome", tipoDespesas.getNome());
                }
                Map<String,Object> json= new HashMap<>();
                json.put("id", despesas.getId());
                json.put("val", despesas.getValor());
                json.put("data_venc", despesas.getData_venc());
                json.put("data_lanc", despesas.getData_lanc());
                json.put("pagamento", despesas.getPagamento());
                json.put("descricao", despesas.getDescricao());
                json.put("status_conci", despesas.getStatus_conci());
                json.put("categoria",tipoDespesaJson);
                json.put("evento",eventoJson);
                jsonlist.add(json);
                conexao.getConexao().commit();
            }
            conexao.Desconectar();

        }
        return jsonlist;
    }
    public Map<String,Object> uptDespesaQuitar(int id,String valor, String data_venc, String data_lanc, String pagamento, String descricao, String status_conci, String tipoDespesa_id, String usuario_id, String evento_id,String data_pag,String obs){
        Map<String,Object> json = new HashMap<>();
        System.out.println("TIpo evento: "+evento_id);
        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            int evento;
            if(!Objects.equals(evento_id, "null"))
                evento=Integer.parseInt(evento_id);
            else
                evento=0;
            Double pag;
            if(!Objects.equals(pagamento, ""))
                pag=Double.parseDouble(pagamento);
            else
                pag=0.0;
            Despesas novaDespesas =new Despesas(id,Double.parseDouble(valor),data_venc,data_lanc,pag,descricao,status_conci,Integer.parseInt(tipoDespesa_id),Integer.parseInt(usuario_id),evento,data_pag,obs);
            novaDespesas = despesasModel.alterar(novaDespesas,conexao);
            if(novaDespesas !=null){
                json.put("id", novaDespesas.getId());
                json.put("val", novaDespesas.getValor());
                json.put("data_venc", novaDespesas.getData_venc());
                json.put("data_lanc", novaDespesas.getData_lanc());
                json.put("pagamento", novaDespesas.getPagamento());
                json.put("descricao", novaDespesas.getDescricao());
                json.put("status", novaDespesas.getStatus_conci());
                json.put("tipo_id", novaDespesas.getTipoDespesa_id());
                json.put("usuario_id", novaDespesas.getUsuario_id());
                json.put("evento_id", novaDespesas.getEvento_id());
                json.put("data_pag", novaDespesas.getData_pag());
                json.put("obs", novaDespesas.getObs());
                json.put("pai_id", novaDespesas.getPai_id());
                conexao.getConexao().commit();
                return json;
            }
            else{
                json.put("erro","Erro ao cadastrar a Empresa");
                conexao.getConexao().rollback();
            }
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar com o BD");
        return json;
    }
    public Map<String,Object> addDespesa(String valor, String data_venc, String data_lanc, String pagamento, String descricao, String status_conci, String tipoDespesa_id, String usuario_id, String evento_id,Integer pai_id){
        Map<String,Object> json = new HashMap<>();
        System.out.println("Tipo Despesa: "+usuario_id);
        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            int evento;
            if(!Objects.equals(evento_id, ""))
                evento=Integer.parseInt(evento_id);
            else
                evento=0;
            Double pag;
            if(!Objects.equals(pagamento, ""))
                pag=Double.parseDouble(pagamento);
            else
                pag=0.0;
            Despesas novaDespesas =new Despesas(Double.parseDouble(valor),data_venc,data_lanc,pag,descricao,status_conci,Integer.parseInt(tipoDespesa_id),Integer.parseInt(usuario_id),evento,pai_id);
            novaDespesas = despesasModel.add(novaDespesas,conexao);
            if(novaDespesas !=null){
                json.put("id", novaDespesas.getId());
                json.put("val", novaDespesas.getValor());
                json.put("data_venc", novaDespesas.getData_venc());
                json.put("data_lanc", novaDespesas.getData_lanc());
                json.put("pagamento", novaDespesas.getPagamento());
                json.put("descricao", novaDespesas.getDescricao());
                json.put("status", novaDespesas.getStatus_conci());
                json.put("tipo_id", novaDespesas.getTipoDespesa_id());
                json.put("usuario_id", novaDespesas.getUsuario_id());
                json.put("evento_id", novaDespesas.getEvento_id());
                json.put("data_pag", novaDespesas.getData_pag());
                json.put("obs", novaDespesas.getObs());
                json.put("pai_id", novaDespesas.getPai_id());
                conexao.getConexao().commit();
                return json;
            }
            else{
                json.put("erro","Erro ao cadastrar a Empresa");
                conexao.getConexao().rollback();
            }
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar com o BD");
        return json;
    }
}
