package casoft.mvc.dao;

import casoft.mvc.model.MovimentacaoBancaria;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovimentacaoBancariaDAO implements IDAO<MovimentacaoBancaria> {

    public MovimentacaoBancaria gravar(MovimentacaoBancaria entidade, Singleton conexao) {
        String sql = """
                INSERT INTO movimentacaobancaria (
                    movbanc_data,
                    movbanc_total,
                    usuario_user_id,
                    contabancaria_contab_id
                ) VALUES (
                    '#1', #2, #3, #4
                );
                """;

        sql = sql.replace("#1", entidade.getMovbancData().toString());
        sql = sql.replace("#2", String.valueOf(entidade.getMovbancTotal()));
        sql = sql.replace("#3", String.valueOf(entidade.getUsuarioUserId()));
        sql = sql.replace("#4", String.valueOf(entidade.getContabancariaContabId()));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    public List<MovimentacaoBancaria> consultar(String filtro, Singleton conexao) {
        List<MovimentacaoBancaria> movimentacoes = new ArrayList<>();
        String sql = "SELECT * FROM movimentacaobancaria";

        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }

        try (var rs = conexao.getConexao().consultar(sql)) {
            while (rs.next()) {
                MovimentacaoBancaria m = new MovimentacaoBancaria();
                m.setMovbancId(rs.getInt("movbanc_id"));
                m.setMovbancData(rs.getDate("movbanc_data").toLocalDate());
                m.setMovbancTotal(rs.getDouble("movbanc_total"));
                m.setUsuarioUserId(rs.getInt("usuario_user_id"));
                m.setContabancariaContabId(rs.getInt("contabancaria_contab_id"));
                movimentacoes.add(m);
            }
        } catch (Exception er) {
            System.out.println("Erro ao carregar movimentações: " + er.getMessage());
        }
        return movimentacoes;
    }

    @Override
    public boolean apagar(MovimentacaoBancaria entidade, Singleton conexao) {
        String sql = "DELETE FROM movimentacaobancaria WHERE movbanc_id = " + entidade.getMovbancId();
        return conexao.getConexao().manipular(sql);
    }

    @Override
    public MovimentacaoBancaria get(int id, Singleton conexao) {
        String sql = "SELECT * FROM movimentacaobancaria WHERE movbanc_id = " + id;
        try (var rs = conexao.getConexao().consultar(sql)) {
            try {
                if (rs.next()) {
                    MovimentacaoBancaria movimentacao = new MovimentacaoBancaria();
                    movimentacao.setMovbancId(rs.getInt("movbanc_id"));
                    movimentacao.setMovbancData(rs.getDate("movbanc_data").toLocalDate());
                    movimentacao.setMovbancTotal(rs.getDouble("movbanc_total"));
                    movimentacao.setUsuarioUserId(rs.getInt("usuario_user_id"));
                    movimentacao.setContabancariaContabId(rs.getInt("contabancaria_contab_id"));
                    return movimentacao;
                }
            } catch (Exception e) {
                System.out.println("Erro ao buscar movimentação: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<MovimentacaoBancaria> get(String filtro, Singleton conexao) {
        return consultar(filtro, conexao);
    }

    @Override
    public MovimentacaoBancaria alterar(MovimentacaoBancaria entidade, Singleton conexao) {
        String sql = """
                UPDATE movimentacaobancaria SET
                    movbanc_data = '#1',
                    movbanc_total = #2,
                    usuario_user_id = #3,
                    contabancaria_contab_id = #4
                WHERE movbanc_id = #5
                """;

        sql = sql.replace("#1", entidade.getMovbancData().toString())
                .replace("#2", String.valueOf(entidade.getMovbancTotal()))
                .replace("#3", String.valueOf(entidade.getUsuarioUserId()))
                .replace("#4", String.valueOf(entidade.getContabancariaContabId()))
                .replace("#5", String.valueOf(entidade.getMovbancId()));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }
}
