package ClassicAlgorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloydWarshallSolver {

    private int n;
    private boolean solved;
    private double[][] dp;
    private Integer[][] next;

    private static final int REACHES_NEGETIVE_CYCLE = -1;

    public FloydWarshallSolver(double[][] m) {

        n = m.length;
        dp = new double[n][n];
        next = new Integer[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (m[i][j] != Double.POSITIVE_INFINITY) {
                    next[i][j] = j;
                }
                dp[i][j] = m[i][j];
            }
        }
    }

    public static void main(String[] args) {
        int n = 7;
        double[][] m = createGraph(n);

        m[0][1] = 2;
        m[0][2] = 5;
        m[0][6] = 10;
        m[1][2] = 2;
        m[1][4] = 11;
        m[2][6] = 2;
        m[6][5] = 11;
        m[4][5] = 1;
        m[5][4] = -2;

        FloydWarshallSolver solver = new FloydWarshallSolver(m);

        double[][] dist = solver.getApspMatrix();
        System.out.println(Arrays.deepToString(dist));

        List<Integer> path = solver.reconstructShortestPath(1, 5);
        System.out.println(path);
    }

    private List<Integer> reconstructShortestPath(int start, int end) {

        if (!solved) solve();

        List<Integer> path = new ArrayList<>();
        if (dp[start][end] == Double.POSITIVE_INFINITY) return path;

        int at = start;
        for (;at != end; at = next[at][end]) {
            if (at == REACHES_NEGETIVE_CYCLE) return null;
            path.add(at);
        }
        if (next[at][end] == REACHES_NEGETIVE_CYCLE) return null;
        path.add(end);
        return path;
    }

    private double[][] getApspMatrix() {
        if (!solved) solve();
        return dp;
    }

    private void solve() {
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dp[i][k] + dp[k][j] < dp[i][j]) {
                        dp[i][j] = dp[i][k] + dp[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dp[i][k] + dp[k][j] < dp[i][j]) {
                        dp[i][j] = Double.NEGATIVE_INFINITY;
                        next[i][j] = REACHES_NEGETIVE_CYCLE;
                    }
                }
            }
        }

        solved = true;
    }

    private static double[][] createGraph(int n) {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(matrix[i], Double.POSITIVE_INFINITY);
            matrix[i][i] = 0;
        }
        return matrix;
    }
}
