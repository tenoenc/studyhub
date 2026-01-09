package posts.tree;

import java.util.*;

/*
 * B+ Tree
 * - 모든 실제 데이터는 리프 노드에만 저장하고, 내부 노드는 탐색을 돕는 가이드 역할만 수행하는 B-Tree의 변형입니다.
 * - 리프 노드들이 연결 리스트로 이어져 있어 데이터베이스의 범위 검색(Range Scan)에 최적화되어 있습니다.
 */
public class BPlusTree<T extends Comparable<T>> {

    private final int m; // 한 노드가 가질 수 있는 최대 자식 수인 차수입니다.
    private Node root;   // 트리의 시작점인 루트 노드입니다.
    private final int minKeys; // 노드가 최소한 유지해야 하는 키의 개수입니다.

    // 모든 노드가 공통으로 가져야 할 속성을 정의하는 추상 클래스입니다.
    private abstract class Node {
        List<T> keys;        // 노드에 담긴 키 리스트입니다.
        InternalNode parent; // 위쪽으로 밸런싱 작업을 전달하기 위한 부모 참조입니다.

        Node() {
            this.keys = new ArrayList<>();
        }

        abstract boolean isOverflow();   // 키가 너무 많은지 확인합니다.
        abstract boolean isUnderflow();  // 키가 너무 적은지 확인합니다.
        abstract T getFirstLeafKey();    // 서브트리에서 가장 작은 실제 데이터를 가져옵니다.
    }

    // 내부 노드는 실제 데이터 없이 길 찾기 역할만 수행합니다.
    private class InternalNode extends Node {
        List<Node> children; // 자식 노드들을 가리키는 포인터 리스트입니다.

        InternalNode() {
            super();
            this.children = new ArrayList<>();
        }

        @Override
        boolean isOverflow() { return children.size() > m; }

        @Override
        boolean isUnderflow() { return children.size() < Math.ceil(m / 2.0); }

        @Override
        T getFirstLeafKey() { return children.get(0).getFirstLeafKey(); }

        // 새로운 자식 노드를 올바른 위치에 삽입하고 부모 관계를 맺어줍니다.
        void insertChild(T key, Node child) {
            int idx = Collections.binarySearch(keys, key);
            int insertIdx = (idx < 0) ? -(idx + 1) : idx;
            keys.add(insertIdx, key);
            children.add(insertIdx + 1, child);
            child.parent = this;
        }

        // 특정 위치의 키와 그에 대응하는 오른쪽 자식을 제거합니다.
        void removeChild(int keyIdx) {
            if (keyIdx >= 0 && keyIdx < keys.size()) {
                keys.remove(keyIdx);
                if (keyIdx + 1 < children.size()) {
                    children.remove(keyIdx + 1);
                }
            }
        }
    }

    // 리프 노드는 모든 실제 데이터가 저장되는 곳이며 형제끼리 연결됩니다.
    private class LeafNode extends Node {
        LeafNode next; // 오른쪽 형제로 가는 지름길입니다.
        LeafNode prev; // 왼쪽 형제로 가는 지름길입니다.

        LeafNode() {
            super();
        }

        @Override
        boolean isOverflow() { return keys.size() > m - 1; }

        @Override
        boolean isUnderflow() { return keys.size() < Math.floor(m / 2.0); }

        @Override
        T getFirstLeafKey() { return keys.get(0); }
    }

    public BPlusTree(int m) {
        this.m = m;
        // 1. 최소 차수를 바탕으로 노드가 붕괴되지 않을 최소 키 개수를 계산합니다.
        this.minKeys = (int) Math.ceil(m / 2.0) - 1;
        this.root = new LeafNode();
    }

    public boolean search(T key) {
        // 1. 키가 저장될 수 있는 리프 노드를 먼저 찾습니다.
        // 2. 해당 리프 노드의 리스트 안에 키가 있는지 확인하여 반환합니다.
        return findLeafNode(key).keys.contains(key);
    }

    private LeafNode findLeafNode(T key) {
        Node curr = root;
        // 1. 내부 노드인 동안에는 자식 리스트 중 키의 범위에 맞는 곳을 골라 내려갑니다.
        while (curr instanceof InternalNode) {
            InternalNode node = (InternalNode) curr;
            int idx = Collections.binarySearch(node.keys, key);
            int childIdx = (idx < 0) ? -(idx + 1) : idx + 1;
            curr = node.children.get(childIdx);
        }
        // 2. 도달한 리프 노드를 반환합니다.
        return (LeafNode) curr;
    }



