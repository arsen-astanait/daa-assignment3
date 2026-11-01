package mst;

import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

// lazy Prim with PriorityQueue of edges
public final class PrimMST {
    private PrimMST() {
    }

    public static MSTResult run(Graph g) {
        if (g.isDirected()) {
            throw new IllegalArgumentException("MST is defined for undirected graphs");
        }
        int n = g.vertexCount();
        long[] comparisons = {0};
        Comparator<Edge> byWeight = (a, b) -> {
            comparisons[0]++;
            return Integer.compare(a.weight(), b.weight());
        };
        PriorityQueue<Edge> pq = new PriorityQueue<>(byWeight);
        boolean[] inTree = new boolean[n];
        List<Edge> tree = new ArrayList<>(Math.max(0, n - 1));
        long total = 0;
        int components = 0;
        long[] counters = new long[2]; // edges examined, heap inserts
        long heapPolls = 0;
        long staleEdges = 0;

        for (int start = 0; start < n; start++) {
            if (inTree[start]) continue;
            components++; // new component if graph is not connected
            visit(g, start, inTree, pq, counters);
            while (!pq.isEmpty()) {
                Edge e = pq.poll();
                heapPolls++;
                int v = e.to();
                if (inTree[v]) { // old edge, skip
                    staleEdges++;
                    continue;
                }
                tree.add(e);
                total += e.weight();
                visit(g, v, inTree, pq, counters);
            }
        }

        Map<String, Long> ops = new LinkedHashMap<>();
        ops.put("heapComparisons", comparisons[0]);
        ops.put("edgesExamined", counters[0]);
        ops.put("heapInserts", counters[1]);
        ops.put("heapPolls", heapPolls);
        ops.put("staleEdges", staleEdges);
        ops.put("total", comparisons[0] + counters[0] + counters[1] + heapPolls);
        return new MSTResult("Prim", tree, total, components, ops);
    }

    private static void visit(Graph g, int v, boolean[] inTree, PriorityQueue<Edge> pq, long[] counters) {
        inTree[v] = true;
        for (Edge e : g.adj(v)) {
            counters[0]++;
            if (!inTree[e.to()]) {
                pq.add(e);
                counters[1]++;
            }
        }
    }
}
