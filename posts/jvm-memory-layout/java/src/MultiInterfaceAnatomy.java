import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.io.IOException;

/**
 * 다중 인터페이스 구현 시의 ITable 확장 및 영역 격리 메커니즘 실측
 */
public class MultiInterfaceAnatomy {

    interface Phone { void call(); }
    interface Camera { void takePhoto(); }

    // 두 인터페이스를 동시 구현
    static class SmartPhone implements Phone, Camera {
        String modelName = "Galaxy S26"; // Reference (4B with Compressed OOPs)

        @Override public void call() { System.out.println("Calling..."); }
        @Override public void takePhoto() { System.out.println("Taking photo..."); }
    }

    public static void main(String[] args) {
        System.out.println(VM.current().details());
        System.out.println("----------------------------------\n");

        SmartPhone myPhone = new SmartPhone();

        // 1. 인스턴스 레이아웃 출력 (Heap 영역)
        System.out.println("=== [SmartPhone 인스턴스의 메모리 맵 (Heap)] ===");
        System.out.println(ClassLayout.parseInstance(myPhone).toPrintable());

        // 2. 다중 인터페이스 설계도(Klass) 주소 추출
        long klassPointer = VM.current().addressOf(myPhone.getClass());
        System.out.println("=== [ITable 다중 엔트리의 시작점: 메타데이터 주소] ===");
        System.out.printf("Instance Address  : 0x%x%n", VM.current().addressOf(myPhone));
        System.out.printf("Klass Address     : 0x%x (이곳에 Phone/Camera ITable이 위치)%n", klassPointer);

        // 3. 다형적 호출 실행
        Phone p = myPhone;
        Camera c = myPhone;
        p.call();
        c.takePhoto();

        System.out.println("\n[대기 중] 엔터를 누르면 종료됩니다. 이 상태에서 jhsdb를 실행하세요.");
        try {
            System.in.read(); // 프로세스 유지용
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}