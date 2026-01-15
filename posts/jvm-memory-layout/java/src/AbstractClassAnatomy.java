import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * [CODE_ID_05_7]
 * 추상 메서드의 VTable 슬롯 선점 및 자식 클래스의 오버라이딩 메커니즘
 */
public class AbstractClassAnatomy {

    abstract static class Graphic {
        int color = 0xFFFFFF; // 4B

        // 추상 메서드: VTable에 슬롯은 생성되지만 주소는 Stub을 가리킴
        abstract void draw();

        void info() { System.out.println("Graphic color: " + color); }
    }

    static class Circle extends Graphic {
        int radius = 10; // 4B

        @Override
        void draw() { System.out.println("Drawing Circle with radius " + radius); }
    }

    public static void main(String[] args) {
        Graphic circle = new Circle();

        System.out.println(VM.current().details());
        System.out.println("----------------------------------\n");

        // 1. 인스턴스 레이아웃 확인
        System.out.println("=== [Circle 인스턴스의 메모리 맵 (Heap)] ===");
        System.out.println(ClassLayout.parseInstance(circle).toPrintable());

        // 2. 메타데이터 정보 출력
        System.out.println("=== [클래스 설계도 정보] ===");
        System.out.println("Circle은 부모 Graphic의 VTable 구조를 상속받아 draw() 슬롯을 재채웁니다.");
    }
}