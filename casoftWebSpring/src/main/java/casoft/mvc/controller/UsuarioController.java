//package casoft.mvc.controller;
//
//
//import casoft.mvc.dao.UsuarioDAO;
//import casoft.mvc.model.Usuario;
//import casoft.mvc.util.Singleton;
//
//
//import java.util.List;
//
//public class UsuarioController {
//    private UsuarioDAO usuarioDao = new UsuarioDAO();
//
//
//    public boolean cadastrarUsuario(String nome, String login, String senha, String nivelAcesso, String cpf, String telefone) {
//        Singleton conexao= Singleton.getInstancia();
//        // Validação básica
//        if (nome == null || nome.trim().isEmpty() || login == null || login.trim().isEmpty()) {
//            return false;
//        }
//
//        Usuario usuario = new Usuario(
//                nome.trim(),
//                login.trim(),
//                senha,
//                true,
//                nivelAcesso,
//                cpf,
//                telefone
//        );
//
//        return usuarioDao.gravar(usuario,conexao);
//    }
//
//    public boolean editarUsuario(int id, String nome, String login, String senha, boolean ativo, String nivelAcesso, String cpf, String telefone) {
//        Singleton conexao= Singleton.getInstancia();
//        Usuario usuario = usuarioDao.get(id,conexao);
//        if (usuario == null) {
//            return false;
//        }
//
//        usuario.setNome(nome);
//        usuario.setLogin(login);
//        usuario.setSenha(senha);
//        usuario.setAtivo(ativo);
//        usuario.setNivelAcesso(nivelAcesso);
//        usuario.setCpf(cpf);
//        usuario.setTelefone(telefone);
//
//        return usuarioDao.alterar(usuario,conexao);
//    }
//
//    public boolean desativarUsuario(int id) {
//        Singleton conexao= Singleton.getInstancia();
//        Usuario usuario = usuarioDao.get(id,conexao);
//        if (usuario == null) {
//            return false;
//        }
//        usuario.setAtivo(false);
//        return usuarioDao.alterar(usuario,conexao);
//    }
//
//    public Usuario fazerLogin(String login, String senha) {
//        Singleton conexao= Singleton.getInstancia();
//        String filtro = String.format("login = '%s' AND senha = '%s'", login, senha);
//        List<Usuario> usuarios = usuarioDao.get(filtro,conexao);
//
//        if (usuarios.isEmpty()) {
//            return null; // Login/senha inválidos
//        }
//        return usuarios.get(0);
//    }
//
//    public List<Usuario> listarUsuarios(String filtro) {
//        Singleton conexao= Singleton.getInstancia();
//        return usuarioDao.get(filtro,conexao);
//    }
//}
