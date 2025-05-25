package casoft.mvc.model;

import casoft.mvc.dao.ConciliacaoDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class Conciliacao
{
    @Autowired
    private ConciliacaoDAO dao;

    private int concId;
    private LocalDate concDtProblema;
    private String concDescProblema;
    private LocalDate concDtSolucao;
    private String concDescSolucao;
    private int concReceitaId;
    private int concDespesaId;

    public Conciliacao() {

    }

    public Conciliacao(int concId, LocalDate concDtProblema, String concDescProblema,
                       LocalDate concDtSolucao, String concDescSolucao,
                       int concReceitaId, int concDespesaId) {
        this.concId = concId;
        this.concDtProblema = concDtProblema;
        this.concDescProblema = concDescProblema;
        this.concDtSolucao = concDtSolucao;
        this.concDescSolucao = concDescSolucao;
        this.concReceitaId = concReceitaId;
        this.concDespesaId = concDespesaId;
    }

    // Getters e Setters
    public int getConcId() {
        return concId;
    }

    public void setConcId(int concId) {
        this.concId = concId;
    }

    public LocalDate getConcDtProblema() {
        return concDtProblema;
    }

    public void setConcDtProblema(LocalDate concDtProblema) {
        this.concDtProblema = concDtProblema;
    }

    public String getConcDescProblema() {
        return concDescProblema;
    }

    public void setConcDescProblema(String concDescProblema) {
        this.concDescProblema = concDescProblema;
    }

    public LocalDate getConcDtSolucao() {
        return concDtSolucao;
    }

    public void setConcDtSolucao(LocalDate concDtSolucao) {
        this.concDtSolucao = concDtSolucao;
    }

    public String getConcDescSolucao() {
        return concDescSolucao;
    }

    public void setConcDescSolucao(String concDescSolucao) {
        this.concDescSolucao = concDescSolucao;
    }

    public int getConcReceitaId() {
        return concReceitaId;
    }

    public void setConcReceitaId(int concReceitaId) {
        this.concReceitaId = concReceitaId;
    }

    public int getConcDespesaId() {
        return concDespesaId;
    }

    public void setConcDespesaId(int concDespesaId) {
        this.concDespesaId = concDespesaId;
    }

    public Conciliacao gravar(Conciliacao conc, Singleton conexao) {
        return dao.gravar(conc, conexao);
    }

    public Conciliacao alterar(Conciliacao conc, Singleton conexao) {
        return dao.alterar(conc, conexao);
    }

    public boolean apagar(Conciliacao conc, Singleton conexao) {
        return dao.apagar(conc, conexao);
    }

    public Conciliacao consultar(int id, Singleton conexao) {
        return dao.get(id, conexao);
    }

    public List<Conciliacao> consultar(String filtro, Singleton conexao) {
        return dao.get(filtro, conexao);
    }

    public boolean isEmpty(Singleton conexao) {
        return dao.isEmpty(conexao);
    }

    public List<Map<String, Object>> consultarItensNaoConciliados(Singleton conexao) {
        return dao.getItensNaoConciliados(conexao);
    }

    public boolean marcarDespesaComoConciliada(int despesaId, Singleton conexao) {
        return dao.marcarDespesaComoConciliada(despesaId, conexao);
    }

    public boolean marcarReceitaComoConciliada(int receitaId, Singleton conexao) {
        return dao.marcarReceitaComoConciliada(receitaId, conexao);
    }

}