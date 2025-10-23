import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Classe principal do sistema.
 * Gerencia a Interface Gráfica (JFrame) e a lógica de interação com o Grafo.
 */
public class SistemaColetaReciclaveis extends JFrame {

    private GrafoColetaReciclaveis grafoArquivos;   // Instância do grafo para dados carregados de arquivos
    private GrafoColetaReciclaveis grafoManual;     // Instância do grafo para dados inseridos manualmente
    private GrafoColetaReciclaveis grafoAtual;      // Referência ao grafo atualmente em uso (ou grafoArquivos ou grafoManual)
    private PainelGrafo painelGrafo;                // Painel para visualização gráfica do grafo
    private JLabel lblModo;                         // Label para mostrar o modo de operação atual
    private boolean modoArquivos = true;            // Flag que indica se o sistema está no modo de arquivos (true) ou manual (false)

    /**
     * Construtor da classe principal. Inicializa o JFrame e os componentes.
     */
    public SistemaColetaReciclaveis() {
        // Inicializa as instâncias dos grafos
        grafoArquivos = new GrafoColetaReciclaveis();
        grafoManual = new GrafoColetaReciclaveis();
        grafoAtual = grafoArquivos; // Inicia no modo arquivos

        // Configurações básicas da janela
        setTitle("Sistema de Coleta de Recicláveis");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Encerra a aplicação ao fechar
        setLayout(new BorderLayout());

        // Configuração do Painel Superior (mostra o modo atual)
        JPanel painelSuperior = new JPanel();
        painelSuperior.setBackground(new Color(70, 130, 180)); // Azul escuro
        lblModo = new JLabel("MODO: Dados dos Arquivos");
        lblModo.setFont(new Font("Arial", Font.BOLD, 16));
        lblModo.setForeground(Color.WHITE);
        painelSuperior.add(lblModo);
        add(painelSuperior, BorderLayout.NORTH);

        // Configuração do Painel de Visualização do Grafo
        painelGrafo = new PainelGrafo(grafoAtual);
        add(painelGrafo, BorderLayout.CENTER);

        // Cria e exibe o menu principal em um diálogo flutuante
        criarMenu();

        setVisible(true); // Torna a janela visível
    }

