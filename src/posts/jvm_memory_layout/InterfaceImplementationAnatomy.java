package posts.jvm_memory_layout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 인터페이스 구현에 따른 인스턴스 레이아웃 유지 및 ITable 간접 참조 분석
 */
public class InterfaceImplementationAnatomy {

    // 인터페이스 정의 (능력 부여)
    interface Flyable {
        void fly();
    }

    // 인터페이스 구현 클래스 (상태 + 구현)
    static class Drone implements Flyable {
        int battery = 100;

        @Override
        public void fly() {
            System.out.println("Drone is flying... Battery: " + battery + "%");
        }
    }

    public static void main(String[] args) {
        // 1. JVM 환경 정보
        System.out.println(VM.current().details());
        System.out.println("----------------------------------\n");

        // 2. 인터페이스 타입으로 인스턴스 참조
        Flyable drone = new Drone();

        // 3. 인스턴스 레이아웃 출력 (Heap 영역 확인)
        System.out.println("=== [Drone 인스턴스의 메모리 맵 (Heap)] ===");
        System.out.println(ClassLayout.parseInstance(drone).toPrintable());

        // 4. 인터페이스 메서드 호출 (invokeinterface 동작 시뮬레이션)
        System.out.print("drone.fly() 호출 결과: ");
        drone.fly();

        // 5. Klass Word가 가리키는 Metaspace 물리 주소 추출
        // JOL 출력에서 본 VALUE는 '압축된 별명'입니다.
        // 아래 코드는 JVM이 그 별명을 해독(Decoding)하여 찾아낸 '진짜 64비트 절대 주소'를 가져옵니다.
        long klassPointer = VM.current().addressOf(drone.getClass());

        System.out.println("\n=== [ITable 탐색의 시작점: 물리 주소 및 영역 해독] ===");
        System.out.printf("1. Drone 인스턴스 주소 (Heap 영역)      : 0x%x%n", VM.current().addressOf(drone));
        System.out.printf("2. Drone 클래스 메타데이터 주소 (Metaspace) : 0x%x%n", klassPointer);
    }
}