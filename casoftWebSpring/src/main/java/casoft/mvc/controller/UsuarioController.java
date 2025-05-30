package casoft.mvc.controller;

import casoft.mvc.model.Usuario;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuarioController {

    @Autowired
    private Usuario usuarioContro;


    private UsuarioController() {

    }


    public Map<String, Object> deletar(int id) {
        Map<String, Object> resultado = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        if (!conexao.conectar()) {
            resultado.put("erro", "Não foi possível conectar ao banco de dados");
            return resultado;
        }

        try {
            // Primeiro verifica se o usuário existe
            Usuario usuario = usuarioContro.get(id, conexao);
            if (usuario == null) {
                resultado.put("erro", "Usuário não encontrado");
                return resultado;
            }

            System.out.println(usuario.getNome());

            // Verifica se é o último admin ativo (usando o DAO)
            if (usuarioContro.isUltimoAdminAtivo(id, conexao) && usuario.isAtivo()) {
                resultado.put("erro", "Não é possível excluir o último ADMIN ativo!");
                return resultado;
            }

            // Tenta apagar o usuário
            boolean sucesso = usuario.apagar(usuario, conexao);
            if (sucesso) {
                resultado.put("mensagem", "Usuário deletado com sucesso");
            } else {
                resultado.put("erro", "Não foi possível deletar o usuário");
            }
        } catch (Exception e) {
            System.err.println("Erro interno: " + e.getMessage());
            resultado.put("erro", "Erro interno: " + e.getMessage());
        }

        return resultado;
    }


    public List<Usuario> listarTodos() {
        Singleton conexao = Singleton.getInstancia();
        if (conexao.conectar()) {
            return usuarioContro.get("", conexao);
        }
        return null;
    }

    public Usuario buscarPorId(int id) {
        Singleton conexao = Singleton.getInstancia();
        if (conexao.conectar()) {
            return usuarioContro.get(id, conexao);
        }
        return null;
    }

    public Map<String, Object> atualizarUsuario(Usuario usuario) {
        Map<String, Object> resultado = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        if (conexao.conectar()) {
            try {
                // 1. Verifica se o ID foi informado
                if (usuario.getId() == 0) {
                    resultado.put("erro", "ID do usuário não informado");
                    return resultado;
                }

                // 2. Busca usuário existente
                Usuario usuarioExistente = usuarioContro.get(usuario.getId(), conexao);
                if (usuarioExistente == null) {
                    resultado.put("erro", "Usuário não encontrado");
                    return resultado;
                }

                // 3. Validações básicas
                if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                    resultado.put("erro", "Nome é obrigatório");
                    resultado.put("campo", "nome");
                    return resultado;
                }

                // 4. Mantém dados sensíveis que não vieram do frontend
                usuario.setNivelAcesso(usuarioExistente.getNivelAcesso());
                if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
                    usuario.setSenha(usuarioExistente.getSenha());
                }

                // 5. Validação especial para ADMIN
                if (Usuario.NIVEL_ADMIN.equals(usuarioExistente.getNivelAcesso())) {
                    if (!usuario.isAtivo() && usuarioContro.isUltimoAdminAtivo(usuario.getId(), conexao)) {
                        resultado.put("erro", "Não é possível desativar o último ADMIN ativo");
                        return resultado;
                    }
                }

                // 6. Executa a atualização
                Usuario usuarioAtualizado = usuarioContro.alterar(usuario, conexao);
                if (usuarioAtualizado != null) {
                    resultado.put("mensagem", "Usuário atualizado com sucesso");
                    resultado.put("usuario", usuarioAtualizado);
                } else {
                    resultado.put("erro", "Falha ao executar atualização no banco de dados");
                }

            } catch (Exception e) {
                System.err.println("Erro na conexão: " + e.getMessage());
                resultado.put("erro", "Erro interno: " + e.getMessage());
            }
        } else {
            resultado.put("erro", "Erro ao conectar com o banco de dados");
        }
        return resultado;
    }

    public Map<String, Object> cadastrarUsuario(Usuario usuario) {
        System.out.println("Dados recebidos no controller: " + usuario.toString());
        Map<String, Object> resultado = new HashMap<>();
        Singleton conexao=Singleton.getInstancia();
        if(conexao.conectar()){
            try{
                System.out.println("Verificando login existente...");
                if (usuarioContro.existeLogin(usuario.getLogin(), conexao)) {
                    resultado.put("erro", "Login já está em uso");
                    return resultado;
                }

                System.out.println("Configurando nível de acesso...");
                if (usuarioContro.contarUsuarios(conexao) == 0) {
                    usuario.setNivelAcesso(Usuario.NIVEL_ADMIN);
                    usuario.setAtivo(true);
                } else {
                    usuario.setNivelAcesso(Usuario.NIVEL_PADRAO);
                    usuario.setAtivo(true);
                }

                System.out.println("Tentando gravar usuário...");
                if (usuarioContro.gravar(usuario, conexao)!=null) {
                    resultado.put("mensagem", "Usuário cadastrado com sucesso");
                } else {
                    resultado.put("erro", "Falha ao cadastrar usuário");
                }
            } catch (Exception e){
                System.err.println("Erro no cadastro: " + e.getMessage());
                resultado.put("erro", "Erro interno: " + e.getMessage());
            }
        } else {
            resultado.put("erro","Erro ao conectar com o BD");
        }
        return resultado;
    }

    public Map<String, Object> desativarUsuario(int id) {
        Map<String, Object> resultado = new HashMap<>();
        Singleton conexao=Singleton.getInstancia();

        if (conexao.conectar()) {
            try {
                Usuario usuario = usuarioContro.get(id, conexao);
                if (usuario == null) {
                    resultado.put("erro", "Usuário não encontrado");
                    return resultado;
                }

                // Verifica se é o último admin ativo
                if (Usuario.NIVEL_ADMIN.equals(usuario.getNivelAcesso())) {
                    int adminsAtivos = usuarioContro.contarAdminsAtivos(conexao);
                    if (adminsAtivos <= 1) {
                        resultado.put("erro", "Não é possível desativar o último ADMIN ativo");
                        return resultado;
                    }
                }

                // Apenas atualiza o status
                usuario.setAtivo(false);

                if (usuarioContro.alterar(usuario, conexao)!=null) {
                    resultado.put("mensagem", "Usuário desativado com sucesso");
                } else {
                    resultado.put("erro", "Falha ao desativar usuário");
                }

            } catch (Exception e) {
                resultado.put("erro", "Erro interno: " + e.getMessage());
            }
        } else {
            resultado.put("erro", "Erro ao conectar ao BD");
        }

        return resultado;
    }

}