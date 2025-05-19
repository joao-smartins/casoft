package casoft.mvc.controller;

import casoft.mvc.dao.ContaDAO;
import casoft.mvc.model.Conta;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContaController {

    @Autowired
    private Conta contaModel;
    @Autowired
    private ContaDAO contaDAO;

    public Map<String, Object> getConta(int id_conta) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                Conta conta = contaModel.consultar(id_conta, conexao);
                if (conta != null) {
                    json.put("id", conta.getId());
                    json.put("agencia", conta.getAgencia());
                    json.put("numero", conta.getNumero());
                    json.put("banco", conta.getBanco());
                    json.put("telefone", conta.getTelefone());
                    json.put("endereco", conta.getEndereco());
                    json.put("ende_num", conta.getEnde_num());
                    json.put("gerente", conta.getGerente());
                } else {
                    json.put("erro", "Conta não encontrada");
                }
            } else {
                json.put("erro", "Erro ao conectar ao banco de dados");
            }
        } finally {
            conexao.Desconectar();
        }

        return json;
    }

    public List<Map<String, Object>> getContas() {
        List<Map<String, Object>> contasList = new ArrayList<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                List<Conta> contas = contaModel.consultarTodos(conexao);

                for (Conta conta : contas) {
                    Map<String, Object> json = new HashMap<>();
                    json.put("id", conta.getId());
                    json.put("agencia", conta.getAgencia());
                    json.put("numero", conta.getNumero());
                    json.put("banco", conta.getBanco());
                    json.put("telefone", conta.getTelefone());
                    json.put("endereco", conta.getEndereco());
                    json.put("ende_num", conta.getEnde_num());
                    json.put("gerente", conta.getGerente());

                    contasList.add(json);
                }
            } else {
                Map<String, Object> erro = new HashMap<>();
                erro.put("erro", "Erro ao conectar ao banco de dados");
                contasList.add(erro);
            }
        } finally {
            conexao.Desconectar();
        }

        return contasList;
    }


    public Map<String, Object> addConta(Conta conta) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                if (contaModel.gravar(conta, conexao) != null) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Conta cadastrada com sucesso");
                    json.put("id", conta.getId());
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao cadastrar a conta");
                }
            } else {
                json.put("erro", "Erro ao conectar com o banco de dados");
            }
        } catch (Exception e) {
            try {
                conexao.getConexao().rollback();
            } catch (Exception ignored) {}
            json.put("erro", "Erro ao armazenar o conteúdo: " + e.getMessage());
        } finally {
            conexao.Desconectar();
        }

        return json;
    }

    public Map<String, Object> updtConta(int id_conta, Conta contaAtualizada) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                contaAtualizada.setId(id_conta);
                if (contaModel.alterar(contaAtualizada, conexao) != null) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Conta atualizada com sucesso");
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao atualizar a conta");
                }
            } else {
                json.put("erro", "Erro ao conectar com o banco de dados");
            }
        } catch (Exception e) {
            conexao.getConexao().rollback();
            json.put("erro", "Erro ao atualizar a conta: " + e.getMessage());
        } finally {
            conexao.Desconectar();
        }

        return json;
    }

    public Map<String, Object> deletarConta(int id_conta) {
        Map<String, Object> json = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        try {
            if (conexao.conectar()) {
                Conta conta = new Conta();
                conta.setId(id_conta);

                if (contaModel.deletarConta(conta, conexao)) {
                    conexao.getConexao().commit();
                    json.put("mensagem", "Conta deletada com sucesso");
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao deletar a conta");
                }
            } else {
                json.put("erro", "Erro ao conectar com o banco de dados");
            }
        } catch (Exception e) {
            conexao.getConexao().rollback();
            json.put("erro", "Erro ao excluir a conta: " + e.getMessage());
        } finally {
            conexao.Desconectar();
        }

        return json;
    }

}
