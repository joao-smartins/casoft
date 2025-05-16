package casoft.mvc.model;

public class TipoReceitas {
    private int id;
    private String nome;

    public TipoReceitas(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public TipoReceitas() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
