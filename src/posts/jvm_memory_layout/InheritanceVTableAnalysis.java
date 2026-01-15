package posts.jvm_memory_layout;

import org.openjdk.jol.info.ClassLayout;

/**
 * 상속 관계에서의 필드 선형 확장 및 vtable 논리 구조 시뮬레이션
 */
public class InheritanceVTableAnalysis {

    static class A {
        long aField;
        void m1() { System.out.println("A.m1()"); }
        void m2() { System.out.println("A.m2()"); }
    }

    static class B extends A {
        int bField;
        @Override
        void m1() { System.out.println("B.m1() - Overridden"); }
        void m3() { System.out.println("B.m3()"); }
    }

    static class C extends B {
        byte cField;
        @Override
        void m3() { System.out.println("C.m3() - Overridden"); }
        void m4() { System.out.println("C.m4()"); }
    }

    public static void main(String[] args) {
        C instanceC = new C();

        // 1. 물리적 필드 배치 확인 (선형 확장)
        System.out.println("=== [Class C의 메모리 레이아웃: 부모 필드부터 쌓이는 구조] ===");
        System.out.println(ClassLayout.parseInstance(instanceC).toPrintable());

        // 2. 다형성 호출 시뮬레이션 (vtable 기반)
        System.out.println("=== [다형성 호출 시 런타임 메서드 바인딩] ===");
        A polyA = instanceC; // C 인스턴스를 A 타입으로 참조

        System.out.print("polyA.m1() 호출 결과: ");
        polyA.m1(); // vtable 인덱스 0번(예시) 추적 -> B.m1() 실행

        System.out.print("polyA.m2() 호출 결과: ");
        polyA.m2(); // vtable 인덱스 1번(예시) 추적 -> A.m2() 실행 (부모 것 유지)
    }
}