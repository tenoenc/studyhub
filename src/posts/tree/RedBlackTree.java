package posts.tree;

/*
 * Red-Black Tree
 * - 노드에 검정(Black)과 빨강(Red)의 색상을 부여하고 5가지 규칙을 적용하여 균형을 유지하는 트리입니다.
 * - AVL 트리보다 균형 조건이 유연하여 삽입과 삭제 연산 시 발생하는 회전 빈도가 적어 실무에서 널리 사용됩니다.
 */
public class RedBlackTree<T extends Comparable<T>> {

    // 노드의 색상을 표현하는 열거형입니다.
    enum Color { RED, BLACK }

    // 트리의 각 데이터를 저장하고 구조를 형성하는 노드 클래스입니다.
    private class Node {
        T data;
        Node left, right, parent; // 밸런싱을 위해 부모 노드에 대한 참조가 반드시 필요합니다.
        Color color;

        Node(T data) {
            this.data = data;
            // 1. 신규 노드는 항상 RED로 삽입하여 Black-height 규칙 위반을 최소화합니다.
            this.color = Color.RED;
        }
    }

    private Node root;
    // 모든 리프 노드의 끝을 담당하는 Sentinel Node입니다. 항상 BLACK입니다.
    private Node TNULL;

    public RedBlackTree() {
        // 1. TNULL 노드를 초기화하고 색상을 BLACK으로 설정하여 트리의 경계를 만듭니다.
        TNULL = new Node(null);
        TNULL.color = Color.BLACK;
        root = TNULL;
    }

    // 트리 높이 균형을 위해 특정 노드를 기준으로 구조를 바꾸는 연산입니다.
    private void leftRotate(Node x) {
        // 1. 오른쪽 자식 y를 기준으로 위치를 변경합니다.
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) y.left.parent = x;

        // 2. x의 부모와 y를 연결합니다.
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;

