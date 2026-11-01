package app;

import connectivity.TarjanSCC;
import connectivity.TopologicalSort;
import graph.Edge;
import graph.Graph;
import mst.KruskalMST;
import mst.MSTResult;
import mst.PrimMST;
import shortestpath.BellmanFord;
import shortestpath.Dijkstra;
import shortestpath.ShortestPaths;

import java.util.ArrayList;
import java.util.List;

public final class SmartCityDemo {
    private static final String[] DISTRICTS = {
            "Old Town", "Central Station", "University", "Tech Park",
            "Airport", "Riverside", "Industrial Zone", "Green Hills"
    };

    private SmartCityDemo() {
    }

    public static void main(String[] args) {
        roadConstruction();
        oneWayStreets();
        serviceDependencies();
        routePlanning();
        negativeWeightCounterexample();
    }

    private static void roadConstruction() {
        header("Task 1. Road construction (MST), weights = construction cost");
        Graph roads = Graph.undirected(8)
                .addEdge(0, 1, 4).addEdge(0, 2, 7).addEdge(1, 2, 3)
                .addEdge(1, 3, 8).addEdge(2, 3, 5).addEdge(2, 5, 6)
                .addEdge(3, 4, 9).addEdge(3, 6, 4).addEdge(4, 6, 10)
                .addEdge(5, 6, 6).addEdge(5, 7, 2).addEdge(6, 7, 7)
                .addEdge(0, 7, 12);
        MSTResult k = KruskalMST.run(roads);
        MSTResult p = PrimMST.run(roads);
        printMst(k);
        printMst(p);
        System.out.println("Same total cost: " + (k.totalWeight() == p.totalWeight()));

        Graph split = Graph.undirected(4).addEdge(0, 1, 3).addEdge(2, 3, 5);
        MSTResult forest = KruskalMST.run(split);
        System.out.println("Disconnected city (two islands): " + forest);
    }

    private static void oneWayStreets() {
        header("Task 2. One-way streets (strongly connected components)");
        Graph streets = Graph.directed(8)
                .addEdge(0, 1, 1).addEdge(1, 2, 1).addEdge(2, 0, 1)   // cycle
                .addEdge(2, 3, 1).addEdge(3, 4, 1).addEdge(4, 3, 1)
                .addEdge(4, 5, 1).addEdge(5, 6, 1).addEdge(6, 7, 1)
                .addEdge(7, 5, 1);                                     // another cycle
        TarjanSCC.Result scc = TarjanSCC.run(streets);
        System.out.println("Number of SCCs: " + scc.count());
        for (List<Integer> c : scc.components()) {
            System.out.println("  " + names(c));
        }
        Graph condensed = TarjanSCC.condensation(streets, scc);
        TopologicalSort.Result order = TopologicalSort.sort(condensed);
        System.out.println("Condensation graph is a DAG: " + order.isDag()
                + ", component order: " + order.order());
    }

    private static void serviceDependencies() {
        header("Task 2. Service start-up order (topological sort)");
        String[] services = {"Power grid", "Water pumps", "Traffic lights", "Metro", "Hospitals", "Data center"};
        Graph deps = Graph.directed(6)
                .addEdge(0, 1, 1).addEdge(0, 2, 1).addEdge(0, 5, 1)
                .addEdge(2, 3, 1).addEdge(5, 3, 1).addEdge(1, 4, 1)
                .addEdge(5, 4, 1);
        TopologicalSort.Result ok = TopologicalSort.sort(deps);
        List<String> named = new ArrayList<>();
        for (int v : ok.order()) named.add(services[v]);
        System.out.println("DAG -> start order: " + String.join(" -> ", named));

        deps.addEdge(4, 0, 1); // makes a cycle
        TopologicalSort.Result bad = TopologicalSort.sort(deps);
        List<String> cycle = new ArrayList<>();
        for (int v : bad.cycle()) cycle.add(services[v]);
        System.out.println("After adding Hospitals -> Power grid: isDag=" + bad.isDag()
                + ", cycle: " + String.join(" -> ", cycle));
    }

    private static void routePlanning() {
        header("Task 3. Route planning from Central Station (Dijkstra), weights = km");
        Graph city = Graph.directed(8)
                .addEdge(1, 0, 3).addEdge(1, 2, 2).addEdge(2, 0, 1)
                .addEdge(0, 3, 6).addEdge(2, 3, 7).addEdge(3, 4, 5)
                .addEdge(2, 5, 4).addEdge(5, 3, 1).addEdge(5, 6, 8)
                .addEdge(6, 4, 2);
        // nothing goes into 7, so it is unreachable
        ShortestPaths sp = Dijkstra.run(city, 1);
        for (int v = 0; v < city.vertexCount(); v++) {
            if (sp.isReachable(v)) {
                System.out.printf("  %-16s %3d km  via %s%n", DISTRICTS[v], sp.distanceTo(v), names(sp.pathTo(v)));
            } else {
                System.out.printf("  %-16s unreachable%n", DISTRICTS[v]);
            }
        }
    }

    private static void negativeWeightCounterexample() {
        header("Why Dijkstra fails with negative weights");
        // 0->2->1 is shorter because of -4
        Graph g = Graph.directed(4)
                .addEdge(0, 1, 2).addEdge(0, 2, 5)
                .addEdge(2, 1, -4).addEdge(1, 3, 1);
        ShortestPaths wrong = Dijkstra.runWithoutValidation(g, 0);
        ShortestPaths right = BellmanFord.run(g, 0);
        for (int v = 1; v < 4; v++) {
            System.out.printf("  vertex %d: Dijkstra=%d, Bellman-Ford=%d%n", v, wrong.distanceTo(v), right.distanceTo(v));
        }
        try {
            Dijkstra.run(g, 0);
        } catch (IllegalArgumentException ex) {
            System.out.println("  Dijkstra.run(...) rejects the graph: " + ex.getMessage());
        }
    }

    private static void printMst(MSTResult r) {
        List<String> roads = new ArrayList<>();
        for (Edge e : r.edges()) {
            roads.add(DISTRICTS[e.from()] + "-" + DISTRICTS[e.to()] + "(" + e.weight() + ")");
        }
        System.out.println(r.algorithm() + ": total cost " + r.totalWeight() + ", roads " + roads);
        System.out.println("  operations: " + r.operations());
    }

    private static String names(List<Integer> vertices) {
        List<String> out = new ArrayList<>();
        for (int v : vertices) out.add(DISTRICTS[v]);
        return "[" + String.join(", ", out) + "]";
    }

    private static void header(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }
}
