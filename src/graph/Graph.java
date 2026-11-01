package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Graph {
    private final int vertexCount;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> edges = new ArrayList<>();

    public Graph(int vertexCount, boolean directed) {
        if (vertexCount < 0) {
            throw new IllegalArgumentException("Vertex count must be non-negative: " + vertexCount);
        }
        this.vertexCount = vertexCount;
        this.directed = directed;
        this.adj = new ArrayList<>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public static Graph undirected(int vertexCount) {
        return new Graph(vertexCount, false);
    }

    public static Graph directed(int vertexCount) {
        return new Graph(vertexCount, true);
    }

    public Graph addEdge(int u, int v, int weight) {
        checkVertex(u);
        checkVertex(v);
        Edge e = new Edge(u, v, weight);
        edges.add(e);
        adj.get(u).add(e);
        // undirected: store it both ways
        if (!directed && u != v) {
            adj.get(v).add(new Edge(v, u, weight));
        }
        return this;
    }

    public List<Edge> adj(int u) {
        checkVertex(u);
        return Collections.unmodifiableList(adj.get(u));
    }

    public List<Edge> edges() {
        return Collections.unmodifiableList(edges);
    }

    public int vertexCount() {
        return vertexCount;
    }

    public int edgeCount() {
        return edges.size();
    }

    public boolean isDirected() {
        return directed;
    }

    public boolean hasNegativeWeight() {
        for (Edge e : edges) {
            if (e.weight() < 0) return true;
        }
        return false;
    }

    private void checkVertex(int v) {
        if (v < 0 || v >= vertexCount) {
            throw new IndexOutOfBoundsException("Vertex " + v + " not in [0, " + vertexCount + ")");
        }
    }

    @Override
    public String toString() {
        return (directed ? "Directed" : "Undirected") + "Graph(V=" + vertexCount + ", E=" + edges.size() + ")";
    }
}
