package TreeAlgorithms;

public class MinSparseTable {

    private int n;

    private int P;

    private int[] log2;

    private long[][] dp;

    private int[][] it;

    public MinSparseTable(long[] values) {

        n = values.length;
        P = (int) (Math.log(n) / Math.log(2));
        dp = new long[P + 1][n];
        it = new int[P + 1][n];

        for (int i = 0; i < n; i++) {
            dp[0][i] = values[i];
            it[0][i] = i;
        }

        log2 = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i / 2] + 1;
        }

        for (int p = 1; p <= P; p++) {
            for (int i = 0; i + (1 << p) <= n; i++) {
                long leftInterval = dp[p - 1][i];
                long rightInterval = dp[p - 1][i + (1 << (p - 1))];

                dp[p][i] = Math.min(leftInterval, rightInterval);

                if (leftInterval <= rightInterval) {
                    it[p][i] = it[p - 1][i];
                } else {
                    it[p][i] = it[p - 1][i + (1 << (p - 1))];
                }
            }
        }
    }

    public long queryMin(int l, int r) {
        int length = r - l + 1;
        int p = log2[length];
        int k = 1 << p;
        return Math.min(dp[p][l], dp[p][r - k + 1]);
    }

    public long queryMinIndex(int l, int r) {
        int length = r - l + 1;
        int p = log2[length];
        int k = 1 << p;

        long leftInterval = dp[p][l];
        long rightInterval = dp[p][r - k + 1];

        if (leftInterval <= rightInterval) {
            return it[p][l];
        } else {
            return it[p][r - k + 1];
        }
    }

    public static void main(String[] args) {
        long[] values = {1, 2, 3, 4, 5};
        MinSparseTable sparseTable = new MinSparseTable(values);
        System.out.println(sparseTable.queryMin(0, 4));
        System.out.println(sparseTable.queryMinIndex(0, 4));
    }
}
