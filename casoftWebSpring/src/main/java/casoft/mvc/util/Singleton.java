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
        if (conexao == null) {
            conexao = new Conexao();
            boolean conectado = conexao.conectar("jdbc:postgresql://localhost:5432/", "casofa_db", "postgres", "postgres123");
            if (!conectado) {
                conexao = null;  // impede uso de conexão inválida
            }
            return conectado;
        }
        return conexao.getEstadoConexao();
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
