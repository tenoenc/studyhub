package posts.jvm_memory_layout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 상속 구조에서의 필드 선형 확장과 오프셋 고정 원리 실측
 */
public class InheritanceLayoutAnatomy {

    // 부모 클래스: 고유 ID를 가짐
    static class BaseEntity {
        int id; // 4 bytes
    }

    // 자식 클래스: 부모를 상속받고 나이 정보를 추가
    static class User extends BaseEntity {
        int age; // 4 bytes
    }

    public static void main(String[] args) {
        // 1. JVM 환경 정보 출력
        System.out.println(VM.current().details());
        System.out.println("--------------------------------------------------\n");

        // 2. User 인스턴스 생성
        User user = new User();

        // 3. 물리적 메모리 레이아웃 출력
        System.out.println("=== [User 인스턴스(상속 구조)의 메모리 동작 메커니즘 분석] ===");
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }
}