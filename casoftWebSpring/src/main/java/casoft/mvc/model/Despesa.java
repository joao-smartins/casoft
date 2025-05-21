package casoft.mvc.model;

import casoft.mvc.dao.DespesaDAO;
import casoft.mvc.util.Singleton;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
@Repository
public class Despesa {

    @Autowired
    private DespesaDAO despesaDAO;

    private int id;
    private double valor;
    private String data_venc;
    private String data_lanc;
    private double pagamento;
    private String descricao;
    private String status_conci;
    private int tipoDespesa_id;
    private int usuario_id;
    private int evento_id;

    public Despesa() {
    }

    public Despesa(double valor, String data_venc, String data_lanc, double pagamento, String descricao, String status_conci, int tipoDespesa_id, int usuario_id, int evento_id) {
        this.valor = valor;
        this.data_venc = data_venc;
        this.data_lanc = data_lanc;
        this.pagamento = pagamento;
        this.descricao = descricao;
        this.status_conci = status_conci;
        this.tipoDespesa_id = tipoDespesa_id;
        this.usuario_id = usuario_id;
        this.evento_id = evento_id;
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

    public String getData_venc() {
        return data_venc;
    }

    public void setData_venc(String data_venc) {
        this.data_venc = data_venc;
    }

    public String getData_lanc() {
        return data_lanc;
    }

    public void setData_lanc(String data_lanc) {
        this.data_lanc = data_lanc;
    }

    public double getPagamento() {
        return pagamento;
    }

    public void setPagamento(double pagamento) {
        this.pagamento = pagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus_conci() {
        return status_conci;
    }

    public void setStatus_conci(String status_conci) {
        this.status_conci = status_conci;
    }

    public int getTipoDespesa_id() {
        return tipoDespesa_id;
    }

    public void setTipoDespesa_id(int tipoDespesa_id) {
        this.tipoDespesa_id = tipoDespesa_id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getEvento_id() {
        return evento_id;
    }

    public void setEvento_id(int evento_id) {
        this.evento_id = evento_id;
    }

    public Despesa add(Despesa despesa, Singleton conexao) {
        return despesaDAO.gravar(despesa,conexao);
    }
}
