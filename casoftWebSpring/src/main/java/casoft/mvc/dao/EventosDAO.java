package casoft.mvc.dao;

import casoft.mvc.model.Evento;
import casoft.mvc.model.Voluntario;
import casoft.mvc.util.Conexao;
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
                INSERT INTO evento (evento_nome, evento_desc, evento_data, evento_status, evento_id_resp) 
                VALUES ('#1', '#2', '#3', '#4', '#5')
                """;
        sql = sql.replace("#1", entidade.getNome());
        sql = sql.replace("#2", entidade.getDescricao());
        sql = sql.replace("#3", entidade.getData().toString());
        sql = sql.replace("#4",""+entidade.isStatus());
        sql = sql.replace("#5", ""+entidade.getId_resp());

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
                e.setId_resp(rs.getInt("evento_id_resp"));
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
            evento_status = '#4',
            evento_id_resp = '#5'
        WHERE evento_id = #6;
        """;
        sql = sql.replace("#1", entidade.getNome())
                .replace("#2", entidade.getDescricao())
                .replace("#3", entidade.getData().toString())
                .replace("#4", ""+entidade.isStatus())
                .replace("#5", ""+entidade.getId_resp())
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
                e.setId_resp(rs.getInt("evento_id_resp"));
                return e;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar evento: " + e.getMessage());
        }
        return null;
    }

    public List<Voluntario> getVolu(Conexao conexao, int idEvento){
        String sql = """
        SELECT v.* FROM voluntario v
        JOIN volu_even ve ON ve.voluntario_voluntario_id = v.voluntario_id
        WHERE ve.evento_evento_id = #1
        """.replace("#1", String.valueOf(idEvento));

        List<Voluntario> voluntarios = new ArrayList<>();
        var rs = conexao.consultar(sql);
        try {
            while (rs.next()) {
                Voluntario v = new Voluntario();
                v.setId(rs.getInt("voluntario_id"));
                v.setCell(rs.getString("volu_cell"));
                v.setNome(rs.getString("volu_nome"));
                v.setBairro(rs.getString("volu_bairro"));
                v.setEmail(rs.getString("volu_email"));
                v.setLogradouro(rs.getString("volu_logradouro"));
                v.setComp(rs.getString("volu_comp"));
                v.setCep(rs.getString("volu_cep"));
                v.setCpf(rs.getString("volu_cpf"));
                v.setNumero(rs.getInt("volu_numero_end"));
                voluntarios.add(v);
            }
            return voluntarios;
        } catch (Exception e) {
            System.out.println("Erro ao buscar voluntários do evento: " + e.getMessage());
            return null;
        }
    }

    public boolean adicionarVoluntarioAoEvento(int eventoId, int voluntarioId, Singleton conexao) {
        String sql = """
        INSERT INTO volun_even (evento_evento_id, voluntario_voluntario_id)
        VALUES ('#1', '#2')
        """;
        sql = sql.replace("#1", ""+eventoId)
                .replace("#2",""+ voluntarioId);

        if(conexao.getConexao().manipular(sql))
            return true;
        return false;
    }

    public boolean removerVoluntarioDoEvento(int eventoId, int voluntarioId, Singleton conexao) {
        String sql = """
        DELETE FROM volun_even
        WHERE evento_id = #1 AND voluntario_id = #2
        """;
        sql = sql.replace("#1", String.valueOf(eventoId))
                .replace("#2", String.valueOf(voluntarioId));

        return conexao.getConexao().manipular(sql);
    }


    public void inativarEventos(LocalDate hoje, Singleton conexao){
        String sql = "UPDATE evento SET evento_status = 'I' where evento_data < '#1' and evento_status = 'A'";
        sql=sql.replace("#1", hoje.toString());
        conexao.getConexao().manipular(sql);}
}
