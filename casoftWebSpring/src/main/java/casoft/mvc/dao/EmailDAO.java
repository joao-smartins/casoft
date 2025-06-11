package casoft.mvc.dao;
import casoft.mvc.dto.EmailDTO;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class EmailDAO {
    public void gravar(String destinatario, String assunto, String mensagem, Singleton conexao){
        String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String sql = """
                INSERT INTO Email (email_dest, email_assunto, email_texto, email_data) 
                VALUES ('#1', '#2', '#3', '#4')
                """;
        sql.replace("#1", destinatario)
                .replace("#2", assunto)
                .replace("#3", mensagem)
                .replace("#4",dataAtual);
        conexao.getConexao().manipular(sql);
        }
}
