import java.util.LinkedList;
import java.util.Queue;

/*
 * Binary Search Tree (BST)
 * - 왼쪽 자식 < 부모 < 오른쪽 자식의 구조를 갖는 가장 기본적인 이진 탐색 트리입니다.
 * - 데이터의 정렬 상태를 유지하며 탐색, 삽입, 삭제를 수행하는 모든 탐색 트리의 기초가 됩니다.
 */
public class BinarySearchTree<T extends Comparable<T>> {

    // 데이터를 저장하고 자식 노드를 가리키는 트리의 기본 단위입니다.
    private static class Node<T> {
        T data;
        Node<T> left, right;

        Node(T data) {
            this.data = data;
            this.left = this.right = null;
        }
    }

    private Node<T> root; // 트리의 최상단 루트 노드입니다.
    private int size;     // 트리에 포함된 전체 노드의 개수입니다.

    public BinarySearchTree() {
        this.root = null;
    }

    public void insert(T data) {
        root = insertRecursive(root, data);
        size++;
    }

    private Node<T> insertRecursive(Node<T> current, T data) {
        // 1. 빈 자리에 도달하면 새로운 노드를 생성하여 반환합니다.
        if (current == null) {
            return new Node<>(data);
        }

        // 2. 값을 비교하여 작으면 왼쪽, 크면 오른쪽 서브트리로 내려갑니다.
        if (data.compareTo(current.data) < 0) {
            current.left = insertRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            current.right = insertRecursive(current.right, data);
        }
        // 3. 중복된 데이터는 트리의 성질을 유지하기 위해 추가하지 않고 현재 노드를 반환합니다.

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

        // 1. 삭제할 데이터가 현재 노드보다 작거나 큰지 비교하여 위치를 찾습니다.
        if (data.compareTo(current.data) < 0) {
            current.left = deleteRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            current.right = deleteRecursive(current.right, data);
        } else {
            // 2. 삭제할 노드를 발견했을 때, 자식 노드의 개수에 따라 처리가 달라집니다.

            // Case 1 & 2: 자식이 없거나 하나만 있는 경우입니다.
            // 자식이 없으면 null이 반환되고, 하나면 존재하는 자식이 부모에게 연결됩니다.
            if (current.left == null) return current.right;
            if (current.right == null) return current.left;

            // Case 3: 자식이 둘인 경우입니다.
            // 3-1. 오른쪽 서브트리에서 가장 작은 값(후계자)을 찾아 현재 노드에 덮어씌웁니다.
            current.data = minValue(current.right);
            // 3-2. 값을 복사해왔으므로 오른쪽 서브트리에서 원래 있던 후계자 노드를 삭제합니다.
            current.right = deleteRecursive(current.right, current.data);
        }
        return current;
    }

    public boolean contains(T data) {
        return containsRecursive(root, data);
    }

    private boolean containsRecursive(Node<T> current, T data) {
        // 1. 노드가 null이면 트리에 데이터가 존재하지 않는 것입니다.
        if (current == null) return false;

        // 2. 현재 노드의 데이터와 비교하여 같으면 true, 다르면 방향을 정해 다시 탐색합니다.
        if (data.compareTo(current.data) < 0) {
            return containsRecursive(current.left, data);
        } else if (data.compareTo(current.data) > 0) {
            return containsRecursive(current.right, data);
        }
        return true;
    }

    public T minValue() {
        if (root == null) return null;
        return minValue(root);
    }

    // 최솟값은 트리의 성질상 항상 가장 왼쪽 끝 노드에 위치합니다.
    private T minValue(Node<T> node) {
        return node.left == null ? node.data : minValue(node.left);
    }

    public T maxValue() {
        if (root == null) return null;
        Node<T> current = root;
        // 최댓값은 트리의 성질상 항상 가장 오른쪽 끝 노드에 위치합니다.
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }



    // 중위 순회: 왼쪽 자식 -> 루트 -> 오른쪽 자식 순으로 방문하여 오름차순 정렬 결과를 얻습니다.
    public void inorder() {
        System.out.println("Inorder: ");
        inorderRecursive(root);
        System.out.println();
    }

    private void inorderRecursive(Node<T> node) {
        if (node != null) {
            inorderRecursive(node.left);
            System.out.print(node.data + " ");
            inorderRecursive(node.right);
        }
    }

    // 전위 순회: 루트 -> 왼쪽 자식 -> 오른쪽 자식 순으로 방문합니다.
    public void preorder() {
        System.out.println("Preorder: ");
        preorderRecursive(root);
        System.out.println();
    }

    private void preorderRecursive(Node<T> node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preorderRecursive(node.left);
            preorderRecursive(node.right);
        }
    }

    // 후위 순회: 왼쪽 자식 -> 오른쪽 자식 -> 루트 순으로 방문합니다.
    public void postorder() {
        System.out.println("Postorder: ");
        postorderRecursive(root);
        System.out.println();
    }

    private void postorderRecursive(Node<T> node) {
        if (node != null) {
            postorderRecursive(node.left);
            postorderRecursive(node.right);
            System.out.print(node.data + " ");
        }
    }

    // 레벨 순회: 큐를 사용하여 위에서부터 아래로 각 층별 노드들을 탐색합니다.
    public void levelOrder() {
        System.out.println("Level-order: ");
        if (root == null) return;
        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node<T> tempNode = queue.poll();
            System.out.print(tempNode.data + " ");
            if (tempNode.left != null) queue.add(tempNode.left);
            if (tempNode.right != null) queue.add(tempNode.right);
        }
        System.out.println();
    }

    public int size() { return size; }

    // 트리의 전체 높이를 계산하며, 더 깊은 자식 쪽의 깊이를 기준으로 삼습니다.
    public int height() { return calculateHeight(root); }

    private int calculateHeight(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(calculateHeight(node.left), calculateHeight(node.right));
    }
}