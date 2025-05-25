package casoft.mvc.dao;

import casoft.mvc.model.Conciliacao;
import casoft.mvc.util.Conexao;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ConciliacaoDAO
{
    private static final DateTimeFormatter SQL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Conciliacao gravar(Conciliacao entidade, Singleton conexao) {
        String sql = """
                INSERT INTO conciliacao (conc_dt_problema, conc_desc_problema, conc_dt_solucao, conc_desc_solucao, conc_receita_id, conc_despesa_id)
                VALUES ('#1', '#2', #3, #4, #5, #6)
                """;

        sql = sql.replace("#1", entidade.getConcDtProblema().format(SQL_DATE_FORMATTER));
        sql = sql.replace("#2", entidade.getConcDescProblema());

        if (entidade.getConcDtSolucao() != null) {
            sql = sql.replace("#3", "'" + entidade.getConcDtSolucao().format(SQL_DATE_FORMATTER) + "'");
        } else {
            sql = sql.replace("#3", "NULL");
        }

        if (entidade.getConcDescSolucao() != null && !entidade.getConcDescSolucao().isEmpty()) {
            sql = sql.replace("#4", "'" + entidade.getConcDescSolucao() + "'");
        } else {
            sql = sql.replace("#4", "NULL");
        }

        if (entidade.getConcReceitaId() != 0) {
            sql = sql.replace("#5", String.valueOf(entidade.getConcReceitaId()));
        } else {
            sql = sql.replace("#5", "NULL");
        }

        if (entidade.getConcDespesaId() != 0) {
            sql = sql.replace("#6", String.valueOf(entidade.getConcDespesaId()));
        } else {
            sql = sql.replace("#6", "NULL");
        }

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro ao gravar conciliação: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    public Conciliacao alterar(Conciliacao entidade, Singleton conexao) {
        String sql = """
                UPDATE conciliacao SET
                conc_dt_problema = '#1',
                conc_desc_problema = '#2',
                conc_dt_solucao = #3,
                conc_desc_solucao = #4,
                conc_receita_id = #5,
                conc_despesa_id = #6
                WHERE conc_id = #7
                """;

        sql = sql.replace("#1", "'" + entidade.getConcDtProblema().format(SQL_DATE_FORMATTER) + "'");
        sql = sql.replace("#2", "'" + entidade.getConcDescProblema() + "'");

        if (entidade.getConcDtSolucao() != null) {
            sql = sql.replace("#3", "'" + entidade.getConcDtSolucao().format(SQL_DATE_FORMATTER) + "'");
        } else {
            sql = sql.replace("#3", "NULL");
        }

        if (entidade.getConcDescSolucao() != null && !entidade.getConcDescSolucao().isEmpty()) {
            sql = sql.replace("#4", "'" + entidade.getConcDescSolucao() + "'");
        } else {
            sql = sql.replace("#4", "NULL");
        }

        if (entidade.getConcReceitaId() != 0) {
            sql = sql.replace("#5", String.valueOf(entidade.getConcReceitaId()));
        } else {
            sql = sql.replace("#5", "NULL");
        }

        if (entidade.getConcDespesaId() != 0) {
            sql = sql.replace("#6", String.valueOf(entidade.getConcDespesaId()));
        } else {
            sql = sql.replace("#6", "NULL");
        }

        sql = sql.replace("#7", String.valueOf(entidade.getConcId()));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro ao alterar conciliação: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    public boolean apagar(Conciliacao entidade, Singleton conexao) {
        String sql = "DELETE FROM public.conciliacao WHERE conc_id = " + entidade.getConcId();
        return conexao.getConexao().manipular(sql);
    }

    public Conciliacao get(int id, Singleton conexao) {
        String sql = "SELECT * FROM public.conciliacao WHERE conc_id = " + id;
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                Conciliacao conc = new Conciliacao();
                conc.setConcId(rs.getInt("conc_id"));
                conc.setConcDtProblema(rs.getObject("conc_dt_problema", LocalDate.class));
                conc.setConcDescProblema(rs.getString("conc_desc_problema"));
                conc.setConcDtSolucao(rs.getObject("conc_dt_solucao", LocalDate.class));
                conc.setConcDescSolucao(rs.getString("conc_desc_solucao"));

                int receitaId = rs.getInt("conc_receita_id");
                if (rs.wasNull()) {
                    conc.setConcReceitaId(0);
                } else {
                    conc.setConcReceitaId(receitaId);
                }

                int despesaId = rs.getInt("conc_despesa_id");
                if (rs.wasNull()) {
                    conc.setConcDespesaId(0);
                } else {
                    conc.setConcDespesaId(despesaId);
                }
                return conc;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar conciliação por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Conciliacao> get(String filtro, Singleton conexao) {
        List<Conciliacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM public.conciliacao";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Conciliacao conc = new Conciliacao();
                conc.setConcId(rs.getInt("conc_id"));
                conc.setConcDtProblema(rs.getObject("conc_dt_problema", LocalDate.class));
                conc.setConcDescProblema(rs.getString("conc_desc_problema"));
                conc.setConcDtSolucao(rs.getObject("conc_dt_solucao", LocalDate.class));
                conc.setConcDescSolucao(rs.getString("conc_desc_solucao"));

                int receitaId = rs.getInt("conc_receita_id");
                if (rs.wasNull()) {
                    conc.setConcReceitaId(0);
                } else {
                    conc.setConcReceitaId(receitaId);
                }

                int despesaId = rs.getInt("conc_despesa_id");
                if (rs.wasNull()) {
                    conc.setConcDespesaId(0);
                } else {
                    conc.setConcDespesaId(despesaId);
                }
                lista.add(conc);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar conciliações: " + e.getMessage());
            return null;
        }
        return lista;
    }

    public boolean isEmpty(Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM public.conciliacao";
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar se conciliações estão vazias: " + e.getMessage());
            return true;
        }
        return true;
    }

    public List<Map<String, Object>> getItensNaoConciliados(Singleton conexao) {
        List<Map<String, Object>> resultado = new ArrayList<>();
        String sql = """
            SELECT
                'Receita' AS tipo,
                r.receita_id AS id,
                r.receita_val AS valor,
                r.receita_desc AS descricao,
                r.receita_statusconci AS statusConciliacao
            FROM public.receita r
            WHERE (r.receita_statusconci IS NULL OR r.receita_statusconci != 'Conciliado')
            AND r.receita_id NOT IN (SELECT conc_receita_id FROM public.conciliacao WHERE conc_receita_id IS NOT NULL)

            UNION ALL

            SELECT
                'Despesa' AS tipo,
                d.despesa_id AS id,
                d.despesa_val AS valor,
                d.despesa_desc AS descricao,
                d.despesa_statusconci AS statusConciliacao
            FROM public.despesa d
            WHERE (d.despesa_statusconci IS NULL OR d.despesa_statusconci != 'Conciliado')
            AND d.despesa_id NOT IN (SELECT conc_despesa_id FROM public.conciliacao WHERE conc_despesa_id IS NOT NULL)
            ORDER BY tipo, id
            """;

        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("tipo", rs.getString("tipo"));
                item.put("id", rs.getInt("id"));
                item.put("valor", rs.getDouble("valor"));
                item.put("descricao", rs.getString("descricao"));
                item.put("statusConciliacao", rs.getString("statusConciliacao"));
                resultado.add(item);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar itens não conciliados no DAO: " + e.getMessage());
            return new ArrayList<>();
        }
        return resultado;
    }

    public boolean marcarDespesaComoConciliada(int despesaId, Singleton conexao) {
        String sql = "UPDATE public.despesa SET despesa_statusconci = 'Conciliado' WHERE despesa_id = " + despesaId;
        return conexao.getConexao().manipular(sql);
    }

    public boolean marcarReceitaComoConciliada(int receitaId, Singleton conexao) {
        String sql = "UPDATE public.receita SET receita_statusconci = 'Conciliado' WHERE receita_id = " + receitaId;
        return conexao.getConexao().manipular(sql);
    }


}