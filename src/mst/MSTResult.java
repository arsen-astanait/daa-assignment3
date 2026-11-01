package mst;

import graph.Edge;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MSTResult {
    private final String algorithm;
    private final List<Edge> edges;
    private final long totalWeight;
    private final int components;
    private final Map<String, Long> operations;

    public MSTResult(String algorithm, List<Edge> edges, long totalWeight, int components,
                     Map<String, Long> operations) {
        this.algorithm = algorithm;
        this.edges = Collections.unmodifiableList(edges);
        this.totalWeight = totalWeight;
        this.components = components;
        this.operations = Collections.unmodifiableMap(new LinkedHashMap<>(operations));
    }

    public String algorithm() {
        return algorithm;
    }

    public List<Edge> edges() {
        return edges;
    }

    public long totalWeight() {
        return totalWeight;
    }

    public int components() {
        return components;
    }

    public boolean isConnected() {
        return components <= 1;
    }

    public Map<String, Long> operations() {
        return operations;
    }

    @Override
    public String toString() {
        return algorithm + ": weight=" + totalWeight + ", edges=" + edges.size()
                + ", connected=" + isConnected() + (isConnected() ? "" : " (components=" + components + ")");
    }
}
