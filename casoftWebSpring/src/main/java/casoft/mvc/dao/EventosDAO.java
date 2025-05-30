package casoft.mvc.dao;

import casoft.mvc.model.Evento;
import casoft.mvc.util.Singleton;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class EventosDAO implements IDAO<Evento> {

    @Override
    public Evento gravar(Evento entidade, Singleton conexao)
    {
        String sql = """
                INSERT INTO evento (evento_nome, evento_desc, evento_data, evento_status) 
                VALUES ('#1', '#2', '#3', '#4')
                """;
        sql = sql.replace("#1", entidade.getNome());
        sql = sql.replace("#2", entidade.getDescricao());
        sql = sql.replace("#3", entidade.getData().toString());
        sql = sql.replace("#4",""+entidade.isStatus());

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public List<Evento> get(String filtro, Singleton conexao) {
        List<Evento> eventos = new ArrayList<>();
        String sql = """
                SELECT * FROM evento
                """;
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE "+filtro;
        }
        var rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Evento e = new Evento();
                e.setId(rs.getInt("evento_id"));
                e.setNome(rs.getString("evento_nome"));
                e.setDescricao(rs.getString("evento_desc"));
                e.setData(rs.getDate("evento_data").toString());
                e.setStatus(rs.getString("evento_status").charAt(0));
                eventos.add(e);
            }

        } catch(Exception er){
            System.out.println("Erro: " + er.getMessage());
        }
        return eventos;
    }

    @Override
    public Evento alterar(Evento entidade, Singleton conexao) {
        String sql = """
        UPDATE evento SET 
            evento_nome = '#1',
            evento_desc = '#2',
            evento_data = '#3',
            evento_status = '#4'
        WHERE evento_id = #6;
        """;
        sql = sql.replace("#1", entidade.getNome())
                .replace("#2", entidade.getDescricao())
                .replace("#3", entidade.getData().toString())
                .replace("#4", ""+entidade.isStatus())
                .replace("#6", "" + entidade.getId());

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(Evento entidade, Singleton conexao) {
        String sql = "DELETE FROM evento WHERE evento_id = ";
        sql += entidade.getId();
        if(conexao.getConexao().manipular(sql)) {
            return true;
        }
        return false;
    }

    @Override
    public Evento get(int id, Singleton conexao) {
        String sql = "SELECT * FROM evento WHERE evento_id = " + id;
        var rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                Evento e = new Evento();
                e.setId(rs.getInt("evento_id"));
                e.setNome(rs.getString("evento_nome"));
                e.setDescricao(rs.getString("evento_desc"));
                e.setData(rs.getDate("evento_data").toString());
                e.setStatus(rs.getString("evento_status").charAt(0));
                return e;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar evento: " + e.getMessage());
        }
        return null;
    }

    public void inativarEventos(LocalDate hoje, Singleton conexao){
        String sql = "UPDATE evento SET evento_status = 'I' where evento_data < '#1' and evento_status = 'A'";
        sql=sql.replace("#1", hoje.toString());
        conexao.getConexao().manipular(sql);}
}
