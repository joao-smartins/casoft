package casoft.mvc.model;

import casoft.mvc.dao.DespesaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
@Component
public class Despesa {

    @Autowired
    private DespesaDAO despesaDAO;

    private int id;
    private double valor;
    private LocalDate data_venc;
    private LocalDate data_lanc;
    private double pagamento;
    private String descricao;
    private boolean status_conci;
    private TipoDespesas tipoDespesa;
    private Usuario usuario;
    private Evento evento;

    public Despesa(double valor, LocalDate data_venc, LocalDate data_lanc, Double pagamento, String descricao, boolean status_conci, TipoDespesas tipoDespesa, Usuario usuario, Evento evento) {
        this.valor = valor;
        this.data_venc = data_venc;
        this.data_lanc = data_lanc;
        this.pagamento = pagamento;
        this.descricao = descricao;
        this.status_conci = status_conci;
        this.tipoDespesa = tipoDespesa;
        this.usuario = usuario;
        this.evento = evento;
    }

    public Despesa() {
    }

    public Despesa(int id, double valor, LocalDate data_venc, LocalDate data_lanc, double pagamento, String descricao, boolean status_conci, TipoDespesas tipoDespesa, Usuario usuario, Evento evento) {
        this.id = id;
        this.valor = valor;
        this.data_venc = data_venc;
        this.data_lanc = data_lanc;
        this.pagamento = pagamento;
        this.descricao = descricao;
        this.status_conci = status_conci;
        this.tipoDespesa = tipoDespesa;
        this.usuario = usuario;
        this.evento = evento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData_venc() {
        return data_venc;
    }

    public void setData_venc(LocalDate data_venc) {
        this.data_venc = data_venc;
    }

    public LocalDate getData_lanc() {
        return data_lanc;
    }

    public void setData_lanc(LocalDate data_lanc) {
        this.data_lanc = data_lanc;
    }

    public Double getPagamento() {
        return pagamento;
    }

    public void setPagamento(Double pagamento) {
        this.pagamento = pagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isStatus_conci() {
        return status_conci;
    }

    public void setStatus_conci(boolean status_conci) {
        this.status_conci = status_conci;
    }

    public TipoDespesas getTipoDespesa() {
        return tipoDespesa;
    }

    public void setTipoDespesa(TipoDespesas tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
