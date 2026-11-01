package shortestpath;

import graph.Edge;
import graph.Graph;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

// only used to check Dijkstra and for the negative weight example
public final class BellmanFord {
    private BellmanFord() {
    }

    public static ShortestPaths run(Graph g, int source) {
        int n = g.vertexCount();
        long[] dist = new long[n];
        int[] prev = new int[n];
        Arrays.fill(dist, ShortestPaths.INFINITY);
        Arrays.fill(prev, -1);
        dist[source] = 0;
        long edgesExamined = 0;
        for (int round = 0; round < n - 1; round++) {
            boolean changed = false;
            for (int u = 0; u < n; u++) {
                if (dist[u] == ShortestPaths.INFINITY) continue;
                for (Edge e : g.adj(u)) {
                    edgesExamined++;
                    if (dist[u] + e.weight() < dist[e.to()]) {
                        dist[e.to()] = dist[u] + e.weight();
                        prev[e.to()] = u;
                        changed = true;
                    }
                }
            }
            if (!changed) break;
        }
        for (int u = 0; u < n; u++) {
            if (dist[u] == ShortestPaths.INFINITY) continue;
            for (Edge e : g.adj(u)) {
                if (dist[u] + e.weight() < dist[e.to()]) {
                    throw new IllegalStateException("Negative cycle reachable from source " + source);
                }
            }
        }
        Map<String, Long> ops = new LinkedHashMap<>();
        ops.put("edgesExamined", edgesExamined);
        return new ShortestPaths("Bellman-Ford", source, dist, prev, ops);
    }
}
