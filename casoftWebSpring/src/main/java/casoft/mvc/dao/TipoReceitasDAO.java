package casoft.mvc.dao;

import casoft.mvc.model.TipoReceitas;
import casoft.mvc.model.Evento;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TipoReceitasDAO implements IDAO<TipoReceitas> {

    @Override
    public TipoReceitas gravar(TipoReceitas entidade, Singleton conexao)
    {
        String sql = """
            INSERT INTO CategoriaRec (nome) 
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
    public List<TipoReceitas> get(String filtro, Singleton conexao) {
        List<TipoReceitas> tipoReceitas = new ArrayList<>();
        String sql = """
            SELECT * FROM CategoriaRec
            """;
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE "+filtro;
        }
        var rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                TipoReceitas r = new TipoReceitas();
                r.setId(rs.getInt("id"));
                r.setNome(rs.getString("nome"));
                tipoReceitas.add(r);
            }

        } catch(Exception er){
            System.out.println("Erro: " + er.getMessage());
        }
        return tipoReceitas;
    }

    @Override
    public TipoReceitas alterar(TipoReceitas entidade, Singleton conexao) {
        String sql = """
    UPDATE CategoriaRec SET 
        nome = '#1'
    WHERE CatReg_ID = #2;
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
    public boolean apagar(TipoReceitas entidade, Singleton conexao) {
        String sql = "DELETE FROM CategoriaRec WHERE CatReg_ID = ";
        sql += entidade.getId();
        if(conexao.getConexao().manipular(sql)) {
            return true;
        }
        return false;
    }

    @Override
    public TipoReceitas get(int id, Singleton conexao) {
        String sql = "SELECT * FROM CategoriaRec WHERE  CatRec_ID = " + id;
        var rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                TipoReceitas r = new TipoReceitas();
                r.setId(rs.getInt("CatRec_ID"));
                r.setNome(rs.getString("CatRec_nome"));
                return r;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar evento: " + e.getMessage());
        }
        return null;
    }
}
