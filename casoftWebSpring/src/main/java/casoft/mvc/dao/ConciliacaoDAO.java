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
        sql = sql.replace("#2", entidade.getConcDescProblema().replaceAll("'", "''"));

        if (entidade.getConcDtSolucao() != null) {
            sql = sql.replace("#3", "'" + entidade.getConcDtSolucao().format(SQL_DATE_FORMATTER) + "'");
        } else {
            sql = sql.replace("#3", "NULL");
        }

        if (entidade.getConcDescSolucao() != null && !entidade.getConcDescSolucao().isEmpty()) {
            sql = sql.replace("#4", "'" + entidade.getConcDescSolucao().replaceAll("'", "''") + "'"); // Adicionado .replaceAll("'", "''")
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
        sql = sql.replace("#2", "'" + entidade.getConcDescProblema().replaceAll("'", "''") + "'");

        if (entidade.getConcDtSolucao() != null) {
            sql = sql.replace("#3", "'" + entidade.getConcDtSolucao().format(SQL_DATE_FORMATTER) + "'");
        } else {
            sql = sql.replace("#3", "NULL");
        }

        if (entidade.getConcDescSolucao() != null && !entidade.getConcDescSolucao().isEmpty()) {
            sql = sql.replace("#4", "'" + entidade.getConcDescSolucao().replaceAll("'", "''") + "'");
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


    public List<Conciliacao> getProblemas(String filtro, Singleton conexao) {
        List<Conciliacao> listaProblemas = new ArrayList<>();
        ResultSet rs = null;

        String sql = "SELECT c.conc_id, c.conc_dt_problema, c.conc_desc_problema, " +
                "c.conc_dt_solucao, c.conc_desc_solucao, c.conc_receita_id, c.conc_despesa_id, " +
                "CASE " +
                "    WHEN c.conc_receita_id IS NOT NULL AND c.conc_receita_id != 0 THEN 'Receita' " +
                "    WHEN c.conc_despesa_id IS NOT NULL AND c.conc_despesa_id != 0 THEN 'Despesa' " +
                "    ELSE 'Desconhecido' " +
                "END AS item_tipo, " +
                "COALESCE(r.receita_val, d.despesa_val) AS item_valor, " +
                "COALESCE(r.receita_desc, d.despesa_desc) AS item_descricao " +
                "FROM conciliacao c " +
                "LEFT JOIN receita r ON c.conc_receita_id = r.receita_id " +
                "LEFT JOIN despesa d ON c.conc_despesa_id = d.despesa_id " +
                "WHERE c.conc_dt_solucao IS NULL ";

        if (filtro != null && !filtro.trim().isEmpty()) {
            String sanitizedFiltro = filtro.replaceAll("'", "''");
            sql += "AND (LOWER(c.conc_desc_problema) LIKE '%" + sanitizedFiltro.toLowerCase() + "%' OR " +
                    "LOWER(CAST(c.conc_id AS TEXT)) LIKE '%" + sanitizedFiltro.toLowerCase() + "%' OR " +
                    "LOWER(CAST(c.conc_receita_id AS TEXT)) LIKE '%" + sanitizedFiltro.toLowerCase() + "%' OR " +
                    "LOWER(CAST(c.conc_despesa_id AS TEXT)) LIKE '%" + sanitizedFiltro.toLowerCase() + "%' OR " +
                    "LOWER(COALESCE(r.receita_desc, d.despesa_desc)) LIKE '%" + sanitizedFiltro.toLowerCase() + "%') "; // **CORRIGIDO: de r.rec_descricao para r.receita_desc**
        }

        sql += "ORDER BY c.conc_id DESC";

        try {
            rs = conexao.getConexao().consultar(sql);

            while (rs.next()) {
                int concId = rs.getInt("conc_id");
                LocalDate concDtProblema = null;
                String concDtProblemaStr = rs.getString("conc_dt_problema");
                if (concDtProblemaStr != null && !concDtProblemaStr.isEmpty()) {
                    try {
                        concDtProblema = LocalDate.parse(concDtProblemaStr);
                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Aviso: conc_dt_problema (ID: " + concId + ") contém formato de data inválido: " + concDtProblemaStr);
                    }
                }

                String concDescProblema = rs.getString("conc_desc_problema");

                LocalDate concDtSolucao = null;
                String dtSolucaoStr = rs.getString("conc_dt_solucao");
                if (dtSolucaoStr != null && !dtSolucaoStr.isEmpty()) {
                    try {
                        concDtSolucao = LocalDate.parse(dtSolucaoStr);
                    } catch (java.time.format.DateTimeParseException e) {
                        System.err.println("Aviso: conc_dt_solucao (ID: " + concId + ") contém formato de data inválido: " + dtSolucaoStr);
                    }
                }

                String concDescSolucao = rs.getString("conc_desc_solucao");

                int concReceitaId = rs.getInt("conc_receita_id");
                int concDespesaId = rs.getInt("conc_despesa_id");
                String itemTipo = rs.getString("item_tipo");
                double itemValor = rs.getDouble("item_valor");
                String itemDescricao = rs.getString("item_descricao");

                listaProblemas.add(new Conciliacao(concId, concDtProblema, concDescProblema,
                        concDtSolucao, concDescSolucao, concReceitaId, concDespesaId,
                        itemTipo, itemValor, itemDescricao));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter problemas de conciliação: " + e.getMessage());
            throw new RuntimeException("Erro no DAO ao obter problemas de conciliação.", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar ResultSet em getProblemas: " + e.getMessage());
                }
            }
        }
        return listaProblemas;
    }

    public boolean atualizarSolucao(int concId, LocalDate concDtSolucao, String concDescSolucao, Singleton conexao) {
        StringBuilder sqlBuilder = new StringBuilder("UPDATE conciliacao SET ");

        boolean firstField = true;

        if (concDtSolucao != null) {
            sqlBuilder.append("conc_dt_solucao = '").append(concDtSolucao.format(SQL_DATE_FORMATTER)).append("'");
            firstField = false;
        } else {
            sqlBuilder.append("conc_dt_solucao = NULL");
            firstField = false;
        }

        if (concDescSolucao != null && !concDescSolucao.trim().isEmpty()) {
            if (!firstField) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("conc_desc_solucao = '").append(concDescSolucao.replaceAll("'", "''")).append("'");
            firstField = false;
        } else {
            if (!firstField) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("conc_desc_solucao = NULL");
            firstField = false;
        }

        sqlBuilder.append(" WHERE conc_id = ").append(concId);

        String sql = sqlBuilder.toString();

        try {
            System.out.println("SQL de atualização de solução: " + sql);
            return conexao.getConexao().manipular(sql);
        } catch (RuntimeException e) {
            System.err.println("Erro ao atualizar solução da conciliação ID " + concId + ": " + e.getMessage());
            throw new RuntimeException("Erro no DAO ao atualizar solução da conciliação.", e);
        }
    }

    public boolean apagarPorId(int concId, Singleton conexao) {
        String sql = "DELETE FROM public.conciliacao WHERE conc_id = " + concId;
        try {
            return conexao.getConexao().manipular(sql);
        } catch (RuntimeException e) {
            System.err.println("Erro ao apagar conciliação ID " + concId + ": " + e.getMessage());
            throw new RuntimeException("Erro no DAO ao apagar conciliação por ID.", e);
        }
    }
}