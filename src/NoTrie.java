import java.util.HashMap;
import java.util.Map;

/**
 * Classe que representa um Nó da Árvore Trie (Prefix Tree).
 * É utilizada para buscar Pontos de Coleta por prefixo (autocomplete).
 */
public class NoTrie {
    // Mapa que armazena os nós filhos, onde a chave é o próximo caractere
    Map<Character, NoTrie> filhos = new HashMap<>();
    // Referência ao PontoColeta, armazenado SOMENTE se este nó marcar o fim de uma palavra
    PontoColeta ponto;
    // Flag que indica se o caminho até este nó forma um nome completo de PontoColeta
    boolean fimPalavra;
}