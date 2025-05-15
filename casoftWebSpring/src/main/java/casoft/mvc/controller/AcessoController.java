package casoft.mvc.controller;

import casoft.mvc.model.Usuario;
import casoft.mvc.util.Mensagem;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AcessoController {
    @Autowired
    private Usuario usuario;

    public String autenticar(String login, String senha){
        Singleton conexao=Singleton.getInstancia();
        if(conexao.conectar()){
            String token=usuario.autenticar(login, senha,conexao);
            return token;
        }
        return null;
    }
    public String getNivel(String login){
        Singleton conexao=Singleton.getInstancia();
        if(conexao.conectar()){
            return usuario.getNivel(login,conexao);
        }
        return null;
    }

}
