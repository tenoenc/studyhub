import java.util.LinkedList;
import java.util.Queue;

/*
 * AVL Tree
 * - 모든 노드에서 왼쪽과 오른쪽 서브트리의 높이 차이가 1 이하가 되도록 스스로 균형을 잡는 트리입니다.
 * - 회전(Rotation) 연산을 통해 엄격한 균형을 유지하여 최악의 경우에도 탐색 성능 O(log N)을 보장합니다.
 */
public class AVLTree<T extends Comparable<T>> {

    private class Node {
        T data;
        int height;
        Node left, right;

        Node(T data) {
            this.data = data;
            this.height = 1;
        }
    }

    private Node root;

    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // 1. 회전을 수행하여 x를 부모로, y를 오른쪽 자식으로 위치를 바꿉니다.
        x.right = y;
        y.left = T2;

        // 2. 위치가 변한 노드들의 높이를 아래에 있는 노드(y)부터 순서대로 갱신합니다.
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // 1. 회전을 수행하여 y를 부모로, x를 왼쪽 자식으로 위치를 바꿉니다.
        y.left = x;
        x.right = T2;

        // 2. 위치가 변한 노드들의 높이를 아래에 있는 노드(x)부터 순서대로 갱신합니다.
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    public void insert(T data) {
        root = insertRecursive(root, data);
    }

    private Node insertRecursive(Node node, T data) {
        // 1. 재귀적으로 내려가며 표준 BST 방식으로 빈 자리에 노드를 삽입합니다.
        if (node == null) return new Node(data);

        int cmp = data.compareTo(node.data);
        if (cmp < 0) node.left = insertRecursive(node.left, data);
        else if (cmp > 0) node.right = insertRecursive(node.right, data);
        else return node;

        // 2. 삽입이 완료된 후 위로 올라오며 현재 노드의 높이를 최신화합니다.
        updateHeight(node);

        // 3. 현재 노드의 균형 상태를 확인하고 필요한 회전 연산을 호출합니다.
        return rebalance(node, data);
    }

    private Node rebalance(Node node, T data) {
        int balance = getBalance(node);

        // 1. LL 케이스: 왼쪽 자식의 왼쪽에 데이터가 삽입되어 우회전을 수행합니다.
        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rotateRight(node);
        }
        // 2. RR 케이스: 오른쪽 자식의 오른쪽에 데이터가 삽입되어 좌회전을 수행합니다.
        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return rotateLeft(node);
        }
        // 3. LR 케이스: 왼쪽 자식의 오른쪽에 삽입된 경우로, 자식을 먼저 좌회전시킨 후 부모를 우회전합니다.
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // 4. RL 케이스: 오른쪽 자식의 왼쪽에 삽입된 경우로, 자식을 먼저 우회전시킨 후 부모를 좌회전합니다.
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public void delete(T data) {
        root = deleteRecursive(root, data);
    }

    private Node deleteRecursive(Node root, T data) {
        if (root == null) return root;

        // 1. 삭제할 노드의 위치를 재귀적으로 탐색합니다.
        int cmp = data.compareTo(root.data);
        if (cmp < 0) {
            root.left = deleteRecursive(root.left, data);
        } else if (cmp > 0) {
            root.right = deleteRecursive(root.right, data);
        } else {
            // 2. 삭제 대상을 찾았을 때 자식의 개수에 따라 적절히 노드를 교체합니다.
            if ((root.left == null) || (root.right == null)) {
                Node temp = (root.left != null) ? root.left : root.right;
                if (temp == null) {
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                // 3. 자식이 둘인 경우 오른쪽 서브트리의 최솟값을 찾아 현재 노드로 복사합니다.
                Node temp = minValueNode(root.right);
                root.data = temp.data;
                root.right = deleteRecursive(root.right, temp.data);
            }
        }

        if (root == null) return root;

        // 4. 노드 삭제 후 높이를 갱신하고 불균형 여부를 다시 체크합니다.
        updateHeight(root);
        int balance = getBalance(root);

        // 5. 삭제 작업으로 인해 무너진 균형을 4가지 케이스에 맞춰 회전 보정합니다.
        if (balance > 1 && getBalance(root.left) >= 0) return rotateRight(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0) return rotateLeft(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }

        return root;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) current = current.left;
        return current;
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