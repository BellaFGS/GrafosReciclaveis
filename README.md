# ♻️ Projeto Recicláveis  

O **Projeto Recicláveis** é um sistema em **Java (Swing)** que gerencia e visualiza uma **rede de pontos de coleta**.  
Ele utiliza estruturas de dados avançadas — **Grafos, Árvores BST e Trie** — para otimizar buscas, rotas e representações da rede.  

---

## 🧱 Estrutura Geral do Sistema

O projeto é dividido em quatro partes principais:

1. **Classes de Dados Fundamentais**  
2. **Estruturas de Busca (BST e Trie)**  
3. **Estrutura Principal do Grafo**  
4. **Interface Gráfica e Lançador**

---

## I. ⚙️ Classes de Dados Fundamentais

Essas classes representam os **elementos básicos** da rede.

### 🧩 `PontoColeta.java`
- **Função:** Representa um **nó (vértice)** da rede.  
- **Atributos principais:**  
  - `id (int)`: identificador único do ponto.  
  - `nome (String)`: nome descritivo do local.  
- **Lógica:** Armazena as informações essenciais de cada ponto de coleta.

### 🔗 `Conexao.java`
- **Função:** Representa uma **rota (aresta)** entre dois pontos.  
- **Atributos principais:**  
  - `origem (int)`: ponto de partida.  
  - `destino (int)`: ponto de chegada.  
  - `distancia (int)`: peso da aresta (distância em metros).  
- **Lógica:** Define conexões **direcionadas e ponderadas** entre pontos.

---

## II. 🌲 Estruturas de Dados de Busca

Implementações para **buscas rápidas e eficientes** de pontos na rede.

### 🔍 `ArvoreBST.java`
- **Função:** Estrutura **BST (Árvore Binária de Busca)** para indexar pontos pelo nome.  
- **Lógica:** Ordena alfabeticamente os nomes e percorre a árvore por comparação binária.  
- **Métodos principais:**  
  - `inserir(PontoColeta ponto)` → adiciona ponto mantendo a ordenação.  
  - `buscar(String nome)` → busca exata por nome.

### ⚡ `ArvoreTrie.java`
- **Função:** Implementa uma **Trie (Árvore de Prefixos)** para buscas por prefixo (autocomplete).  
- **Lógica:** Quebra o nome em caracteres e armazena cada letra em nós sequenciais.  
- **Métodos principais:**  
  - `inserir(PontoColeta ponto)` → adiciona caracteres e marca o final do nome.  
  - `buscarPorPrefixo(String prefixo)` → retorna todos os pontos que compartilham o prefixo.

---

## III. 🕸️ Estrutura Principal do Grafo

Classe central que **gerencia toda a rede**, suas representações e algoritmos.

### 🧠 `GrafoColetaReciclaveis.java`
- **Função:** Controla o grafo, suas listas, matrizes e operações.  
- **Atributos principais:**  
  - `pontos (Map<Integer, PontoColeta>)` → todos os vértices.  
  - `conexoes (List<Conexao>)` → todas as arestas.  
  - `listaSucessores (Map<Integer, List<Conexao>>)` → lista de adjacência/sucessores.  

#### 🔧 Funções de manutenção de dados
- `carregarPontosDeColeta(String arquivo)`  
- `carregarConexoes(String arquivo)`  
- Métodos CRUD: adicionar, editar e excluir pontos ou conexões.  
- Atualizações automáticas da **BST** e **Trie**.

#### 📊 Representações do Grafo
- `gerarMatrizAdjacencia()` → tabela de distâncias diretas.  
- `gerarMatrizIncidencia()` → mapeia pontos e conexões (1 = saída, -1 = entrada).  
- `getListaSucessores()` → lista os vizinhos diretos.  
- `calcularGraus()` → calcula o grau de cada ponto.

#### 🚀 Algoritmos Implementados
- `encontrarCaminhoBFS(int origem, int destino)` → busca em largura (menor número de conexões).  
- `calcularEstatisticas()` → gera médias, somas e extremos das distâncias da rede.

---

## IV. 💻 Interface Gráfica e Lançador

Essas classes compõem a **camada visual** e a inicialização do sistema.

### 🎨 `PainelGrafo.java`
- **Função:** Renderiza o grafo na tela.  
- **Lógica:**  
  - Desenha os pontos em layout circular.  
  - Conecta os nós com linhas exibindo as distâncias.  
- **Método principal:**  
  - `paintComponent(Graphics g)` → desenha vértices e arestas com pesos.

### 🪄 `SistemaColetaReciclaveis.java`
- **Função:** Classe principal e **menu do sistema** (interface Swing).  
- **Lógica:**  
  - Controla duas instâncias de grafo: por **arquivo** e **manual**.  
  - Gerencia ações de menu e integra com `GrafoColetaReciclaveis`.  
- **Métodos principais:**  
  - `criarMenu()` → constrói o menu de botões.  
  - `executarOpcao(int opcao)` → direciona as ações (ex: mostrar matriz, buscar caminho).  
  - `alternarModo()` → alterna entre os modos de operação.  
  - `main(String[] args)` → ponto de entrada da aplicação.

---

## 🧭 Conclusão

O **Projeto Recicláveis** combina **conceitos de teoria dos grafos e árvores** com uma **interface interativa**, facilitando a visualização e análise de redes urbanas.  
Ele demonstra na prática como estruturas de dados otimizam **buscas, rotas e representações** em sistemas reais.

---

### 📌 Tecnologias utilizadas
- **Java (JDK 17+)**
- **Swing (Interface Gráfica)**
- **Collections Framework**
- **Arquivos `.txt`** para entrada de dados

---

> ✨ Desenvolvido como projeto prático da disciplina **Estruturas de Dados**  
> (Análise e Implementação de Grafos, Árvores BST/Trie e Leitura de Arquivos)
