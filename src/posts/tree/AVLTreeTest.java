package posts.tree;

public class AVLTreeTest {
    public static void main(String[] args) {
        AVLTree<Integer> avl = new AVLTree<>();

        /* 1. RR 회전 유도 (Right-Right Case)
         * 10 -> 20 -> 30 순서로 넣으면 오른쪽으로 치우친 편향 트리가 되지만,
         * AVL 트리는 20을 루트로 우회전하여 균형을 맞춥니다.
         */
        System.out.println("--- 10, 20, 30 삽입 (RR 회전 유도) ---");
        avl.insert(10);
        avl.insert(20);
        avl.insert(30);
        avl.printTree(); // 출력: 20(2) \n 10(1) 30(1)

        /* 2. 추가 삽입으로 더 복잡한 균형 잡기
         * 40, 50을 넣어 트리를 더 키워봅니다.
         */
        System.out.println("\n--- 40, 50 추가 삽입 ---");
        avl.insert(40);
        avl.insert(50);
        avl.printTree();

        /* 3. LR 회전 유도 (Left-Right Case)
         * 트리의 특정 지점에 불균형을 만드는 값을 삽입합니다.
         */
        System.out.println("\n--- 25 삽입 (LR 회전 유도) ---");
        avl.insert(25);
        avl.printTree();

        /* 4. 삭제 시 재균형 (Rebalance on Delete)
         * 노드를 삭제했을 때도 트리가 회전하며 균형을 유지하는지 확인합니다.
         */
        System.out.println("\n--- 40 삭제 (삭제 후 재균형 확인) ---");
        avl.delete(40);
        avl.printTree();

        System.out.println("\n--- 10 삭제 (루트 변경 가능성 확인) ---");
        avl.delete(10);
        avl.printTree();
    }
}
