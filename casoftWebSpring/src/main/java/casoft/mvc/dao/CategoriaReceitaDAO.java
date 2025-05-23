package casoft.mvc.dao;

import casoft.mvc.model.CategoriaReceita;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoriaReceitaDAO implements IDAO<CategoriaReceita> {

    @Override
    public CategoriaReceita gravar(CategoriaReceita entidade, Singleton conexao)
    {
        String sql = """
            INSERT INTO categoriarec (catrec_nome) 
            VALUES ('#1')
            """;
        sql = sql.replace("#1", entidade.getNome());

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public List<CategoriaReceita> get(String filtro, Singleton conexao) {
        List<CategoriaReceita> tipoReceitas = new ArrayList<>();
        String sql = """
            SELECT * FROM categoriarec
            """;
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE "+filtro;
        }
        var rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                CategoriaReceita r = new CategoriaReceita();
                r.setId(rs.getInt("catrec_id"));
                r.setNome(rs.getString("catrec_nome"));
                tipoReceitas.add(r);
            }

        } catch(Exception er){
            System.out.println("Erro: " + er.getMessage());
        }
        return tipoReceitas;
    }

    @Override
    public CategoriaReceita alterar(CategoriaReceita entidade, Singleton conexao) {
        String sql = """
    UPDATE CategoriaRec SET 
        catrec_nome = '#1'
    WHERE catrec_id = #2;
    """;
        sql = sql.replace("#1", entidade.getNome())
                .replace("#2", "" + entidade.getId());

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(CategoriaReceita entidade, Singleton conexao) {
        String sql = "DELETE FROM categoriarec WHERE catrec_id = ";
        sql += entidade.getId();
        if(conexao.getConexao().manipular(sql)) {
            return true;
        }
        return false;
    }

    @Override
    public CategoriaReceita get(int id, Singleton conexao) {
        String sql = "SELECT * FROM categoriarec WHERE  catrec_id = " + id;
        var rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                CategoriaReceita r = new CategoriaReceita();
                r.setId(rs.getInt("catrec_id"));
                r.setNome(rs.getString("catrec_nome"));
                return r;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar evento: " + e.getMessage());
        }
        return null;
    }
}
