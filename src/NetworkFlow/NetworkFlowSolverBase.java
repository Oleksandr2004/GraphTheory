package NetworkFlow;

import java.util.ArrayList;
import java.util.List;

abstract class NetworkFlowSolverBase {

    static class Edge {

        public int from, to;
        public Edge residual;
        public long flow;
        public final long capacity;

        public Edge(int from, int to, long capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }

        public boolean isResidual() {
            return capacity == 0;
        }

        public long remainingCapacity() {
            return capacity - flow;
        }

        public void augment(long bottleNeck) {
            flow += bottleNeck;
            residual.flow -= bottleNeck;
        }

        public String toString(int s, int t) {
            String u = (from == s) ? "s" : ((from == t) ? "t" : String.valueOf(from));
            String v = (to == s) ? "s" : ((to == t) ? "t" : String.valueOf(to));
            return String.format("Edge %s -> %s | flow = %d | capacity = %d | isResidual = %b", u, v, flow, capacity, isResidual());
        }
    }

    static final long INF = Long.MAX_VALUE / 2;

    final int n, s, t;

    protected int visitedToken = 1;
    protected int[] visited;

    protected boolean solved;

    protected long maxFlow;

    protected List<Edge>[] graph;

    protected NetworkFlowSolverBase(int n, int s, int t) {
        this.n = n;
        this.s = s;
        this.t = t;
        initializeEmptyFlowGraph();
        visited = new int[n];
    }

    private void initializeEmptyFlowGraph() {
        graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
    }

    public void addEdge(int from, int to, int capacity) {
        Edge e1 = new Edge(from, to, capacity);
        Edge e2 = new Edge(to, from, 0);

        e1.residual = e2;
        e2.residual = e1;

        graph[from].add(e1);
        graph[to].add(e2);
    }

    public List<Edge>[] getGraph() {
        execute();
        return graph;
    }

    public long getMaxFlow() {
        execute();
        return maxFlow;
    }

    public void visit(int i) {
        visited[i] = visitedToken;
    }

    public boolean visited(int i) {
        return visited[i] == visitedToken;
    }

    public void markAllNodesAsUnvisited() {
        visitedToken++;
    }

    private void execute() {
        if (solved) return;
        solved = true;
        solve();
    }

    protected abstract void solve();

}
