package casoft.mvc.controller;
import casoft.mvc.model.Parametrizacao;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParametrizacaoController {
    @Autowired
    private Parametrizacao paramModel;
    public Map<String,Object> getParam(int id) {
        Singleton conexao= Singleton.getInstancia();
        if(conexao.conectar()){
            Parametrizacao param = paramModel.consultar(id,conexao);
            if (param!=null){
                Map<String,Object> json= new HashMap<>();
                json.put("id",param.getId());
                json.put("nomeEmpresa",param.getNomeEmpresa());
                json.put("cnpj",param.getCnpj());
                json.put("logradouro",param.getLogradouro());
                json.put("numero",param.getNumero());
                json.put("bairro",param.getBairro());
                json.put("cidade",param.getCidade());
                json.put("estado",param.getEstado());
                json.put("cep",param.getCep());
                json.put("telefone",param.getTelefone());
                json.put("email",param.getEmail());
                conexao.Desconectar();
                return json;
            }
            conexao.Desconectar();
            return null;
        }
        return null;
    }
    public Map<String,Object> addParam(String nome, String cnpj, String logradouro, String numero, String bairro, String cidade, String estado, String cep, String telefone,String email, MultipartFile file) {
        Singleton conexao= Singleton.getInstancia();
        Map<String,Object> json= new HashMap<>();
        if(conexao.conectar()){
            //regra de negocio

            if (paramModel.isEmpty(conexao)) {
                try{
                    File uploadFolder = new File("casoftWebSpring/src/main/resources/templates/img/");
                    if (!uploadFolder.exists())
                        uploadFolder.mkdirs();
                    file.transferTo(new File(uploadFolder.getAbsoluteFile()+"\\"+"logo.png"));
                    cnpj=cnpj.replaceAll("[^\\d]", "");
                    cep=cep.replaceAll("[^\\d]", "");
                    telefone=telefone.replaceAll("[^\\d]", "");
                    Parametrizacao param=new Parametrizacao(nome,cnpj,logradouro,Integer.parseInt(numero),bairro,cidade,estado,cep,telefone,email);
                    if (paramModel.gravar(param,conexao) != null){
                        json.put("id",param.getId());
                        json.put("nomeEmpresa",param.getNomeEmpresa());
                        json.put("cnpj",param.getCnpj());
                        json.put("logradouro",param.getLogradouro());
                        json.put("numero",param.getNumero());
                        json.put("bairro",param.getBairro());
                        json.put("cidade",param.getCidade());
                        json.put("estado",param.getEstado());
                        json.put("cep",param.getCep());
                        json.put("telefone",param.getTelefone());
                        json.put("email",param.getEmail());
                        conexao.getConexao().commit();
                    }
                    else{
                        json.put("erro","Erro ao cadastrar a Empresa");
                        conexao.getConexao().rollback();
                    }
                } catch (Exception e) {
                    json.put("erro", "Erro ao armazenar o arquivo. " + e.getMessage());
                }
            } else {
                json.put("erro","Já existe uma Empresa cadastrada");
            }
            conexao.Desconectar();
        }
        else{
            json.put("erro","Erro ao conectar com o BD");
        }
        return json;
    }
    public Map<String,Object> updtParam(String nome, String cnpj, String logradouro,String numero, String bairro, String cidade, String estado, String cep, String telefone,String email, MultipartFile file) {
        Singleton conexao= Singleton.getInstancia();
        Map<String,Object> json= new HashMap<>();
        if(conexao.conectar()){
            try{
                File uploadFolder = new File("casoftWebSpring/src/main/resources/templates/img/");
                if (!uploadFolder.exists())
                    uploadFolder.mkdir();
                file.transferTo(new File(uploadFolder.getAbsoluteFile()+"\\"+"logo.png"));
                cnpj=cnpj.replaceAll("[^\\d]", "");
                cep=cep.replaceAll("[^\\d]", "");
                telefone=telefone.replaceAll("[^\\d]", "");
                Parametrizacao param=new Parametrizacao(1,nome,cnpj,logradouro,Integer.parseInt(numero),bairro,cidade,estado,cep,telefone,email);
                if (paramModel.alterar(param,conexao) != null){
                    json.put("id",param.getId());
                    json.put("nomeEmpresa",param.getNomeEmpresa());
                    json.put("cnpj",param.getCnpj());
                    json.put("logradouro",param.getLogradouro());
                    json.put("numero",param.getNumero());
                    json.put("bairro",param.getBairro());
                    json.put("cidade",param.getCidade());
                    json.put("estado",param.getEstado());
                    json.put("cep",param.getCep());
                    json.put("telefone",param.getTelefone());
                    json.put("email",param.getEmail());
                    conexao.getConexao().commit();
                }
                else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao alterar a Empresa");
                }
            } catch (Exception e) {
                json.put("erro", "Erro ao armazenar o arquivo. " + e.getMessage());
            }
            conexao.Desconectar();
        }
        else
            json.put("erro","Erro ao conectar com o BD");

        return json;
    }
}
