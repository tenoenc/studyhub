package posts.tree;

import java.util.LinkedList;
import java.util.Queue;

public class MyBinarySearchTree<T extends Comparable<T>> {
    private class Node<T> {
        private T data;
        private Node<T> left;
        private Node<T> right;

        Node(T data) {
            this.data = data;
            this.left = this.right = null;
        }
    }

    Node<T> root;
    int size;

    public void insert(T data) {
        if (data == null) return;
        root = insertRecursive(root, data);
        size++;
    }

    private Node<T> insertRecursive(Node<T> current, T data) {
        if (current == null) return new Node<T>(data);
        if (data.compareTo(current.data) < 0) {
            current.left = insertRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            current.right = insertRecursive(current.right, data);
        }
        return current;
    }

    public void delete(T data) {
        if (contains(data)) {
            root = deleteRecursive(root, data);
            size--;
        }
    }

    private Node<T> deleteRecursive(Node<T> current, T data) {
        if (current == null) return null;
        if (data.compareTo(current.data) < 0) {
            current.left = insertRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            current.right = insertRecursive(current.right, data);
        } else {
            if (current.left == null) return current.right;
            if (current.right == null) return current.left;

            current.data = findMin(current.right);
            current.right = deleteRecursive(current.right, data);
        }
        return current;
    }

    private boolean contains(T data) {
        if (data == null) return false;
        return containsRecursive(root, data);
    }

    private boolean containsRecursive(Node<T> current, T data) {
        if (root == null) return false;
        if (data == current.data) return true;

        return data.compareTo(current.data) < 0 ? containsRecursive(current.left, data) :
                containsRecursive(current.right, data);
    }

    public T findMin() {
        if (root == null) return null;
        return findMin(root);
    }

    private T findMin(Node<T> node) {
        return node.left == null ? node.data : findMin(node.left);
    }

    public T findMax() {
        if (root == null) return null;
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }

    public void inorder() {
        if (root == null) return;
        System.out.print("Inorder: ");
        inorderRecursive(root);
        System.out.println();
    }

    private void inorderRecursive(Node<T> node) {
        if (node == null) return;
        inorderRecursive(node.left);
        System.out.print(node.data + " ");
        inorderRecursive(node.right);
    }

    public void preorder() {
        if (root == null) return;
        System.out.print("Preorder: ");
        postorderRecursive(root);
        System.out.println();
    }

    private void preorderRecursive(Node<T> node) {
        if (node == null) return;
        System.out.print(node.data + " ");
        postorderRecursive(node.left);
        postorderRecursive(node.right);
    }

    public void postorder() {
        if (root == null) return;
        System.out.print("Postorder: ");
        postorderRecursive(root);
        System.out.println();
    }

    private void postorderRecursive(Node<T> node) {
        if (node == null) return;
        System.out.print(node.data + " ");
        postorderRecursive(node.left);
        postorderRecursive(node.right);
    }

    public void levelOrder() {
        if (root == null) return;
        System.out.println("Level-order: ");
        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node<T> tempNode = queue.poll();
            System.out.print(tempNode.data + " ");
            if (tempNode.left != null) queue.add(tempNode.left);
            if (tempNode.right != null) queue.add(tempNode.right);
        }
    }

    public int size() { return size; }

    public int height() { return calculateHeight(root); }

    private int calculateHeight(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
    }
}
