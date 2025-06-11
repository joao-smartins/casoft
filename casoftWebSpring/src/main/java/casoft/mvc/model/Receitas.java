package casoft.mvc.model;

import casoft.mvc.dao.ReceitasDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class Receitas {
    private int id;
    private double valor;
    private boolean futura;
    private String descricao;
    private int eventoId;
    private int categoria;
    private LocalDate datavencimento;
    private boolean quitada;
    private String statusConciliacao;
    private int pagamento;
    private Integer usuario_id;

    @Autowired
    private ReceitasDAO receitasDAO;

    public Receitas() {
    }

    public Receitas(double valor, boolean futura, String descricao, int eventoId,
                    int categoria, LocalDate datavencimento, boolean quitada,
                    String statusConciliacao, int pagamento, Integer usuario_id) {
        this.valor = valor;
        this.futura = futura;
        this.descricao = descricao;
        this.eventoId = eventoId;
        this.categoria = categoria;
        this.datavencimento = datavencimento;
        this.quitada = quitada;
        this.statusConciliacao = statusConciliacao;
        this.pagamento = pagamento;
        this.usuario_id = usuario_id;
    }

    public Receitas(int id, double valor, boolean futura, String descricao, int eventoId,
                    int categoria, LocalDate datavencimento, boolean quitada,
                    String statusConciliacao, int pagamento, Integer usuario_id) {
        this.id = id;
        this.valor = valor;
        this.futura = futura;
        this.descricao = descricao;
        this.eventoId = eventoId;
        this.categoria = categoria;
        this.datavencimento = datavencimento;
        this.quitada = quitada;
        this.statusConciliacao = statusConciliacao;
        this.pagamento = pagamento;
        this.usuario_id = usuario_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isFutura() {
        return futura;
    }

    public void setFutura(boolean futura) {
        this.futura = futura;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public LocalDate getDatavencimento() {
        return datavencimento;
    }

    public void setDatavencimento(LocalDate datavencimento) {
        this.datavencimento = datavencimento;
    }

    public boolean isQuitada() {
        return quitada;
    }

    public void setQuitada(boolean quitada) {
        this.quitada = quitada;
    }

    public String getStatusConciliacao() {
        return statusConciliacao;
    }

    public void setStatusConciliacao(String statusConciliacao) {
        this.statusConciliacao = statusConciliacao;
    }

    public int getPagamento() {
        return pagamento;
    }

    public void setPagamento(int pagamento) {
        this.pagamento = pagamento;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

    public Receitas alterar(Receitas entidade, Singleton conexao) {return receitasDAO.alterar(entidade, conexao);}
    public List<Receitas> consultar(String filtro, Singleton conexao) {return receitasDAO.consultar(filtro,conexao);}
    public Receitas get(int id, Singleton conexao) {return receitasDAO.consultar(id,conexao);}
    public boolean apagar(int id, Singleton conexao) {return receitasDAO.apagar(id,conexao);}
    public Receitas gravar(Receitas entidade, Singleton conexao) {return receitasDAO.gravar(entidade,conexao);}



    }