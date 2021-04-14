package ClassicAlgorithms;

import java.util.*;

public class TopologicalSort {

    private static class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {

        final int N = 9;
        Map<Integer, List<Edge>> graph = new HashMap<>();
        for (int i = 0; i < N; i++) graph.put(i, new ArrayList<>());
        graph.get(0).add(new Edge(0, 1, 50));
        graph.get(1).add(new Edge(1, 2, 30));
        graph.get(2).add(new Edge(2, 3, 20));
        graph.get(1).add(new Edge(1, 4, 40));
        graph.get(4).add(new Edge(4, 3, 15));
        graph.get(0).add(new Edge(0, 5, 40));
        graph.get(5).add(new Edge(5, 6, 10));
        graph.get(5).add(new Edge(5, 7, 25));
        graph.get(7).add(new Edge(7, 6, 35));
        graph.get(7).add(new Edge(7, 8, 100));

//        int[] ordering = topologicalSort(graph, N);
//        System.out.println(Arrays.toString(ordering));

        Integer[] dist = dagShortestPath(graph, 0, N);

        System.out.println(dist[8]);
//        System.out.println(Arrays.toString(dist));
    }

    private static Integer[] dagShortestPath(Map<Integer, List<Edge>> graph, int start, int numNodes) {
        int[] topsort = topologicalSort(graph, numNodes);
        Integer[] dist = new Integer[numNodes];
        dist[start] = 0;

        for (int i = 0; i < numNodes; i++) {

            int nodeIndex = topsort[i];
            if (dist[nodeIndex] != null) {
                List<Edge> adjacentEdges = graph.get(nodeIndex);
                if (adjacentEdges != null) {
                    for (Edge edge : adjacentEdges) {
                        int newDist = dist[nodeIndex] + edge.weight;
                        if (dist[edge.to] == null) dist[edge.to] = newDist;
                        else dist[edge.to] = Math.min(dist[edge.to], newDist);
                    }
                }
            }
        }
        return dist;
    }

    private static int[] topologicalSort(Map<Integer, List<Edge>> graph, int numNodes) {
        int[] ordering = new int[numNodes];
        boolean[] visited = new boolean[numNodes];

        int i = numNodes - 1;

        for (int at = 0; at < numNodes; at++)
            if (!visited[at]) i = dfs(graph, visited, ordering, i, at);

        return ordering;
    }

    private static int dfs(Map<Integer, List<Edge>> graph, boolean[] visited, int[] ordering, int i, int at) {

        visited[at] = true;

        List<Edge> edges = graph.get(at);
        if (edges != null) {
            for (Edge edge : edges) {
                if (!visited[edge.to]) {
                    i = dfs(graph, visited, ordering, i, edge.to);
                }
            }
        }

        ordering[i] = at;
        return i - 1;
    }
}
