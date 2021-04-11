package TreeAlgorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TreeIsomorphism {

    private static class TreeNode {

        private int id;
        private TreeNode parent;
        private List<TreeNode> children;

        private TreeNode(int id) {
            this(id, null);
        }

        private TreeNode(int id, TreeNode parent) {
            this.id = id;
            this.parent = parent;
            this.children = new LinkedList<>();
        }

        private void addChildren(TreeNode... nodes) {
            for (TreeNode node : nodes) {
                children.add(node);
            }
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
    }

    public static void main(String[] args) {
//        isomorphismTest();

    }

    private static void isomorphismTest() {
        final List<List<Integer>> tree1 = createEmptyGraph(6);
        addUndirectedEdge(tree1, 0, 1);
        addUndirectedEdge(tree1, 0, 2);
        addUndirectedEdge(tree1, 2, 3);
        addUndirectedEdge(tree1, 3, 5);
        addUndirectedEdge(tree1, 3, 4);

        final List<List<Integer>> tree2 = createEmptyGraph(6);
        addUndirectedEdge(tree2, 3, 2);
        addUndirectedEdge(tree2, 5, 1);
        addUndirectedEdge(tree2, 1, 3);
        addUndirectedEdge(tree2, 3, 4);
        addUndirectedEdge(tree2, 3, 0);

        System.out.println(treesAreIsomorphic(tree1, tree2));
    }

    private static boolean treesAreIsomorphic(List<List<Integer>> tree1, List<List<Integer>> tree2) {
        if (tree1.isEmpty() || tree2.isEmpty()) {
            throw new IllegalArgumentException("Trees are empty!");
        }

        List<Integer> tree1Centers = findTreeCenters(tree1);
        List<Integer> tree2Centers = findTreeCenters(tree2);

        TreeNode rootedTree1 = rootTree(tree1, tree1Centers.get(0));
        String tree1Encoding = encode(rootedTree1);

        for (Integer tree2Center : tree2Centers) {
            TreeNode rootedTree2 = rootTree(tree2, tree2Center);
            String tree2Encoding = encode(rootedTree2);

            if (tree1Encoding.equals(tree2Encoding)) {
                return true;
            }
        }

        return false;
    }

    private static String encode(TreeNode node) {
        if (node == null) {
            return "";
        }
        List<String> labels = new LinkedList<>();
        for (TreeNode child : node.getChildren()) {
            labels.add(encode(child));
        }
        Collections.sort(labels);
        StringBuilder sb = new StringBuilder();
        for (String label : labels) {
            sb.append(label);
        }
        return "(" + sb.toString() + ")";
    }

    private static TreeNode rootTree(List<List<Integer>> graph, Integer rootId) {
        TreeNode root = new TreeNode(rootId);
        return buildTree(graph, root);
    }

    private static TreeNode buildTree(List<List<Integer>> graph, TreeNode node) {

        for (int neighbor : graph.get(node.getId())) {
            if (node.getParent() != null && neighbor == node.getParent().getId()) {
                continue;
            }
            TreeNode child = new TreeNode(neighbor, node);
            node.addChildren(child);
            buildTree(graph, child);
        }
        return node;
    }

    private static List<Integer> findTreeCenters(List<List<Integer>> tree) {
        int n = tree.size();
        List<Integer> leaves = new ArrayList<>();
        int[] degree = new int[n];

        for (int i = 0; i < n; i++) {
            List<Integer> edges = tree.get(i);
            degree[i] = edges.size();
            if (degree[i] <= 1) {
                leaves.add(i);
//                degree[i] = 0;
            }
        }

        int processedLeaves = leaves.size();
        while (processedLeaves < n) {
            List<Integer> newLeaves = new ArrayList<>();
            for (int node : leaves) {
                for (Integer neighbor : tree.get(node)) {
                    if (--degree[neighbor] == 1) {
                        newLeaves.add(neighbor);
                    }
                }
//                degree[node] = 0;
            }
            processedLeaves += newLeaves.size();
            leaves = newLeaves;
        }

        return leaves;
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
