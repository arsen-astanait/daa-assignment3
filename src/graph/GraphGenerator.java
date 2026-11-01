package graph;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class GraphGenerator {
    public static final int DEFAULT_MAX_WEIGHT = 100;

    private GraphGenerator() {
    }

    public static Graph connectedUndirected(int vertices, int edges, long seed, int maxWeight) {
        long maxEdges = (long) vertices * (vertices - 1) / 2;
        if (vertices > 0 && edges < vertices - 1) {
            throw new IllegalArgumentException("A connected graph needs at least V-1 edges");
        }
        if (edges > maxEdges) {
            throw new IllegalArgumentException("Too many edges for a simple graph: " + edges);
        }
        Random rnd = new Random(seed);
        Graph g = Graph.undirected(vertices);
        Set<Long> used = new HashSet<>();
        // random spanning tree first, so the graph is always connected
        int[] perm = shuffledVertices(vertices, rnd);
        for (int i = 1; i < vertices; i++) {
            int u = perm[i];
            int v = perm[rnd.nextInt(i)];
            used.add(undirectedKey(u, v, vertices));
            g.addEdge(u, v, 1 + rnd.nextInt(maxWeight));
        }
        while (g.edgeCount() < edges) {
            int u = rnd.nextInt(vertices);
            int v = rnd.nextInt(vertices);
            if (u == v) continue;
            if (used.add(undirectedKey(u, v, vertices))) {
                g.addEdge(u, v, 1 + rnd.nextInt(maxWeight));
            }
        }
        return g;
    }

    public static Graph randomDirected(int vertices, int edges, long seed, int maxWeight) {
        long maxEdges = (long) vertices * (vertices - 1);
        if (edges > maxEdges) {
            throw new IllegalArgumentException("Too many edges for a simple digraph: " + edges);
        }
        Random rnd = new Random(seed);
        Graph g = Graph.directed(vertices);
        Set<Long> used = new HashSet<>();
        while (g.edgeCount() < edges) {
            int u = rnd.nextInt(vertices);
            int v = rnd.nextInt(vertices);
            if (u == v) continue;
            if (used.add((long) u * vertices + v)) {
                g.addEdge(u, v, 1 + rnd.nextInt(maxWeight));
            }
        }
        return g;
    }

    public static Graph randomDag(int vertices, int edges, long seed, int maxWeight) {
        long maxEdges = (long) vertices * (vertices - 1) / 2;
        if (edges > maxEdges) {
            throw new IllegalArgumentException("Too many edges for a DAG: " + edges);
        }
        Random rnd = new Random(seed);
        Graph g = Graph.directed(vertices);
        int[] perm = shuffledVertices(vertices, rnd);
        Set<Long> used = new HashSet<>();
        while (g.edgeCount() < edges) {
            int i = rnd.nextInt(vertices);
            int j = rnd.nextInt(vertices);
            if (i == j) continue;
            // edges only go forward in perm order, so no cycles
            int a = Math.min(i, j);
            int b = Math.max(i, j);
            if (used.add((long) a * vertices + b)) {
                g.addEdge(perm[a], perm[b], 1 + rnd.nextInt(maxWeight));
            }
        }
        return g;
    }

    private static int[] shuffledVertices(int n, Random rnd) {
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = perm[i];
            perm[i] = perm[j];
            perm[j] = t;
        }
        return perm;
    }

    private static long undirectedKey(int u, int v, int n) {
        return (long) Math.min(u, v) * n + Math.max(u, v);
    }
}
