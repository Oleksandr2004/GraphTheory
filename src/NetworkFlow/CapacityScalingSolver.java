package NetworkFlow;

public class CapacityScalingSolver extends NetworkFlowSolverBase {

    private long delta;

    protected CapacityScalingSolver(int n, int s, int t) {
        super(n, s, t);
    }

    @Override
    public void addEdge(int from, int to, int capacity) {
        super.addEdge(from, to, capacity);
        delta = Math.max(delta, capacity);
    }

    @Override
    protected void solve() {
        delta = Long.highestOneBit(delta);

        for (long f = 0; delta > 0; delta /= 2) {
            do {
                markAllNodesAsUnvisited();
                f = dfs(s, INF);
                maxFlow += f;
            } while (f != 0);
        }
    }

    private long dfs(int node, long flow) {
        if (node == t) return flow;
        visit(node);

        for (Edge edge : graph[node]) {
            long cap = edge.remainingCapacity();
            if (cap >= delta && !visited(edge.to)) {
                long bottleNeck = dfs(edge.to, Math.min(flow, cap));

                if (bottleNeck > 0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        int n = 6;

        int s = n - 2;
        int t = n - 1;

        NetworkFlowSolverBase solver = new CapacityScalingSolver(n, s, t);

        // Edges from source
        solver.addEdge(s, 0, 6);
        solver.addEdge(s, 1, 14);

        // Middle edges
        solver.addEdge(0, 1, 1);
        solver.addEdge(0, 2, 5);
        solver.addEdge(1, 2, 7);
        solver.addEdge(1, 3, 10);
        solver.addEdge(2, 3, 1);

        // Edges to sink
        solver.addEdge(2, t, 11);
        solver.addEdge(3, t, 12);

        // Prints:
        // Maximum Flow is: 20
        System.out.printf("Maximum Flow is: %d\n", solver.getMaxFlow());
    }
}
