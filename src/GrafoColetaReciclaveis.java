import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Classe principal que gerencia o Grafo de Pontos de Coleta de Recicláveis.
 * Contém a lista de nós (pontos), arestas (conexões) e as estruturas de busca (BST, Trie).
 */
public class GrafoColetaReciclaveis {
    // Map para armazenar os PontosColeta. A chave é o ID, garantindo acesso rápido.
    // LinkedHashMap mantém a ordem de inserção.
    public Map<Integer, PontoColeta> pontos = new LinkedHashMap<>();
    // Lista de Arestas: Lista para armazenar as Conexões (arestas) entre os pontos.
    public List<Conexao> conexoes = new ArrayList<>();
    // Estrutura para busca rápida de pontos por nome.
    public ArvoreBST bst = new ArvoreBST();
    // Estrutura para busca de pontos por prefixo (autocomplete).
    public ArvoreTrie trie = new ArvoreTrie();

    // Lista de Sucessores (ou Lista de Adjacência):
    // Map onde a chave é o ID do ponto e o valor é uma lista das conexões (arestas)
    // que saem desse ponto.
    private Map<Integer, List<Conexao>> listaSucessores = new HashMap<>();

    /**
     * Atualiza a Lista de Sucessores a partir da Lista de Arestas (conexoes).
     * Essa estrutura é crucial para operações eficientes como o BFS.
     */
    private void atualizarListaSucessores() {
        listaSucessores.clear();
        // Inicializa uma lista vazia para cada ponto (vértice)
        for (int id : pontos.keySet()) {
            listaSucessores.put(id, new ArrayList<>());
        }

        // Preenche as listas de sucessores
        for (Conexao c : conexoes) {
            // Se a origem da conexão existe no mapa de pontos, adiciona a conexão à lista de sucessores
            if (listaSucessores.containsKey(c.origem)) {
                listaSucessores.get(c.origem).add(c);
            }
        }
    }

