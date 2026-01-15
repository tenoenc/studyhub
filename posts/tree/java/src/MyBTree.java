// 하나의 노드가 여러 개의 키와 자식을 가질 수 있는 균형 잡힌 다원 탐색 트리
// 노드 내 키 개수를 일정 범위 내로 관리하여 트리의 높이를 낮게 유지하므로 대용량 데이터 인덱싱에 적합
public class MyBTree<T extends Comparable<T>> {
    private Node root;
    private final int t; // 최소 차수 : 각 노드는 최소 t-1개, 최대 2t-1개의 키를 가짐

    private class Node {
        int n; // 현재 노드에 저장된 키의 개수
        T[] keys; // 키들을 저장하는 배열
        Node[] children; // 자식 노드 포인터들을 저장하는 배열
        boolean leaf; // 리프 노드 여부

        @SuppressWarnings("unchecked")
        Node(int t, boolean leaf) {
            this.n = 0;
            this.leaf = leaf;
            // 최대 2t-1개의 키와 2t개의 자식을 가질 수 있는 공간 할당
            this.keys = (T[]) new Comparable[2 * t - 1];
            this.children = new MyBTree.Node[2 * t];
        }

        // 현재 노드에서 k보다 크거나 같은 첫 번째 키의 인덱스를 찾음
        int findKey(T k) {
            int idx = 0;
            while (idx < n && keys[idx].compareTo(k) < 0) idx++;
            return idx;
        }

        // 삭제 메인 메서드
        void remove(T k) {
            int idx = findKey(k);

            // Case 1 & 2: 현재 노드에 삭제하려는 키 k가 존재하는 경우
            if (idx < n && keys[idx].compareTo(k) == 0) {
                if (leaf) removeFromLeaf(idx);
                else removeFromNonLeaf(idx);
            } else {
                // Case 3: ㅎ녀재 노드에 키 k가 없는 겨우 (리프까지 왔는데 없으면 종료)
                if (leaf) {
                    System.out.println("키 " + k + "가 트리에 없습니다.");
                    return;
                }

                // 삭제할 키가 마지막 자식 서브트리에 있는지 확인
                boolean flag = (idx == n);

                // 내려갈 자식 노드가 최소 키 개수(t-1)만 가지고 있다면,
                // 삭제 후 규칙 위반을 방지하기 위해 미리 채워줌(fill)
                if (children[idx].n < t) fill(idx);

                // fill(병합) 과정에서 현재 인덱스의 자식이 사라졌을 수 있음
                if (flag && idx > n) children[idx - 1].remove(k);
                else children[idx].remove(k);
            }
        }

        // 리프 노드에서 인덱스 idx의 키 삭제 (단순 배열 시프팅)
        void removeFromLeaf(int idx) {
            for (int i = idx + 1; i < n; ++i) keys[i - 1] = keys[i];
            n--;
        }

        // 내부 노드에서 인덱스 idx의 키 삭제
        void removeFromNonLeaf(int idx) {
            T k = keys[idx];

            // 1. 왼쪽 자식(children[idx])이 충분한 키(t개 이상)를 가진 경우
            if (children[idx].n >= t) {
                T pred = getPred(idx); // 왼쪽 서브트리에서 가장 큰 값(선행자)을 찾음
                keys[idx] = pred; // 현재 키를 선행자로 대체
                children[idx].remove(pred); // 서브트리에서 선행자 삭제
            }
            // 2. 오른쪽 자식(children[idx + 1])이 충분한 키를 가진 경우
            else if (children[idx + 1].n >= t) {
                T succ = getSucc(idx); // 오른쪽 서브트리에서 가장 작은 값(후속자)을 찾음
                keys[idx] = succ; // 현재 키를 후속자로 대체
                children[idx + 1].remove(succ); // 서브트리에서 후속자 삭제
            }
            // 3. 양쪽 자식 모두 키가 t-1개뿐인 경우 -> 두 자식을 병함
            else {
                merge(idx);
                children[idx].remove(k); // 병합된 노드에서 k 삭제
            }
        }

        // 왼쪽 서브트리의 최대값 찾기 (오른쪽으로 계속 이동)
        T getPred(int idx) {
            Node cur = children[idx];
            while (!cur.leaf) cur = cur.children[cur.n];
            return cur.keys[cur.n - 1];
        }

