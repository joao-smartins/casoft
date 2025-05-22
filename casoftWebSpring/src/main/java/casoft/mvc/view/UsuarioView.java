package casoft.mvc.view;


import casoft.mvc.controller.UsuarioController;
import casoft.mvc.model.Usuario;
import casoft.mvc.util.Mensagem;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioView {

    @Autowired
    private UsuarioController usuarioController;

    @GetMapping("/todos")
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioController.listarTodos();
        if (usuarios != null) {
            return ResponseEntity.ok(usuarios);
        }
        return ResponseEntity.status(500).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable int id) {
        Usuario usuario = usuarioController.buscarPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mensagem> atualizar(@PathVariable int id, @RequestBody Usuario usuario) {

        usuario.setId(id); // Garante que o ID seja o mesmo da URL
        Map<String, Object> resultado = usuarioController.atualizarUsuario(usuario);

        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
        }

        return ResponseEntity.ok(new Mensagem(resultado.get("mensagem").toString()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Mensagem> deletar(@PathVariable int id) {
        System.out.println("Tentando excluir usuário ID: " + id);

        try {
            Map<String, Object> resultado = usuarioController.deletar(id);

            if (resultado.containsKey("erro")) {
                System.out.println("Erro ao excluir: " + resultado.get("erro"));
                return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
            }

            System.out.println("Usuário excluído com sucesso");
            return ResponseEntity.ok(new Mensagem(resultado.get("mensagem").toString()));
        } catch (Exception e) {
            System.out.println("Exceção ao excluir usuário: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Mensagem("Erro interno ao excluir usuário"));
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Mensagem> cadastrar(@RequestBody Usuario usuario) {
        Map<String, Object> resultado = usuarioController.cadastrarUsuario(usuario);
        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
        }
        return ResponseEntity.ok(new Mensagem("Usuário criado com sucesso"));
    }

    @PutMapping("/{id}/desativar")
    public ResponseEntity<Mensagem> desativar(@PathVariable int id) {
        Map<String, Object> resultado = usuarioController.desativarUsuario(id);

        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest()
                    .body(new Mensagem(resultado.get("erro").toString()));
        }

        return ResponseEntity.ok()
                .body(new Mensagem(resultado.get("mensagem").toString()));
    }
}