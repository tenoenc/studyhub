import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 필드 쉐도잉(Field Shadowing)에 의한 물리적 메모리 공존 및 정적 바인딩 실측
 */
public class FieldShadowingAnatomy {

    static class Account {
        int bonus = 100; // 부모의 보너스
    }

    static class VIPAccount extends Account {
        int bonus = 500; // 자식의 보너스 (Shadowing)
    }

    public static void main(String[] args) {
        System.out.println(VM.current().details());
        System.out.println("----------------------------------\n");

        VIPAccount vip = new VIPAccount();
        Account accountRef = vip; // 업캐스팅

        // 1. 참조 타입별 필드 접근 결과 (정적 바인딩 확인)
        System.out.println("=== [참조 타입에 따른 필드 접근 결과] ===");
        System.out.println("VIPAccount ref bonus: " + vip.bonus);        // 자식 필드 접근
        System.out.println("Account ref bonus:    " + accountRef.bonus); // 부모 필드 접근
        System.out.println();

        // 2. 물리적 메모리 레이아웃 출력
        System.out.println("=== [VIPAccount 인스턴스의 메모리 맵 (Field Shadowing)] ===");
        System.out.println(ClassLayout.parseInstance(vip).toPrintable());
    }
}