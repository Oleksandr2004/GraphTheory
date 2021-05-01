package ClassicAlgorithms;

import java.util.ArrayList;
import java.util.List;

public class ArticulationPoints {

    private List<List<Integer>> graph;
    private int n;

    private int id, rootNodeOutComingEdgeCount;
    private int[] ids, low;
    private boolean[] visited, isArtPoint;

    public ArticulationPoints(List<List<Integer>> graph, int n) {
        this.graph = graph;
        this.n = n;
    }

    public static void main(String[] args) {
        int n = 9;
        List<List<Integer>> graph = createGraph(n);

        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 1, 2);
        addEdge(graph, 2, 3);
        addEdge(graph, 3, 4);
        addEdge(graph, 2, 5);
        addEdge(graph, 5, 6);
        addEdge(graph, 6, 7);
        addEdge(graph, 7, 8);
        addEdge(graph, 8, 5);

    }

    private List<Integer> findArtPoints() {
        id = 0;
        low = new int[n];
        ids = new int[n];
        visited = new boolean[n];
        isArtPoint = new boolean[n];

        List<Integer> bridges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                rootNodeOutComingEdgeCount = 0;
                dfs(i, i, -1);
                isArtPoint[i] = (rootNodeOutComingEdgeCount > 1);
            }
        }

        return bridges;
    }

    private void dfs(int root, int at, int parent) {
        if (parent == root) rootNodeOutComingEdgeCount++;
        visited[at] = true;
        low[at] = ids[at] = id++;

        for (Integer to : graph.get(at)) {
            if (to == parent) continue;
            if (!visited[to]) {
                dfs(root, to, at);
                low[at] = Math.min(low[at], low[to]);
                if (ids[at] <= low[to]) {
                    isArtPoint[at] = true;
                }
            } else {
                low[at] = Math.min(low[at], ids[to]);
            }
        }
    }

    private static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
        graph.get(to).add(from);
    }

    private static List<List<Integer>> createGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        return graph;
    }
}
