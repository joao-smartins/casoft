package casoft.mvc.controller;

import casoft.mvc.model.Receitas;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceitasController {

    @Autowired
    private Receitas receitasD;

    public List<Map<String, Object>> getAll(String filtro) {
        Singleton conexao = Singleton.getInstancia();
        List<Map<String, Object>> listaReceitas = new ArrayList<>();
        Map<String, Object> jsonErro = new HashMap<>();

        if (conexao.conectar()) {
            List<Receitas> receitas = receitasD.get(filtro, conexao);
            if (!receitas.isEmpty()) {
                for (Receitas r : receitas) {
                    Map<String, Object> json = new HashMap<>();
                    json.put("valor", r.getValor());
                    json.put("futura", r.isFutura());
                    json.put("descricao", r.getDescricao());
                    json.put("categoria", r.getCategoria().getId());
                    json.put("data", r.getData().toString());
                    listaReceitas.add(json);
                }
                conexao.Desconectar();
                return listaReceitas;
            }
            conexao.Desconectar();
            jsonErro.put("erro", "Nenhuma receita encontrada.");
            listaReceitas.add(jsonErro);
            return listaReceitas;
        }
        jsonErro.put("erro", "Erro ao conectar ao banco de dados.");
        listaReceitas.add(jsonErro);
        return listaReceitas;
    }

    public Map<String, Object> get(int id) {
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if (conexao.conectar()) {
            Receitas r = receitasD.get(id, conexao);
            if (r != null) {
                json.put("valor", r.getValor());
                json.put("futura", r.isFutura());
                json.put("descricao", r.getDescricao());
                json.put("categoria", r.getCategoria().getNome());
                json.put("data", r.getData().toString());
                conexao.Desconectar();
                return json;
            }
            json.put("erro", "Receita não encontrada.");
            conexao.Desconectar();
        } else {
            json.put("erro", "Erro ao conectar ao banco de dados.");
        }
        return json;
    }

    public Map<String, Object> create(Receitas receita) {
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if (conexao.conectar()) {
            Receitas r = receitasD.gravar(receita, conexao);
            if (r != null) {
                json.put("mensagem", "Receita cadastrada com sucesso!");
                conexao.Desconectar();
                return json;
            }
            json.put("erro", "Erro ao cadastrar receita.");
            conexao.Desconectar();
        } else {
            json.put("erro", "Erro ao conectar ao banco de dados.");
        }
        return json;
    }

    public Map<String, Object> update(Receitas receita) {
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if (conexao.conectar()) {
            Receitas r = receitasD.alterar(receita, conexao);
            if (r != null) {
                json.put("mensagem", "Receita atualizada com sucesso!");
                conexao.Desconectar();
                return json;
            }
            json.put("erro", "Erro ao atualizar receita.");
            conexao.Desconectar();
        } else {
            json.put("erro", "Erro ao conectar ao banco de dados.");
        }
        return json;
    }
}