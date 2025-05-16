package casoft.mvc.controller;

import casoft.mvc.dao.UsuarioDAO;
import casoft.mvc.model.Usuario;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDao;

    private UsuarioController() {

    }

    public Map<String, Object> cadastrarUsuario(Usuario usuario) {
        System.out.println("Dados recebidos no controller: " + usuario.toString());
        Map<String, Object> resultado = new HashMap<>();
        Singleton conexao=Singleton.getInstancia();
        if(conexao.conectar()){
            try{
                System.out.println("Verificando login existente...");
                if (usuarioDao.existeLogin(usuario.getLogin(), conexao)) {
                    resultado.put("erro", "Login já está em uso");
                    return resultado;
                }

                System.out.println("Configurando nível de acesso...");
                if (usuarioDao.contarUsuarios(conexao) == 0) {
                    usuario.setNivelAcesso(Usuario.NIVEL_ADMIN);
                    usuario.setAtivo(true);
                } else {
                    usuario.setNivelAcesso(Usuario.NIVEL_PADRAO);
                    usuario.setAtivo(true);
                }

                System.out.println("Tentando gravar usuário...");
                if (usuarioDao.gravar(usuario, conexao)!=null) {
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
                Usuario usuario = usuarioDao.get(id, conexao);
                if (usuario == null) {
                    resultado.put("erro", "Usuário não encontrado");
                    return resultado;
                }

                // Verifica se é o último admin ativo
                if (Usuario.NIVEL_ADMIN.equals(usuario.getNivelAcesso())) {
                    int adminsAtivos = usuarioDao.contarAdminsAtivos(conexao);
                    if (adminsAtivos <= 1) {
                        resultado.put("erro", "Não é possível desativar o último ADMIN ativo");
                        return resultado;
                    }
                }

                // Apenas atualiza o status
                usuario.setAtivo(false);

                if (usuarioDao.alterar(usuario, conexao)!=null) {
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