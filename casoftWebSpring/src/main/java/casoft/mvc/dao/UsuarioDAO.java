package casoft.mvc.dao;

import casoft.mvc.model.Usuario;
import casoft.mvc.util.Singleton;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
@Repository
public class UsuarioDAO implements IDAO<Usuario> {
    @Override
    public Usuario gravar(Usuario usuario, Singleton conexao) {
        if (existeUsuarioComLogin(usuario.getLogin(),conexao)) {
            System.out.println("Erro: Já existe um usuário com este login!");
            return null;
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

        if(conexao.getConexao().manipular(sql)){
            return usuario;
        }
        else{
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Usuario alterar(Usuario usuario, Singleton conexao) {
        if (existeOutroUsuarioComLogin(usuario.getLogin(), usuario.getId(),conexao)) {
            System.out.println("Erro: Login já está em uso por outro usuário!");
            return null;
        }

        if ("ADMIN".equals(usuario.getNivelAcesso()) && !usuario.isAtivo()) {
            if (isUltimoAdminAtivo(usuario.getId(),conexao)) {
                System.out.println("Erro: Não é possível desativar o último ADMIN!");
                return null;
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

        if(conexao.getConexao().manipular(sql)){
            return usuario;
        }
        else{
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(Usuario usuario, Singleton conexao) {
        return conexao.getConexao().manipular("DELETE FROM usuario WHERE id=" + usuario.getId());
    }

    @Override
    public Usuario get(int id, Singleton conexao) {
        String sql = "SELECT * FROM usuario WHERE id=" + id;
        try {
            var rs = conexao.getConexao().consultar(sql);
            if (rs == null) {
                System.err.println("Consulta retornou ResultSet null para ID: " + id);
                return null;
            }
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
            System.out.println("Erro ao buscar usuarios: "+e.getMessage());
            System.err.println("Erro ao buscar usuário com ID: " + id);
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
        var rs = conexao.getConexao().consultar(sql);
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
    public int contarUsuarios(Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM usuario";
        var rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuarios: "+e.getMessage());
        }
        return 0;
    }
    public boolean existeUsuarioComLogin(String login,Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE login = '" + login + "'";
        var rs = conexao.getConexao().consultar(sql);
        try {
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            System.out.println("Erro existe usuario com login: "+e.getMessage());
            return false;
        }
    }

    public boolean existeOutroUsuarioComLogin(String login, int idUsuario, Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE login = '" + login + "' AND id != " + idUsuario;
        var rs = conexao.getConexao().consultar(sql);
        try {
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            System.out.println("Existe outro usuario com login: "+e.getMessage());
            return false;
        }
    }

    public boolean isUltimoAdminAtivo(int idUsuario, Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE nivel_acesso = 'ADMIN' AND ativo = true";
        var rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Quantidade de admins ativos (excluindo o atual): " + count);
                return count == 0;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Erro ao buscar admin: " + e.getMessage());
            return false;
        }
    }

    private String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private String formatarTelefone(String telefone) {
        return telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }
    public boolean existeLogin(String login, Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE login = '" + login + "'";
        var rs = conexao.getConexao().consultar(sql);
        try {
            return rs.next() && rs.getInt(1) > 0;
        } catch (Exception e) {
            System.out.println("Erro ao buscar login: "+e.getMessage());
            return false;
        }
    }
    public int contarAdminsAtivos(Singleton conexao) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE nivel_acesso = '" +
                Usuario.NIVEL_ADMIN + "' AND ativo = true";
        var rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar Admins ativos: "+e.getMessage());
        }
        return 0;
    }
}
