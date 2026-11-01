package shortestpath;

import graph.Edge;
import graph.Graph;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;

public final class Dijkstra {
    private Dijkstra() {
    }

    public static ShortestPaths run(Graph g, int source) {
        if (g.hasNegativeWeight()) {
            throw new IllegalArgumentException("Dijkstra requires non-negative edge weights");
        }
        return runWithoutValidation(g, source);
    }

    public static ShortestPaths runWithoutValidation(Graph g, int source) {
        int n = g.vertexCount();
        if (source < 0 || source >= n) {
            throw new IndexOutOfBoundsException("Source " + source + " not in [0, " + n + ")");
        }
        long[] dist = new long[n];
        int[] prev = new int[n];
        boolean[] settled = new boolean[n];
        Arrays.fill(dist, ShortestPaths.INFINITY);
        Arrays.fill(prev, -1);

        long[] comparisons = {0};
        Comparator<long[]> byDistance = (a, b) -> {
            comparisons[0]++;
            return Long.compare(a[0], b[0]);
        };
        PriorityQueue<long[]> pq = new PriorityQueue<>(byDistance);
        long pushes = 0;
        long polls = 0;
        long stale = 0;
        long edgesExamined = 0;
        long relaxations = 0;

        dist[source] = 0;
        pq.add(new long[]{0, source});
        pushes++;
        while (!pq.isEmpty()) {
            long[] top = pq.poll();
            polls++;
            int u = (int) top[1];
            if (settled[u]) { // old entry in heap
                stale++;
                continue;
            }
            settled[u] = true;
            for (Edge e : g.adj(u)) {
                edgesExamined++;
                int v = e.to();
                if (settled[v]) continue;
                long candidate = dist[u] + e.weight();
                if (candidate < dist[v]) {
                    dist[v] = candidate;
                    prev[v] = u;
                    pq.add(new long[]{candidate, v});
                    pushes++;
                    relaxations++;
                }
            }
        }

        Map<String, Long> ops = new LinkedHashMap<>();
        ops.put("heapComparisons", comparisons[0]);
        ops.put("edgesExamined", edgesExamined);
        ops.put("relaxations", relaxations);
        ops.put("heapPushes", pushes);
        ops.put("heapPolls", polls);
        ops.put("stalePolls", stale);
        ops.put("total", comparisons[0] + edgesExamined + pushes + polls);
        return new ShortestPaths("Dijkstra", source, dist, prev, ops);
    }
}
