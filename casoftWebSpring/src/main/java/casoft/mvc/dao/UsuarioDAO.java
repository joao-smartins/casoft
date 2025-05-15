package casoft.mvc.dao;

import casoft.mvc.model.Usuario;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
@Repository
public class UsuarioDAO implements IDAO<Usuario> {
    @Override
    public Object gravar(Usuario usuario, Singleton conexao) {
        if (existeUsuarioComLogin(usuario.getLogin(),conexao)) {
            System.out.println("Erro: Já existe um usuário com este login!");
            return false;
        }

        usuario.setCpf(formatarCPF(usuario.getCpf()));
        usuario.setTelefone(formatarTelefone(usuario.getTelefone()));

        String sql = "INSERT INTO usuario (nome, login, senha, ativo, nivel_acesso, cpf, telefone) " +
                "VALUES ('#1', '#2', '#3', #4, '#5', '#6', '#7')";

        sql = sql.replace("#1", usuario.getNome())
                .replace("#2", usuario.getLogin())
                .replace("#3", usuario.getSenha())
                .replace("#4", usuario.isAtivo() ? "true" : "false")
                .replace("#5", usuario.getNivelAcesso())
                .replace("#6", usuario.getCpf())
                .replace("#7", usuario.getTelefone());

        return conexao.getConexao().manipular(sql);
    }

    @Override
    public Object alterar(Usuario usuario, Singleton conexao) {
        if (existeOutroUsuarioComLogin(usuario.getLogin(), usuario.getId(),conexao)) {
            System.out.println("Erro: Login já está em uso por outro usuário!");
            return false;
        }

        if ("ADMIN".equals(usuario.getNivelAcesso()) && !usuario.isAtivo()) {
            if (isUltimoAdminAtivo(usuario.getId(),conexao)) {
                System.out.println("Erro: Não é possível desativar o último ADMIN!");
                return false;
            }
        }


        String sql = "UPDATE usuario SET nome='#1', login='#2', senha='#3', ativo=#4, " +
                "nivel_acesso='#5', cpf='#6', telefone='#7' WHERE id=#8";

        sql = sql.replace("#1", usuario.getNome())
                .replace("#2", usuario.getLogin())
                .replace("#3", usuario.getSenha())
                .replace("#4", usuario.isAtivo() ? "true" : "false")
                .replace("#5", usuario.getNivelAcesso())
                .replace("#6", usuario.getCpf())
                .replace("#7", usuario.getTelefone())
                .replace("#8", String.valueOf(usuario.getId()));

        return conexao.getConexao().manipular(sql);
    }

    @Override
    public boolean apagar(Usuario usuario,Singleton conexao) {
        if ("ADMIN".equals(usuario.getNivelAcesso())) {
            System.out.println("Erro: Não é possível excluir um usuário ADMIN!");
            return false;
        }
        return conexao.getConexao().manipular("DELETE FROM usuario WHERE id=" + usuario.getId());
    }

    @Override
    public Usuario get(int id,Singleton conexao) {
        String sql = "SELECT * FROM usuario WHERE id=" + id;
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getBoolean("ativo"),
                        rs.getString("nivel_acesso"),
                        rs.getString("cpf"),
                        rs.getString("telefone")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Usuario> get(String filtro,Singleton conexao) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";
        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getBoolean("ativo"),
                        rs.getString("nivel_acesso"),
                        rs.getString("cpf"),
                        rs.getString("telefone")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    private boolean existeUsuarioComLogin(String login,Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE login = '" + login + "'";
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean existeOutroUsuarioComLogin(String login, int idUsuario,Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE login = '" + login + "' AND id != " + idUsuario;
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isUltimoAdminAtivo(int idUsuario,Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE nivel_acesso = 'ADMIN' AND ativo = true AND id != " + idUsuario;
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            return rs.next() && rs.getInt(1) == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarTelefone(String telefone) {
        return telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }
}
