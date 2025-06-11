package casoft.mvc.model;

import casoft.mvc.dao.UsuarioDAO;
import casoft.mvc.util.JWTTokenProvider;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Usuario {

    @Autowired
    private UsuarioDAO usuarioDAO;
    public static final String NIVEL_ADMIN = "ADMIN";
    public static final String NIVEL_PADRAO = "USER";
    private int id;
    private String nome;
    private String login;
    private String senha;
    private boolean ativo;
    private String nivelAcesso;
    private String cpf;
    private String telefone;

    // Construtores
    public Usuario() {}


    public Usuario(String nome, String login, String senha, boolean ativo, String nivelAcesso, String cpf, String telefone) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
        this.nivelAcesso = nivelAcesso;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public Usuario(int id, String nome, String login, String senha, boolean ativo, String nivelAcesso, String cpf, String telefone) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
        this.nivelAcesso = nivelAcesso;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(String nivelAcesso) { this.nivelAcesso = nivelAcesso; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return login.equals(usuario.login);
    }
    public String autenticar(String login, String senha,Singleton conexao){
        String token;
        List<Usuario> usuarios=usuarioDAO.get("login = "+"'"+login+"'",conexao);
        if(!usuarios.isEmpty()){
            Usuario usuario= usuarios.getFirst();
            if(usuario!=null && usuario.getSenha().equals(senha)){
                token= JWTTokenProvider.getToken(login,""+usuario.getNivelAcesso());
                return token;
            }
        }
        return null;
    }
    public String getNivel(String login,Singleton conexao){
        List<Usuario> usuarios=usuarioDAO.get("login = "+"'"+login+"'",conexao);
        if(!usuarios.isEmpty()){
            Usuario usuario= usuarios.getFirst();
            if(usuario!=null){
                return usuario.getNivelAcesso();
            }
        }
        return null;
    }
    public Usuario gravar(Usuario usuario, Singleton conexao) {
        return usuarioDAO.gravar(usuario, conexao);
    }

    public Usuario alterar(Usuario usuario, Singleton conexao) {
        return usuarioDAO.alterar(usuario, conexao);
    }

    public boolean apagar(Usuario usuario, Singleton conexao) {
        return usuarioDAO.apagar(usuario, conexao);
    }

    public Usuario get(int id, Singleton conexao) {
        return usuarioDAO.get(id, conexao);
    }

    public int contarAdminsAtivos(Singleton conexao) {
        return usuarioDAO.contarAdminsAtivos(conexao);
    }

    public boolean existeLogin(String login, Singleton conexao) {
        return usuarioDAO.existeLogin(login, conexao);
    }

    public boolean isUltimoAdminAtivo(int idUsuario, Singleton conexao) {
        return usuarioDAO.isUltimoAdminAtivo(idUsuario, conexao);
    }

    public int contarUsuarios(Singleton conexao) {
        return usuarioDAO.contarUsuarios(conexao);
    }

    public boolean existeOutroUsuarioComLogin(String login,int id,Singleton conexao) {
        return usuarioDAO.existeOutroUsuarioComLogin(login,id,conexao);
    }

    public List<Usuario> get(String filtro,Singleton conexao) {
        return usuarioDAO.get(filtro,conexao);
    }
    public Usuario getUsuario(String login,Singleton conexao){
        List<Usuario> usuarios=usuarioDAO.get("login = "+"'"+login+"'",conexao);
        if(!usuarios.isEmpty()){
            return usuarios.getFirst();
        }
        return null;
    }
}
