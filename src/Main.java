import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    
    // Matriz de adjacência para armazenar as capacidades (e o grafo residual)
    static int[][] cap;
    // Array para armazenar o caminho aumentante encontrado pela BFS
    static int[] parent;
    // Número de nós na rede
    static int n;

    // Busca em Largura (BFS) para encontrar o menor caminho aumentante
    static boolean bfs(int s, int t) {
        Arrays.fill(parent, -1);
        Queue<Integer> q = new LinkedList<>();
        
        q.add(s);
        parent[s] = -2; // Marca a origem como visitada

        while (!q.isEmpty()) {
            int u = q.poll();

            if (u == t) return true;

            for (int v = 1; v <= n; v++) {
                // Se o vizinho 'v' ainda não foi visitado e existe capacidade residual
                if (parent[v] == -1 && cap[u][v] > 0) {
                    parent[v] = u; 
                    q.add(v);
                }
            }
        }
        return false;
    }

    // Algoritmo de Edmonds-Karp para Fluxo Máximo
    static int edmondsKarp(int s, int t) {
        int maxFlow = 0;

        // Enquanto houver um caminho aumentante no grafo residual
        while (bfs(s, t)) {
            // 1. Encontra o "gargalo" (capacidade mínima) no caminho encontrado
            int flow = Integer.MAX_VALUE;
            int curr = t;
            
            while (curr != s) {
                int prev = parent[curr];
                flow = Math.min(flow, cap[prev][curr]);
                curr = prev;
            }

            // Adiciona o fluxo do gargalo ao fluxo máximo total
            maxFlow += flow;

            // 2. Atualiza as capacidades no grafo residual
            curr = t;
            while (curr != s) {
                int prev = parent[curr];
                cap[prev][curr] -= flow; // Subtrai da aresta direta
                cap[curr][prev] += flow; // Adiciona à aresta reversa
                curr = prev;
            }
        }
        return maxFlow;
    }

    public static void main(String[] args) throws IOException {
        // Verifica se o arquivo local existe. Se sim, redireciona o System.in para ele.
        // No servidor do UVA esse arquivo não existirá, então ele usará o System.in normal.
        File inputFile = new File("dados/entradas_do_problema.txt");
        if (inputFile.exists()) {
            System.setIn(new FileInputStream(inputFile));
        }

        // Leitura rápida otimizada
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int networkId = 1;
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            n = Integer.parseInt(line);
            if (n == 0) break; // Condição de parada do problema

            StringTokenizer st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());

            // Instancia a matriz para nós indexados de 1 a n
            cap = new int[n + 1][n + 1];
            parent = new int[n + 1];

            for (int i = 0; i < c; i++) {
                st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                int bandwidth = Integer.parseInt(st.nextToken());

                // Soma das capacidades para tratar múltiplas arestas entre os mesmos nós
                // Bidirecional: atualiza ambos os sentidos
                cap[u][v] += bandwidth;
                cap[v][u] += bandwidth;
            }

            int result = edmondsKarp(s, t);

            // Saída formatada conforme exigido pelo UVA
            System.out.println("Network " + networkId++);
            System.out.println("The bandwidth is " + result + ".");
            System.out.println(); // Linha em branco obrigatória após cada caso
        }
    }
}