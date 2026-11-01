package tests;

import connectivity.TarjanSCC;
import connectivity.TopologicalSort;
import dsu.DisjointSetUnion;
import graph.Edge;
import graph.Graph;
import graph.GraphGenerator;
import mst.KruskalMST;
import mst.MSTResult;
import mst.PrimMST;
import shortestpath.BellmanFord;
import shortestpath.Dijkstra;
import shortestpath.ShortestPaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// simple test runner, no junit needed
public final class AllTests {
    private static int passed;
    private static final List<String> failures = new ArrayList<>();

    private AllTests() {
    }

    public static void main(String[] args) {
        // DSU
        test("DSU: union/find/connected and component count", AllTests::dsuBasics);
        test("DSU: all four variants agree on random operations", AllTests::dsuVariantsAgree);
        test("DSU: path compression flattens a long chain", AllTests::dsuPathCompression);
        // MST
        test("MST: known graph has weight 14", AllTests::mstKnownGraph);
        test("MST: multiple valid MSTs (all weights equal)", AllTests::mstMultipleValidTrees);
        test("MST: duplicate weights", AllTests::mstDuplicateWeights);
        test("MST: disconnected graph gives a forest", AllTests::mstDisconnected);
        test("MST: single vertex and empty graph", AllTests::mstSingleVertex);
        test("MST: Kruskal == Prim == brute force on 300 random graphs", AllTests::mstRandomAgreement);
        test("MST: result is a spanning tree (V-1 edges, acyclic)", AllTests::mstIsSpanningTree);
        // Topological sort
        test("Topo: valid order for a small DAG", AllTests::topoSmallDag);
        test("Topo: cycle detected and reported", AllTests::topoCycle);
        test("Topo: self-loop is a cycle", AllTests::topoSelfLoop);
        test("Topo: single vertex and edgeless graph", AllTests::topoTrivial);
        test("Topo: random DAGs always valid, random cycles always detected", AllTests::topoRandom);
        // SCC
        test("SCC: known graph has 3 components", AllTests::sccKnownGraph);
        test("SCC: DAG has V singleton components", AllTests::sccDag);
        test("SCC: single vertex and a big cycle", AllTests::sccTrivial);
        test("SCC: matches brute-force mutual reachability", AllTests::sccBruteForce);
        test("SCC: Tarjan handles a 100000-vertex path (no stack overflow)", AllTests::sccDeepGraph);
        // Dijkstra
        test("Dijkstra: known distances and paths", AllTests::dijkstraKnown);
        test("Dijkstra: unreachable vertices", AllTests::dijkstraUnreachable);
        test("Dijkstra: single vertex", AllTests::dijkstraSingleVertex);
        test("Dijkstra: zero-weight and duplicate-weight edges", AllTests::dijkstraZeroAndDuplicate);
        test("Dijkstra: rejects negative weights", AllTests::dijkstraNegative);
        test("Dijkstra == Bellman-Ford on 300 random graphs", AllTests::dijkstraRandom);
        // Generators
        test("Generators: exact sizes, connectivity, reproducibility", AllTests::generators);

        System.out.println();
        System.out.println(passed + " passed, " + failures.size() + " failed");
        for (String f : failures) System.out.println("  FAILED: " + f);
        if (!failures.isEmpty()) System.exit(1);
    }

    private static void dsuBasics() {
        DisjointSetUnion d = new DisjointSetUnion(6);
        check(d.components() == 6, "6 singletons");
        check(d.union(0, 1), "first union merges");
        check(d.union(1, 2), "second union merges");
        check(!d.union(0, 2), "same set: no merge");
        check(d.connected(0, 2), "0 ~ 2");
        check(!d.connected(0, 3), "0 !~ 3");
        d.union(3, 4);
        check(d.components() == 3, "components {0,1,2},{3,4},{5}");
        check(d.find(5) == 5, "singleton is its own root");
    }

