/**
 * Classe que representa um Nó da Árvore Binária de Busca (BST).
 * Cada nó armazena uma instância de PontoColeta.
 */
public class NoBST {
    // Referência ao PontoColeta armazenado neste nó
    PontoColeta ponto;
    // Referências aos nós filhos
    NoBST esquerda, direita;

    /**
     * Construtor do Nó BST.
     * * @param ponto O PontoColeta a ser armazenado no nó.
     */
    public NoBST(PontoColeta ponto) {
        this.ponto = ponto;
    }
}