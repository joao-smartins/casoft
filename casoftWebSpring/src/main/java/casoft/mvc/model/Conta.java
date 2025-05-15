package casoft.mvc.model;

import casoft.mvc.dao.ContaDAO;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Conta {

    @Autowired
    private ContaDAO dao;

    private int id;
    private int agencia;
    private String numero;
    private String banco;
    private String telefone;
    private String endereco;
    private int ende_num;
    private String gerente;


    public Conta(int id, int agencia, String numero, String banco, String telefone,
                         String endereco, int ende_num, String gerente) {
        this.id = id;
        this.agencia = agencia;
        this.numero = numero;
        this.banco = banco;
        this.telefone = telefone;
        this.endereco = endereco;
        this.ende_num = ende_num;
        this.gerente = gerente;
    }

    public Conta(){

    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getEnde_num() {
        return ende_num;
    }

    public void setEnde_num(int ende_num) {
        this.ende_num = ende_num;
    }

    public String getGerente() {
        return gerente;
    }

    public void setGerente(String gerente) {
        this.gerente = gerente;
    }

    public Conta consultar(int id_conta, Singleton conexao){
        return dao.get(id_conta,conexao);
    }
    public boolean isEmpty(Singleton conexao){
        return dao.get("",conexao).isEmpty();
    }
    public Conta gravar(Conta conta, Singleton conexao){
        return dao.gravar(conta,conexao);
    }
    public Conta alterar(Conta conta, Singleton conexao){
        return dao.alterar(conta,conexao);
    }
    public boolean deletarConta(Conta conta,Singleton conexao){
        return dao.apagar(conta,conexao);
    }

}

