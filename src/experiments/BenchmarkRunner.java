package experiments;

import connectivity.TarjanSCC;
import connectivity.TopologicalSort;
import dsu.DisjointSetUnion;
import graph.Graph;
import graph.GraphGenerator;
import mst.KruskalMST;
import mst.MSTResult;
import mst.PrimMST;
import shortestpath.Dijkstra;
import shortestpath.ShortestPaths;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public final class BenchmarkRunner {
    public static final long SEED = 2026L;
    public static final int REPEATS = 5;
    public static final int WARMUP_RUNS = 3;
    public static final double MIN_BATCH_MS = 10.0;
    public static final double MIN_WARMUP_MS = 100.0;
    private static final int GLOBAL_WARMUP_ITERATIONS = 2_000;
    private static final int MAX_W = GraphGenerator.DEFAULT_MAX_WEIGHT;

    private static final String[][] REQUIRED = {
            {"Small", "10", "30"},
            {"Medium", "100", "500"},
            {"Large", "1000", "5000"},
    };

    private BenchmarkRunner() {
    }

    static final class Row {
        String algorithm;
        String graphType;
        String size;
        int v;
        int e;
        double[] runsMs;
        int batch;
        Map<String, Long> ops;
        String result;

        double meanMs() {
            double s = 0;
            for (double t : runsMs) s += t;
            return s / runsMs.length;
        }

        double stdMs() {
            double m = meanMs();
            double s = 0;
            for (double t : runsMs) s += (t - m) * (t - m);
            return Math.sqrt(s / runsMs.length);
        }
    }

    static final class Outcome {
        final Map<String, Long> ops;
        final String result;

        Outcome(Map<String, Long> ops, String result) {
            this.ops = ops;
            this.result = result;
        }
    }

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);
        File out = new File(args.length > 0 ? args[0] : "results/tables");
        if (!out.isDirectory() && !out.mkdirs()) throw new IOException("Cannot create " + out);

        // warm up JIT first, otherwise small graphs are measured in interpreter
        System.out.println("JIT warm-up...");
        globalWarmUp();

        System.out.println("1/4 required graph sizes");
        List<Row> required = new ArrayList<>();
        for (String[] cfg : REQUIRED) {
            required.addAll(runAllAlgorithms(cfg[0], Integer.parseInt(cfg[1]), Integer.parseInt(cfg[2]), true));
        }
        writeCsv(new File(out, "benchmark.csv"), required);
        writeMarkdown(new File(out, "benchmark.md"), required);

        System.out.println("2/4 scaling with V (E = 5V)");
        List<Row> byV = new ArrayList<>();
        for (int v : new int[]{1_000, 2_000, 5_000, 10_000, 20_000, 50_000, 100_000}) {
            byV.addAll(runAllAlgorithms("V=" + v, v, 5 * v, false));
        }
        writeCsv(new File(out, "scaling_vertices.csv"), byV);
        writeMarkdown(new File(out, "scaling_vertices.md"), byV);

        System.out.println("3/4 scaling with E (V = 10000)");
        List<Row> byE = new ArrayList<>();
        for (int e : new int[]{10_000, 20_000, 50_000, 100_000, 200_000, 500_000}) {
            byE.addAll(runAllAlgorithms("E=" + e, 10_000, e, false));
        }
        writeCsv(new File(out, "scaling_edges.csv"), byE);
        writeMarkdown(new File(out, "scaling_edges.md"), byE);

        System.out.println("4/4 DSU optimisations");
        dsuExperiments(out);
        System.out.println("Done. Tables written to " + out.getPath());
    }

    static List<Row> runAllAlgorithms(String size, int v, int e, boolean verbose) {
        long seed = SEED + 31L * v + e;
        Graph roads = GraphGenerator.connectedUndirected(v, e, seed, MAX_W);
        Graph oneWay = GraphGenerator.randomDirected(v, e, seed + 1, MAX_W);
        Graph dag = GraphGenerator.randomDag(v, e, seed + 2, MAX_W);
        List<Row> rows = new ArrayList<>();

        rows.add(measure("Kruskal", "undirected connected", size, roads, () -> mst(KruskalMST.run(roads))));
        rows.add(measure("Prim", "undirected connected", size, roads, () -> mst(PrimMST.run(roads))));
        rows.add(measure("Tarjan SCC", "directed random", size, oneWay, () -> scc(TarjanSCC.run(oneWay))));
        rows.add(measure("Tarjan SCC", "DAG", size, dag, () -> scc(TarjanSCC.run(dag))));
        rows.add(measure("Topological sort", "DAG", size, dag, () -> topo(TopologicalSort.sort(dag))));
        rows.add(measure("Topological sort", "directed random", size, oneWay, () -> topo(TopologicalSort.sort(oneWay))));
        rows.add(measure("Dijkstra", "undirected connected", size, roads, () -> sp(Dijkstra.run(roads, 0))));
        rows.add(measure("Dijkstra", "directed random", size, oneWay, () -> sp(Dijkstra.run(oneWay, 0))));

        if (verbose) {
            for (Row r : rows) {
                System.out.printf("  %-7s %-17s %-21s %10.2f us  %s%n", size, r.algorithm, r.graphType,
                        r.meanMs() * 1000, r.result);
            }
        }
        return rows;
    }

    static Row measure(String algorithm, String graphType, String size, Graph g, Supplier<Outcome> task) {
        System.gc();
        warmUp(task);
        int k = batchSize(task);
        double[] times = new double[REPEATS];
        Outcome last = null;
        for (int i = 0; i < REPEATS; i++) {
            long start = System.nanoTime();
            for (int j = 0; j < k; j++) last = task.get();
            times[i] = (System.nanoTime() - start) / 1e6 / k;
        }
        Row r = new Row();
        r.algorithm = algorithm;
        r.graphType = graphType;
        r.size = size;
        r.v = g.vertexCount();
        r.e = g.edgeCount();
        r.runsMs = times;
        r.batch = k;
        r.ops = last.ops;
        r.result = last.result;
        return r;
    }

    private static void warmUp(Supplier<?> task) {
        long start = System.nanoTime();
        int runs = 0;
        while (runs < WARMUP_RUNS || (System.nanoTime() - start) / 1e6 < MIN_WARMUP_MS) {
            task.get();
            runs++;
        }
    }

    // small graphs run in a few microseconds, so time k runs together and divide by k
    private static int batchSize(Supplier<Outcome> task) {
        long start = System.nanoTime();
        task.get();
        double ms = Math.max((System.nanoTime() - start) / 1e6, 1e-4);
        return (int) Math.max(1, Math.min(100_000, Math.ceil(MIN_BATCH_MS / ms)));
    }

    private static Outcome mst(MSTResult r) {
        return new Outcome(r.operations(), "weight=" + r.totalWeight() + ", connected=" + r.isConnected());
    }

    private static Outcome scc(TarjanSCC.Result r) {
        Map<String, Long> ops = new LinkedHashMap<>();
        ops.put("verticesVisited", r.verticesVisited());
        ops.put("edgesExamined", r.edgesExamined());
        ops.put("total", r.verticesVisited() + r.edgesExamined());
        return new Outcome(ops, "SCCs=" + r.count() + ", largest=" + r.largestComponentSize());
    }

    private static Outcome topo(TopologicalSort.Result r) {
        Map<String, Long> ops = new LinkedHashMap<>();
        ops.put("vertexRemovals", r.vertexRemovals());
        ops.put("edgesExamined", r.edgesExamined());
        ops.put("total", r.vertexRemovals() + r.edgesExamined());
        String res = r.isDag() ? "DAG, order of " + r.order().size() + " vertices"
                : "cycle of length " + (r.cycle().size() - 1);
        return new Outcome(ops, res);
    }

    private static Outcome sp(ShortestPaths r) {
        return new Outcome(r.operations(), "reachable=" + r.reachableCount() + "/" + r.vertexCount());
    }

    private static final String[] DSU_VARIANTS = {"none", "path compression", "union by rank", "both"};

    private static DisjointSetUnion dsu(int n, int variant) {
        return new DisjointSetUnion(n, variant == 1 || variant == 3, variant == 2 || variant == 3);
    }

    private static void dsuExperiments(File out) throws IOException {
        List<String[]> rows = new ArrayList<>();
        // worst case for naive dsu (chain)
        for (int n : new int[]{1_000, 5_000, 20_000}) {
            for (int variant = 0; variant < 4; variant++) {
                final int fv = variant;
                rows.add(dsuRow("chain unions + n finds", n, variant, () -> {
                    DisjointSetUnion d = dsu(n, fv);
                    for (int i = 0; i + 1 < n; i++) d.union(i, i + 1);
                    for (int i = 0; i < n; i++) d.find(i);
                    return d;
                }));
            }
        }
        // random unions and finds
        for (int n : new int[]{10_000, 50_000, 100_000}) {
            for (int variant = 0; variant < 4; variant++) {
                final int fv = variant;
                rows.add(dsuRow("random unions + finds", n, variant, () -> {
                    Random rnd = new Random(SEED);
                    DisjointSetUnion d = dsu(n, fv);
                    for (int i = 0; i < n; i++) {
                        d.union(rnd.nextInt(n), rnd.nextInt(n));
                        d.find(rnd.nextInt(n));
                    }
                    return d;
                }));
            }
        }
        // dsu inside kruskal
        int[][] kruskalSizes = {{1_000, 5_000}, {10_000, 50_000}};
        for (int[] s : kruskalSizes) {
            Graph g = GraphGenerator.connectedUndirected(s[0], s[1], SEED + s[0], MAX_W);
            for (int variant = 0; variant < 4; variant++) {
                final boolean pc = variant == 1 || variant == 3;
                final boolean ubr = variant == 2 || variant == 3;
                System.gc();
                warmUp(() -> KruskalMST.run(g, pc, ubr));
                double total = 0;
                MSTResult last = null;
                for (int i = 0; i < REPEATS; i++) {
                    long t0 = System.nanoTime();
                    last = KruskalMST.run(g, pc, ubr);
                    total += (System.nanoTime() - t0) / 1e6;
                }
                rows.add(new String[]{"Kruskal (V=" + s[0] + ", E=" + s[1] + ")", String.valueOf(s[0]),
                        DSU_VARIANTS[variant], String.valueOf(last.operations().get("findCalls")),
                        String.valueOf(last.operations().get("parentSteps")),
                        String.format("%.4f", total / REPEATS)});
            }
        }

        try (PrintWriter csv = new PrintWriter(new File(out, "dsu.csv"), "UTF-8");
             PrintWriter md = new PrintWriter(new File(out, "dsu.md"), "UTF-8")) {
            csv.println("workload,n,variant,find_calls,parent_steps,avg_ms");
            md.println("| Workload | n | Optimisations | find calls | parent steps | steps per find | Avg time (ms) |");
            md.println("|---|---:|---|---:|---:|---:|---:|");
            for (String[] r : rows) {
                csv.println(String.join(",", r));
                double perFind = Double.parseDouble(r[4]) / Double.parseDouble(r[3]);
                md.printf("| %s | %,d | %s | %,d | %,d | %.2f | %s |%n", r[0], Long.parseLong(r[1]), r[2],
                        Long.parseLong(r[3]), Long.parseLong(r[4]), perFind, r[5]);
            }
        }
    }

    private static String[] dsuRow(String workload, int n, int variant, Supplier<DisjointSetUnion> task) {
        System.gc();
        warmUp(task);
        double total = 0;
        DisjointSetUnion last = null;
        for (int i = 0; i < REPEATS; i++) {
            long t0 = System.nanoTime();
            last = task.get();
            total += (System.nanoTime() - t0) / 1e6;
        }
        System.out.printf("  %-24s n=%-8d %-17s steps=%,d%n", workload, n, DSU_VARIANTS[variant], last.parentSteps());
        return new String[]{workload, String.valueOf(n), DSU_VARIANTS[variant], String.valueOf(last.findCalls()),
                String.valueOf(last.parentSteps()), String.format("%.4f", total / REPEATS)};
    }

    private static void globalWarmUp() {
        Graph roads = GraphGenerator.connectedUndirected(500, 2500, 1, MAX_W);
        Graph oneWay = GraphGenerator.randomDirected(500, 2500, 2, MAX_W);
        Graph dag = GraphGenerator.randomDag(500, 2500, 3, MAX_W);
        long sink = 0;
        for (int i = 0; i < GLOBAL_WARMUP_ITERATIONS; i++) {
            sink += KruskalMST.run(roads).totalWeight();
            sink += PrimMST.run(roads).totalWeight();
            sink += TarjanSCC.run(oneWay).count() + TarjanSCC.run(dag).count();
            sink += TopologicalSort.sort(dag).order().size() + TopologicalSort.sort(oneWay).cycle().size();
            sink += Dijkstra.run(roads, 0).reachableCount() + Dijkstra.run(oneWay, 0).reachableCount();
        }
        if (sink == 42) System.out.println();
    }

    private static void writeCsv(File f, List<Row> rows) throws IOException {
        try (PrintWriter w = new PrintWriter(f, "UTF-8")) {
            StringBuilder header = new StringBuilder("algorithm,graph_type,size,V,E");
            for (int i = 1; i <= REPEATS; i++) header.append(",run").append(i).append("_ms");
            header.append(",avg_ms,std_ms,batch_k,total_ops,operations,result");
            w.println(header);
            for (Row r : rows) {
                StringBuilder sb = new StringBuilder();
                sb.append(r.algorithm).append(',').append(r.graphType).append(',').append(r.size)
                        .append(',').append(r.v).append(',').append(r.e);
                for (double t : r.runsMs) sb.append(',').append(String.format("%.5f", t));
                sb.append(',').append(String.format("%.5f", r.meanMs()))
                        .append(',').append(String.format("%.5f", r.stdMs()))
                        .append(',').append(r.batch)
                        .append(',').append(r.ops.get("total"))
                        .append(",\"").append(opsToString(r.ops, ";")).append('"')
                        .append(",\"").append(r.result).append('"');
                w.println(sb);
            }
        }
    }

    private static void writeMarkdown(File f, List<Row> rows) throws IOException {
        try (PrintWriter w = new PrintWriter(f, "UTF-8")) {
            w.println("| Algorithm | Graph | Size | V | E | Avg time (µs) | Std (µs) | Total ops | Operation counts | Result |");
            w.println("|---|---|---|---:|---:|---:|---:|---:|---|---|");
            for (Row r : rows) {
                w.printf("| %s | %s | %s | %,d | %,d | %.2f | %.2f | %,d | %s | %s |%n", r.algorithm, r.graphType,
                        r.size, r.v, r.e, r.meanMs() * 1000, r.stdMs() * 1000, r.ops.get("total"),
                        opsToString(r.ops, ", "), r.result);
            }
        }
    }

    private static String opsToString(Map<String, Long> ops, String sep) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> e : ops.entrySet()) {
            if (e.getKey().equals("total")) continue;
            if (sb.length() > 0) sb.append(sep);
            sb.append(e.getKey()).append('=').append(e.getValue());
        }
        return sb.toString();
    }
}
