package connectivity;

import graph.Edge;
import graph.Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class TopologicalSort {
    private TopologicalSort() {
    }

    public static final class Result {
        private final boolean dag;
        private final List<Integer> order;
        private final List<Integer> cycle;
        private final long vertexRemovals;
        private final long edgesExamined;

        Result(boolean dag, List<Integer> order, List<Integer> cycle, long vertexRemovals, long edgesExamined) {
            this.dag = dag;
            this.order = Collections.unmodifiableList(order);
            this.cycle = Collections.unmodifiableList(cycle);
            this.vertexRemovals = vertexRemovals;
            this.edgesExamined = edgesExamined;
        }

        public boolean isDag() {
            return dag;
        }

        public List<Integer> order() {
            return order;
        }

        public List<Integer> cycle() {
            return cycle;
        }

        public long vertexRemovals() {
            return vertexRemovals;
        }

        public long edgesExamined() {
            return edgesExamined;
        }

        @Override
        public String toString() {
            return dag ? "Topological order: " + order : "Cycle detected: " + cycle;
        }
    }

    // Kahn's algorithm
    public static Result sort(Graph g) {
        if (!g.isDirected()) {
            throw new IllegalArgumentException("Topological order is defined for directed graphs");
        }
        int n = g.vertexCount();
        int[] inDegree = new int[n];
        long edgesExamined = 0;
        for (Edge e : g.edges()) {
            inDegree[e.to()]++;
            edgesExamined++;
        }
        ArrayDeque<Integer> ready = new ArrayDeque<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) ready.add(v);
        }
        List<Integer> order = new ArrayList<>(n);
        while (!ready.isEmpty()) {
            int u = ready.poll();
            order.add(u);
            for (Edge e : g.adj(u)) {
                edgesExamined++;
                if (--inDegree[e.to()] == 0) ready.add(e.to());
            }
        }
        long removals = order.size();
        if (order.size() == n) {
            return new Result(true, order, Collections.emptyList(), removals, edgesExamined);
        }
        // not all vertices removed -> cycle, find it with dfs
        long[] dfsEdges = {0};
        List<Integer> cycle = findCycle(g, dfsEdges);
        return new Result(false, Collections.emptyList(), cycle, removals, edgesExamined + dfsEdges[0]);
    }

    public static boolean isValidOrder(Graph g, List<Integer> order) {
        int n = g.vertexCount();
        if (order.size() != n) return false;
        int[] position = new int[n];
        boolean[] seen = new boolean[n];
        for (int i = 0; i < n; i++) {
            int v = order.get(i);
            if (v < 0 || v >= n || seen[v]) return false;
            seen[v] = true;
            position[v] = i;
        }
        for (Edge e : g.edges()) {
            if (position[e.from()] >= position[e.to()]) return false;
        }
        return true;
    }

    public static List<Integer> findCycle(Graph g) {
        return findCycle(g, new long[1]);
    }

    private static List<Integer> findCycle(Graph g, long[] edgesExamined) {
        int n = g.vertexCount();
        final int white = 0;
        final int grey = 1;
        final int black = 2;
        int[] color = new int[n];
        int[] parent = new int[n];
        int[] nextEdge = new int[n];
        int[] stack = new int[n];
        for (int s = 0; s < n; s++) {
            if (color[s] != white) continue;
            int top = 0;
            stack[top++] = s;
            color[s] = grey;
            parent[s] = -1;
            while (top > 0) {
                int u = stack[top - 1];
                List<Edge> out = g.adj(u);
                if (nextEdge[u] < out.size()) {
                    int v = out.get(nextEdge[u]++).to();
                    edgesExamined[0]++;
                    if (color[v] == white) {
                        color[v] = grey;
                        parent[v] = u;
                        stack[top++] = v;
                    } else if (color[v] == grey) { // back edge = cycle
                        LinkedList<Integer> cycle = new LinkedList<>();
                        for (int x = u; x != v; x = parent[x]) cycle.addFirst(x);
                        cycle.addFirst(v);
                        cycle.addLast(v);
                        return new ArrayList<>(cycle);
                    }
                } else {
                    color[u] = black;
                    top--;
                }
            }
        }
        return Collections.emptyList();
    }
}