    public void insert(T key) {
        // 1. 데이터가 들어갈 리프 노드를 탐색합니다.
        LeafNode leaf = findLeafNode(key);
        int idx = Collections.binarySearch(leaf.keys, key);
        if (idx >= 0) return; // 중복 데이터는 삽입하지 않습니다.

        // 2. 리프 노드 내 정렬된 위치에 데이터를 추가합니다.
        leaf.keys.add(-(idx + 1), key);

        // 3. 노드가 꽉 찼다면(Overflow) 노드를 반으로 쪼개고 부모에게 보고합니다.
        if (leaf.isOverflow()) {
            Node sibling = splitLeaf(leaf);
            handleOverflow(leaf, sibling);
        }
    }

    private Node splitLeaf(LeafNode leaf) {
        // 1. 리프 노드의 중간 지점을 찾아 새로운 형제 노드를 만듭니다.
        int mid = leaf.keys.size() / 2;
        LeafNode sibling = new LeafNode();
        // 2. 오른쪽 절반의 데이터를 형제 노드로 옮깁니다.
        sibling.keys.addAll(leaf.keys.subList(mid, leaf.keys.size()));
        leaf.keys.subList(mid, leaf.keys.size()).clear();

        // 3. 리프 간 연결 리스트(next, prev)를 갱신하여 수평 이동이 가능하게 합니다.
        sibling.next = leaf.next;
        if (leaf.next != null) leaf.next.prev = sibling;
        leaf.next = sibling;
        sibling.prev = leaf;

        return sibling;
    }

    private void handleOverflow(Node left, Node right) {
        // 1. 루트에서 분할이 일어났다면 새로운 루트를 만들어 트리의 높이를 1 늘립니다.
        if (left.parent == null) {
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(right.getFirstLeafKey());
            newRoot.children.add(left);
            newRoot.children.add(right);
            left.parent = newRoot;
            right.parent = newRoot;
            root = newRoot;
        } else {
            // 2. 부모가 있다면 부모 노드에 새 자식 정보를 삽입합니다.
            InternalNode parent = left.parent;
            parent.insertChild(right.getFirstLeafKey(), right);
            // 3. 부모 역시 꽉 찼다면 다시 분할 작업을 재귀적으로 반복합니다.
            if (parent.isOverflow()) {
                Node sibling = splitInternal(parent);
                handleOverflow(parent, sibling);
            }
        }
    }

    private Node splitInternal(InternalNode node) {
        // 1. 내부 노드를 분할할 때는 중간 키 하나를 부모로 올리고 나머지로 노드를 나눕니다.
        int mid = node.keys.size() / 2;
        InternalNode sibling = new InternalNode();
        sibling.keys.addAll(node.keys.subList(mid + 1, node.keys.size()));
        sibling.children.addAll(node.children.subList(mid + 1, node.children.size()));

        // 2. 새로 생성된 노드로 옮겨진 자식들의 부모 참조를 갱신합니다.
        for (Node child : sibling.children) child.parent = sibling;

        node.keys.subList(mid, node.keys.size()).clear();
        node.children.subList(mid + 1, node.children.size()).clear();

        return sibling;
    }

    public void delete(T key) {
        // 1. 삭제할 데이터가 있는 리프 노드를 찾고 데이터를 제거합니다.
        LeafNode leaf = findLeafNode(key);
        int idx = Collections.binarySearch(leaf.keys, key);
        if (idx < 0) return;

        leaf.keys.remove(idx);

        // 2. 삭제 후 루트가 텅 비었는지 확인합니다.
        if (leaf == root && leaf.keys.isEmpty()) {
            return;
        }

        // 3. 노드에 남은 키가 너무 적다면(Underflow) 형제에게 빌리거나 합칩니다.
        if (leaf != root && leaf.isUnderflow()) {
            handleUnderflow(leaf);
        }

        // 4. 삭제로 인해 내부 노드의 가이드 키가 실제 리프 데이터와 다를 수 있으므로 일치시켜줍니다.
        updateGuideKeys(root);
    }

    private void handleUnderflow(Node node) {
        // 1. 루트가 비어 있고 자식이 하나라면 트리 높이를 줄이며 자식을 루트로 만듭니다.
        if (node == root) {
            if (node instanceof InternalNode && node.keys.isEmpty()) {
                root = ((InternalNode) node).children.get(0);
                root.parent = null;
            }
            return;
        }

        InternalNode parent = node.parent;
        int idx = parent.children.indexOf(node);

        // 2. 왼쪽 형제에게서 키를 빌려올 수 있는지 확인합니다.
        if (idx > 0 && parent.children.get(idx - 1).keys.size() > minKeys) {
            borrowFromLeft(node, idx);
        }
        // 3. 오른쪽 형제에게서 키를 빌려올 수 있는지 확인합니다.
        else if (idx < parent.children.size() - 1 && parent.children.get(idx + 1).keys.size() > minKeys) {
            borrowFromRight(node, idx);
        }
        // 4. 빌릴 수 없다면 형제와 노드를 하나로 합칩니다(Merge).
        else {
            if (idx > 0) {
                merge(parent.children.get(idx - 1), node);
            } else {
                merge(node, parent.children.get(idx + 1));
            }
            // 5. 합병 후 부모 노드에 다시 Underflow가 생겼는지 재귀적으로 확인합니다.
            if (parent.isUnderflow()) {
                handleUnderflow(parent);
            }
        }
    }

