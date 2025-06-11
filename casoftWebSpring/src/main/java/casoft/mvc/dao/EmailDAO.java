package casoft.mvc.dao;
import casoft.mvc.model.EmailDTO;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class EmailDAO {
    public void gravar(String destinatario, String assunto, String mensagem, Singleton conexao){
        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String sql = """
                INSERT INTO email (email_dest, email_assunto, email_texto, email_data) 
                VALUES ('#1', '#2', '#3', '#4')
                """;
        sql = sql.replace("#1", destinatario)
                .replace("#2", assunto)
                .replace("#3", mensagem)
                .replace("#4",dataAtual);
        System.out.println(sql);
        System.out.println(conexao.getConexao().getEstadoConexao());
        conexao.getConexao().manipular(sql);
        }
}
