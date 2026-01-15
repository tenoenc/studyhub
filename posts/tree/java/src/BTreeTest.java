public class BTreeTest {
    public static void main(String[] args) {
        // 1. 최소 차수 t = 3인 B-트리 생성
        // (각 노드는 최소 2개, 최대 5개의 키를 가질 수 있음)
        BTree<Integer> btree = new BTree<>(3);

        System.out.println("=== [1] 데이터 삽입 테스트 ===");
        // 노드 분할을 유도하기 위해 다량의 데이터를 순차적으로 삽입
        int[] insertData = {10, 20, 5, 6, 12, 30, 7, 17, 8, 22, 13, 25, 23, 15, 1, 4};
        for (int val : insertData) {
            btree.insert(val);
        }

        System.out.print("삽입 완료 후 트리 순회 (정렬된 결과): ");
        btree.traverse(); // 1 4 5 6 7 8 10 12 13 15 17 20 22 23 25 30

        System.out.println("\n\n=== [2] 탐색 테스트 ===");
        int[] searchData = {12, 25, 100};
        for (int val : searchData) {
            System.out.println("키 " + val + " 탐색 결과: " + (btree.search(val) ? "성공" : "실패"));
        }

        System.out.println("\n=== [3] 데이터 삭제 테스트 (다양한 케이스) ===");

        // Case 1: 리프 노드에서 단순 삭제
        System.out.println("삭제: 1 (리프 노드 삭제)");
        btree.remove(1);
        btree.traverse();

        // Case 2: 내부 노드 삭제 (자식으로부터 빌려오거나 병합 발생)
        System.out.println("삭제: 6 (내부 노드 삭제)");
        btree.remove(6);
        btree.traverse();

        // Case 3: 트리의 높이가 줄어들 수 있는 대규모 삭제
        System.out.println("삭제: 13, 7, 4 삭제 (재조정 및 병합 유도)");
        btree.remove(13);
        btree.remove(7);
        btree.remove(4);

        System.out.print("최종 트리 상태: ");
        btree.traverse();
    }
}
