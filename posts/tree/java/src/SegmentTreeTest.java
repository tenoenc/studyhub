public class SegmentTreeTest {
    public static void main(String[] args) {
        // 1. 초기 배열 설정 (1부터 10까지의 숫자)
        long[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int n = arr.length;

        System.out.println("=== [1] 세그먼트 트리 빌드 ===");
        MySegmentTree st = new MySegmentTree(arr);
        System.out.println("배열: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");

        // 2. 초기 구간 합 쿼리
        // 0번부터 9번 인덱스까지 전체 합: 55
        System.out.println("\n=== [2] 초기 구간 합 탐색 ===");
        System.out.println("전체 구간(0~9) 합: " + st.queryRange(0, 9));
        // 2번부터 6번 인덱스까지 합: 3+4+5+6+7 = 25
        System.out.println("일부 구간(2~6) 합: " + st.queryRange(2, 6));

        // 3. 구간 업데이트 (Lazy Propagation 발생)
        // 3번부터 7번 인덱스까지 모든 원소에 +5 더하기
        // {1, 2, 3, (4+5), (5+5), (6+5), (7+5), (8+5), 9, 10}
        // = {1, 2, 3, 9, 10, 11, 12, 13, 9, 10}
        System.out.println("\n=== [3] 구간 업데이트 (3~7 구간에 +5 더하기) ===");
        st.updateRange(3, 7, 5);
        System.out.println("업데이트 완료.");

        // 4. 업데이트 후 쿼리 확인
        System.out.println("\n=== [4] 업데이트 후 결과 확인 ===");
        // 3~7 구간 합 변화: 원래 30 (4+5+6+7+8) -> 변경 후 55 (9+10+11+12+13)
        System.out.println("업데이트된 구간(3~7) 합: " + st.queryRange(3, 7));
        // 전체 구간 합 변화: 원래 55 -> 55 + (5개 원소 * 5) = 80
        System.out.println("업데이트 후 전체 구간(0~9) 합: " + st.queryRange(0, 9));

        // 5. 추가 업데이트 및 복합 쿼리
        System.out.println("\n=== [5] 추가 업데이트 (0~2 구간에 +10 더하기) ===");
        st.updateRange(0, 2, 10);
        // 0~2 구간 합: (1+10)+(2+10)+(3+10) = 33
        System.out.println("구간(0~2) 합: " + st.queryRange(0, 2));
        System.out.println("최종 전체 구간(0~9) 합: " + st.queryRange(0, 9));
    }
}
