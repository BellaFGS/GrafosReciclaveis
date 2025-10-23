/**
 * Implementa a estrutura de dados de Árvore Binária de Busca (BST).
 * É usada para indexar os Pontos de Coleta e permitir buscas eficientes
 * pelo NOME, utilizando a ordenação alfabética.
 */
public class ArvoreBST {
    // Raiz da Árvore
    NoBST raiz;

    /**
     * Insere um novo PontoColeta na BST.
     * * @param ponto O PontoColeta a ser inserido.
     */
    public void inserir(PontoColeta ponto) {
        raiz = inserirRecursivo(raiz, ponto);
    }

    /**
     * Método auxiliar recursivo para inserir um ponto na BST.
     * A ordenação é feita pelo nome do PontoColeta (case-insensitive).
     * * @param raiz O nó raiz do sub-árvore atual.
     * @param ponto O PontoColeta a ser inserido.
     * @return O nó raiz da sub-árvore atualizada.
     */
    private NoBST inserirRecursivo(NoBST raiz, PontoColeta ponto) {
        // Se a raiz for nula, o novo ponto se torna a raiz desta sub-árvore
        if (raiz == null) return new NoBST(ponto);

        // Compara o nome do novo ponto com o nome do ponto no nó atual
        int comparacao = ponto.nome.compareToIgnoreCase(raiz.ponto.nome);

        if (comparacao < 0)
            // Se o nome for menor, insere na sub-árvore esquerda
            raiz.esquerda = inserirRecursivo(raiz.esquerda, ponto);
        else if (comparacao > 0)
            // Se o nome for maior, insere na sub-árvore direita
            raiz.direita = inserirRecursivo(raiz.direita, ponto);

        // Se a comparação for 0 (nomes iguais), não faz nada (não permite duplicatas)

        return raiz;
    }

    /**
     * Busca um PontoColeta na BST com base no nome.
     * * @param nome O nome do ponto a ser buscado.
     * @return O PontoColeta encontrado, ou null se não for encontrado.
     */
    public PontoColeta buscar(String nome) {
        return buscarRecursivo(raiz, nome);
    }

    /**
     * Método auxiliar recursivo para buscar um ponto na BST.
     * * @param raiz O nó raiz do sub-árvore atual.
     * @param nome O nome a ser buscado.
     * @return O PontoColeta encontrado ou null.
     */
    private PontoColeta buscarRecursivo(NoBST raiz, String nome) {
        // Se a raiz for nula, o ponto não foi encontrado
        if (raiz == null) return null;

        // Compara o nome buscado com o nome do ponto no nó atual
        int comp = nome.compareToIgnoreCase(raiz.ponto.nome);

        if (comp == 0)
            // Se a comparação for 0, o ponto foi encontrado
            return raiz.ponto;

        // Se for menor, busca na esquerda, senão, busca na direita
        return comp < 0 ? buscarRecursivo(raiz.esquerda, nome) : buscarRecursivo(raiz.direita, nome);
    }
}