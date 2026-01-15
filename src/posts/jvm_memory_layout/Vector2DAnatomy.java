package posts.jvm_memory_layout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 단일 클래스(Vector2D)의 기본 레이아웃과 헤더/필드/패딩의 조화
 */
public class Vector2DAnatomy {

    // 2차원 좌표를 나타내는 원자적 클래스
    static class Vector2D {
        int x; // 4 bytes
        int y; // 4 bytes
    }

    public static void main(String[] args) {
        // 1. JVM 환경 확인 (Compressed OOPs 활성화 여부 등)
        System.out.println(VM.current().details());
        System.out.println("--------------------------------------------------\n");

        // 2. Vector2D 인스턴스 생성
        Vector2D vector = new Vector2D();

        // 3. 물리적 메모리 레이아웃 출력
        System.out.println("=== [Vector2D 인스턴스의 메모리 동작 메커니즘 분석] ===");
        System.out.println(ClassLayout.parseInstance(vector).toPrintable());
    }
}