    private static void dsuVariantsAgree() {
        Random rnd = new Random(7);
        int n = 300;
        DisjointSetUnion[] all = {
                new DisjointSetUnion(n, true, true), new DisjointSetUnion(n, true, false),
                new DisjointSetUnion(n, false, true), new DisjointSetUnion(n, false, false)};
        for (int i = 0; i < 2000; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            if (rnd.nextBoolean()) {
                boolean r = all[0].union(a, b);
                for (int k = 1; k < 4; k++) check(all[k].union(a, b) == r, "union result equal");
            } else {
                boolean r = all[0].connected(a, b);
                for (int k = 1; k < 4; k++) check(all[k].connected(a, b) == r, "connected result equal");
            }
        }
        for (int k = 1; k < 4; k++) check(all[k].components() == all[0].components(), "component count equal");
    }

    private static void dsuPathCompression() {
        int n = 1000;
        DisjointSetUnion naive = new DisjointSetUnion(n, false, false);
        DisjointSetUnion opt = new DisjointSetUnion(n, true, true);
        for (int i = 0; i + 1 < n; i++) {
            naive.union(i, i + 1);
            opt.union(i, i + 1);
        }
        long naiveBefore = naive.parentSteps();
        long optBefore = opt.parentSteps();
        for (int r = 0; r < 3; r++) {
            for (int i = 0; i < n; i++) {
                naive.find(i);
                opt.find(i);
            }
        }
        long naiveSteps = naive.parentSteps() - naiveBefore;
        long optSteps = opt.parentSteps() - optBefore;
        check(naiveSteps == 3L * n * (n - 1) / 2, "naive chain costs n(n-1)/2 steps per pass, got " + naiveSteps);
        check(optSteps < 3L * n, "optimised finds take O(1) steps each, got " + optSteps);
    }

    private static void mstKnownGraph() {
        // answer is 14
        Graph g = Graph.undirected(6)
                .addEdge(0, 1, 4).addEdge(0, 2, 4).addEdge(1, 2, 2)
                .addEdge(2, 3, 3).addEdge(2, 5, 2).addEdge(2, 4, 4)
                .addEdge(3, 4, 3).addEdge(5, 4, 3);
        MSTResult k = KruskalMST.run(g);
        MSTResult p = PrimMST.run(g);
        check(k.totalWeight() == 14, "Kruskal weight 14, got " + k.totalWeight());
        check(p.totalWeight() == 14, "Prim weight 14, got " + p.totalWeight());
        check(k.edges().size() == 5 && p.edges().size() == 5, "5 edges");
        check(k.isConnected() && p.isConnected(), "connected");
    }

    private static void mstMultipleValidTrees() {
        // all weights equal -> many MSTs
        Graph g = Graph.undirected(4);
        for (int i = 0; i < 4; i++) for (int j = i + 1; j < 4; j++) g.addEdge(i, j, 1);
        MSTResult k = KruskalMST.run(g);
        MSTResult p = PrimMST.run(g);
        check(k.totalWeight() == 3 && p.totalWeight() == 3, "both weight 3");
        check(isSpanningTree(g, k.edges()) && isSpanningTree(g, p.edges()), "both are spanning trees");

        Graph sq = Graph.undirected(4).addEdge(0, 1, 5).addEdge(1, 2, 5).addEdge(2, 3, 5).addEdge(3, 0, 5);
        check(KruskalMST.run(sq).totalWeight() == 15 && PrimMST.run(sq).totalWeight() == 15, "square weight 15");
    }

    private static void mstDuplicateWeights() {
        Graph g = Graph.undirected(5)
                .addEdge(0, 1, 2).addEdge(1, 2, 2).addEdge(2, 3, 2).addEdge(3, 4, 2)
                .addEdge(0, 4, 2).addEdge(0, 2, 1).addEdge(1, 3, 1).addEdge(2, 4, 3);
        long brute = bruteForceMst(g);
        check(KruskalMST.run(g).totalWeight() == brute, "Kruskal = brute force " + brute);
        check(PrimMST.run(g).totalWeight() == brute, "Prim = brute force " + brute);
    }

