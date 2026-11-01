package mst;

import dsu.DisjointSetUnion;
import graph.Edge;
import graph.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Kruskal: sort edges, take edge if it joins two different components (DSU)
public final class KruskalMST {
    private KruskalMST() {
    }

    public static MSTResult run(Graph g) {
        return run(g, true, true);
    }

    public static MSTResult run(Graph g, boolean pathCompression, boolean unionByRank) {
        if (g.isDirected()) {
            throw new IllegalArgumentException("MST is defined for undirected graphs");
        }
        int n = g.vertexCount();
        long[] comparisons = {0};
        Comparator<Edge> byWeight = (a, b) -> {
            comparisons[0]++;
            return Integer.compare(a.weight(), b.weight());
        };
        Edge[] sorted = g.edges().toArray(new Edge[0]);
        Arrays.sort(sorted, byWeight);

        DisjointSetUnion dsu = new DisjointSetUnion(n, pathCompression, unionByRank);
        List<Edge> tree = new ArrayList<>(Math.max(0, n - 1));
        long total = 0;
        long examined = 0;
        for (Edge e : sorted) {
            if (tree.size() == n - 1) break; // tree is done
            examined++;
            if (dsu.union(e.from(), e.to())) {
                tree.add(e);
                total += e.weight();
            }
        }

        Map<String, Long> ops = new LinkedHashMap<>();
        ops.put("sortComparisons", comparisons[0]);
        ops.put("edgesExamined", examined);
        ops.put("findCalls", dsu.findCalls());
        ops.put("unionCalls", dsu.unionCalls());
        ops.put("parentSteps", dsu.parentSteps());
        ops.put("total", comparisons[0] + examined + dsu.findCalls() + dsu.unionCalls());
        return new MSTResult("Kruskal", tree, total, dsu.components(), ops);
    }
}
