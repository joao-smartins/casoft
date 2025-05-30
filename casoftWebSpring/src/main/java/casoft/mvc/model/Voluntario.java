package casoft.mvc.model;

import casoft.mvc.dao.VoluntarioDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Voluntario {
    private int id;
    private String nome;
    private int cell;
    private String cpf;
    private String email;
    private String logradouro;
    private String bairro;
    private String comp;
    private String cep;

    @Autowired
    private VoluntarioDAO dao;

    public Voluntario() {
    }

    public Voluntario(int id, String nome, int cell, String cpf, String email, String logradouro, String bairro, String comp, String cep) {
        this.id = id;
        this.nome = nome;
        this.cell = cell;
        this.cpf = cpf;
        this.email = email;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.comp = comp;
        this.cep = cep;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<Voluntario> consultar(String filtro, Singleton conexao){
        return dao.get(filtro, conexao);
    }

    public Voluntario consultar(int Id, Singleton conexao){
        return dao.get(Id, conexao);
    }

    public Voluntario update(Voluntario voluntario, Singleton conexao){
        return dao.alterar(voluntario,conexao);
    }

    public boolean delete(Voluntario voluntario, Singleton conexao){
        return dao.apagar(voluntario, conexao);
    }

    public Voluntario create(Voluntario voluntario, Singleton conexao){
        return dao.gravar(voluntario, conexao);
    }

}
