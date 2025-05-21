package casoft.mvc.controller;

import casoft.mvc.model.Despesa;
import casoft.mvc.model.Parametrizacao;
import casoft.mvc.model.TipoDespesas;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String,Object> addDespesa(Despesa despesa){
        Map<String,Object> json = new HashMap<>();
        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            System.out.println(despesa.getData_lanc());
            Despesa novaDespesa=despesaModel.add(despesa,conexao);
            if(novaDespesa!=null){
                json.put("id",novaDespesa.getId());
                json.put("val",novaDespesa);
                json.put("data_venc",novaDespesa.getData_venc());
                json.put("data_lanc",novaDespesa.getData_lanc());
                json.put("pagamento",novaDespesa.getPagamento());
                json.put("descricao",novaDespesa.getDescricao());
                json.put("status",novaDespesa.getStatus_conci());
                json.put("tipo_id",novaDespesa.getTipoDespesa_id());
                json.put("usuario_id",novaDespesa.getUsuario_id());
                json.put("evento_id",novaDespesa.getEvento_id());
                conexao.Desconectar();
                return json;
            }

            conexao.Desconectar();
        }
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
