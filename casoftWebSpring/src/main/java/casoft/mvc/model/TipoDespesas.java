package casoft.mvc.model;

import casoft.mvc.dao.TipoDespesasDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TipoDespesas
{

    @Autowired
    private TipoDespesasDAO dao;

    private int id;
    private String nome;


    public TipoDespesas()
    {

    }

    public TipoDespesas(int id, String nome)
    {
        this.id = id;
        this.nome = nome;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public TipoDespesas consultar(int id, Singleton conexao)
    {
        return dao.get(id,conexao);
    }

    public boolean isEmpty(Singleton conexao)
    {
        return dao.get("",conexao).isEmpty();
    }

    public TipoDespesas gravar(TipoDespesas tipo, Singleton conexao)
    {
        return dao.gravar(tipo,conexao);
    }

    public TipoDespesas alterar(TipoDespesas tipo, Singleton conexao)
    {
        return dao.alterar(tipo,conexao);
    }

    public List<TipoDespesas> consultar(String filtro, Singleton conexao)
    {
        return dao.get(filtro,conexao);
    }

    public boolean apagar(TipoDespesas tipo, Singleton conexao){
        return dao.apagar(tipo,conexao);
    }

    public TipoDespesas consultarID(int id, Singleton conexao){
        return dao.get(id,conexao);
    }


}
