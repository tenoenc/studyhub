import java.util.LinkedList;
import java.util.Queue;

public class MyBinarySearchTree<T extends Comparable<T>> {
    private class Node {
        private T data;
        private Node left;
        private Node right;

        Node(T data) {
            this.data = data;
        }
    }

    private Node root;
    private int size;

    public void insert(T data) {
        root = insertRecursive(root, data);
        size++;
    }

    private Node insertRecursive(Node node, T data) {
        if (node == null) return new Node(data);

        if (data.compareTo(node.data) < 0) {
            node.left = insertRecursive(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = insertRecursive(node.right, data);
        }

        return node;
    }

    public void delete(T data) {
        if (contains(data)) {
            root = deleteRecursive(root, data);
            size--;
        }
    }

    private Node deleteRecursive(Node node, T data) {
        if (node == null) return null;

        if (data.compareTo(node.data) < 0) {
            node.left = deleteRecursive(node.left, data);
        } else if (data.compareTo(node.data) > 0) {
            node.right = deleteRecursive(node.right, data);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            node.data = minValue(node.right);
            node.right = deleteRecursive(node.right, node.data);
        }
        return node;
    }

    public  T minValue() {
        return minValue(root);
    }

    private T minValue(Node node) {
        if (node == null) return null;
        return node.left == null ? node.data : minValue(node.left);
    }

    public T maxValue() {
        if (root == null) return null;
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }

    public boolean contains(T data) {
        return containsRecursive(root, data);
    }

    private boolean containsRecursive(Node node, T data) {
        if (node == null) return false;
        if (data.compareTo(node.data) == 0) return true;
        return data.compareTo(node.data) < 0 ? containsRecursive(node.left, data) : containsRecursive(node.right, data);
    }

    public void inorder() {
        System.out.print("Inorder: ");
        inorderRecursive(root);
        System.out.println();
    }

    private void inorderRecursive(Node node) {
        if (node == null) return;
        inorderRecursive(node.left);
        System.out.print(node.data + " ");
        inorderRecursive(node.right);
    }

    public void preorder() {
        System.out.print("Preorder: ");
        preorderRecursive(root);
        System.out.println();
    }

    private void preorderRecursive(Node node) {
        if (node == null) return;
        System.out.print(node.data + " ");
        preorderRecursive(node.left);
        preorderRecursive(node.right);
    }

    public void postorder() {
        System.out.print("Postorder: ");
        postorderRecursive(root);
        System.out.println();
    }

    private void postorderRecursive(Node node) {
        if (node == null) return;
        postorderRecursive(node.left);
        postorderRecursive(node.right);
        System.out.print(node.data + " ");
    }

    public int size() { return size; }
    public int height() {
        return calculateHeight(root);
    }

    private int calculateHeight(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
    }

    public void levelOrder() {
        System.out.print("Level-Order: ");

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node tempNode = queue.poll();
            System.out.print(tempNode.data + " ");
            if (tempNode.left != null) queue.add(tempNode.left);
            if (tempNode.right != null) queue.add(tempNode.right);
        }

        System.out.println();
    }
}