    private static void mstDisconnected() {
        Graph g = Graph.undirected(7)
                .addEdge(0, 1, 3).addEdge(1, 2, 1).addEdge(0, 2, 2)
                .addEdge(3, 4, 7);
        // 5 and 6 have no edges
        MSTResult k = KruskalMST.run(g);
        MSTResult p = PrimMST.run(g);
        check(!k.isConnected() && !p.isConnected(), "detected as disconnected");
        check(k.components() == 4 && p.components() == 4, "4 components, got " + k.components() + "/" + p.components());
        check(k.totalWeight() == 10 && p.totalWeight() == 10, "forest weight 10");
        check(k.edges().size() == 3 && p.edges().size() == 3, "V - components = 3 forest edges");
    }

    private static void mstSingleVertex() {
        Graph one = Graph.undirected(1);
        check(KruskalMST.run(one).totalWeight() == 0 && KruskalMST.run(one).isConnected(), "Kruskal on 1 vertex");
        check(PrimMST.run(one).totalWeight() == 0 && PrimMST.run(one).isConnected(), "Prim on 1 vertex");
        Graph empty = Graph.undirected(0);
        check(KruskalMST.run(empty).edges().isEmpty() && PrimMST.run(empty).edges().isEmpty(), "empty graph");
    }

    private static void mstRandomAgreement() {
        Random rnd = new Random(11);
        for (int t = 0; t < 300; t++) {
            int n = 1 + rnd.nextInt(7);
            int maxE = Math.min(n * (n - 1) / 2, 15); // brute force is slow
            int m = (n - 1) + rnd.nextInt(maxE - (n - 1) + 1);
            Graph g = GraphGenerator.connectedUndirected(n, m, rnd.nextLong(), 1 + rnd.nextInt(5));
            long k = KruskalMST.run(g).totalWeight();
            long p = PrimMST.run(g).totalWeight();
            long b = bruteForceMst(g);
            check(k == p && p == b, "graph " + t + ": Kruskal " + k + ", Prim " + p + ", brute " + b);
        }
        for (long seed = 1; seed <= 5; seed++) {
            Graph big = GraphGenerator.connectedUndirected(1000, 5000, seed, 100);
            check(KruskalMST.run(big).totalWeight() == PrimMST.run(big).totalWeight(), "large graph seed " + seed);
        }
    }

    private static void mstIsSpanningTree() {
        Graph g = GraphGenerator.connectedUndirected(200, 1000, 3, 20);
        check(isSpanningTree(g, KruskalMST.run(g).edges()), "Kruskal spanning tree");
        check(isSpanningTree(g, PrimMST.run(g).edges()), "Prim spanning tree");
    }

    private static void topoSmallDag() {
        Graph g = Graph.directed(6)
                .addEdge(5, 2, 1).addEdge(5, 0, 1).addEdge(4, 0, 1)
                .addEdge(4, 1, 1).addEdge(2, 3, 1).addEdge(3, 1, 1);
        TopologicalSort.Result r = TopologicalSort.sort(g);
        check(r.isDag(), "is a DAG");
        check(TopologicalSort.isValidOrder(g, r.order()), "order valid: " + r.order());
        check(r.cycle().isEmpty(), "no cycle reported");
    }

    private static void topoCycle() {
        Graph g = Graph.directed(5)
                .addEdge(0, 1, 1).addEdge(1, 2, 1).addEdge(2, 3, 1)
                .addEdge(3, 1, 1).addEdge(3, 4, 1);
        TopologicalSort.Result r = TopologicalSort.sort(g);
        check(!r.isDag(), "cycle detected");
        check(r.order().isEmpty(), "no order returned");
        check(isRealCycle(g, r.cycle()), "reported cycle is a real cycle: " + r.cycle());
    }

    private static void topoSelfLoop() {
        Graph g = Graph.directed(3).addEdge(0, 1, 1).addEdge(1, 1, 1);
        TopologicalSort.Result r = TopologicalSort.sort(g);
        check(!r.isDag(), "self-loop detected");
        check(isRealCycle(g, r.cycle()), "self-loop cycle: " + r.cycle());
    }

    private static void topoTrivial() {
        TopologicalSort.Result one = TopologicalSort.sort(Graph.directed(1));
        check(one.isDag() && one.order().size() == 1, "single vertex");
        Graph noEdges = Graph.directed(4);
        TopologicalSort.Result r = TopologicalSort.sort(noEdges);
        check(r.isDag() && TopologicalSort.isValidOrder(noEdges, r.order()), "edgeless graph");
    }