    /**
     * Cria e exibe o diálogo do Menu Principal com botões para todas as operações.
     */
    private void criarMenu() {
        JPanel painelMenu = new JPanel(new GridBagLayout()); // Layout para centralizar botões
        painelMenu.setBackground(new Color(240, 248, 255));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints(); // Configurações para o GridBagLayout
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Título ocupa 2 colunas

        // Título do menu
        JLabel titulo = new JLabel("MENU PRINCIPAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        painelMenu.add(titulo, gbc);

        // Opções do menu, incluindo as novas representações
        String[] opcoes = {
                "1. Carregar Arquivos",
                "2. Alternar para Modo Manual",
                "3. Ver Matriz de Incidência",
                "4. Ver Lista de Sucessores",
                "5. Ver Matriz de Adjacência",
                "6. Ver Lista de Arestas",
                "7. Ver Graus dos Pontos",
                "8. Buscar Caminho Mais Curto (BFS)",
                "9. Buscar Ponto por Nome (BST)",
                "10. Buscar por Prefixo (Trie)",
                "11. Ver Estatísticas",
                "12. Adicionar Ponto",
                "13. Editar Ponto",
                "14. Excluir Ponto",
                "15. Adicionar Conexão",
                "16. Editar Conexão",
                "17. Excluir Conexão",
                "18. Limpar Dados Manuais",
                "0. Sair"
        };

        // Cria e adiciona os botões
        gbc.gridwidth = 1;
        int col = 0;
        for (int i = 0; i < opcoes.length; i++) {
            gbc.gridy = (i / 2) + 1;
            gbc.gridx = col;

            JButton btn = new JButton(opcoes[i]);
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            btn.setPreferredSize(new Dimension(250, 35));

            final int opcao = i; // Índice no array de opções
            // Mapeia o clique do botão para a execução da opção
            btn.addActionListener(e -> executarOpcao(opcao));

            painelMenu.add(btn, gbc);
            col = (col + 1) % 2; // Alterna entre coluna 0 e 1
        }

        // Cria um ScrollPane para o menu
        JScrollPane scroll = new JScrollPane(painelMenu);
        scroll.setPreferredSize(new Dimension(600, 500));

        // Cria e configura o diálogo do menu
        JDialog dialog = new JDialog(this, "Menu", false); // Diálogo não modal
        dialog.add(scroll);
        dialog.pack();
        dialog.setLocationRelativeTo(this); // Centraliza na janela principal
        dialog.setVisible(true);
    }

    /**
     * Executa a funcionalidade correspondente ao índice da opção.
     * @param opcao O índice da opção no array.
     */
    private void executarOpcao(int opcao) {
        // Mapeamento dos índices para os métodos
        switch (opcao) {
            case 0: carregarArquivos(); break;
            case 1: alternarModo(); break;
            case 2: mostrarMatrizIncidencia(); break;
            case 3: mostrarListaSucessores(); break;
            case 4: mostrarMatrizAdjacencia(); break;
            case 5: mostrarListaConexoes(); break;
            case 6: mostrarGraus(); break;
            case 7: buscarCaminho(); break;
            case 8: buscarPorNome(); break;
            case 9: buscarPorPrefixo(); break;
            case 10: mostrarEstatisticas(); break;
            case 11: adicionarPonto(); break;
            case 12: editarPonto(); break;
            case 13: excluirPonto(); break;
            case 14: adicionarConexao(); break;
            case 15: editarConexao(); break;
            case 16: excluirConexao(); break;
            case 17: limparDadosManuais(); break;
            case 18: System.exit(0); break;
        }
    }

    /**
     * Carrega os dados dos Pontos de Coleta e Conexões a partir de arquivos.
     */
    private void carregarArquivos() {
        try {
            // Tenta carregar os dados
            grafoArquivos.carregarPontosDeColeta("pontos_coleta.txt");
            grafoArquivos.carregarConexoes("rotas_coletas.txt");

            // Se estiver no modo arquivos, atualiza a visualização
            if (modoArquivos) {
                grafoAtual = grafoArquivos;
                painelGrafo.repaint();
            }
            JOptionPane.showMessageDialog(this, "Arquivos carregados com sucesso!");
        } catch (IOException e) {
            // Mostra um erro se os arquivos não puderem ser lidos
            JOptionPane.showMessageDialog(this, "Erro: Arquivos 'pontos_coleta.txt' ou 'rotas_coletas.txt' não encontrados ou inválidos. Detalhe: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Alterna entre o modo de dados dos Arquivos e o modo Manual.
     */
    private void alternarModo() {
        modoArquivos = !modoArquivos; // Inverte o modo
        grafoAtual = modoArquivos ? grafoArquivos : grafoManual; // Atualiza a referência

        // Atualiza o label
        lblModo.setText("MODO: " + (modoArquivos ? "Dados dos Arquivos" : "Dados Manuais"));

        // Substitui e redesenha o PainelGrafo
        painelGrafo = new PainelGrafo(grafoAtual);
        getContentPane().removeAll(); // Remove todos os componentes antigos

        // Recria e adiciona o painel superior e o painel do grafo
        JPanel painelSuperior = new JPanel();
        painelSuperior.setBackground(new Color(70, 130, 180));
        painelSuperior.add(lblModo);
        add(painelSuperior, BorderLayout.NORTH);
        add(painelGrafo, BorderLayout.CENTER);

        revalidate(); // Recalcula o layout
        repaint(); // Redesenha a tela
    }

    /**
     * Gera e exibe a Matriz de Incidência do grafo atual em uma tabela.
     */
    private void mostrarMatrizIncidencia() {
        int[][] matriz = grafoAtual.gerarMatrizIncidencia();
        List<Integer> idsPontos = new ArrayList<>(grafoAtual.pontos.keySet());

        // 1. Prepara os cabeçalhos das colunas (índices das Conexões)
        String[] colunas = new String[grafoAtual.conexoes.size() + 1];
        colunas[0] = "Ponto \\ Conexão";
        for (int j = 0; j < grafoAtual.conexoes.size(); j++) {
            // Exibe o número da Conexão (1-based) e sua descrição
            Conexao c = grafoAtual.conexoes.get(j);
            colunas[j + 1] = String.format("%d (ID%d->ID%d)", j + 1, c.origem, c.destino);
        }

        // 2. Prepara os dados da tabela
        Object[][] dados = new Object[idsPontos.size()][grafoAtual.conexoes.size() + 1];
        for (int i = 0; i < idsPontos.size(); i++) {
            dados[i][0] = idsPontos.get(i); // Primeira coluna é o ID do Ponto
            for (int j = 0; j < grafoAtual.conexoes.size(); j++) {
                dados[i][j + 1] = matriz[i][j]; // Valores: 1 (saída), -1 (entrada), 0 (não incidente)
            }
        }

        // Cria a tabela e exibe em um diálogo
        JTable tabela = new JTable(dados, colunas);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Permite barra de rolagem horizontal
        JOptionPane.showMessageDialog(this, new JScrollPane(tabela),
                "Matriz de Incidência (1=Saída, -1=Entrada)", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Exibe a Lista de Sucessores (Lista de Adjacência) do grafo atual.
     */
    private void mostrarListaSucessores() {
        Map<Integer, List<Conexao>> lista = grafoAtual.getListaSucessores();
        StringBuilder sb = new StringBuilder("LISTA DE SUCESSORES (ADJACÊNCIA):\n\n");

        // Itera sobre todos os pontos (garante que mesmo os sem sucessores apareçam)
        for (Map.Entry<Integer, PontoColeta> pontoEntry : grafoAtual.pontos.entrySet()) {
            int idOrigem = pontoEntry.getKey();
            PontoColeta origem = pontoEntry.getValue();
            List<Conexao> sucessores = lista.getOrDefault(idOrigem, Collections.emptyList());

            sb.append(String.format("Ponto %d (%s) -> ", idOrigem, origem.nome));

            if (sucessores.isEmpty()) {
                sb.append("[]\n");
            } else {
                List<String> vizinhos = new ArrayList<>();
                for (Conexao c : sucessores) {
                    PontoColeta destino = grafoAtual.pontos.get(c.destino);
                    vizinhos.add(String.format("%d (%s - %dm)", c.destino, destino != null ? destino.nome : "?", c.distancia));
                }
                sb.append("[").append(String.join(", ", vizinhos)).append("]\n");
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        area.setRows(20);
        area.setColumns(50);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Lista de Sucessores", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Gera e exibe a Matriz de Adjacência do grafo atual em uma tabela.
     */
    private void mostrarMatrizAdjacencia() {
        int[][] matriz = grafoAtual.gerarMatrizAdjacencia();
        List<Integer> ids = new ArrayList<>(grafoAtual.pontos.keySet());

        // 1. Cria os cabeçalhos das colunas (IDs dos pontos)
        String[] colunas = new String[ids.size() + 1];
        colunas[0] = "ID \\ ID"; // Cabeçalho da primeira coluna
        for (int i = 0; i < ids.size(); i++) colunas[i + 1] = String.valueOf(ids.get(i));

        // 2. Preenche os dados da tabela
        Object[][] dados = new Object[ids.size()][ids.size() + 1];
        for (int i = 0; i < ids.size(); i++) {
            dados[i][0] = ids.get(i); // Primeira coluna é o ID de origem
            for (int j = 0; j < ids.size(); j++) {
                dados[i][j + 1] = matriz[i][j]; // Valores da matriz (distâncias)
            }
        }

        // Cria a tabela e exibe em um diálogo
        JTable tabela = new JTable(dados, colunas);
        // Desativa a edição da tabela
        tabela.setEnabled(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(tabela), "Matriz de Adjacência (Distância em m)", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Exibe a lista de todas as Conexões (Lista de Arestas) do grafo atual em um formato legível.
     */
    private void mostrarListaConexoes() {
        StringBuilder sb = new StringBuilder("CONEXÕES (Lista de Arestas - Índice | Origem → Destino (Distância)):\n\n");
        for (int i = 0; i < grafoAtual.conexoes.size(); i++) {
            Conexao c = grafoAtual.conexoes.get(i);
            PontoColeta origem = grafoAtual.pontos.get(c.origem);
            PontoColeta destino = grafoAtual.pontos.get(c.destino);

            // Formata a string: (índice + 1). Nome_Origem -> Nome_Destino (Distância_m)
            sb.append(String.format("%d. %s → %s (%dm)\n", i + 1,
                    origem != null ? origem.nome : "? (ID " + c.origem + ")",
                    destino != null ? destino.nome : "? (ID " + c.destino + ")",
                    c.distancia));
        }

        // Exibe o texto em uma área de texto com scroll
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Lista de Conexões (Arestas)", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Calcula e exibe o grau (número de conexões) de cada ponto de coleta.
     */
    private void mostrarGraus() {
        Map<Integer, Integer> graus = grafoAtual.calcularGraus();
        StringBuilder sb = new StringBuilder("GRAUS DOS PONTOS (Entrada + Saída):\n\n");
        for (Map.Entry<Integer, PontoColeta> entry : grafoAtual.pontos.entrySet()) {
            // Formata a string: Nome do Ponto (ID) : Grau
            sb.append(String.format("%s (ID %d): %d\n",
                    entry.getValue().nome, entry.getKey(), graus.get(entry.getKey())));
        }

        // Exibe o resultado
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Graus dos Pontos", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Solicita IDs de origem e destino e busca o caminho mais curto (em conexões) via BFS.
     * Exibe o caminho e a distância total.
     */
    private void buscarCaminho() {
        String origemStr = JOptionPane.showInputDialog("ID do ponto de origem:");
        String destinoStr = JOptionPane.showInputDialog("ID do ponto de destino:");
        if (origemStr == null || destinoStr == null) return; // Cancelado

        try {
            int origem = Integer.parseInt(origemStr);
            int destino = Integer.parseInt(destinoStr);
            List<Integer> caminho = grafoAtual.encontrarCaminhoBFS(origem, destino);

            if (caminho == null) {
                JOptionPane.showMessageDialog(this, "Não há caminho entre os pontos ou IDs inválidos!");
            } else {
                StringBuilder sb = new StringBuilder("Caminho encontrado (BFS - Mais Curto em Conexões):\n\n");
                int distTotal = 0;

                // Percorre o caminho encontrado (lista de IDs)
                for (int i = 0; i < caminho.size(); i++) {
                    PontoColeta p = grafoAtual.pontos.get(caminho.get(i));
                    sb.append(p != null ? p.nome : "? (ID " + caminho.get(i) + ")");

                    if (i < caminho.size() - 1) {
                        sb.append(" → ");
                        // Calcula a distância entre o ponto atual e o próximo
                        for (Conexao c : grafoAtual.conexoes) {
                            if (c.origem == caminho.get(i) && c.destino == caminho.get(i + 1)) {
                                distTotal += c.distancia;
                                break;
                            }
                        }
                    }
                }
                sb.append(String.format("\n\nDistância total do caminho: %d metros", distTotal));
                JOptionPane.showMessageDialog(this, sb.toString(), "Busca em Largura (BFS)", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "IDs inválidos! Por favor, insira números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Solicita um nome e busca o Ponto de Coleta correspondente usando a BST.
     */
    private void buscarPorNome() {
        String nome = JOptionPane.showInputDialog("Nome do ponto a buscar (Exato):");
        if (nome == null) return;

        PontoColeta ponto = grafoAtual.bst.buscar(nome);

        if (ponto != null) {
            JOptionPane.showMessageDialog(this, "Ponto encontrado:\n" + ponto, "Busca BST", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Ponto não encontrado!", "Busca BST", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Solicita um prefixo e busca todos os Pontos de Coleta que começam com ele, usando a Trie.
     */
    private void buscarPorPrefixo() {
        String prefixo = JOptionPane.showInputDialog("Prefixo para busca (Autocomplete):");
        if (prefixo == null) return;

        List<PontoColeta> resultados = grafoAtual.trie.buscarPorPrefixo(prefixo);

        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum ponto encontrado com o prefixo '" + prefixo + "'!", "Busca Trie", JOptionPane.WARNING_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("Pontos encontrados:\n\n");
            for (PontoColeta p : resultados) sb.append(p).append("\n");
            JOptionPane.showMessageDialog(this, sb.toString(), "Busca Trie", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Exibe as estatísticas gerais do grafo atual.
     */
    private void mostrarEstatisticas() {
        String stats = grafoAtual.calcularEstatisticas();
        JOptionPane.showMessageDialog(this, stats, "Estatísticas do Grafo", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== OPERAÇÕES DE MUTAÇÃO (Adicionar/Editar/Excluir) ====================

    /**
     * Adiciona um novo Ponto de Coleta.
     */
    private void adicionarPonto() {
        String nome = JOptionPane.showInputDialog("Nome do novo ponto:");
        if (nome != null && !nome.trim().isEmpty()) {
            grafoAtual.adicionarPonto(nome.trim());
            painelGrafo.repaint(); // Redesenha o grafo
            JOptionPane.showMessageDialog(this, "Ponto adicionado!");
        }
    }

    /**
     * Edita o nome de um Ponto de Coleta existente.
     */
    private void editarPonto() {
        String idStr = JOptionPane.showInputDialog("ID do ponto a editar:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            if (!grafoAtual.pontos.containsKey(id)) {
                JOptionPane.showMessageDialog(this, "ID de ponto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String novoNome = JOptionPane.showInputDialog("Novo nome:");
            if (novoNome != null && !novoNome.trim().isEmpty()) {
                grafoAtual.editarPonto(id, novoNome.trim());
                painelGrafo.repaint();
                JOptionPane.showMessageDialog(this, "Ponto editado!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exclui um Ponto de Coleta.
     */
    private void excluirPonto() {
        String idStr = JOptionPane.showInputDialog("ID do ponto a excluir:");
        if (idStr == null) return;

        try {
            int id = Integer.parseInt(idStr);
            if (!grafoAtual.pontos.containsKey(id)) {
                JOptionPane.showMessageDialog(this, "ID de ponto não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int resp = JOptionPane.showConfirmDialog(this, "Confirma exclusão? Todas as conexões associadas também serão removidas.", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (resp == JOptionPane.YES_OPTION) {
                grafoAtual.excluirPonto(id);
                painelGrafo.repaint();
                JOptionPane.showMessageDialog(this, "Ponto excluído!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adiciona uma nova Conexão.
     */
    private void adicionarConexao() {
        try {
            int origem = Integer.parseInt(JOptionPane.showInputDialog("ID origem:"));
            int destino = Integer.parseInt(JOptionPane.showInputDialog("ID destino:"));
            int distancia = Integer.parseInt(JOptionPane.showInputDialog("Distância (m):"));

            // Validação simples de IDs (se existem)
            if (!grafoAtual.pontos.containsKey(origem) || !grafoAtual.pontos.containsKey(destino)) {
                JOptionPane.showMessageDialog(this, "Um ou ambos os IDs de ponto não existem!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            grafoAtual.adicionarConexao(origem, destino, distancia);
            painelGrafo.repaint();
            JOptionPane.showMessageDialog(this, "Conexão adicionada!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dados inválidos! Certifique-se de que IDs e Distância são números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Edita uma Conexão existente pelo seu índice.
     */
    private void editarConexao() {
        try {
            // O índice é 1-based para o usuário, por isso subtrai 1
            int index = Integer.parseInt(JOptionPane.showInputDialog("Número da conexão a editar (veja em 'Ver Lista de Arestas'):")) - 1;

            if (index >= 0 && index < grafoAtual.conexoes.size()) {
                int origem = Integer.parseInt(JOptionPane.showInputDialog("Nova ID origem:"));
                int destino = Integer.parseInt(JOptionPane.showInputDialog("Nova ID destino:"));
                int distancia = Integer.parseInt(JOptionPane.showInputDialog("Nova distância (m):"));

                // Validação simples de IDs (se existem)
                if (!grafoAtual.pontos.containsKey(origem) || !grafoAtual.pontos.containsKey(destino)) {
                    JOptionPane.showMessageDialog(this, "Um ou ambos os novos IDs de ponto não existem!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                grafoAtual.editarConexao(index, origem, destino, distancia);
                painelGrafo.repaint();
                JOptionPane.showMessageDialog(this, "Conexão editada!");
            } else {
                JOptionPane.showMessageDialog(this, "Índice de conexão inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dados inválidos! Certifique-se de usar números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exclui uma Conexão existente pelo seu índice.
     */
    private void excluirConexao() {
        try {
            // O índice é 1-based para o usuário, por isso subtrai 1
            int index = Integer.parseInt(JOptionPane.showInputDialog("Número da conexão a excluir (veja em 'Ver Lista de Arestas'):")) - 1;

            if (index >= 0 && index < grafoAtual.conexoes.size()) {
                int resp = JOptionPane.showConfirmDialog(this, "Confirma exclusão?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    grafoAtual.excluirConexao(index);
                    painelGrafo.repaint();
                    JOptionPane.showMessageDialog(this, "Conexão excluída!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Índice de conexão inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dados inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpa completamente a estrutura de dados do grafo manual.
     */
    private void limparDadosManuais() {
        int resp = JOptionPane.showConfirmDialog(this,
                "Isso irá apagar TODOS os dados manuais. Confirma?",
                "Confirmar Limpeza", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resp == JOptionPane.YES_OPTION) {
            grafoManual = new GrafoColetaReciclaveis(); // Cria uma nova instância limpa
            // Se o modo atual for manual, atualiza a referência e redesenha
            if (!modoArquivos) {
                grafoAtual = grafoManual;
                painelGrafo.repaint();
            }
            JOptionPane.showMessageDialog(this, "Dados manuais limpos! O grafo manual agora está vazio.");
        }
    }

    /**
     * Método principal (main) para iniciar a aplicação Swing.
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        // Garante que a GUI seja criada e executada na thread de Event Dispatch (EDT)
        SwingUtilities.invokeLater(() -> new SistemaColetaReciclaveis());
    }
}