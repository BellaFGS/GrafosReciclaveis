import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Componente JPanel customizado responsável por desenhar a representação visual do Grafo.
 * Utiliza um layout circular para posicionar os nós.
 */
public class PainelGrafo extends JPanel {
    // Referência ao objeto Grafo que será desenhado
    private GrafoColetaReciclaveis grafo;

    /**
     * Construtor do painel.
     * * @param grafo A instância do grafo a ser visualizada.
     */
    public PainelGrafo(GrafoColetaReciclaveis grafo) {
        this.grafo = grafo;
        setBackground(Color.WHITE); // Define o fundo como branco
    }

    /**
     * Sobrescreve o método principal de pintura do componente.
     * * @param g O contexto gráfico.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Chama a implementação padrão
        Graphics2D g2 = (Graphics2D) g;
        // Ativa o antialiasing para um desenho mais suave
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Map para armazenar as coordenadas (X, Y) de cada ponto de coleta
        Map<Integer, Point> posicoes = new HashMap<>();
        // Lista de IDs para manter a ordem de posicionamento
        List<Integer> ids = new ArrayList<>(grafo.pontos.keySet());

        // Parâmetros para o layout circular
        int raio = Math.min(getWidth(), getHeight()) / 3; // Raio do círculo
        int cx = getWidth() / 2, cy = getHeight() / 2; // Centro do painel
        int n = ids.size(); // Número de pontos

        // Calcula a posição circular de cada ponto
        for (int i = 0; i < n; i++) {
            // Calcula o ângulo em radianos para espaçamento uniforme
            double ang = 2 * Math.PI * i / Math.max(1, n);
            // Calcula as coordenadas (x, y)
            int x = (int) (cx + raio * Math.cos(ang));
            int y = (int) (cy + raio * Math.sin(ang));
            posicoes.put(ids.get(i), new Point(x, y));
        }

        // 1. Desenhar conexões (arestas)
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(2)); // Linhas de espessura 2
        for (Conexao c : grafo.conexoes) {
            Point p1 = posicoes.get(c.origem);
            Point p2 = posicoes.get(c.destino);
            if (p1 != null && p2 != null) {
                // Desenha a linha entre os pontos
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                // Desenha o peso (distância) da conexão no meio da linha
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.drawString(c.distancia + "m", (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            }
        }

        // 2. Desenhar pontos (vértices)
        for (Map.Entry<Integer, Point> entry : posicoes.entrySet()) {
            Point p = entry.getValue();

            // Desenha a elipse (círculo) preenchida
            g2.setColor(new Color(100, 149, 237)); // Azul bonito
            g2.fillOval(p.x - 25, p.y - 25, 50, 50);

            // Desenha a borda do círculo
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(p.x - 25, p.y - 25, 50, 50);

            // Desenha o ID do ponto no centro
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String label = String.valueOf(entry.getKey());
            FontMetrics fm = g2.getFontMetrics();
            int w = fm.stringWidth(label);
            g2.drawString(label, p.x - w/2, p.y + 5); // Centraliza o texto
        }
    }
}