    private void borrowFromLeft(Node node, int idx) {
        InternalNode parent = node.parent;
        Node leftSibling = parent.children.get(idx - 1);

        // 1. 왼쪽 형제의 마지막 데이터를 가져와서 현재 노드의 맨 앞에 넣습니다.
        T borrowedKey = leftSibling.keys.remove(leftSibling.keys.size() - 1);
        node.keys.add(0, borrowedKey);

        // 2. 내부 노드인 경우 자식 포인터도 함께 옮겨줍니다.
        if (node instanceof InternalNode) {
            Node movedChild = ((InternalNode) leftSibling).children.remove(((InternalNode) leftSibling).children.size() - 1);
            ((InternalNode) node).children.add(0, movedChild);
            movedChild.parent = (InternalNode) node;
        }
        // 3. 부모 노드에 저장된 가이드 키를 현재 노드의 새로운 최소값으로 바꿉니다.
        parent.keys.set(idx - 1, node.getFirstLeafKey());
    }

    private void borrowFromRight(Node node, int idx) {
        InternalNode parent = node.parent;
        Node rightSibling = parent.children.get(idx + 1);

        // 1. 오른쪽 형제의 첫 데이터를 가져와 현재 노드의 맨 뒤에 넣습니다.
        T borrowedKey = rightSibling.keys.remove(0);
        node.keys.add(borrowedKey);

        if (node instanceof InternalNode) {
            Node movedChild = ((InternalNode) rightSibling).children.remove(0);
            ((InternalNode) node).children.add(movedChild);
            movedChild.parent = (InternalNode) node;
        }
        // 2. 부모 노드의 가이드 키를 오른쪽 형제의 새로운 최소값으로 바꿉니다.
        parent.keys.set(idx, rightSibling.getFirstLeafKey());
    }

    private void merge(Node left, Node right) {
        InternalNode parent = left.parent;
        int idxInParent = parent.children.indexOf(left);

        // 1. 오른쪽 노드의 모든 내용을 왼쪽 노드로 합칩니다.
        if (left instanceof LeafNode) {
            left.keys.addAll(right.keys);
            ((LeafNode) left).next = ((LeafNode) right).next;
            if (((LeafNode) right).next != null) {
                ((LeafNode) right).next.prev = (LeafNode) left;
            }
        } else {
            // 2. 내부 노드를 합칠 때는 부모의 구분 키를 내려받은 뒤 자식들을 합칩니다.
            InternalNode l = (InternalNode) left;
            InternalNode r = (InternalNode) right;
            l.keys.add(parent.keys.get(idxInParent));
            l.keys.addAll(r.keys);
            l.children.addAll(r.children);
            for (Node child : r.children) child.parent = l;
        }

        // 3. 부모 노드에서 병합되어 사라진 자식의 정보를 지웁니다.
        parent.removeChild(idxInParent);
    }

    private void updateGuideKeys(Node node) {
        if (node instanceof InternalNode) {
            InternalNode internal = (InternalNode) node;
            // 1. 각 내부 노드 키를 오른쪽 자식의 최솟값과 동기화합니다.
            for (int i = 0; i < internal.keys.size(); i++) {
                internal.keys.set(i, internal.children.get(i + 1).getFirstLeafKey());
            }
            // 2. 모든 하위 노드에 대해 동일하게 반복합니다.
            for (Node child : internal.children) updateGuideKeys(child);
        }
    }



    public List<T> rangeSearch(T start, T end) {
        List<T> result = new ArrayList<>();
        // 1. 탐색 시작 범위가 속한 리프 노드를 찾습니다.
        LeafNode curr = findLeafNode(start);

        // 2. 리프 노드 간의 수평 연결을 이용해 끝 범위를 만날 때까지 데이터를 수집합니다.
        while (curr != null) {
            for (T key : curr.keys) {
                if (key.compareTo(start) >= 0 && key.compareTo(end) <= 0) result.add(key);
                else if (key.compareTo(end) > 0) return result;
            }
            // 3. 부모로 다시 올라갈 필요 없이 next 포인터로 바로 옆 리프를 방문합니다.
            curr = curr.next;
        }
        return result;
    }
}