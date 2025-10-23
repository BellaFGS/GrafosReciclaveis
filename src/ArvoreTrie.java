import java.util.ArrayList;
import java.util.List;

/**
 * Implementa a estrutura de dados de Árvore Trie (Prefix Tree).
 * É usada para indexar os Pontos de Coleta e permitir a busca eficiente
 * por um determinado prefixo (funcionalidade de 'autocomplete').
 */
public class ArvoreTrie {
    // Raiz da Árvore Trie
    NoTrie raiz = new NoTrie();

    /**
     * Insere um PontoColeta na Trie, decompondo seu nome em caracteres.
     * * @param ponto O PontoColeta a ser inserido.
     */
    public void inserir(PontoColeta ponto) {
        NoTrie atual = raiz;
        // Percorre cada caractere do nome em minúsculas
        for (char c : ponto.nome.toLowerCase().toCharArray()) {
            // Se o filho para o caractere 'c' não existe, ele é criado
            atual.filhos.putIfAbsent(c, new NoTrie());
            // Move para o próximo nó
            atual = atual.filhos.get(c);
        }
        // Marca o nó final como o fim de uma palavra e armazena o ponto
        atual.fimPalavra = true;
        atual.ponto = ponto;
    }

    /**
     * Busca todos os Pontos de Coleta cujo nome começa com o prefixo fornecido.
     * * @param prefixo O prefixo de busca.
     * @return Uma lista de PontosColeta que correspondem ao prefixo.
     */
    public List<PontoColeta> buscarPorPrefixo(String prefixo) {
        List<PontoColeta> resultados = new ArrayList<>();
        NoTrie atual = raiz;

        // 1. Percorre a árvore até o final do prefixo
        for (char c : prefixo.toLowerCase().toCharArray()) {
            if (!atual.filhos.containsKey(c))
                // Se um caractere do prefixo não for encontrado, não há resultados
                return resultados;
            atual = atual.filhos.get(c);
        }

        // 2. A partir do nó final do prefixo, coleta todos os Pontos de Coleta
        coletarTodosPontos(atual, resultados);
        return resultados;
    }

    /**
     * Método auxiliar recursivo que percorre todas as sub-árvores a partir de um nó
     * e coleta todos os PontosColeta que encontra (onde fimPalavra é true).
     * * @param no O nó a partir do qual a coleta começa.
     * @param resultados A lista para adicionar os pontos encontrados.
     */
    private void coletarTodosPontos(NoTrie no, List<PontoColeta> resultados) {
        // Se o nó atual marca o fim de uma palavra, adiciona o ponto à lista
        if (no.fimPalavra) resultados.add(no.ponto);

        // Recursivamente, visita todos os nós filhos
        for (NoTrie filho : no.filhos.values())
            coletarTodosPontos(filho, resultados);
    }
}