    private static void topoRandom() {
        Random rnd = new Random(5);
        for (int t = 0; t < 200; t++) {
            int n = 2 + rnd.nextInt(40);
            int m = rnd.nextInt(n * (n - 1) / 2 + 1);
            Graph dag = GraphGenerator.randomDag(n, m, rnd.nextLong(), 10);
            TopologicalSort.Result r = TopologicalSort.sort(dag);
            check(r.isDag() && TopologicalSort.isValidOrder(dag, r.order()), "random DAG " + t);

            // add reverse edge -> cycle
            Graph cyclic = GraphGenerator.randomDag(n, m, rnd.nextLong(), 10);
            boolean expectedCycle = m > 0;
            if (expectedCycle) {
                Edge e = cyclic.edges().get(rnd.nextInt(m));
                cyclic.addEdge(e.to(), e.from(), 1);
            }
            TopologicalSort.Result c = TopologicalSort.sort(cyclic);
            check(c.isDag() != expectedCycle, "cycle detection " + t);
            if (expectedCycle) check(isRealCycle(cyclic, c.cycle()), "cycle witness " + t);
        }
    }

    private static void sccKnownGraph() {
        Graph g = Graph.directed(8)
                .addEdge(0, 1, 1).addEdge(1, 2, 1).addEdge(2, 0, 1)
                .addEdge(2, 3, 1).addEdge(3, 4, 1).addEdge(4, 3, 1)
                .addEdge(4, 5, 1).addEdge(5, 6, 1).addEdge(6, 7, 1).addEdge(7, 5, 1);
        TarjanSCC.Result r = TarjanSCC.run(g);
        check(r.count() == 3, "3 SCCs, got " + r.count());
        check(r.componentOf(0) == r.componentOf(2) && r.componentOf(3) == r.componentOf(4)
                && r.componentOf(5) == r.componentOf(7), "grouping");
        check(r.componentOf(0) != r.componentOf(3), "different SCCs");
        checkPartition(g, r);
        check(TopologicalSort.sort(TarjanSCC.condensation(g, r)).isDag(), "condensation is a DAG");
    }

    private static void sccDag() {
        Graph dag = GraphGenerator.randomDag(50, 300, 9, 10);
        TarjanSCC.Result r = TarjanSCC.run(dag);
        check(r.count() == 50, "every vertex alone");
        checkPartition(dag, r);
    }

    private static void sccTrivial() {
        TarjanSCC.Result one = TarjanSCC.run(Graph.directed(1));
        check(one.count() == 1, "single vertex = one SCC");
        Graph ring = Graph.directed(1000);
        for (int i = 0; i < 1000; i++) ring.addEdge(i, (i + 1) % 1000, 1);
        check(TarjanSCC.run(ring).count() == 1, "one big cycle = one SCC");
    }

