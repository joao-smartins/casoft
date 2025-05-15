package casoft.mvc.util;

public class Singleton {
    private static Singleton instancia;
    private Conexao conexao;

    private Singleton() {
    }
    public static Singleton getInstancia() {
        if (instancia==null) {
            instancia=new Singleton();
        }
        return instancia;
    }
    public boolean conectar()
    {
        if (conexao==null) {
            conexao=new Conexao();
            return conexao.conectar("jdbc:postgresql://localhost:5432/","casofa_db","postgres","postgres123");
            //return conexao.conectar("jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:5432/","postgres","postgres.txzqfrvdwaucmdcbnzfg","postgres123");
        }
        return true;
    }
    public boolean Desconectar() {
        if (conexao!=null) {
            boolean resultado=conexao.desconectar();
            conexao=null;
            return resultado;
        }
        return false;
    }

    public Conexao getConexao() {
        return conexao;
    }
}
