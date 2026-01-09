package posts.tree;

/*
 * Segment Tree (with Lazy Propagation)
 * - 배열의 특정 구간에 대한 쿼리(합, 최솟값 등)와 구간 업데이트를 O(log N)에 처리하는 자료구조입니다.
 * - Lazy Propagation 기법을 적용하여 대규모 구간 수정 연산을 효율적으로 수행할 수 있습니다.
 */
public class SegmentTree {
    private long[] tree;     // 각 노드가 담당하는 구간의 합을 저장하는 배열입니다.
    private long[] lazy;     // 특정 구간에 더해질 값을 잠시 미뤄두는 지연 배열입니다.
    private int n;           // 원본 데이터 배열의 크기입니다.

    public SegmentTree(long[] arr) {
        this.n = arr.length;
        // 1. 트리 배열의 크기는 모든 구간을 안전하게 표현하기 위해 원본의 4배로 할당합니다.
        this.tree = new long[4 * n];
        this.lazy = new long[4 * n];
        // 2. 루트 노드 번호 1, 전체 인덱스 범위 0 ~ n-1을 기준으로 트리를 빌드합니다.
        build(arr, 1, 0, n - 1);
    }

    // 원본 배열을 바탕으로 구간 합 트리를 재귀적으로 생성하는 메서드입니다.
    private void build(long[] arr, int node, int start, int end) {
        // 1. start와 end가 같다면 리프 노드에 도달한 것이므로 배열의 실제 값을 트리 노드에 저장합니다.
        if (start == end) {
            tree[node] = arr[start];
            return;
        }

        // 2. 구간을 절반으로 나누어 왼쪽 자식과 오른쪽 자식 노드로 내려갑니다.
        int mid = (start + end) / 2;
        build(arr, 2 * node, start, mid);       // 왼쪽 자식의 인덱스는 현재 노드 번호의 2배입니다.
        build(arr, 2 * node + 1, mid + 1, end); // 오른쪽 자식의 인덱스는 (2 * 현재 노드 번호 + 1)입니다.

        // 3. 자식 노드들의 계산이 끝나면 두 자식의 합을 부모 노드에 저장하여 구간 합을 완성합니다.
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    // Lazy Propagation의 핵심으로, 미뤄두었던 업데이트 값을 노드에 반영하고 자식에게 전파합니다.
    private void push(int node, int start, int end) {
        // 1. lazy 배열에 값이 0이 아니라면 아직 반영되지 않은 업데이트가 남아있다는 뜻입니다.
        if (lazy[node] != 0) {
            // 2. 현재 노드가 담당하는 구간의 길이만큼 업데이트 값을 곱해 tree에 즉시 반영합니다.
            tree[node] += (end - start + 1) * lazy[node];

            // 3. 리프 노드가 아니라면 자식들의 lazy 배열에 현재 값을 누적시켜 나중에 처리하도록 예약합니다.
            if (start != end) {
                lazy[2 * node] += lazy[node];
                lazy[2 * node + 1] += lazy[node];
            }

            // 4. 현재 노드의 반영과 전파가 끝났으므로 lazy 값을 0으로 초기화합니다.
            lazy[node] = 0;
        }
    }

    public void updateRange(int left, int right, long val) {
        updateRange(1, 0, n - 1, left, right, val);
    }

    // 특정 구간(left ~ right)의 모든 데이터에 값을 더하는 메서드입니다.
    private void updateRange(int node, int start, int end, int left, int right, long val) {
        // 1. 노드를 방문할 때마다 이전에 미뤄두었던 지연 업데이트가 있는지 확인하고 처리합니다.
        push(node, start, end);

        // 2. 업데이트하려는 범위와 현재 노드의 구간이 전혀 겹치지 않으면 연산을 중단합니다.
        if (left > end || right < start) return;

        // 3. 현재 노드의 구간이 업데이트 범위에 완전히 포함되면 여기에 값을 기록하고 즉시 빠져나옵니다.
        if (left <= start && end <= right) {
            lazy[node] += val;
            push(node, start, end); // 현재 노드의 데이터는 즉시 갱신해줍니다.
            return;
        }

        // 4. 구간이 일부만 겹치는 경우 자식 노드로 내려가서 동일한 작업을 수행합니다.
        int mid = (start + end) / 2;
        updateRange(2 * node, start, mid, left, right, val);
        updateRange(2 * node + 1, mid + 1, end, left, right, val);

        // 5. 하위 노드들의 값이 변경되었으므로 현재 부모 노드의 합계 데이터도 다시 계산합니다.
        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    public long queryRange(int left, int right) {
        return queryRange(1, 0, n - 1, left, right);
    }

    // 특정 구간(left ~ right)의 합계를 구하는 메서드입니다.
    private long queryRange(int node, int start, int end, int left, int right) {
        // 1. 정확한 값을 얻기 위해 탐색 전에도 항상 지연된 업데이트를 먼저 반영해줘야 합니다.
        push(node, start, end);

        // 2. 탐색 범위가 현재 구간을 완전히 벗어나면 합계에 영향을 주지 않는 0을 반환합니다.
        if (left > end || right < start) return 0;

        // 3. 현재 노드의 구간이 탐색 범위 안에 완전히 들어온다면 저장된 구간 합을 즉시 반환합니다.
        if (left <= start && end <= right) {
            return tree[node];
        }

        // 4. 구간이 걸쳐있다면 왼쪽 자식과 오른쪽 자식에게 각각 결과를 물어본 뒤 합쳐서 반환합니다.
        int mid = (start + end) / 2;
        return queryRange(2 * node, start, mid, left, right) +
                queryRange(2 * node + 1, mid + 1, end, left, right);
    }
}