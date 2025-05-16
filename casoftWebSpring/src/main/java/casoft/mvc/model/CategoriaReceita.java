package casoft.mvc.model;

import casoft.mvc.dao.CategoriaReceitaDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriaReceita {
    private int id;
    private String nome;

    @Autowired
    CategoriaReceitaDAO dao;

    public CategoriaReceita(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public CategoriaReceita() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<CategoriaReceita> consultar(String filtro, Singleton conexao){
        return dao.get(filtro, conexao);
    }

    public CategoriaReceita consultar(int Id, Singleton conexao){
        return dao.get(Id, conexao);
    }

    public CategoriaReceita update(CategoriaReceita categoriaReceita, Singleton conexao){
        return dao.alterar(categoriaReceita,conexao);
    }

    public boolean delete(CategoriaReceita categoriaReceita, Singleton conexao){
        return dao.apagar(categoriaReceita, conexao);
    }

    public CategoriaReceita create(CategoriaReceita categoriaReceita, Singleton conexao){return dao.gravar(categoriaReceita, conexao);
    }
}
