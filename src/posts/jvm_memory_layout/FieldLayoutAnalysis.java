package posts.jvm_memory_layout;

import org.openjdk.jol.info.ClassLayout;

/**
 * JVM의 필드 재배치(Field Reordering)와 패딩(Padding) 관찰
 * * 포인트: JVM은 선언 순서를 무시하고 '헤더 빈틈 메우기'와 '정렬'을 우선합니다.
 */
public class FieldLayoutAnalysis {

    // 최적화가 없다면 타입 사이사이에 막대한 패딩이 발생해야 함
    static class SuboptimalLayout {
        // [개발자의 선언 순서]
        boolean a; // 1 byte
        long b;    // 8 bytes
        int c;     // 4 bytes
        byte d;    // 1 byte
        Object e;  // 4 bytes (Compressed OOPs 적용 시)
    }

    public static void main(String[] args) {
        System.out.println("=== [SuboptimalLayout 클래스의 물리적 배치 분석] ===");

        // 클래스의 레이아웃 정보 출력
        System.out.println(ClassLayout.parseClass(SuboptimalLayout.class).toPrintable());

        /*
         * 관전 포인트:
         * 1. Reordering: 선언 순서(a, b, c, d, e)가 유지되는가,
         * 아니면 헤더가 끝나는 지점의 '자투리 공간'을 먼저 채우는가?
         * 2. Internal Padding: 필드 사이를 메우는 빈 공간이 있는가?
         * 3. External Padding (Loss): 전체 크기를 8의 배수로 맞추기 위해
         * 마지막에 추가된 공간이 얼마인가?
         */
    }
}