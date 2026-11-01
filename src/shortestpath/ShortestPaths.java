package shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ShortestPaths {
    public static final long INFINITY = Long.MAX_VALUE;

    private final String algorithm;
    private final int source;
    private final long[] dist;
    private final int[] prev;
    private final Map<String, Long> operations;

    ShortestPaths(String algorithm, int source, long[] dist, int[] prev, Map<String, Long> operations) {
        this.algorithm = algorithm;
        this.source = source;
        this.dist = dist;
        this.prev = prev;
        this.operations = Collections.unmodifiableMap(new LinkedHashMap<>(operations));
    }

    public String algorithm() {
        return algorithm;
    }

    public int source() {
        return source;
    }

    public boolean isReachable(int v) {
        return dist[v] != INFINITY;
    }

    public long distanceTo(int v) {
        return dist[v];
    }

    public List<Integer> pathTo(int v) {
        if (!isReachable(v)) return Collections.emptyList();
        List<Integer> path = new ArrayList<>();
        for (int x = v; x != -1; x = prev[x]) path.add(x);
        Collections.reverse(path);
        return path;
    }

    public int reachableCount() {
        int c = 0;
        for (long d : dist) if (d != INFINITY) c++;
        return c;
    }

    public int vertexCount() {
        return dist.length;
    }

    public Map<String, Long> operations() {
        return operations;
    }
}
