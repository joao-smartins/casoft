package casoft.mvc.dao;

import casoft.mvc.model.Conta;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContaDAO implements IDAO<Conta>{

    @Override
    public Conta gravar(Conta entidade, Singleton conexao) {
        String sql = """
        INSERT INTO contabancaria(contab_agencia, contab_numero, contab_banco, contab_telefone, contab_endereco, contab_ende_num, contab_gerente)\s
        VALUES ('#1', '#2', '#3', '#4', '#5', '#6', '#7');
       \s""";

        sql = sql.replace("#1", String.valueOf(entidade.getAgencia()))
                .replace("#2", entidade.getNumero())
                .replace("#3", entidade.getBanco())
                .replace("#4", entidade.getTelefone())
                .replace("#5", entidade.getEndereco())
                .replace("#6", String.valueOf(entidade.getEnde_num()))
                .replace("#7", entidade.getGerente());

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Conta alterar(Conta entidade, Singleton conexao) {
        String sql = """
        UPDATE contabancaria SET\s
           contab_agencia = '#1',
           contab_numero = '#2',
           contab_banco = '#3',
           contab_telefone = '#4',
           contab_endereco = '#5',
           contab_ende_num = '#6',
           contab_gerente = '#7'
        WHERE contab_id = #8;
       \s""";

        sql = sql.replace("#1", String.valueOf(entidade.getAgencia()))
                .replace("#2", entidade.getNumero())
                .replace("#3", entidade.getBanco())
                .replace("#4", entidade.getTelefone())
                .replace("#5", entidade.getEndereco())
                .replace("#6", String.valueOf(entidade.getEnde_num()))
                .replace("#7", entidade.getGerente())
                .replace("#8", String.valueOf(entidade.getId()));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Conta get(int id, Singleton conexao) {
        String sql = "SELECT * FROM contabancaria WHERE contab_id = " + id;
        try (var rs = conexao.getConexao().consultar(sql)) {
            try {
                if (rs.next()) {
                    Conta conta = new Conta();
                    conta.setId(rs.getInt("contab_id"));
                    conta.setAgencia(rs.getInt("contab_agencia"));
                    conta.setNumero(rs.getString("contab_numero"));
                    conta.setBanco(rs.getString("contab_banco"));
                    conta.setTelefone(rs.getString("contab_telefone"));
                    conta.setEndereco(rs.getString("contab_endereco"));
                    conta.setEnde_num(rs.getInt("contab_ende_num"));
                    conta.setGerente(rs.getString("contab_gerente"));

                    return conta;
                }
            } catch (Exception e) {
                System.out.println("Erro ao buscar conta: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Conta> get(String filtro, Singleton conexao) {
        List<Conta> lista = new ArrayList<>();
        String sql = "SELECT * FROM contabancaria";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }

        try (var rs = conexao.getConexao().consultar(sql)) {
            while (rs.next()) {
                Conta conta = new Conta();
                conta.setId(rs.getInt("id"));
                conta.setAgencia(rs.getInt("agencia"));
                conta.setNumero(rs.getString("numero"));
                conta.setBanco(rs.getString("banco"));
                conta.setTelefone(rs.getString("telefone"));
                conta.setEndereco(rs.getString("endereco"));
                conta.setEnde_num(rs.getInt("ende_num"));
                conta.setGerente(rs.getString("gerente"));

                lista.add(conta);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar contas: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean apagar(Conta entidade, Singleton conexao) {
        if (entidade == null || entidade.getId() <= 0) {
            System.out.println("Conta inválida para exclusão.");
            return false;
        }
        String sql = "DELETE FROM contabancaria WHERE contab_id = " + entidade.getId();
        return conexao.getConexao().manipular(sql);
    }


    public boolean isEmpty(Singleton conexao) {
        String sql = "SELECT 1 FROM contabancaria";
        try (ResultSet rs = conexao.getConexao().consultar(sql)) {
            return !rs.next();
        } catch (SQLException e) {
            return true;
        }
    }


}
