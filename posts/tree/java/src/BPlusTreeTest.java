public class BPlusTreeTest {
    public static void main(String[] args) {
        // 1. 차수(Order)가 3인 B+ 트리 생성
        // (리프 노드는 최대 2개 키, 내부 노드는 최대 3개의 자식을 가짐)
        BPlusTree<Integer> bpt = new BPlusTree<>(3);

        System.out.println("=== [1] 데이터 삽입 테스트 ===");
        int[] insertData = {10, 20, 30, 40, 50, 60, 70, 80, 90};

        for (int key : insertData) {
            System.out.println("삽입: " + key);
            bpt.insert(key);
        }

        // 2. 단일 검색 테스트
        System.out.println("\n=== [2] 단일 검색 테스트 ===");
        int[] searchKeys = {30, 55, 80};
        for (int key : searchKeys) {
            System.out.println("검색 " + key + ": " + (bpt.search(key) ? "성공" : "실패"));
        }

        // 3. 범위 검색 테스트 (B+ 트리의 핵심 기능)
        System.out.println("\n=== [3] 범위 검색 테스트 (25 ~ 65) ===");
        // 리프 노드의 연결 리스트를 따라 순차적으로 읽어옵니다.
        System.out.println("결과: " + bpt.rangeSearch(25, 65));

        // 4. 삭제 테스트 (Underflow 발생 및 병합/빌려오기)
        System.out.println("\n=== [4] 삭제 및 트리 재조정 테스트 ===");

        System.out.println("삭제: 10 (리프에서 단순 삭제)");
        bpt.delete(10);
        System.out.println("현재 전체 데이터: " + bpt.rangeSearch(0, 100));

        System.out.println("삭제: 20 (Underflow 발생 -> 병합/빌리기 유도)");
        bpt.delete(20);
        System.out.println("현재 전체 데이터: " + bpt.rangeSearch(0, 100));

        System.out.println("삭제: 30 (가이드 키 업데이트 확인)");
        bpt.delete(30);
        System.out.println("현재 전체 데이터: " + bpt.rangeSearch(0, 100));
    }
}
