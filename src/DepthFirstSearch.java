import java.util.*;

public class DepthFirstSearch {

    private static Map<Integer, List<Integer>> graph = new HashMap<>();

    private static boolean[] visited = new boolean[10];

    private static int[] components = new int[10];

    private static int count = 0;

    public static void main(String[] args) {

        graph.put(0, List.of(1, 2));
        graph.put(1, List.of(0, 2));
        graph.put(2, List.of(1));

        graph.put(3, List.of(4));
        graph.put(4, Collections.emptyList());

        graph.put(5, List.of(7));
        graph.put(6, Collections.emptyList());
        graph.put(7, List.of(5));

        findComponents();
        bfs(0, 1);
        System.out.println(count);
        System.out.println(components[0]);
    }

    private static void bfs(final int s, final int e) {
        solve(s);
    }

    private static void solve(final int s) {

    }

    private static void findComponents() {

        for (int i = 0; i < graph.size(); i++) {
            if (!visited[i]) {
                count++;
                dfs(i);
            }
        }
    }

    private static void dfs(final int at) {

        if (visited[at]) return;
        visited[at] = true;
        components[at] = count;

        final List<Integer> neighbours = graph.get(at);
        if (neighbours != null) {
            for (final Integer next : neighbours) {
                dfs(next);
            }
        }
    }
}
