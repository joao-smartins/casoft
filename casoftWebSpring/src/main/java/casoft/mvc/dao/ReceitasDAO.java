package casoft.mvc.dao;

import casoft.mvc.model.CategoriaReceita;
import casoft.mvc.model.Receitas;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReceitasDAO implements IDAO<Receitas> {

    @Override
    public Receitas gravar(Receitas entidade, Singleton conexao) {
        String sql = """
            INSERT INTO receitas (rec_valor, rec_futura, rec_descricao, rec_evento_id, rec_categoria_id, rec_data)
            VALUES (#1, #2, '#3', #4, #5, '#6')
            """;
        sql = sql.replace("#1", String.valueOf(entidade.getValor()))
                .replace("#2", entidade.isFutura() ? "true" : "false")
                .replace("#3", entidade.getDescricao())
                .replace("#4", entidade.getEvento() != null ? String.valueOf(entidade.getEvento().getId()) : "NULL")
                .replace("#5", String.valueOf(entidade.getCategoria().getId()))
                .replace("#6", entidade.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }



    @Override
    public List<Receitas> get(String filtro, Singleton conexao) {
        List<Receitas> receitas = new ArrayList<>();
        String sql = "SELECT * FROM receitas";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Receitas r = new Receitas(
                        rs.getDouble("rec_valor"),
                        rs.getBoolean("rec_futura"),
                        rs.getString("rec_descricao"),
                        null, // Evento (seria necessário buscar do banco)
                        new CategoriaReceita(rs.getInt("rec_categoria_id"), ""), // Categoria (simplificado)
                        rs.getDate("rec_data").toLocalDate()
                );
                receitas.add(r);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar receitas: " + e.getMessage());
        }
        return receitas;
    }

    @Override
    public Receitas alterar(Receitas entidade, Singleton conexao) {
        String sql = """
            UPDATE receitas SET
                rec_valor = #1,
                rec_futura = #2,
                rec_descricao = '#3',
                rec_evento_id = #4,
                rec_categoria_id = #5,
                rec_data = '#6'
            WHERE rec_id = #7
            """;
        sql = sql.replace("#1", String.valueOf(entidade.getValor()))
                .replace("#2", entidade.isFutura() ? "1" : "0")
                .replace("#3", entidade.getDescricao())
                .replace("#4", entidade.getEvento() != null ? String.valueOf(entidade.getEvento().getId()) : "NULL")
                .replace("#5", String.valueOf(entidade.getCategoria().getId()))
                .replace("#6", entidade.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .replace("#7", "1"); // Supondo que haja um ID na tabela

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(Receitas entidade, Singleton conexao) {
        return false;
    }

    @Override
    public Receitas get(int id, Singleton conexao) {
        String sql = "SELECT * FROM receitas WHERE rec_id = " + id;
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return new Receitas(
                        rs.getDouble("rec_valor"),
                        rs.getBoolean("rec_futura"),
                        rs.getString("rec_descricao"),
                        null, // Evento (seria necessário buscar)
                        new CategoriaReceita(rs.getInt("rec_categoria_id"), ""), // Categoria (simplificado)
                        rs.getDate("rec_data").toLocalDate()
                );
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar receita: " + e.getMessage());
        }
        return null;
    }
}