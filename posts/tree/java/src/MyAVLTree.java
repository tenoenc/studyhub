import java.util.LinkedList;
import java.util.Queue;

public class MyAVLTree<T extends Comparable<T>> {

    private class Node {
        private T data;
        private int height;
        private Node left;
        private Node right;

        Node(T data) {
            this.height = 1;
            this.data = data;
        }
    }

    Node root;

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int balance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node y) {
        Node x = y.right;
        Node T2 = x.left;

        x.left = y;
        y.right = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    public void insert(T data) {
        root = insertRecursive(root, data);
    }

    private Node insertRecursive(Node node, T data) {
        if (node == null) return new Node(data);

        if (data.compareTo(node.data) < 0) {
            node.left = insertRecursive(node.left, data);
        } else if(data.compareTo(node.data) > 0) {
            node.right = insertRecursive(node.right, data);
        } else {
            return node;
        }

        updateHeight(node);

        return rebalance(node, data);
    }

    private Node rebalance(Node node, T data) {
        int balance = balance(node);

        // LL Case
        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rotateRight(node);
        }

        // RR Case
        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return rotateLeft(node);
        }

        // LR Case
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // RL Case
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public void delete(T data) {
        root = deleteRecursive(root, data);
    }

    private Node deleteRecursive(Node node, T data) {
        if (node == null) return node;

        if (data.compareTo(node.data) < 0) {
            node.left = deleteRecursive(node.left, data);
        } else if(data.compareTo(node.data) > 0) {
            node.right = deleteRecursive(node.right, data);
        } else {
            // 자식이 하나이거나 없는 경우
            if (node.left == null || node.right == null) {
                Node temp = node.left != null ? node.left : node.right;
                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            }
            // 자식이 둘인 경우
            else {
                Node temp = minValueNode(node.right);
                node.data = temp.data;
                node.right = deleteRecursive(node.right, temp.data);
            }
        }

        if (node == null) return node;

        updateHeight(node);
        int balance = balance(node);

        if (balance > 1 && balance(node.left) >= 0) {
            return rotateRight(node);
        }
        if (balance > 1 && balance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && balance(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (balance < -1 && balance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node minValueNode(Node node) {
        if (node == null) return null;
        return node.left == null ? node : minValueNode(node.left);
    }

    public void printTree() {
        if (root == null) {
            System.out.println("Tree is empty.");
            return;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            while (levelSize-- > 0) {
                Node node = queue.poll();
                System.out.print(node.data + "(" + node.height + ") ");
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            System.out.println();
        }
    }
}
