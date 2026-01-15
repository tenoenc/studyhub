import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 다단계 상속에서의 필드 적층(Stacking)과 누적 패딩 실측
 */
public class MultiLevelInheritanceAnatomy {

    // 조부모 클래스 (GrandParent)
    static class Device {
        long serial; // 8 bytes
    }

    // 부모 클래스 (Parent)
    static class Computer extends Device {
        int cpu;     // 4 bytes
    }

    // 손자 클래스 (Child)
    static class Laptop extends Computer {
        short weight; // 2 bytes
    }

    public static void main(String[] args) {
        // 1. JVM 환경 정보 출력
        System.out.println(VM.current().details());
        System.out.println("----------------------------------\n");

        // 2. Laptop 인스턴스 생성
        Laptop laptop = new Laptop();

        // 3. 물리적 메모리 레이아웃 출력
        System.out.println("=== [Laptop 인스턴스(3단계 상속)의 메모리 동작 메커니즘 분석] ===");
        System.out.println(ClassLayout.parseInstance(laptop).toPrintable());
    }
}