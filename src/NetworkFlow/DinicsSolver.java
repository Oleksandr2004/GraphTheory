package NetworkFlow;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class DinicsSolver extends NetworkFlowSolverBase {

    private int[] level;

    protected DinicsSolver(int n, int s, int t) {
        super(n, s, t);
        level = new int[n];
    }

    @Override
    protected void solve() {
        int[] next = new int[n];

        while (bfs()) {
            Arrays.fill(next, 0);
            for (long f = dfs(s, next, INF); f != 0; f = dfs(s, next, INF)) {
                maxFlow += f;
            }
        }
    }

    private long dfs(int at, int[] next, long flow) {
        if (at == t) return flow;
        final int numEdges = graph[at].size();

        for (; next[at] < numEdges; next[at]++) {
            Edge edge = graph[at].get(next[at]);
            long cap = edge.remainingCapacity();
            if (cap > 0 && level[edge.to] == level[at] + 1) {
                long bottleNeck = dfs(edge.to, next, Math.min(flow, cap));
                if (bottleNeck > 0) {
                    edge.augment(bottleNeck);
                    return bottleNeck;
                }
            }
        }
        return 0;
    }

    private boolean bfs() {
        Arrays.fill(level, -1);
        Deque<Integer> q = new ArrayDeque<>(n);
        q.offer(s);
        level[s] = 0;
        while (!q.isEmpty()) {
            Integer node = q.poll();
            for (Edge edge : graph[node]) {
                long cap = edge.remainingCapacity();
                if (cap > 0 && level[edge.to] == -1) {
                    level[edge.to] = level[node] + 1;
                    q.offer(edge.to);
                }
            }
        }
        return level[t] != -1;
    }

    public static void main(String[] args) {
        int n = 11;
        int s = n - 1;
        int t = n - 2;

        NetworkFlowSolverBase solver;
        solver = new DinicsSolver(n, s, t);

        // Source edges
        solver.addEdge(s, 0, 5);
        solver.addEdge(s, 1, 10);
        solver.addEdge(s, 2, 15);

        // Middle edges
        solver.addEdge(0, 3, 10);
        solver.addEdge(1, 0, 15);
        solver.addEdge(1, 4, 20);
        solver.addEdge(2, 5, 25);
        solver.addEdge(3, 4, 25);
        solver.addEdge(3, 6, 10);
        solver.addEdge(4, 2, 5);
        solver.addEdge(4, 7, 30);
        solver.addEdge(5, 7, 20);
        solver.addEdge(5, 8, 10);
        solver.addEdge(7, 8, 15);

        // Sink edges
        solver.addEdge(6, t, 5);
        solver.addEdge(7, t, 15);
        solver.addEdge(8, t, 10);

        // Prints: "Maximum flow: 30"
        System.out.printf("Maximum flow: %d\n", solver.getMaxFlow());
    }
}