        // 3. x를 y의 왼쪽 자식으로 내려서 회전을 마무리합니다.
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        // 1. 왼쪽 자식 y를 기준으로 위치를 변경합니다.
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) y.right.parent = x;

        // 2. x의 부모와 y를 연결합니다.
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;

        // 3. x를 y의 오른쪽 자식으로 내려서 회전을 마무리합니다.
        y.right = x;
        x.parent = y;
    }

    public void insert(T key) {
        Node node = new Node(key);
        node.left = TNULL;
        node.right = TNULL;

        Node y = null;
        Node x = this.root;

        // 1단계: 일반적인 이진 탐색 트리(BST) 규칙에 따라 삽입될 위치를 탐색합니다.
        while (x != TNULL) {
            y = x;
            if (node.data.compareTo(x.data) < 0) x = x.left;
            else x = x.right;
        }

        // 2단계: 찾은 부모 노드 y의 아래에 새 노드를 연결합니다.
        node.parent = y;
        if (y == null) root = node;
        else if (node.data.compareTo(y.data) < 0) y.left = node;
        else y.right = node;

        // 3단계: 루트 노드이거나 부모가 BLACK이면 보정이 불필요하므로 종료합니다.
        if (node.parent == null) {
            node.color = Color.BLACK;
            return;
        }
        if (node.parent.parent == null) return;

        // 4단계: RED 노드가 연속되는 규칙 위반(Double Red)을 해결합니다.
        fixInsert(node);
    }

    private void fixInsert(Node k) {
        Node u; // 삼촌(Uncle) 노드: 부모의 형제 노드입니다.
        // 부모의 색상이 RED인 동안(규칙 위반 상태) 반복하여 보정합니다.
        while (k.parent.color == Color.RED) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;

                // Case 1: 삼촌 노드가 RED인 경우 -> 부모, 삼촌, 할아버지의 색상만 변경합니다.
                if (u.color == Color.RED) {
                    u.color = Color.BLACK;
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent; // 할아버지 노드부터 다시 검사를 진행합니다.
                } else {
                    // Case 2: 삼촌이 BLACK이고 노드 방향이 꺾인 형태(Left-Child)인 경우 -> 회전하여 직선을 만듭니다.
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    // Case 3: 삼촌이 BLACK이고 노드 방향이 직선 형태인 경우 -> 회전 및 색상 변경으로 균형을 맞춥니다.
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    leftRotate(k.parent.parent);
                }
            } else {
                // 부모가 왼쪽 자식인 경우의 대칭 로직입니다.
                u = k.parent.parent.right;
                if (u.color == Color.RED) {
                    u.color = Color.BLACK;
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) break;
        }
        // 루트 노드는 항상 BLACK이어야 한다는 규칙을 강제합니다.
        root.color = Color.BLACK;
    }

    public void delete(T data) {
        deleteNodeHelper(this.root, data);
    }

    private void deleteNodeHelper(Node node, T key) {
        Node z = TNULL; // 삭제할 대상 노드
        Node x, y; // x는 y의 자리를 대체할 노드, y는 실제 트리에서 제거될 노드입니다.

        // 1단계: 삭제할 데이터가 들어있는 노드 z를 찾습니다.
        while (node != TNULL) {
            if (node.data.equals(key)) z = node;
            if (node.data.compareTo(key) <= 0) node = node.right;
            else node = node.left;
        }

        if (z == TNULL) return;

        y = z;
        Color yOriginalColor = y.color; // y의 원래 색상을 기억하여 Black-height 변화를 체크합니다.

        // 2단계: BST 삭제 방식에 따라 노드를 대체하고 연결합니다.
        if (z.left == TNULL) {
            x = z.right;
            rbTransplant(z, z.right); // 자식이 없거나 오른쪽만 있는 경우
        } else if (z.right == TNULL) {
            x = z.left;
            rbTransplant(z, z.left); // 왼쪽 자식만 있는 경우
        } else {
            // 3단계: 자식이 둘인 경우, 오른쪽 서브트리의 최솟값(Successor)을 찾아 y로 설정합니다.
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) x.parent = y;
            else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color; // 원래 삭제 노드의 색상을 물려받아 속성 위반을 방지합니다.
        }

        // 4단계: 실제 삭제된 노드(y)의 색상이 BLACK이면 경로상의 BLACK 개수가 깨지므로 보정을 실행합니다.
        if (yOriginalColor == Color.BLACK) fixDelete(x);
    }

    private void fixDelete(Node x) {
        Node s; // 형제(Sibling) 노드입니다.
        // x가 루트가 아니고 색상이 BLACK인 동안(Double Black 상태) 반복합니다.
        while (x != root && x.color == Color.BLACK) {
            if (x == x.parent.left) {
                s = x.parent.right;
                // Case 1: 형제가 RED인 경우 -> 형제를 BLACK으로 만들고 회전하여 다른 케이스로 유도합니다.
                if (s.color == Color.RED) {
                    s.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }
                // Case 2: 형제와 그 자식들이 모두 BLACK인 경우 -> 형제를 RED로 바꾸고 부모 단계에서 다시 검사합니다.
                if (s.left.color == Color.BLACK && s.right.color == Color.BLACK) {
                    s.color = Color.RED;
                    x = x.parent;
                } else {
                    // Case 3: 형제는 BLACK인데 왼쪽 자식이 RED인 경우 -> 우회전하여 Case 4로 변환합니다.
                    if (s.right.color == Color.BLACK) {
                        s.left.color = Color.BLACK;
                        s.color = Color.RED;
                        rightRotate(s);
                        s = x.parent.right;
                    }
                    // Case 4: 형제가 BLACK이고 오른쪽 자식이 RED인 경우 -> 회전 및 색상 보정으로 최종 해결합니다.
                    s.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    s.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                // 대칭되는 로직(x가 부모의 오른쪽 자식인 경우)입니다.
                s = x.parent.left;
                if (s.color == Color.RED) {
                    s.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }
                if (s.right.color == Color.BLACK && s.right.color == Color.BLACK) {
                    s.color = Color.RED;
                    x = x.parent;
                } else {
                    if (s.left.color == Color.BLACK) {
                        s.right.color = Color.BLACK;
                        s.color = Color.RED;
                        leftRotate(s);
                        s = x.parent.left;
                    }
                    s.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    s.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        // 마지막 노드를 BLACK으로 칠하여 보정을 마무리합니다.
        x.color = Color.BLACK;
    }

    // u 노드의 자리에 v 노드를 대체하여 넣는 보조 메서드입니다.
    private void rbTransplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }

    // 서브트리에서 가장 작은 값을 가진 노드를 반환합니다.
    private Node minimum(Node node) {
        while (node.left != TNULL) node = node.left;
        return node;
    }

    // 중위 순회하며 데이터와 색상을 함께 출력하여 규칙 준수 여부를 확인합니다.
    public void printInorder() {
        inorderHelper(this.root);
        System.out.println();
    }

    private void inorderHelper(Node node) {
        if (node != TNULL) {
            inorderHelper(node.left);
            System.out.print(node.data + "(" + node.color + ") ");
            inorderHelper(node.right);
        }
    }

    public boolean contains(T key) {
        Node node = root;
        while (node != TNULL) {
            int cmp = key.compareTo(node.data);
            if (cmp == 0) return true;
            else if (cmp < 0) node = node.left;
            else node = node.right;
        }
        return false;
    }
}