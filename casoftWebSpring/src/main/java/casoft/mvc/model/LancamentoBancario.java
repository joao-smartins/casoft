package casoft.mvc.model;

import casoft.mvc.dao.LancamentoBancarioDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class LancamentoBancario {

    @Autowired
    private LancamentoBancarioDAO dao;

    private Integer id;
    private LocalDate dt_lanc;
    private String descricao;
    private String origem;
    private String destino;
    private Integer contaBancariaId;
    private Integer movimentacaoBancariaId;
    private Integer receitaId;
    private Integer despesaId;

    public LancamentoBancario() {
    }

    public LancamentoBancario(Integer id, LocalDate dt_lanc, String descricao,
                              String origem, String destino, Integer contaBancariaId,
                              Integer movimentacaoBancariaId, Integer receitaId, Integer despesaId) {
        this.id = id;
        this.dt_lanc = dt_lanc;
        this.descricao = descricao;
        this.origem = origem;
        this.destino = destino;
        this.contaBancariaId = contaBancariaId;
        this.movimentacaoBancariaId = movimentacaoBancariaId;
        this.receitaId = receitaId;
        this.despesaId = despesaId;
    }

    public Integer getLancamentoId() {
        return id;
    }

    public void setLancamentoId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataLancamento() {
        return dt_lanc;
    }

    public void setDataLancamento(LocalDate dt_lanc) {
        this.dt_lanc = dt_lanc;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Integer getContaBancariaId() {
        return contaBancariaId;
    }

    public void setContaBancariaId(Integer contaBancariaId) {
        this.contaBancariaId = contaBancariaId;
    }

    public Integer getMovimentacaoBancariaId() {
        return movimentacaoBancariaId;
    }

    public void setMovimentacaoBancariaId(Integer movimentacaoBancariaId) {
        this.movimentacaoBancariaId = movimentacaoBancariaId;
    }

    public Integer getReceitaId() {
        return receitaId;
    }

    public void setReceitaId(Integer receitaId) {
        this.receitaId = receitaId;
    }

    public Integer getDespesaId() {
        return despesaId;
    }

    public void setDespesaId(Integer despesaId) {
        this.despesaId = despesaId;
    }

    
    // Consultar por ID
    public LancamentoBancario consultar(int id, Singleton conexao) {
        return dao.get(id, conexao);
    }

    public List<LancamentoBancario> consultarTodos(Singleton conexao) {
        return dao.get("", conexao); // ou dao.consultar("", conexao);
    }

    public List<LancamentoBancario> consultarComFiltro(String filtro, Singleton conexao) {
        return dao.consultar(filtro, conexao);
    }

    public boolean isEmpty(Singleton conexao) {
        return dao.get("", conexao).isEmpty();
    }

    public LancamentoBancario gravar(LancamentoBancario lancamento, Singleton conexao) {
        return dao.gravar(lancamento, conexao);
    }

    public LancamentoBancario alterar(LancamentoBancario lancamento, Singleton conexao) {
        return dao.alterar(lancamento, conexao);
    }

    public boolean apagar(LancamentoBancario lancamento, Singleton conexao) {
        return dao.apagar(lancamento, conexao);
    }

}
