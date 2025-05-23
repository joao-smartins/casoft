package casoft.mvc.controller;

import casoft.mvc.model.Despesa;
import casoft.mvc.model.Evento;
import casoft.mvc.model.Parametrizacao;
import casoft.mvc.model.TipoDespesas;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLOutput;
import java.util.*;

@Service
public class DespesaController {
    @Autowired
    private Despesa despesaModel;

    @Autowired
    private TipoDespesas tipoDespesasModel;

    @Autowired
    private Evento eventoModel;

    public List<Map<String,Object>> getTipoDespesa() {
        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            List<TipoDespesas> tipoDespesasList=tipoDespesasModel.consultar("",conexao);
            List<Map<String,Object>> listJson=new ArrayList<>();
            for (TipoDespesas td : tipoDespesasList){
                Map<String,Object> json= new HashMap<>();
                json.put("id",td.getId());
                json.put("nome",td.getNome());
                listJson.add(json);
            }
            conexao.Desconectar();
            return listJson;
        }
        return null;
    }
    public Map<String,Object> addDespesa(String valor, String data_venc, String data_lanc, String pagamento, String descricao, String status_conci, String tipoDespesa_id, String usuario_id, String evento_id){
        Map<String,Object> json = new HashMap<>();
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
            Despesa novaDespesa=new Despesa(Double.parseDouble(valor),data_venc,data_lanc,pag,descricao,status_conci,Integer.parseInt(tipoDespesa_id),Integer.parseInt(usuario_id),evento);
            novaDespesa=despesaModel.add(novaDespesa,conexao);
            if(novaDespesa!=null){
                json.put("id",novaDespesa.getId());
                json.put("val",novaDespesa.getValor());
                json.put("data_venc",novaDespesa.getData_venc());
                json.put("data_lanc",novaDespesa.getData_lanc());
                json.put("pagamento",novaDespesa.getPagamento());
                json.put("descricao",novaDespesa.getDescricao());
                json.put("status",novaDespesa.getStatus_conci());
                json.put("tipo_id",novaDespesa.getTipoDespesa_id());
                json.put("usuario_id",novaDespesa.getUsuario_id());
                json.put("evento_id",novaDespesa.getEvento_id());
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
            List<Despesa> despesaList=despesaModel.listar("",conexao);

            for (Despesa despesa : despesaList){
                Evento evento=null;
                if(despesa.getEvento_id()!=0)
                    evento=eventoModel.consultar(despesa.getEvento_id(),conexao);
                TipoDespesas tipoDespesas=tipoDespesasModel.consultar(despesa.getTipoDespesa_id(),conexao);
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
                json.put("id",despesa.getId());
                json.put("val",despesa.getValor());
                json.put("data_venc",despesa.getData_venc());
                json.put("data_lanc",despesa.getData_lanc());
                json.put("pagamento",despesa.getPagamento());
                json.put("descricao",despesa.getDescricao());
                json.put("status_conci",despesa.getStatus_conci());
                json.put("categoria",tipoDespesaJson);
                json.put("evento",eventoJson);
                jsonlist.add(json);
                conexao.getConexao().commit();
            }
            conexao.Desconectar();

        }
        return jsonlist;
    }


}
