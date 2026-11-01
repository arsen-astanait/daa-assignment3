package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Tarjan SCC. dfs is iterative because recursion overflows on big graphs
public final class TarjanSCC {
    private TarjanSCC() {
    }

    public static final class Result {
        private final List<List<Integer>> components;
        private final int[] componentOf;
        private final long edgesExamined;
        private final long verticesVisited;

        Result(List<List<Integer>> components, int[] componentOf, long edgesExamined, long verticesVisited) {
            this.components = components;
            this.componentOf = componentOf;
            this.edgesExamined = edgesExamined;
            this.verticesVisited = verticesVisited;
        }

        public List<List<Integer>> components() {
            return components;
        }

        public int count() {
            return components.size();
        }

        public int componentOf(int v) {
            return componentOf[v];
        }

        public int largestComponentSize() {
            int best = 0;
            for (List<Integer> c : components) best = Math.max(best, c.size());
            return best;
        }

        public long edgesExamined() {
            return edgesExamined;
        }

        public long verticesVisited() {
            return verticesVisited;
        }
    }

    public static Result run(Graph g) {
        if (!g.isDirected()) {
            throw new IllegalArgumentException("SCC is defined for directed graphs");
        }
        int n = g.vertexCount();
        int[] index = new int[n];
        int[] low = new int[n];
        int[] nextEdge = new int[n];
        boolean[] onStack = new boolean[n];
        int[] sccStack = new int[n];
        int[] callStack = new int[n];
        int[] componentOf = new int[n];
        java.util.Arrays.fill(index, -1);
        List<List<Integer>> components = new ArrayList<>();
        int counter = 0;
        int sccTop = 0;
        long edgesExamined = 0;
        long visited = 0;

        for (int s = 0; s < n; s++) {
            if (index[s] != -1) continue;
            int callTop = 0;
            callStack[callTop++] = s;
            index[s] = low[s] = counter++;
            sccStack[sccTop++] = s;
            onStack[s] = true;
            visited++;

            while (callTop > 0) {
                int u = callStack[callTop - 1];
                List<Edge> out = g.adj(u);
                if (nextEdge[u] < out.size()) {
                    int v = out.get(nextEdge[u]++).to();
                    edgesExamined++;
                    if (index[v] == -1) {           // go deeper
                        index[v] = low[v] = counter++;
                        sccStack[sccTop++] = v;
                        onStack[v] = true;
                        callStack[callTop++] = v;
                        visited++;
                    } else if (onStack[v]) {
                        low[u] = Math.min(low[u], index[v]);
                    }
                } else {                            // done with u
                    callTop--;
                    if (callTop > 0) {
                        int parent = callStack[callTop - 1];
                        low[parent] = Math.min(low[parent], low[u]);
                    }
                    if (low[u] == index[u]) {       // found scc
                        List<Integer> component = new ArrayList<>();
                        int w;
                        do {
                            w = sccStack[--sccTop];
                            onStack[w] = false;
                            componentOf[w] = components.size();
                            component.add(w);
                        } while (w != u);
                        Collections.sort(component);
                        components.add(component);
                    }
                }
            }
        }
        return new Result(components, componentOf, edgesExamined, visited);
    }

    public static Graph condensation(Graph g, Result scc) {
        Graph dag = Graph.directed(scc.count());
        Set<Long> seen = new HashSet<>();
        for (Edge e : g.edges()) {
            int a = scc.componentOf(e.from());
            int b = scc.componentOf(e.to());
            if (a != b && seen.add((long) a * scc.count() + b)) {
                dag.addEdge(a, b, e.weight());
            }
        }
        return dag;
    }
}