    /**
     * Carrega os Pontos de Coleta a partir de um arquivo de texto.
     * O arquivo deve ter o formato: ID;Nome
     * @param arquivo O caminho do arquivo de pontos.
     * @throws IOException Se houver erro de leitura do arquivo.
     */
    public void carregarPontosDeColeta(String arquivo) throws IOException {
        pontos.clear(); // Limpa dados existentes
        // Usa try-with-resources para fechar o BufferedReader automaticamente
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            // Lê linha por linha
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                int id = Integer.parseInt(partes[0].trim()); // ID do ponto
                String nome = partes[1].trim(); // Nome do ponto
                PontoColeta ponto = new PontoColeta(id, nome);
                pontos.put(id, ponto);
                // Insere nas estruturas de busca
                bst.inserir(ponto);
                trie.inserir(ponto);
            }
        }
    }

    /**
     * Carrega as Conexões (rotas) a partir de um arquivo de texto.
     * O arquivo deve ter o formato: OrigemID;DestinoID;Distancia
     * @param arquivo O caminho do arquivo de conexões.
     * @throws IOException Se houver erro de leitura do arquivo.
     */
    public void carregarConexoes(String arquivo) throws IOException {
        conexoes.clear(); // Limpa dados existentes
        // Usa try-with-resources para fechar o BufferedReader automaticamente
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            // Lê linha por linha
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                int origem = Integer.parseInt(partes[0].trim());
                int destino = Integer.parseInt(partes[1].trim());
                int distancia = Integer.parseInt(partes[2].trim());
                conexoes.add(new Conexao(origem, destino, distancia)); // Adiciona a conexão
            }
        }
        // Recria a lista de sucessores após carregar as conexões
        atualizarListaSucessores();
    }

    /**
     * Adiciona um novo Ponto de Coleta ao grafo.
     * @param nome O nome do novo ponto.
     */
    public void adicionarPonto(String nome) {
        // Gera um novo ID: 1 se a lista estiver vazia, ou o maior ID + 1
        int novoId = pontos.isEmpty() ? 1 : Collections.max(pontos.keySet()) + 1;
        PontoColeta ponto = new PontoColeta(novoId, nome);
        pontos.put(novoId, ponto);
        // Atualiza as estruturas de busca
        bst.inserir(ponto);
        trie.inserir(ponto);

        // Adiciona o novo ponto na lista de sucessores (com lista vazia)
        listaSucessores.put(novoId, new ArrayList<>());
    }

    /**
     * Edita o nome de um Ponto de Coleta existente.
     * @param id O ID do ponto a ser editado.
     * @param novoNome O novo nome para o ponto.
     */
    public void editarPonto(int id, String novoNome) {
        if (pontos.containsKey(id)) {
            // Atualiza o nome no Map
            pontos.get(id).nome = novoNome;
            // É necessário reconstruir a BST e a Trie, pois a chave de ordenação/estrutura (o nome) mudou.
            bst = new ArvoreBST();
            trie = new ArvoreTrie();
            for (PontoColeta p : pontos.values()) {
                bst.inserir(p);
                trie.inserir(p);
            }
        }
    }

    /**
     * Exclui um Ponto de Coleta e todas as conexões associadas a ele.
     * @param id O ID do ponto a ser excluído.
     */
    public void excluirPonto(int id) {
        pontos.remove(id); // Remove o ponto do Map
        // Remove todas as conexões que têm o ponto como origem ou destino
        conexoes.removeIf(c -> c.origem == id || c.destino == id);
        // Reconstruir BST e Trie, pois um elemento foi removido.
        bst = new ArvoreBST();
        trie = new ArvoreTrie();
        for (PontoColeta p : pontos.values()) {
            bst.inserir(p);
            trie.inserir(p);
        }
        // Recria a lista de sucessores
        atualizarListaSucessores();
    }

    /**
     * Adiciona uma nova conexão (aresta) ao grafo.
     * @param origem O ID do ponto de origem.
     * @param destino O ID do ponto de destino.
     * @param distancia A distância (peso) da conexão.
     */
    public void adicionarConexao(int origem, int destino, int distancia) {
        conexoes.add(new Conexao(origem, destino, distancia));
        // Atualiza a lista de sucessores
        atualizarListaSucessores();
    }

    /**
     * Edita uma conexão existente pelo seu índice na lista.
     * @param index O índice da conexão na lista (0-based).
     * @param origem A nova ID de origem.
     * @param destino A nova ID de destino.
     * @param distancia A nova distância.
     */
    public void editarConexao(int index, int origem, int destino, int distancia) {
        if (index >= 0 && index < conexoes.size()) {
            Conexao c = conexoes.get(index);
            c.origem = origem;
            c.destino = destino;
            c.distancia = distancia;
            // Atualiza a lista de sucessores
            atualizarListaSucessores();
        }
    }

    /**
     * Exclui uma conexão existente pelo seu índice na lista.
     * @param index O índice da conexão a ser excluída (0-based).
     */
    public void excluirConexao(int index) {
        if (index >= 0 && index < conexoes.size()) {
            conexoes.remove(index);
            // Atualiza a lista de sucessores
            atualizarListaSucessores();
        }
    }

    /**
     * REPRESENTAÇÃO: Matriz de Incidência.
     * Dimensão: |Vértices| x |Arestas|
     * Valores: -1 se a aresta INCIDE (entra) no vértice.
     * 1 se a aresta SAI (origem) do vértice.
     * 0 se não há incidência.
     * @return Uma matriz de inteiros representando a Matriz de Incidência.
     */
    public int[][] gerarMatrizIncidencia() {
        int numPontos = pontos.size();
        int numConexoes = conexoes.size();
        int[][] matriz = new int[numPontos][numConexoes];

        // Mapeia o ID do ponto para o índice da linha (0 a numPontos-1)
        List<Integer> ids = new ArrayList<>(pontos.keySet());

        // Itera sobre as conexões (colunas da matriz)
        for (int j = 0; j < numConexoes; j++) {
            Conexao c = conexoes.get(j);

            // Encontra o índice da linha de origem e destino
            int iOrigem = ids.indexOf(c.origem);
            int iDestino = ids.indexOf(c.destino);

            // 1 indica que a aresta sai do vértice (origem)
            if (iOrigem >= 0) {
                matriz[iOrigem][j] = 1;
            }

            // -1 indica que a aresta entra no vértice (destino)
            if (iDestino >= 0) {
                matriz[iDestino][j] = -1;
            }
        }
        return matriz;
    }

    /**
     * REPRESENTAÇÃO: Lista de Sucessores (Lista de Adjacência).
     * Retorna a estrutura interna que armazena os vizinhos de cada ponto.
     * @return Um Map com o ID do Ponto e a lista de suas Conexões de saída.
     */
    public Map<Integer, List<Conexao>> getListaSucessores() {
        return listaSucessores;
    }

    /**
     * Gera uma Matriz de Adjacência para representar o grafo.
     * A posição [i][j] armazena a distância (peso) da conexão do ponto 'i' para o ponto 'j'.
     * @return Uma matriz de inteiros.
     */
    public int[][] gerarMatrizAdjacencia() {
        int n = pontos.size();
        int[][] matriz = new int[n][n];
        // Cria uma lista de IDs para mapear o ID real para o índice da matriz (0 a n-1)
        List<Integer> ids = new ArrayList<>(pontos.keySet());

        // Preenche a matriz
        for (Conexao c : conexoes) {
            // Obtém o índice da matriz correspondente ao ID de origem e destino
            int i = ids.indexOf(c.origem);
            int j = ids.indexOf(c.destino);
            // Se ambos os pontos existirem no grafo (tiverem índices válidos)
            if (i >= 0 && j >= 0) {
                matriz[i][j] = c.distancia; // Preenche com a distância
            }
        }
        return matriz;
    }

    /**
     * Calcula o grau de cada ponto de coleta. O grau é o número de conexões (entrada e saída)
     * que um ponto possui.
     * @return Um Map onde a chave é o ID do ponto e o valor é o seu grau.
     */
    public Map<Integer, Integer> calcularGraus() {
        Map<Integer, Integer> graus = new HashMap<>();
        // Inicializa o grau de todos os pontos para 0
        for (int id : pontos.keySet()) graus.put(id, 0);

        // Percorre todas as conexões e incrementa o grau da origem e do destino
        for (Conexao c : conexoes) {
            graus.put(c.origem, graus.get(c.origem) + 1);
            graus.put(c.destino, graus.get(c.destino) + 1);
        }
        return graus;
    }

    /**
     * Encontra o caminho mais curto (em número de conexões) entre dois pontos
     * usando o algoritmo Breadth-First Search (BFS).
     * @param origem O ID do ponto de origem.
     * @param destino O ID do ponto de destino.
     * @return Uma lista de IDs representando o caminho da origem ao destino, ou null se não houver caminho.
     */
    public List<Integer> encontrarCaminhoBFS(int origem, int destino) {
        // Verifica se os pontos existem no grafo
        if (!pontos.containsKey(origem) || !pontos.containsKey(destino)) return null;

        Map<Integer, Integer> predecessor = new HashMap<>();
        Queue<Integer> fila = new LinkedList<>();
        Set<Integer> visitados = new HashSet<>();

        // Inicia o BFS no ponto de origem
        fila.add(origem);
        visitados.add(origem);
        predecessor.put(origem, null); // A origem não tem predecessor

        // Loop principal da BFS
        while (!fila.isEmpty()) {
            int atual = fila.poll(); // Pega o próximo nó a ser visitado
            if (atual == destino) break; // Chegou ao destino

            // Encontra vizinhos usando a Lista de Sucessores para eficiência
            List<Conexao> sucessores = listaSucessores.getOrDefault(atual, Collections.emptyList());
            for (Conexao c : sucessores) {
                if (!visitados.contains(c.destino)) {
                    fila.add(c.destino);
                    visitados.add(c.destino);
                    predecessor.put(c.destino, atual); // Define o predecessor
                }
            }
        }

        // Se o destino não foi encontrado, não há caminho
        if (!predecessor.containsKey(destino)) return null;

        // Reconstrói o caminho a partir dos predecessores (do destino para a origem)
        List<Integer> caminho = new ArrayList<>();
        Integer atual = destino;
        while (atual != null) {
            caminho.add(0, atual); // Adiciona no início para inverter a ordem
            atual = predecessor.get(atual);
        }
        return caminho;
    }

    /**
     * Calcula estatísticas básicas sobre o grafo, como o número de pontos,
     * conexões, e as distâncias (mínima, máxima e média) das conexões.
     * @return Uma string formatada com todas as estatísticas.
     */
    public String calcularEstatisticas() {
        int distTotal = 0, distMin = Integer.MAX_VALUE, distMax = 0;

        // Calcula a soma, o mínimo e o máximo das distâncias
        for (Conexao c : conexoes) {
            distTotal += c.distancia;
            distMin = Math.min(distMin, c.distancia);
            distMax = Math.max(distMax, c.distancia);
        }

        // Calcula a distância média (evita divisão por zero)
        double distMedia = conexoes.isEmpty() ? 0 : (double) distTotal / conexoes.size();

        // Retorna a string formatada com os resultados
        return String.format(
                "Total de pontos: %d\n" +
                        "Total de conexões: %d\n" +
                        "Distância total: %d m\n" +
                        "Distância média: %.2f m\n" +
                        "Menor conexão: %d m\n" +
                        "Maior conexão: %d m",
                pontos.size(), conexoes.size(), distTotal, distMedia,
                // Trata o caso de lista vazia para distMin
                distMin == Integer.MAX_VALUE ? 0 : distMin, distMax
        );
    }
}