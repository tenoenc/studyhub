public class BinarySearchTreeTest {
    public static void main(String[] args) {
        MyBinarySearchTree<Integer> bst = new MyBinarySearchTree<>();

        int[] data = {50, 30, 70, 20, 40, 60, 80};
        for (int x : data) bst.insert(x);

        System.out.println("=== 트리 정보 ===");
        System.out.println("Size: " + bst.size());
        System.out.println("Height: " + bst.height());
        System.out.println("Max Value: " + bst.minValue());
        System.out.println("Min Value: " + bst.maxValue());

        System.out.println("\n=== 순회 결과 ===");
        bst.preorder();
        bst.inorder();
        bst.postorder();
        bst.levelOrder();

        System.out.println("\n=== 삭제 테스트 ===");
        System.out.println("40 삭제 (자식 없음)");
        bst.delete(40);
        bst.inorder();

        System.out.println("30 삭제 (자식 하나 - 20)");
        bst.delete(30);
        bst.inorder();

        System.out.println("50 삭제 (루트 노드이자 자식 둘)");
        bst.delete(50);
        bst.inorder();

        System.out.println("\n=== 탐색 테스트 ===");
        System.out.println("Contains 70? " + bst.contains(70));
        System.out.println("Contains 50? " + bst.contains(50));
    }
}
