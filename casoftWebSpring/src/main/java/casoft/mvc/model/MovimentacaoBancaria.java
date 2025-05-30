package casoft.mvc.model;


import casoft.mvc.dao.MovimentacaoBancariaDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MovimentacaoBancaria {
    @Autowired
    private MovimentacaoBancariaDAO dao;

    private Integer id;
    private Double total;
    private LocalDate data;
    private Integer usuario_user_id;
    private Integer contabancaria_contab_id;

    public MovimentacaoBancaria() {
    }

    public MovimentacaoBancaria(Integer id, Double total, LocalDate data,
                                Integer usuario_user_id, Integer contabancaria_contab_id) {
        this.id = id;
        this.total = total;
        this.data = data;
        this.usuario_user_id = usuario_user_id;
        this.contabancaria_contab_id = contabancaria_contab_id;
    }

    public Integer getMovbancId() {
        return id;
    }

    public void setMovbancId(Integer id) {
        this.id = id;
    }

    public Double getMovbancTotal() {
        return total;
    }

    public void setMovbancTotal(Double total) {
        this.total = total;
    }

    public LocalDate getMovbancData() {
        return data;
    }

    public void setMovbancData(LocalDate data) {
        this.data = data;
    }

    public Integer getUsuarioUserId() {
        return usuario_user_id;
    }

    public void setUsuarioUserId(Integer usuario_user_id) {
        this.usuario_user_id = usuario_user_id;
    }

    public Integer getContabancariaContabId() {
        return contabancaria_contab_id;
    }

    public void setContabancariaContabId(Integer contabancaria_contab_id) {
        this.contabancaria_contab_id = contabancaria_contab_id;
    }

    // Consultar por ID
    public MovimentacaoBancaria consultar(int id, Singleton conexao) {
        return dao.get(id, conexao);
    }

    public List<MovimentacaoBancaria> consultarTodos(Singleton conexao) {
        return dao.get("", conexao);
    }

    public List<MovimentacaoBancaria> consultarComFiltro(String filtro, Singleton conexao) {
        return dao.consultar(filtro, conexao);
    }

    public boolean isEmpty(Singleton conexao) {
        return dao.get("", conexao).isEmpty();
    }

    public MovimentacaoBancaria gravar(MovimentacaoBancaria movimentacao, Singleton conexao) {
        return dao.gravar(movimentacao, conexao);
    }

    public MovimentacaoBancaria alterar(MovimentacaoBancaria movimentacao, Singleton conexao) {
        return dao.alterar(movimentacao, conexao);
    }

    public boolean apagar(MovimentacaoBancaria movimentacao, Singleton conexao) {
        return dao.apagar(movimentacao, conexao);
    }
}
