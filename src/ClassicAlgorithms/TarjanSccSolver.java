package ClassicAlgorithms;

import java.util.*;

public class TarjanSccSolver {

    private static final int UNVISITED = -1;

    private int n;
    private List<List<Integer>> graph;
    private boolean solved;
    private int[] ids, low;
    private boolean[] onStack;
    private int sccCount, id;
    private Deque<Integer> stack;

    public TarjanSccSolver(List<List<Integer>> graph) {
        this.n = graph.size();
        this.graph = graph;
    }

    public static void main(String[] args) {
        int n = 8;
        List<List<Integer>> graph = createGraph(n);

        addEdge(graph, 6, 0);
        addEdge(graph, 6, 2);
        addEdge(graph, 3, 4);
        addEdge(graph, 6, 4);
        addEdge(graph, 2, 0);
        addEdge(graph, 0, 1);
        addEdge(graph, 4, 5);
        addEdge(graph, 5, 6);
        addEdge(graph, 3, 7);
        addEdge(graph, 7, 5);
        addEdge(graph, 1, 2);
        addEdge(graph, 7, 3);
        addEdge(graph, 5, 0);

        TarjanSccSolver solver = new TarjanSccSolver(graph);

        int[] sccs = solver.getSccs();
        Map<Integer, List<Integer>> multimap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (!multimap.containsKey(sccs[i])) multimap.put(sccs[i], new ArrayList<>());
            multimap.get(sccs[i]).add(i);
        }

        // Prints:
        // Number of Strongly Connected Components: 3
        // Nodes: [0, 1, 2] form a Strongly Connected Component.
        // Nodes: [3, 7] form a Strongly Connected Component.
        // Nodes: [4, 5, 6] form a Strongly Connected Component.
        System.out.printf("Number of Strongly Connected Components: %d\n", solver.sccCount());
        for (List<Integer> scc : multimap.values()) {
            System.out.println("Nodes: " + scc + " form a Strongly Connected Component.");
        }
    }

    private static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
    }

    private static List<List<Integer>> createGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        return graph;
    }

    private int[] getSccs() {
        if (!solved) solve();
        return low;
    }

    private int sccCount() {
        if (!solved) solve();
        return sccCount;
    }

    private void solve() {

        if (solved) return;

        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        Arrays.fill(ids, UNVISITED);

        for (int i = 0; i < n; i++) {
            if (ids[i] == UNVISITED) {
                dfs(i);
            }
        }

        solved = true;
    }

    private void dfs(int at) {

        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = id++;

        for (int to : graph.get(at)) {
            if (ids[to] == UNVISITED) dfs(to);
            if (onStack[to]) low[at] = Math.min(low[at], low[to]);
        }

        if (low[at] == ids[at]) {
            for (int node = stack.pop(); ; node = stack.pop()) {
                onStack[node] = false;
//                low[node] = sccCount;
                if (node == at) break;
            }
            sccCount++;
        }
    }

}
