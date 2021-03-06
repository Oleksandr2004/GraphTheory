package TreeAlgorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LowestCommonAncestorEulerTour {

    private final int n;

    private int tourIndex = 0;

    private long[] nodeDepth;

    private TreeNode[] nodeOrder;

    private int[] last;

    private MinSparseTable sparseTable;

    public LowestCommonAncestorEulerTour(TreeNode root) {
        n = root.getSize();
        setup(root);
    }

    private void setup(TreeNode root) {

        int eulerTourSize = 2 * n - 1;
        nodeDepth = new long[eulerTourSize];
        nodeOrder = new TreeNode[eulerTourSize];
        last = new int[n];

        dfs(root, 0);

        sparseTable = new MinSparseTable(nodeDepth);
    }

    private void dfs(TreeNode node, int depth) {
        if (node == null) {
            return;
        }

        visit(node, depth);
        for (TreeNode child : node.getChildren()) {
            dfs(child, depth + 1);
            visit(node, depth);
        }
    }

    private void visit(TreeNode node, int depth) {
        nodeOrder[tourIndex] = node;
        nodeDepth[tourIndex] = depth;
        last[node.getId()] = tourIndex;
        tourIndex++;
    }

    public static class TreeNode {

        private int n;

        private int id;
        private TreeNode parent;
        private List<TreeNode> children;

        public TreeNode(int id) {
            this(id, null);
        }

        public TreeNode(int id, TreeNode parent) {
            this.id = id;
            this.parent = parent;
            this.children = new LinkedList<>();
        }

        public void addChildren(TreeNode... nodes) {
            for (TreeNode node : nodes) {
                children.add(node);
            }
        }

        public void setSize(int n) {
            this.n = n;
        }

        public int getSize() {
            return n;
        }

        private int getId() {
            return id;
        }

        private TreeNode getParent() {
            return parent;
        }

        private List<TreeNode> getChildren() {
            return children;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }

        private static TreeNode rootTree(List<List<Integer>> graph, Integer rootId) {
            TreeNode root = new TreeNode(rootId);
            return buildTree(graph, root);
        }

        private static TreeNode buildTree(List<List<Integer>> graph, TreeNode node) {

            int subtreeNodeCount = 1;

            for (int neighbor : graph.get(node.getId())) {
                if (node.getParent() != null && neighbor == node.getParent().getId()) {
                    continue;
                }
                TreeNode child = new TreeNode(neighbor, node);
                node.addChildren(child);

                buildTree(graph, child);
                subtreeNodeCount += child.getSize();
            }

            node.setSize(subtreeNodeCount);
            return node;
        }

    }

    public static void main(String[] args) {

        List<List<Integer>> graph = createEmptyGraph(5);
        addUndirectedEdge(graph, 0, 1);
        addUndirectedEdge(graph, 0, 2);
        addUndirectedEdge(graph, 2, 3);
        addUndirectedEdge(graph, 2, 4);

        TreeNode root = TreeNode.rootTree(graph, 0);

        LowestCommonAncestorEulerTour solver = new LowestCommonAncestorEulerTour(root);
        System.out.println(solver.lca(3, 4));
    }

    private TreeNode lca(int index1, int index2) {
        int l = Math.min(last[index1], last[index2]);
        int r = Math.max(last[index1], last[index2]);
        int i = (int) sparseTable.queryMinIndex(l, r);
        return nodeOrder[i];
    }

    private static List<List<Integer>> createEmptyGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) graph.add(i, new LinkedList<>());
        return graph;
    }

    private static void addUndirectedEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
        graph.get(to).add(from);
    }
}