    private static void sccBruteForce() {
        Random rnd = new Random(13);
        for (int t = 0; t < 200; t++) {
            int n = 1 + rnd.nextInt(25);
            int m = rnd.nextInt(Math.min(n * (n - 1), 3 * n) + 1);
            Graph g = GraphGenerator.randomDirected(n, m, rnd.nextLong(), 10);
            TarjanSCC.Result r = TarjanSCC.run(g);
            boolean[][] reach = transitiveClosure(g);
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    boolean same = reach[u][v] && reach[v][u];
                    check(same == (r.componentOf(u) == r.componentOf(v)), "graph " + t + " pair " + u + "," + v);
                }
            }
            checkPartition(g, r);
        }
    }

    private static void sccDeepGraph() {
        int n = 100_000;
        Graph path = Graph.directed(n);
        for (int i = 0; i + 1 < n; i++) path.addEdge(i, i + 1, 1);
        check(TarjanSCC.run(path).count() == n, "path: n singleton SCCs");
        path.addEdge(n - 1, 0, 1);
        check(TarjanSCC.run(path).count() == 1, "closed path: one SCC");
    }

    private static void dijkstraKnown() {
        Graph g = Graph.directed(5)
                .addEdge(0, 1, 10).addEdge(0, 2, 3).addEdge(2, 1, 4)
                .addEdge(1, 3, 2).addEdge(2, 3, 8).addEdge(3, 4, 7).addEdge(2, 4, 20);
        ShortestPaths sp = Dijkstra.run(g, 0);
        long[] expected = {0, 7, 3, 9, 16};
        for (int v = 0; v < 5; v++) check(sp.distanceTo(v) == expected[v], "dist " + v);
        check(sp.pathTo(4).equals(List.of(0, 2, 1, 3, 4)), "path to 4: " + sp.pathTo(4));
        check(sp.pathTo(0).equals(List.of(0)), "path to source");
        check(pathWeight(g, sp.pathTo(4)) == 16, "path weight equals distance");
    }

    private static void dijkstraUnreachable() {
        Graph g = Graph.directed(5).addEdge(0, 1, 2).addEdge(1, 2, 2).addEdge(3, 4, 1).addEdge(4, 0, 1);
        ShortestPaths sp = Dijkstra.run(g, 0);
        check(sp.isReachable(2) && sp.distanceTo(2) == 4, "reachable vertex");
        check(!sp.isReachable(3) && !sp.isReachable(4), "3 and 4 unreachable (edges point the wrong way)");
        check(sp.distanceTo(3) == ShortestPaths.INFINITY, "unreachable distance = INFINITY");
        check(sp.pathTo(4).isEmpty(), "no path to unreachable vertex");
        check(sp.reachableCount() == 3, "3 reachable");
    }

    private static void dijkstraSingleVertex() {
        ShortestPaths sp = Dijkstra.run(Graph.directed(1), 0);
        check(sp.distanceTo(0) == 0 && sp.pathTo(0).equals(List.of(0)), "single vertex");
    }

    private static void dijkstraZeroAndDuplicate() {
        Graph g = Graph.undirected(4)
                .addEdge(0, 1, 0).addEdge(1, 2, 5).addEdge(0, 2, 5).addEdge(2, 3, 0).addEdge(1, 3, 5);
        ShortestPaths sp = Dijkstra.run(g, 0);
        check(sp.distanceTo(1) == 0 && sp.distanceTo(2) == 5 && sp.distanceTo(3) == 5, "distances with ties");
    }

    private static void dijkstraNegative() {
        Graph g = Graph.directed(3).addEdge(0, 1, 2).addEdge(0, 2, 5).addEdge(2, 1, -4);
        boolean thrown = false;
        try {
            Dijkstra.run(g, 0);
        } catch (IllegalArgumentException ex) {
            thrown = true;
        }
        check(thrown, "negative weight rejected");
        ShortestPaths wrong = Dijkstra.runWithoutValidation(g, 0);
        ShortestPaths right = BellmanFord.run(g, 0);
        check(wrong.distanceTo(1) == 2 && right.distanceTo(1) == 1, "counterexample: Dijkstra 2, correct 1");
    }

    private static void dijkstraRandom() {
        Random rnd = new Random(17);
        for (int t = 0; t < 300; t++) {
            int n = 1 + rnd.nextInt(30);
            int m = rnd.nextInt(Math.min(n * (n - 1), 4 * n) + 1);
            Graph g = rnd.nextBoolean()
                    ? GraphGenerator.randomDirected(n, m, rnd.nextLong(), 1 + rnd.nextInt(20))
                    : GraphGenerator.connectedUndirected(n, Math.max(n - 1, Math.min(m, n * (n - 1) / 2)),
                    rnd.nextLong(), 1 + rnd.nextInt(20));
            int s = rnd.nextInt(n);
            ShortestPaths d = Dijkstra.run(g, s);
            ShortestPaths b = BellmanFord.run(g, s);
            for (int v = 0; v < n; v++) {
                check(d.distanceTo(v) == b.distanceTo(v), "graph " + t + " vertex " + v);
                if (d.isReachable(v)) check(pathWeight(g, d.pathTo(v)) == d.distanceTo(v), "path weight " + t);
            }
        }
    }

    private static void generators() {
        int[][] sizes = {{10, 30}, {100, 500}, {1000, 5000}};
        for (int[] s : sizes) {
            Graph u = GraphGenerator.connectedUndirected(s[0], s[1], 42, 100);
            check(u.vertexCount() == s[0] && u.edgeCount() == s[1], "undirected size");
            check(KruskalMST.run(u).isConnected(), "undirected generator is connected");
            Graph dag = GraphGenerator.randomDag(s[0], s[1], 42, 100);
            check(dag.edgeCount() == s[1] && TopologicalSort.sort(dag).isDag(), "DAG generator acyclic");
            Graph d = GraphGenerator.randomDirected(s[0], s[1], 42, 100);
            check(d.edgeCount() == s[1], "directed size");
        }
        Graph a = GraphGenerator.connectedUndirected(100, 500, 99, 100);
        Graph b = GraphGenerator.connectedUndirected(100, 500, 99, 100);
        check(a.edges().toString().equals(b.edges().toString()), "same seed -> same graph");
    }

    private static void checkPartition(Graph g, TarjanSCC.Result r) {
        int[] count = new int[g.vertexCount()];
        for (List<Integer> c : r.components()) for (int v : c) count[v]++;
        for (int v = 0; v < g.vertexCount(); v++) {
            check(count[v] == 1, "vertex " + v + " belongs to exactly one SCC (found " + count[v] + ")");
        }
    }

    private static boolean isSpanningTree(Graph g, List<Edge> tree) {
        if (tree.size() != g.vertexCount() - 1) return false;
        DisjointSetUnion d = new DisjointSetUnion(g.vertexCount());
        for (Edge e : tree) if (!d.union(e.from(), e.to())) return false;
        return d.components() == 1;
    }

    private static long bruteForceMst(Graph g) {
        int n = g.vertexCount();
        List<Edge> edges = g.edges();
        if (n <= 1) return 0;
        long best = Long.MAX_VALUE;
        int m = edges.size();
        for (long mask = 0; mask < (1L << m); mask++) {
            if (Long.bitCount(mask) != n - 1) continue;
            List<Edge> chosen = new ArrayList<>();
            long w = 0;
            for (int i = 0; i < m; i++) {
                if ((mask >> i & 1) == 1) {
                    chosen.add(edges.get(i));
                    w += edges.get(i).weight();
                }
            }
            if (w < best && isSpanningTree(g, chosen)) best = w;
        }
        return best;
    }

    private static boolean[][] transitiveClosure(Graph g) {
        int n = g.vertexCount();
        boolean[][] reach = new boolean[n][n];
        for (int s = 0; s < n; s++) {
            ArrayList<Integer> stack = new ArrayList<>();
            stack.add(s);
            reach[s][s] = true;
            while (!stack.isEmpty()) {
                int u = stack.remove(stack.size() - 1);
                for (Edge e : g.adj(u)) {
                    if (!reach[s][e.to()]) {
                        reach[s][e.to()] = true;
                        stack.add(e.to());
                    }
                }
            }
        }
        return reach;
    }

    private static boolean isRealCycle(Graph g, List<Integer> cycle) {
        if (cycle.size() < 2 || !cycle.get(0).equals(cycle.get(cycle.size() - 1))) return false;
        for (int i = 0; i + 1 < cycle.size(); i++) {
            if (findEdge(g, cycle.get(i), cycle.get(i + 1)) == null) return false;
        }
        return true;
    }

    private static long pathWeight(Graph g, List<Integer> path) {
        long w = 0;
        for (int i = 0; i + 1 < path.size(); i++) {
            Edge best = null;
            for (Edge e : g.adj(path.get(i))) {
                if (e.to() == path.get(i + 1) && (best == null || e.weight() < best.weight())) best = e;
            }
            if (best == null) return -1;
            w += best.weight();
        }
        return w;
    }

    private static Edge findEdge(Graph g, int u, int v) {
        for (Edge e : g.adj(u)) if (e.to() == v) return e;
        return null;
    }

    private static String current;
    private static boolean currentOk;

    private static void test(String name, Runnable body) {
        current = name;
        currentOk = true;
        try {
            body.run();
        } catch (RuntimeException ex) {
            fail("exception " + ex);
        }
        if (currentOk) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            System.out.println("FAIL  " + name);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) fail(message);
    }

    private static void fail(String message) {
        if (currentOk) failures.add(current + ": " + message);
        currentOk = false;
    }
}
