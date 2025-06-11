package casoft.mvc.model;

import casoft.mvc.dao.EventosDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public class Evento {
    private int Id;
    private String nome;
    private String descricao;
    private String data;
    private char status;

    @Autowired
    private EventosDAO dao;

    public Evento(String nome, int id, String data, String descricao, char status) {
        this.nome = nome;
        Id = id;
        this.data = data;
        this.descricao = descricao;
        this.status = status;
    }

    public Evento() {
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

    public List<Evento> consultar(String filtro, Singleton conexao){
        return dao.get(filtro, conexao);
    }

    public Evento consultar(int Id, Singleton conexao){
        return dao.get(Id, conexao);
    }

    public Evento update(Evento evento, Singleton conexao){
        return dao.alterar(evento,conexao);
    }

    public boolean delete(Evento evento, Singleton conexao){
        return dao.apagar(evento, conexao);
    }

    public Evento create(Evento evento, Singleton conexao){
        return dao.gravar(evento, conexao);
    }

    public void inativarEventos(LocalDate hoje, Singleton conexao){dao.inativarEventos(hoje,conexao);}
}
