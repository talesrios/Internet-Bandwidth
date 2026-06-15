# Ficha de Acompanhamento: Grupo G
**Problema:** UVA 820 - Internet Bandwidth

## 1. Resumo do problema em linguagem própria
O problema solicita o cálculo da quantidade máxima de dados (largura de banda) que pode ser trafegada entre dois computadores específicos (origem e destino) em uma rede. A principal característica desta rede é que os cabos de internet são bidirecionais: os dados podem fluir em ambos os sentidos simultaneamente, desde que a soma do tráfego não ultrapasse a capacidade total do cabo. Trata-se de um problema clássico de maximização de fluxo.

## 2. Interpretação da entrada e da saída
* **Entrada:** Processada em múltiplos casos de teste. Inicia com o número de nós $n$. A segunda linha contém o nó de origem $s$, o nó de destino $t$ e a quantidade de cabos $c$. As $c$ linhas seguintes trazem as conexões no formato `u v banda`. A leitura termina quando $n = 0$.
* **Saída:** Para cada caso de teste, deve-se imprimir a identificação da rede (`Network X`) e o fluxo máximo calculado (`The bandwidth is Y.`), com uma linha em branco separando as saídas.

## 3. Modelagem da rede de fluxo
* **Vértices:** Os nós da rede numerados de $1$ a $n$.
* **Origem e Sorvedouro:** Fornecidos na entrada como $s$ e $t$.
* **Arestas e Capacidades:** Como as conexões são bidirecionais, cada cabo entre $u$ e $v$ com banda $b$ é desmembrado em **duas arestas direcionadas** no grafo original: $u \rightarrow v$ com capacidade $b$, e $v \rightarrow u$ com capacidade $b$.
* **Arestas Paralelas:** Múltiplos cabos conectando o mesmo par de nós têm suas capacidades somadas na matriz de adjacência.

## 4. Justificativa da escolha do algoritmo
Optamos pelo **Edmonds-Karp** (que utiliza Busca em Largura - BFS). A justificativa é que o Ford-Fulkerson tradicional (com DFS) pode ser ineficiente se as capacidades forem altas, pois pode escolher caminhos ruins e atualizar o fluxo de $1$ em $1$, gerando *Time Limit Exceeded* (TLE). O Edmonds-Karp garante a escolha do caminho mais curto em número de arestas, mantendo a complexidade em $\mathcal{O}(V E^2)$, o que é eficiente e seguro para os limites de tempo do problema ($n \le 100$).

## 5. Instância pequena (Sample Input)
* **Nós:** 4 | **Origem:** 1 | **Sorvedouro:** 4 | **Conexões:** 5
* **Arestas (bidirecionais):**
  * $1 \leftrightarrow 2$: capacidade 20
  * $1 \leftrightarrow 3$: capacidade 10
  * $2 \leftrightarrow 3$: capacidade 5
  * $2 \leftrightarrow 4$: capacidade 10
  * $3 \leftrightarrow 4$: capacidade 20

## 6. Execução manual passo a passo
Fluxo Máximo Inicial = 0.

* **Passo 1: Encontrar caminho aumentante**
  * Caminho BFS: $1 \rightarrow 2 \rightarrow 4$
  * Gargalo: $min(20, 10) = 10$
  * Atualização Residual: Subtrai 10 nas arestas diretas; soma 10 nas reversas.
  * *Fluxo Acumulado:* **10**

* **Passo 2: Encontrar caminho aumentante**
  * Caminho BFS: $1 \rightarrow 3 \rightarrow 4$
  * Gargalo: $min(10, 20) = 10$
  * Atualização Residual: Subtrai 10 nas arestas diretas; soma 10 nas reversas.
  * *Fluxo Acumulado:* 10 + 10 = **20**

* **Passo 3: Encontrar caminho aumentante**
  * Caminho BFS: $1 \rightarrow 2 \rightarrow 3 \rightarrow 4$
  * Capacidades residuais neste caminho: $C(1\rightarrow2)=10$, $C(2\rightarrow3)=5$, $C(3\rightarrow4)=10$
  * Gargalo: $min(10, 5, 10) = 5$
  * Atualização Residual: Subtrai 5 nas arestas diretas; soma 5 nas reversas.
  * *Fluxo Acumulado:* 20 + 5 = **25**

* **Passo 4:** A BFS tenta achar um novo caminho a partir de 1, mas a capacidade residual para os nós vizinhos que levam ao sorvedouro está esgotada ou inacessível. O algoritmo encerra.

## 7. Verificação da resposta final
O fluxo máximo calculado manualmente finalizou em **25**. Ao comparar com o *Sample Output* fornecido pelo problema, o resultado é validado como idêntico (`The bandwidth is 25.`), comprovando que a modelagem bidirecional e a lógica dos caminhos aumentantes estão corretas.