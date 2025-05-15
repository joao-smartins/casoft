package casoft.mvc.dao;

import casoft.mvc.util.Singleton;

import java.util.List;

public interface IDAO<T>{
    public Object gravar(T entidade, Singleton conexao);
    public Object alterar(T entidade, Singleton conexao);
    public boolean apagar(T entidade, Singleton conexao);
    public T get(int id, Singleton conexao);
    public List<T> get(String filtro, Singleton conexao);
}
