package posts.tree;

public class RedBlackTreeTest {
    public static void main(String[] args) {
        // 1. 트리 생성 (Integer 타입)
        RedBlackTree<Integer> rbt = new RedBlackTree<>();

        System.out.println("=== [1] 데이터 삽입 및 자동 균형 테스트 ===");
        /*
         * 10, 20, 30을 순서대로 넣으면 일반 BST는 일직선(편향)이 되지만,
         * Red-Black 트리는 좌회전(Left Rotate)을 통해 균형을 잡습니다.
         */
        int[] insertData = {10, 20, 30, 15, 25, 5, 1};
        for (int val : insertData) {
            System.out.println("삽입: " + val);
            rbt.insert(val);
        }

        System.out.print("\n삽입 완료 후 중위 순회 (오름차순 정렬 결과): ");
        rbt.printInorder(); // 1 5 10 15 20 25 30 순으로 출력되어야 함

        System.out.println("\n\n=== [2] 특정 노드 삭제 및 규칙 보정 테스트 ===");

        // Case 1: 리프 노드(1) 삭제 (가장 간단한 경우)
        System.out.println("삭제 시도: 1 (리프 노드)");
        rbt.delete(1);
        rbt.printInorder();

        // Case 2: 자식이 있는 노드(5) 삭제
        System.out.println("삭제 시도: 5 (자식이 있는 노드)");
        rbt.delete(5);
        rbt.printInorder();

        // Case 3: 루트 부근의 노드(20) 삭제 (복잡한 재배치 발생)
        System.out.println("삭제 시도: 20 (내부 노드)");
        rbt.delete(20);
        rbt.printInorder();

        System.out.println("\n=== [3] 최종 트리 상태 확인 ===");
        System.out.println("현재 트리에 25가 있는가? " + (rbt.contains(25) ? "예" : "아니오"));
        System.out.println("현재 트리에 20이 있는가? " + (rbt.contains(20) ? "예" : "아니오"));
    }
}
