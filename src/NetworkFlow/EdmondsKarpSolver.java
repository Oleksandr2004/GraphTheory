package NetworkFlow;

import java.util.ArrayDeque;
import java.util.Queue;

public class EdmondsKarpSolver extends NetworkFlowSolverBase {

    public EdmondsKarpSolver(int n, int s, int t) {
        super(n, s, t);
    }

    @Override
    protected void solve() {
        long flow;
        do {
            markAllNodesAsUnvisited();
            flow = bfs();
            maxFlow += flow;
        } while (flow != 0);
    }

    private long bfs() {
        Queue<Integer> q = new ArrayDeque<>(n);
        visit(s);
        q.offer(s);

        Edge[] prev = new Edge[n];
        while (!q.isEmpty()) {
            int node = q.poll();
            if (node == t) break;

            for (Edge edge : graph[node]) {
                long cap = edge.remainingCapacity();
                if (cap > 0 && !visited(edge.to)) {
                    visit(edge.to);
                    prev[edge.to] = edge;
                    q.offer(edge.to);
                }
            }
        }

        if (prev[t] == null) return 0;

        long bottleNeck = Long.MAX_VALUE;
        for (Edge edge = prev[t]; edge != null; edge = prev[edge.from])
            bottleNeck = Math.min(bottleNeck, edge.remainingCapacity());

        for (Edge edge = prev[t]; edge != null; edge = prev[edge.from])
            edge.augment(bottleNeck);

        return bottleNeck;
    }

    public static void main(String[] args) {
        int n = 6;
        int s = n - 1;
        int t = n - 2;

        EdmondsKarpSolver solver;
        solver = new EdmondsKarpSolver(n, s, t);

        // Source edges
        solver.addEdge(s, 0, 10);
        solver.addEdge(s, 1, 10);

        // Sink edges
        solver.addEdge(2, t, 10);
        solver.addEdge(3, t, 10);

        // Middle edges
        solver.addEdge(0, 1, 2);
        solver.addEdge(0, 2, 4);
        solver.addEdge(0, 3, 8);
        solver.addEdge(1, 3, 9);
        solver.addEdge(3, 2, 6);

        System.out.println(solver.getMaxFlow());
    }
}
