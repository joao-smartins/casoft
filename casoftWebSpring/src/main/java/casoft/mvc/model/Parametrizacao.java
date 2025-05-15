package casoft.mvc.model;

import casoft.mvc.dao.ParametrizacaoDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Parametrizacao {
    @Autowired
    private ParametrizacaoDAO dao;

    private int id;
    private String nomeEmpresa;
    private String cnpj;
    private String logradouro;
    private int numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;
    private String email;

    public Parametrizacao(String nomeEmpresa, String cnpj, String logradouro, int numero, String bairro, String cidade, String estado, String cep, String telefone,String email) {
        this.nomeEmpresa = nomeEmpresa;
        this.cnpj = cnpj;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.telefone = telefone;
        this.email = email;
    }

    public Parametrizacao(int id, String nomeEmpresa, String cnpj, String logradouro, int numero, String bairro, String cidade, String estado, String cep, String telefone,String email) {
        this.id = id;
        this.nomeEmpresa = nomeEmpresa;
        this.cnpj = cnpj;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.telefone = telefone;
        this.email = email;
    }

    public Parametrizacao() {
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public List<Parametrizacao> consultar(String filtro, Singleton conexao){
        return dao.get(filtro,conexao);
    }
    public Parametrizacao consultar(int id, Singleton conexao){
        return dao.get(id,conexao);
    }
    public boolean isEmpty(Singleton conexao){
        return dao.get("",conexao).isEmpty();
    }
    public Parametrizacao gravar(Parametrizacao param, Singleton conexao){
        return dao.gravar(param,conexao);
    }
    public Parametrizacao alterar(Parametrizacao param, Singleton conexao){
        return dao.alterar(param,conexao);
    }
    public boolean deletarEmpresa(Singleton conexao){
        return dao.deletarEmpresa(conexao);
    }
}
