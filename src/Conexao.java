/**
 * Classe de dados para representar uma Conexão (rota/aresta)
 * entre dois Pontos de Coleta no Grafo.
 */
public class Conexao {
    int origem; // ID do ponto de coleta de partida (origem da rota)
    int destino; // ID do ponto de coleta de chegada (destino da rota)
    int distancia; // Peso da aresta: distância ou custo da rota em metros

    /**
     * Construtor para criar uma instância de Conexao.
     * * @param origem O ID do ponto de origem.
     * @param destino O ID do ponto de destino.
     * @param distancia A distância (peso) da conexão.
     */
    public Conexao(int origem, int destino, int distancia) {
        this.origem = origem;
        this.destino = destino;
        this.distancia = distancia;
    }

    /**
     * Sobrescreve o método toString para fornecer uma representação legível.
     * * @return Uma string formatada mostrando a rota e a distância.
     */
    @Override
    public String toString() {
        return origem + " → " + destino + " (" + distancia + "m)";
    }
}