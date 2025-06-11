package casoft.mvc.dao;

import casoft.mvc.model.Evento;
import casoft.mvc.model.Voluntario;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VoluntarioDAO implements IDAO<Voluntario> {
    @Override
    public Voluntario gravar(Voluntario entidade, Singleton conexao) {
        String sql = """
                INSERT INTO voluntario (volu_nome, volu_cell, volu_email, volu_logradouro,  volu_bairro, volu_comp, volu_cep, volu_cpf, volu_numero_end) 
                VALUES ('#1', '#2', '#3', '#4', '#5', '#6', '#7', '#8', '#9')
                """;
        sql = sql.replace("#1", entidade.getNome());
        sql = sql.replace("#2",entidade.getCell() );
        sql = sql.replace("#3", entidade.getEmail());
        sql = sql.replace("#4",entidade.getLogradouro());
        sql = sql.replace("#5",entidade.getBairro());
        sql = sql.replace("#6",entidade.getComp());
        sql = sql.replace("#7",entidade.getCep().toString());
        sql = sql.replace("#8",entidade.getCpf());
        sql = sql.replace("#9",""+entidade.getNumero());

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Voluntario alterar(Voluntario entidade, Singleton conexao) {
        String sql = """
        UPDATE voluntario SET 
            volu_nome = '#1',
            volu_cell = '#2',
            volu_email = '#3',
            volu_logradouro = '#4',
            volu_bairro = '#5',
            volu_comp = '#6',
            volu_cep = '#7',
            volu_cpf = '#8',
            volu_numero_end = '#9'
        WHERE volu_id = #T;
        """;
        sql = sql.replace("#1", entidade.getNome())
                .replace("#2",entidade.getCell())
                .replace("#3", entidade.getEmail())
                .replace("#4",entidade.getLogradouro())
                .replace("#5",entidade.getBairro())
                .replace("#6",entidade.getComp())
                .replace("#7",entidade.getCep())
                .replace("#8",entidade.getCpf())
                .replace("#9",""+entidade.getNumero())
                .replace("#T",""+ entidade.getId());
        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(Voluntario entidade, Singleton conexao) {
        String sql = "DELETE FROM voluntario WHERE volu_id = ";
        sql += entidade.getId();
        if(conexao.getConexao().manipular(sql)) {
            return true;
        }
        return false;
    }

    @Override
    public Voluntario get(int id, Singleton conexao) {
        String sql = "SELECT * FROM voluntario WHERE volu_id = " + id;
        var rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                Voluntario v = new Voluntario();
                v.setId(rs.getInt("volu_id"));
                v.setCell(rs.getString("volu_cell"));
                v.setNome(rs.getString("volu_nome"));
                v.setBairro(rs.getString("volu_bairro"));
                v.setEmail(rs.getString("volu_email"));
                v.setLogradouro(rs.getString("volu_logradouro"));
                v.setComp(rs.getString("volu_comp"));
                v.setCep(rs.getString("volu_cep"));
                v.setCpf(rs.getString("volu_cpf"));
                v.setNumero(rs.getInt("volu_numero_end"));
                return v;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar evento: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Voluntario> get(String filtro, Singleton conexao) {
        List<Voluntario> voluntarios = new ArrayList<>();
        String sql = """
                SELECT * FROM voluntario
                """;
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE "+filtro;
        }
        var rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Voluntario v = new Voluntario();
                v.setId(rs.getInt("volu_id"));
                v.setNome(rs.getString("volu_nome"));
                v.setBairro(rs.getString("volu_bairro"));
                v.setEmail(rs.getString("volu_email"));
                v.setCell(rs.getString("volu_cell"));
                v.setLogradouro(rs.getString("volu_logradouro"));
                v.setComp(rs.getString("volu_comp"));
                v.setCep(rs.getString("volu_cep"));
                v.setCpf(rs.getString("volu_cpf"));
                v.setNumero(rs.getInt("volu_numero_end"));
                voluntarios.add(v);
            }

        } catch(Exception er){
            System.out.println("Erro: " + er.getMessage());
        }
        return voluntarios;
    }
}
