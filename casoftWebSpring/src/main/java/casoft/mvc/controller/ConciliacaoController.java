package casoft.mvc.controller;

import casoft.mvc.dao.ConciliacaoDAO;
import casoft.mvc.model.Conciliacao;
import casoft.mvc.util.Singleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConciliacaoController
{

    @Autowired
    private Conciliacao conciliacaoModel;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private ConciliacaoDAO conciliacaoDAO;

    public List<Map<String, Object>> getItensNaoConciliados()
    {
        Singleton conexao = Singleton.getInstancia();
        if (conexao.conectar()) {
            List<Map<String, Object>> resultado = null;
            try {
                resultado = conciliacaoModel.consultarItensNaoConciliados(conexao);
            } catch (Exception e) {
                System.err.println("Erro no Controller ao buscar itens não conciliados: " + e.getMessage());
            } finally {
                conexao.Desconectar();
            }
            return resultado;
        }
        return null;
    }

    public Map<String, Object> addConciliacaoProblemas(List<Conciliacao> conciliacoes)
    {
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();

        if (conexao.conectar()) {
            try {
                boolean sucessoTotal = true;
                for (Conciliacao conc : conciliacoes) {
                    if (conciliacaoModel.gravar(conc, conexao) == null) {
                        sucessoTotal = false;
                        break;
                    }
                }
                if (sucessoTotal) {
                    conexao.getConexao().commit();
                    json.put("ok", "Conciliações pendentes salvas com sucesso!");
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao gravar uma ou mais conciliações.");
                }
            } catch (Exception e) {
                conexao.getConexao().rollback();
                System.err.println("Erro no Controller ao salvar conciliações pendentes: " + e.getMessage());
                json.put("erro", "Erro ao salvar conciliações pendentes: " + e.getMessage());
            } finally {
                conexao.Desconectar();
            }
        } else {
            json.put("erro", "Erro ao conectar com o BD para salvar conciliações.");
        }
        return json;
    }

    public Map<String, Object> marcarDespesaConciliada(int despesaId) {
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();

        if (conexao.conectar()) {
            try {
                if (conciliacaoModel.marcarDespesaComoConciliada(despesaId, conexao)) {
                    conexao.getConexao().commit();
                    json.put("ok", "Despesa ID " + despesaId + " conciliada com sucesso!");
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Falha ao conciliar despesa ID " + despesaId + ".");
                }
            } catch (Exception e) {
                conexao.getConexao().rollback();
                System.err.println("Erro no Controller ao conciliar despesa: " + e.getMessage());
                json.put("erro", "Erro ao conciliar despesa: " + e.getMessage());
            } finally {
                conexao.Desconectar();
            }
        } else {
            json.put("erro", "Erro ao conectar com o BD para conciliar despesa.");
        }
        return json;
    }

    public Map<String, Object> marcarReceitaConciliada(int receitaId)
    {
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();

        if (conexao.conectar()) {
            try {
                if (conciliacaoModel.marcarReceitaComoConciliada(receitaId, conexao)) {
                    conexao.getConexao().commit();
                    json.put("ok", "Receita ID " + receitaId + " conciliada com sucesso!");
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Falha ao conciliar receita ID " + receitaId + ".");
                }
            } catch (Exception e) {
                conexao.getConexao().rollback();
                System.err.println("Erro no Controller ao conciliar receita: " + e.getMessage());
                json.put("erro", "Erro ao conciliar receita: " + e.getMessage());
            } finally {
                conexao.Desconectar();
            }
        } else {
            json.put("erro", "Erro ao conectar com o BD para conciliar receita.");
        }
        return json;
    }

    public Map<String,Object> getConciliacaoID(int id)
    {
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();
        if(conexao.conectar()) {
            Conciliacao conc = conciliacaoModel.consultar(id, conexao);
            if(conc != null) {
                json.put("concId", conc.getConcId());
                json.put("concDtProblema", conc.getConcDtProblema().format(DATE_FORMATTER));
                json.put("concDescProblema", conc.getConcDescProblema());
                json.put("concDtSolucao", conc.getConcDtSolucao() != null ? conc.getConcDtSolucao().format(DATE_FORMATTER) : null);
                json.put("concDescSolucao", conc.getConcDescSolucao());
                json.put("concReceitaId", conc.getConcReceitaId() != 0 ? conc.getConcReceitaId() : null);
                json.put("concDespesaId", conc.getConcDespesaId() != 0 ? conc.getConcDespesaId() : null);
            } else {
                json.put("erro", "Conciliação com ID: " + id + " não encontrada");
            }
            conexao.Desconectar();
        } else {
            json.put("erro", "Erro ao conectar com o BD");
        }
        return json;
    }

    public Map<String,Object> addConciliacao(Conciliacao conciliacao)
    {
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json = new HashMap<>();

        if(conciliacao.getConcDescProblema() == null || conciliacao.getConcDescProblema().trim().isEmpty()) {
            json.put("erro", "Descrição do problema não pode estar vazia.");
        }
        else {
            if(conexao.conectar()) {
                if(conciliacaoModel.gravar(conciliacao, conexao) != null) {
                    json.put("ok", "Conciliação registrada com sucesso!");
                    conexao.getConexao().commit();
                } else {
                    json.put("erro", "Erro ao registrar conciliação.");
                    conexao.getConexao().rollback();
                }
                conexao.Desconectar();
            } else {
                json.put("erro", "Erro ao conectar com o BD.");
            }
        }
        return json;
    }

    public Map<String,Object> updateConciliacao(Conciliacao conciliacao) {
        Singleton conexao = Singleton.getInstancia();
        Map<String,Object> json = new HashMap<>();

        if(conciliacao.getConcDescProblema() == null || conciliacao.getConcDescProblema().trim().isEmpty()) {
            json.put("erro", "Descrição do problema não pode estar vazia.");
        } else {
            if(conexao.conectar()) {
                if(conciliacaoModel.alterar(conciliacao, conexao) != null) {
                    json.put("ok", "Conciliação atualizada com sucesso!");
                    conexao.getConexao().commit();
                } else {
                    json.put("erro", "Erro ao atualizar conciliação.");
                    conexao.getConexao().rollback();
                }
                conexao.Desconectar();
            } else {
                json.put("erro", "Erro ao conectar com o BD.");
            }
        }
        return json;
    }

    public Map<String, Object> deleteConciliacao(int id)
    {
        Singleton conexao = Singleton.getInstancia();
        Map<String, Object> json = new HashMap<>();

        if(conexao.conectar()) {
            Conciliacao conc = conciliacaoModel.consultar(id, conexao);
            if(conc != null) {
                if(conciliacaoModel.apagar(conc, conexao)) {
                    conexao.getConexao().commit();
                    json.put("ok", "Conciliação removida com sucesso!");
                } else {
                    conexao.getConexao().rollback();
                    json.put("erro", "Erro ao remover conciliação.");
                }
            } else {
                json.put("erro", "Conciliação não encontrada!");
            }
            conexao.Desconectar();
        } else {
            json.put("erro", "Erro ao conectar com o BD.");
        }
        return json;
    }

    public Map<String, Object> getProblemas(String filtro) {
        Map<String, Object> response = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        if (conexao.conectar()) {
            try {
                List<Conciliacao> problemas = conciliacaoDAO.getProblemas(filtro, conexao);
                conexao.getConexao().commit();

                response.put("ok", problemas);
            } catch (Exception e) {
                conexao.getConexao().rollback();
                System.err.println("Erro ao buscar problemas de conciliação: " + e.getMessage());
                response.put("erro", "Falha ao buscar problemas de conciliação: " + e.getMessage());
            } finally {
                conexao.Desconectar();
            }
        } else {
            response.put("erro", "Erro ao conectar com o banco de dados para buscar problemas.");
            System.err.println("Erro ao conectar com o banco de dados em getProblemas.");
        }
        return response;
    }

    public Map<String, Object> atualizarSolucaoConciliacao(int concId, LocalDate concDtSolucao, String concDescSolucao) {
        Map<String, Object> response = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        if (conexao.conectar()) {
            try {
                boolean updated = conciliacaoDAO.atualizarSolucao(concId, concDtSolucao, concDescSolucao, conexao);
                if (updated) {
                    conexao.getConexao().commit();
                    response.put("ok", "Solução da conciliação atualizada com sucesso.");
                } else {
                    conexao.getConexao().rollback();
                    response.put("erro", "Nenhuma conciliação encontrada com o ID " + concId + " ou não houve alteração.");
                }
            } catch (Exception e) {
                conexao.getConexao().rollback();
                System.err.println("Erro ao atualizar solução da conciliação: " + e.getMessage());
                response.put("erro", "Falha ao atualizar solução da conciliação: " + e.getMessage());
            } finally {
                conexao.Desconectar();
            }
        } else {
            response.put("erro", "Erro ao conectar com o banco de dados para atualizar solução.");
            System.err.println("Erro ao conectar com o banco de dados em atualizarSolucaoConciliacao.");
        }
        return response;
    }

    public Map<String, Object> apagarConciliacaoPorId(int concId) {
        Map<String, Object> response = new HashMap<>();
        Singleton conexao = Singleton.getInstancia();

        if (conexao.conectar()) {
            try {
                boolean deleted = conciliacaoDAO.apagarPorId(concId, conexao); // Chama o novo método do DAO
                if (deleted) {
                    conexao.getConexao().commit();
                    response.put("ok", "Conciliação ID " + concId + " excluída com sucesso.");
                } else {
                    conexao.getConexao().rollback();
                    response.put("erro", "Nenhuma conciliação encontrada com o ID " + concId + " para exclusão.");
                }
            } catch (Exception e) {
                conexao.getConexao().rollback();
                System.err.println("Erro ao apagar conciliação no Controller: " + e.getMessage());
                response.put("erro", "Falha ao apagar conciliação: " + e.getMessage());
            } finally {
                conexao.Desconectar();
            }
        } else {
            response.put("erro", "Erro ao conectar com o banco de dados para apagar conciliação.");
            System.err.println("Erro ao conectar com o banco de dados em apagarConciliacaoPorId.");
        }
        return response;
    }

}