        // 오른쪽 서브트리에서 최소값 찾기 (왼쪽으로 계속 이동)
        T getSucc(int idx) {
            Node cur = children[idx];
            while (!cur.leaf) cur = cur.children[0];
            return cur.keys[0];
        }

        // 자식 노드 children[idx]의 키 개수가 t-1개일 때 보충하는 작업
        void fill(int idx) {
            // 왼쪽 형제에게서 빌려올 수 있는 경우
            if (idx != 0 && children[idx - 1].n >= t) borrowFromPrev(idx);
            else if (idx != n && children[idx + 1].n >= t) borrowFromNext(idx);
            else {
                if (idx != n) merge(idx); // 오른쪽 형제와 병합
                else merge(idx - 1); // 왼쪽 형제와 병합
            }
        }

        // 왼쪽 형제(sibling)로부터 키 하나를 빌려옴
        private void borrowFromPrev(int idx) {
            Node child = children[idx];
            Node sibling = children[idx - 1];

            // 자식의 키와 자식 배열을 한 칸씩 밀어 공간 확보
            for (int i = child.n - 1; i >= 0; --i) child.keys[i + 1] = child.keys[i];
            if (!child.leaf) {
                for (int i = child.n; i >= 0; --i) child.children[i + 1] = child.children[i];
            }

            // 부모의 키를 자식의 첫 번째로 내리고, 형제의 마지막 키를 부모로 올림
            child.keys[0] = keys[idx - 1];
            if (!child.leaf) child.children[0] = sibling.children[sibling.n];
            keys[idx - 1] = sibling.keys[sibling.n - 1];

            child.n += 1;
            sibling.n -= 1;
        }

        // 오른쪽 형제(sibling)로부터 키 하나를 빌려옴
        void borrowFromNext(int idx) {
            Node child = children[idx];
            Node sibling = children[idx + 1];

            // 부모의 키를 자식의 마지막에 추가
            child.keys[child.n] = keys[idx];
            if (!child.leaf) child.children[child.n + 1] = sibling.children[0];

            // 형제의 첫 번째 키를 부모로 올림
            keys[idx] = sibling.keys[0];

            // 형제의 키와 자식 배열을 한 칸씩 당김
            for (int i = 1; i < sibling.n; ++i) sibling.keys[i - 1] = sibling.keys[i];
            if (!sibling.leaf) {
                for (int i = 1; i <= sibling.n; ++i) sibling.children[i - 1] = sibling.children[i];
            }
            child.n += 1;
            sibling.n -= 1;
        }

        // children[idx]와 children[idx+1]을 병합
        void merge(int idx) {
            Node child = children[idx];
            Node sibling = children[idx + 1];

            // 부모의 키를 왼쪽 자식의 중간(t-1 위치)에 삽입
            child.keys[t - 1] = keys[idx];
            // 형제의 모든 키를 왼쪽 자식으로 복사
            for (int i = 0; i < sibling.n; ++i) child.keys[i + t] = sibling.keys[i];
            // 형제가 리프가아니라면 자식 포인터들도 모두 복사
            if (!child.leaf) {
                for (int i = 0; i <= sibling.n; ++i) child.children[i + t] = sibling.children[i];
            }

            // 부모 노드에서 병합된 키와 자식 포인터 정보를 삭제하고 한 칸씩 당김
            for (int i = idx + 1; i < n; ++i) keys[i - 1] = keys[i];
            for (int i = idx + 2; i <= n; ++i) children[i - 1] = children[i];

            child.n += sibling.n + 1;
            n--; // 부모의 키 개수 감소
        }
    }

    public MyBTree(int t) {
        this.t = t;
        this.root = null;
    }

    public void remove(T k) {
        if (root == null) return;
        root.remove(k);
        // 삭제 후 루트의 키가 0개가 되면, 첫 번째 자식을 새로운 루트로 설정 (높이 감소)
        if(root.n == 0) {
            if (root.leaf) root = null;
            else root = root.children[0];
        }
    }

    // 탐색 로직 (BST와 유사하나 노드 내에서 선형 탐색 수행)
    /*public boolean search(T key) {
        return (root == null) ? false : search(root, key);
    }*/
}
