package casoft.mvc.model;

import java.time.LocalDate;

public class Despesas {
    private double valor;
    private boolean futura;
    private LocalDate data;
    private String descricao;
    private Evento evento;
    private CategoriaDespesa categoria;


    public Despesas(double valor, boolean futura, LocalDate data, String descricao, Evento evento, CategoriaDespesa categoria) {
        this.valor = valor;
        this.futura = futura;
        this.data = data;
        this.descricao = descricao;
        this.evento = evento;
        this.categoria = categoria;
    }

    public Despesas(double valor, boolean futura, LocalDate data, String descricao, CategoriaDespesa categoria) {
        this.valor = valor;
        this.futura = futura;
        this.data = data;
        this.descricao = descricao;
        this.categoria = categoria;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
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

    public CategoriaDespesa getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }
}
