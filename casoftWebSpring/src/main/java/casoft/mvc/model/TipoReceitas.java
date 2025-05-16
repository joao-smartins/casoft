package casoft.mvc.model;

import casoft.mvc.dao.TipoReceitasDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TipoReceitas {
    private int id;
    private String nome;

    @Autowired
    TipoReceitasDAO dao;

    public TipoReceitas(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public TipoReceitas() {
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

    public List<TipoReceitas> consultar(String filtro, Singleton conexao){
        return dao.get(filtro, conexao);
    }

    public TipoReceitas consultar(int Id, Singleton conexao){
        return dao.get(Id, conexao);
    }

    public TipoReceitas update(TipoReceitas tipoReceitas, Singleton conexao){
        return dao.alterar(tipoReceitas,conexao);
    }

    public boolean delete(TipoReceitas tipoReceitas, Singleton conexao){
        return dao.apagar(tipoReceitas, conexao);
    }

    public TipoReceitas create(TipoReceitas tipoReceitas, Singleton conexao){return dao.gravar(tipoReceitas, conexao);
    }
}
