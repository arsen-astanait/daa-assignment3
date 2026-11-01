package dsu;

public final class DisjointSetUnion {
    private final int[] parent;
    private final int[] rank;
    private final boolean pathCompression;
    private final boolean unionByRank;
    private int components;

    private long findCalls;
    private long unionCalls;
    private long successfulUnions;
    private long parentSteps;

    public DisjointSetUnion(int n) {
        this(n, true, true);
    }

    public DisjointSetUnion(int n, boolean pathCompression, boolean unionByRank) {
        if (n < 0) throw new IllegalArgumentException("n must be non-negative");
        this.parent = new int[n];
        this.rank = new int[n];
        this.pathCompression = pathCompression;
        this.unionByRank = unionByRank;
        this.components = n;
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        checkElement(x);
        findCalls++;
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
            parentSteps++;
        }
        // path compression
        if (pathCompression) {
            while (parent[x] != root) {
                int next = parent[x];
                parent[x] = root;
                x = next;
            }
        }
        return root;
    }

    public boolean union(int a, int b) {
        unionCalls++;
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return false;
        if (unionByRank) {
            // smaller tree goes under bigger one
            if (rank[ra] < rank[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }
            parent[rb] = ra;
            if (rank[ra] == rank[rb]) rank[ra]++;
        } else {
            parent[ra] = rb; // no rank, just link
        }
        components--;
        successfulUnions++;
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    public int components() {
        return components;
    }

    public int size() {
        return parent.length;
    }

    public long findCalls() {
        return findCalls;
    }

    public long unionCalls() {
        return unionCalls;
    }

    public long successfulUnions() {
        return successfulUnions;
    }

    public long parentSteps() {
        return parentSteps;
    }

    private void checkElement(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException("Element " + x + " not in [0, " + parent.length + ")");
        }
    }
}
