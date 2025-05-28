package casoft.mvc.model;

import casoft.mvc.dao.EventosDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class Eventos {
    private int Id;
    private String nome;
    private String descricao;
    private String data;
    private char status;

    @Autowired
    private EventosDAO dao;

    public Eventos(String nome, int id, String data, String descricao, char status) {
        this.nome = nome;
        Id = id;
        this.data = data;
        this.descricao = descricao;
        this.status = status;
    }

    public Eventos() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public char isStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public List<Eventos> consultar(String filtro, Singleton conexao){
        return dao.get(filtro, conexao);
    }

    public Eventos consultar(int Id, Singleton conexao){
        return dao.get(Id, conexao);
    }

    public Eventos update(Eventos eventos, Singleton conexao){
        return dao.alterar(eventos,conexao);
    }

    public boolean delete(Eventos eventos, Singleton conexao){
        return dao.apagar(eventos, conexao);
    }

    public Eventos create(Eventos eventos, Singleton conexao){
        return dao.gravar(eventos, conexao);
    }


}
