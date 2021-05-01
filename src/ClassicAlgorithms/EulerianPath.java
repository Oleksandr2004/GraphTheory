package ClassicAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class EulerianPath {

    private final int n;
    private int edgeCount;
    private int[] in, out;
    private List<List<Integer>> graph;
    private LinkedList<Integer> path;


    public EulerianPath(List<List<Integer>> graph) {
        this.n = graph.size();
        this.graph = graph;
        path = new LinkedList<>();
    }

    private static List<List<Integer>> initializeEmptyGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        return graph;
    }

    private static void addDirectedEdge(List<List<Integer>> g, int from, int to) {
        g.get(from).add(to);
    }

    public static void main(String[] args) {
        int n = 7;
        List<List<Integer>> graph = initializeEmptyGraph(n);

        addDirectedEdge(graph, 1, 2);
        addDirectedEdge(graph, 1, 3);
        addDirectedEdge(graph, 2, 2);
        addDirectedEdge(graph, 2, 4);
        addDirectedEdge(graph, 2, 4);
        addDirectedEdge(graph, 3, 1);
        addDirectedEdge(graph, 3, 2);
        addDirectedEdge(graph, 3, 5);
        addDirectedEdge(graph, 4, 3);
        addDirectedEdge(graph, 4, 6);
        addDirectedEdge(graph, 5, 6);
        addDirectedEdge(graph, 6, 3);

        EulerianPath solver;
        solver = new EulerianPath(graph);

        // Outputs path: [1, 3, 5, 6, 3, 2, 4, 3, 1, 2, 2, 4, 6]
        System.out.println(Arrays.toString(solver.getEulerianPath()));
    }

    private int[] getEulerianPath() {
        setUp();
        if (edgeCount == 0) return null;

        if (!graphHasEulerianPath()) return null;
        dfs(findStartNode());

        if (path.size() != edgeCount + 1) return null;

        int[] soln = new int[edgeCount + 1];
        for (int i = 0; !path.isEmpty(); i++)
            soln[i] = path.removeFirst();

        return soln;
    }

    private void dfs(int at) {
        while (out[at] != 0) {
            int next = graph.get(at).get(--out[at]);
            dfs(next);
        }
        path.addFirst(at);
    }

    private int findStartNode() {
        int start = 0;
        for (int i = 0; i < n; i++) {
            if (out[i] - in[i] == 1) return i;
            if (out[i] > 0) start = i;
        }
        return start;
    }

    private boolean graphHasEulerianPath() {
        int startNodes = 0, endNodes = 0;

        for (int i = 0; i < n; i++) {
            if (in[i] - out[i] > 1 || out[i] - in[i] > 1) return false;
            else if (out[i] - in[i] == 1) startNodes++;
            else if (in[i] - out[i] == 1) endNodes++;
        }
        return (startNodes == 0 && endNodes == 0) || (startNodes == 1 && endNodes == 1);
    }

    private void setUp() {
        in = new int[n];
        out = new int[n];
        edgeCount = 0;

        for (int from = 0; from < n; from++) {
            for (Integer to : graph.get(from)) {
                in[to]++;
                out[from]++;
                edgeCount++;
            }
        }
    }
}
