package casoft.mvc.model;

import java.time.LocalDate;

public class Receitas {
    private double valor;
    private boolean futura;
    private String descricao;
    private Evento evento;
    private CategoriaReceita categoria;
    private LocalDate data;

    public Receitas(double valor, boolean futura, String descricao, Evento evento, CategoriaReceita categoria, LocalDate data) {
        this.valor = valor;
        this.futura = futura;
        this.descricao = descricao;
        this.evento = evento;
        this.categoria = categoria;
        this.data = data;
    }

    public Receitas(double valor, boolean futura, String descricao, CategoriaReceita categoria, LocalDate data) {
        this.valor = valor;
        this.futura = futura;
        this.descricao = descricao;
        this.categoria = categoria;
        this.data = data;
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

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public CategoriaReceita getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaReceita categoria) {
        this.categoria = categoria;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
