package casoft.mvc.dao;

import casoft.mvc.util.Singleton;
import casoft.mvc.model.Parametrizacao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class ParametrizacaoDAO implements IDAO<Parametrizacao> {
    @Override
    public Parametrizacao gravar(Parametrizacao entidade, Singleton conexao) {
        String sql = """
        INSERT INTO parametrizacao (nome_empresa, cnpj, logradouro, numero, bairro, cidade, estado, cep, telefone,email) 
        VALUES ('#1', '#2', '#3', #4, '#5', '#6', '#7', '#8', '#9','#A');
        """;
        sql = sql.replace("#1", entidade.getNomeEmpresa());
        sql = sql.replace("#2", entidade.getCnpj());
        sql = sql.replace("#3", entidade.getLogradouro());
        sql = sql.replace("#4", String.valueOf(entidade.getNumero()));
        sql = sql.replace("#5", entidade.getBairro());
        sql = sql.replace("#6", entidade.getCidade());
        sql = sql.replace("#7", entidade.getEstado());
        sql = sql.replace("#8", entidade.getCep());
        sql = sql.replace("#9", entidade.getTelefone());
        sql = sql.replace("#A", entidade.getEmail());

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }
    @Override
    public Parametrizacao alterar(Parametrizacao entidade, Singleton conexao) {
        String sql = """
        UPDATE parametrizacao SET 
            nome_empresa = '#1',
            cnpj = '#2',
            logradouro = '#3',
            numero = #4,
            bairro = '#5',
            cidade = '#6',
            estado = '#7',
            cep = '#8',
            telefone = '#9',
            email = '#A'
        WHERE id = #B;
        """;
        sql = sql.replace("#1", entidade.getNomeEmpresa())
                .replace("#2", entidade.getCnpj())
                .replace("#3", entidade.getLogradouro())
                .replace("#4", String.valueOf(entidade.getNumero()))
                .replace("#5", entidade.getBairro())
                .replace("#6", entidade.getCidade())
                .replace("#7", entidade.getEstado())
                .replace("#8", entidade.getCep())
                .replace("#9", entidade.getTelefone())
                .replace("#A", entidade.getEmail())
                .replace("#B", String.valueOf(entidade.getId()));

        if (conexao.getConexao().manipular(sql)) {
            return entidade;
        } else {
            System.out.println("Erro: " + conexao.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(Parametrizacao entidade, Singleton conexao) {
        return false;
    }

    @Override
    public Parametrizacao get(int id, Singleton conexao) {
        String sql = "SELECT * FROM parametrizacao WHERE id = " + id;
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                Parametrizacao p = new Parametrizacao();
                p.setId(rs.getInt("id"));
                p.setNomeEmpresa(rs.getString("nome_empresa"));
                p.setCnpj(rs.getString("cnpj"));
                p.setLogradouro(rs.getString("logradouro"));
                p.setNumero(rs.getInt("numero"));
                p.setBairro(rs.getString("bairro"));
                p.setCidade(rs.getString("cidade"));
                p.setEstado(rs.getString("estado"));
                p.setCep(rs.getString("cep"));
                p.setTelefone(rs.getString("telefone"));
                p.setEmail(rs.getString("email"));
                return p;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar parametrização: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Parametrizacao> get(String filtro, Singleton conexao) {
        List<Parametrizacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM parametrizacao";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Parametrizacao p = new Parametrizacao();
                p.setId(rs.getInt("id"));
                p.setNomeEmpresa(rs.getString("nome_empresa"));
                p.setCnpj(rs.getString("cnpj"));
                p.setLogradouro(rs.getString("logradouro"));
                p.setNumero(rs.getInt("numero"));
                p.setBairro(rs.getString("bairro"));
                p.setCidade(rs.getString("cidade"));
                p.setEstado(rs.getString("estado"));
                p.setCep(rs.getString("cep"));
                p.setTelefone(rs.getString("telefone"));
                p.setEmail(rs.getString("email"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar parametrizações: " + e.getMessage());
        }
        return lista;
    }

    public boolean isEmpty(Singleton conexao) {
        String sql = "SELECT * FROM empresa";
        ResultSet rs = conexao.getConexao().consultar(sql);
        try {
            return !rs.next();
        } catch (SQLException e) {
            return true;
        }
    }
    public boolean deletarEmpresa(Singleton conexao) {
        String sql = "DELETE FROM empresa";
        return conexao.getConexao().manipular(sql);
    }
}
