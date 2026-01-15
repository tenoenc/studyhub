import org.openjdk.jol.info.ClassLayout;

/**
 * JVM 객체 레이아웃 및 락 상태 변화 관찰
 */
public class ObjectLayoutAnalysis {

    public static void main(String[] args) {
        // 1. 일반 객체(Object)의 헤더 구조 분석 (Mark Word + Klass Word)
        Object plainObject = new Object();
        System.out.println("=== 1. 일반 객체의 레이아웃 (12-16 바이트) ===");
        System.out.println(ClassLayout.parseInstance(plainObject).toPrintable());

        // 2. 배열 객체의 헤더 확장 (Array Length 필드 추가)
        int[] intArray = new int[0];
        System.out.println("=== 2. 배열 객체의 레이아웃 (Array Length 포함) ===");
        System.out.println(ClassLayout.parseInstance(intArray).toPrintable());

        // 3. Mark Word의 동적 변화 관찰
        Object lockObject = new Object();

        // [상태 A] 초기 상태 (Unlocked)
        System.out.println("=== 3-A. 초기 상태의 Mark Word ===");
        System.out.println(ClassLayout.parseInstance(lockObject).toPrintable());

        // [상태 B] Identity HashCode 생성 (Mark Word에 해시코드 기록)
        lockObject.hashCode();
        System.out.println("=== 3-B. hashCode() 호출 후 (비트 패턴 변화) ===");
        System.out.println(ClassLayout.parseInstance(lockObject).toPrintable());

        // [상태 C] 락 경합 발생 (Lock Information 기록)
        synchronized (lockObject) {
            System.out.println("=== 3-C. synchronized 블록 내부 (Lock 상태) ===");
            System.out.println(ClassLayout.parseInstance(lockObject).toPrintable());
        }
    }
}