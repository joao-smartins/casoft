package casoft.mvc.controller;

import casoft.mvc.model.Despesa;
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
//    public Map<String,Object> getDespesa() {
//        Singleton conexao= Singleton.getInstancia();
//        if(conexao.conectar()){
//            TipoDespesas tipoDespesas=new TipoDespesas();
//            List<TipoDespesas> tipoDespesasList=tipoDespesas.consultar("",conexao);
//            List<Map<String,Object>> listJson=new ArrayList<>();
//            for (TipoDespesas td : tipoDespesasList){
//                Map<String,Object> json= new HashMap<>();
//                json.put("despesa_id",td.getId());
//                json.put("despesa_val",td.getvalor);
//                json.put("despesa_dt_venc",param.getCnpj());
//                json.put("despesa_lancamento",param.getLogradouro());
//                json.put("despesa_pagamento",param.getNumero());
//                json.put("despesa_desc",param.getBairro());
//                json.put("despesa_statusconci",param.getCidade());
//                json.put("catdesp_id",param.getEstado());
//                json.put("user_id",param.getCep());
//                json.put("evento_id",param.getTelefone());
//                listJson.add(json);
//            }
//
//
//            conexao.Desconectar();
//            return null;
//        }
//        return null;
//    }

}
