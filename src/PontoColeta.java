/**
 * Classe de dados para representar um Ponto de Coleta de Recicláveis.
 * É o nó (vértice) dentro da estrutura do Grafo.
 */
public class PontoColeta {
    // Identificador único do ponto de coleta (chave primária)
    int id;
    // Nome descritivo do ponto de coleta
    String nome;

    /**
     * Construtor para criar uma instância de PontoColeta.
     * * @param id O ID único do ponto.
     * @param nome O nome do ponto.
     */
    public PontoColeta(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /**
     * Sobrescreve o método toString para fornecer uma representação legível.
     * * @return Uma string formatada com o ID e o Nome do ponto.
     */
    @Override
    public String toString() {
        return "ID: " + id + " - " + nome;
    }
}