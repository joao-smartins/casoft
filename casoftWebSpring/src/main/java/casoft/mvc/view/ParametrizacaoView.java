package casoft.mvc.view;

import casoft.mvc.controller.ParametrizacaoController;
import casoft.mvc.util.JWTTokenProvider;
import casoft.mvc.util.Mensagem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("apis/param")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ParametrizacaoView {

    @Autowired
    private ParametrizacaoController paramController;
    @GetMapping(value="/{id}")
    public ResponseEntity<Object> getParam(@PathVariable int id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            Map<String,Object> param;
            param=paramController.getParam(id);
            if (param!=null)
                return ResponseEntity.ok(param);
            else
                return ResponseEntity.badRequest().body(new Mensagem("Necessário cadastrar a Empresa"));
        }
        return ResponseEntity.badRequest().body(new Mensagem("Usuario nao autenticado"));
    }
    @PostMapping
    public ResponseEntity<Object> addParam(@RequestParam("nomeEmpresa") String nomeEmpresa,@RequestParam("cnpj") String cnpj,@RequestParam("logradouro")String logradouro,@RequestParam("numero") String numero,@RequestParam("bairro") String bairro,@RequestParam("cidade") String cidade,@RequestParam("estado")String estado,@RequestParam("cep")String cep,@RequestParam("telefone")String telefone,@RequestParam("email")String email,@RequestParam("complemento")String complemento,@RequestPart("file") MultipartFile file, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
            Map<String,Object> json =paramController.addParam(nomeEmpresa,cnpj,logradouro,numero,bairro,cidade,estado,cep,telefone,email,complemento,file);
            if(json.get("erro")==null) {
                System.out.println("deu certo");
                return ResponseEntity.ok(new Mensagem(nomeEmpresa + " cadastrada com sucesso!"));
            }
            else
                return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
        return ResponseEntity.badRequest().body(new Mensagem("erro ao autenticar"));

    }
    @PutMapping
    public ResponseEntity<Object> updtParam(@RequestParam("nomeEmpresa") String nomeEmpresa,@RequestParam("cnpj") String cnpj,@RequestParam("logradouro")String logradouro,@RequestParam("numero") String numero,@RequestParam("bairro") String bairro,@RequestParam("cidade") String cidade,@RequestParam("estado")String estado,@RequestParam("cep")String cep,@RequestParam("telefone")String telefone,@RequestParam("email")String email,@RequestParam("complemento")String complemento,@RequestPart("file") MultipartFile file,HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getHeader("Authorization"));
        Map<String,Object> json=new HashMap<>();
        String token = httpServletRequest.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7);
        if(JWTTokenProvider.verifyToken(token)) {
             json=paramController.updtParam(nomeEmpresa,cnpj,logradouro,numero,bairro,cidade,estado,cep,telefone,email,complemento,file);
            if(json.get("erro")==null)
                return ResponseEntity.ok(new Mensagem(nomeEmpresa+" alterada com sucesso!"));
        }
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}
