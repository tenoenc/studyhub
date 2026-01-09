package posts.tree;

public class MySegmentTree {
    private long[] tree;
    private long[] lazy;
    private int n;

    public MySegmentTree(long[] arr) {
        this.n = arr.length;
        this.tree = new long[4 * n];
        this.lazy = new long[4 * n];
        build(arr, 1, 0, n - 1);
    }

    private void build(long[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
            return;
        }

        int mid = (start + end) / 2;
        build(arr, 2 * node, start, mid);
        build(arr, 2 * node + 1, mid + 1, end);

        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    private void push(int node, int start, int end) {
        if (lazy[node] != 0) {
            tree[node] += (end - start + 1) * lazy[node];

            if (start != end) {
                lazy[2 * node] += lazy[node];
                lazy[2 * node + 1] += lazy[node];
            }

            lazy[node] = 0;
        }
    }

    public void updateRange(int left, int right, int val) {
        updateRange(1, 0, n - 1, left, right, val);
    }

    private void updateRange(int node, int start, int end, int left, int right, int val) {
        push(node, start, end);

        if (left > end || right < start) return;

        if (left <= start && end <= right) {
            lazy[node] += val;
            push(node, start, end);
            return;
        }

        int mid = (start + end) / 2;
        updateRange(2 * node, start, mid, left, right, val);
        updateRange(2 * node + 1, mid + 1, end, left, right, val);

        tree[node] = tree[2 * node] + tree[2 * node + 1];
    }

    public long queryRange(int left, int right) {
        return queryRange(1, 0, n - 1, left, right);
    }

    private long queryRange(int node, int start, int end, int left, int right) {
        push(node, start, end);

        if (left > end || right < start) return 0;

        if (left <= start && end <= right) {
            return tree[node];
        }

        int mid = (start + end) / 2;
        return queryRange(2 * node, start, mid, left, right) +
                queryRange(2 * node + 1, mid + 1, end, left, right);
    }
}
