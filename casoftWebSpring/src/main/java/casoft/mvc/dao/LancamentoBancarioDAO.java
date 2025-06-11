package casoft.mvc.dao;

import casoft.mvc.model.LancamentoBancario;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LancamentoBancarioDAO implements IDAO<LancamentoBancario> {

    public LancamentoBancario gravar(LancamentoBancario entidade, Singleton conexao) {
        String sql = """
                INSERT INTO lancamentosbancarios (
                    lanbanc_dtlancamento, 
                    lanbanc_desc, 
                    lanbanc_origem, 
                    lanbanc_dest, 
                    contabancaria_contab_id, 
                    movimentacaobancaria_movbanc_id, 
                    receita_receita_id, 
                    despesa_despesa_id
                ) VALUES (
                    '#1', '#2', '#3', '#4', #5, #6, #7, #8
                );
                """;

        sql = sql.replace("#1", entidade.getDataLancamento().toString());
        sql = sql.replace("#2", entidade.getDescricao());
        sql = sql.replace("#3", entidade.getOrigem());
        sql = sql.replace("#4", entidade.getDestino());
        sql = sql.replace("#5", String.valueOf(entidade.getContaBancariaId()));
        sql = sql.replace("#6", String.valueOf(entidade.getMovimentacaoBancariaId()));
        sql = sql.replace("#7", String.valueOf(entidade.getReceitaId()));
        sql = sql.replace("#8", String.valueOf(entidade.getDespesaId()));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    public List<LancamentoBancario> consultar(String filtro, Singleton conexao) {
        List<LancamentoBancario> lancamentos = new ArrayList<>();
        String sql = "SELECT * FROM lancamentosbancarios";

        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }

        try( var rs = conexao.getConexao().consultar(sql)) {
            while (rs.next()) {
                LancamentoBancario d = new LancamentoBancario();
                d.setLancamentoId(rs.getInt("lanbanc_id"));
                d.setDataLancamento(rs.getDate("lanbanc_dtlancamento").toLocalDate());
                d.setDescricao(rs.getString("lanbanc_desc"));
                d.setOrigem(rs.getString("lanbanc_origem"));
                d.setDestino(rs.getString("lanbanc_dest"));
                d.setContaBancariaId(rs.getInt("contabancaria_contab_id"));
                d.setMovimentacaoBancariaId(rs.getInt("movimentacaobancaria_movbanc_id"));
                d.setReceitaId(rs.getInt("receita_receita_id"));
                d.setDespesaId(rs.getInt("despesa_despesa_id"));
                lancamentos.add(d);
            }
        } catch (Exception er) {
            System.out.println("Erro ao carregar lançamentos: " + er.getMessage());
        }
        return lancamentos;
    }

    @Override
    public boolean apagar(LancamentoBancario entidade, Singleton conexao) {
        String sql = "DELETE FROM lancamentosbancarios WHERE lanbanc_id = " + entidade.getLancamentoId() +
                " AND movimentacaobancaria_movbanc_id = " + entidade.getMovimentacaoBancariaId();
        return conexao.getConexao().manipular(sql);
    }


    @Override
    public LancamentoBancario get(int id, Singleton conexao) {
        String sql = "SELECT * FROM lancamentosbancarios WHERE lanbanc_id = " + id;
        try (var rs = conexao.getConexao().consultar(sql)) {
            try {
                if (rs.next()) {
                    LancamentoBancario lancamento = new LancamentoBancario();
                    lancamento.setLancamentoId(rs.getInt("lanbanc_id"));
                    lancamento.setDataLancamento(rs.getDate("lanbanc_dtlancamento").toLocalDate());
                    lancamento.setDescricao(rs.getString("lanbanc_desc"));
                    lancamento.setOrigem(rs.getString("lanbanc_origem"));
                    lancamento.setDestino(rs.getString("lanbanc_dest"));
                    lancamento.setContaBancariaId(rs.getInt("contabancaria_contab_id"));
                    lancamento.setMovimentacaoBancariaId(rs.getInt("movimentacaobancaria_movbanc_id"));
                    lancamento.setReceitaId(rs.getInt("receita_receita_id"));
                    lancamento.setDespesaId(rs.getInt("despesa_despesa_id"));
                    return lancamento;
                }
            } catch (Exception e) {
                System.out.println("Erro ao buscar lançamento: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<LancamentoBancario> get(String filtro, Singleton conexao) {
        return consultar(filtro, conexao);
    }

    @Override
    public LancamentoBancario alterar(LancamentoBancario entidade, Singleton conexao) {
        String sql = """
                UPDATE lancamentosbancarios SET
                    lanbanc_dtlancamento = '#1',
                    lanbanc_desc = '#2',
                    lanbanc_origem = '#3',
                    lanbanc_dest = '#4',
                    contabancaria_contab_id = #5,
                    movimentacaobancaria_movbanc_id = #6,
                    receita_receita_id = #7,
                    despesa_despesa_id = #8
                WHERE lanbanc_id = #9
                """;

        sql = sql.replace("#1", entidade.getDataLancamento().toString())
                .replace("#2", entidade.getDescricao())
                .replace("#3", entidade.getOrigem())
                .replace("#4", entidade.getDestino())
                .replace("#5", String.valueOf(entidade.getContaBancariaId()))
                .replace("#6", String.valueOf(entidade.getMovimentacaoBancariaId()))
                .replace("#7", String.valueOf(entidade.getReceitaId()))
                .replace("#8", String.valueOf(entidade.getDespesaId()))
                .replace("#9", String.valueOf(entidade.getLancamentoId()));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }
}