package posts.jvm_memory_layout;

import org.openjdk.jol.info.ClassLayout;

/**
 * VTable(invokevirtual)과 ITable(invokeinterface)의 구조적 차이 분석
 */
public class InterfaceDispatchAnalysis {

    // 1. VTable 경로: 클래스 상속
    static class BaseClass {
        public void perform() { /* 상속을 통한 호출 */ }
    }
    static class VTableImpl extends BaseClass {
        @Override public void perform() { }
    }

    // 2. ITable 경로: 인터페이스 구현
    interface Action {
        void perform();
    }
    static class ITableImpl implements Action {
        @Override public void perform() { }
    }

    public static void main(String[] args) {
        VTableImpl vTableObj = new VTableImpl();
        ITableImpl iTableObj = new ITableImpl();

        System.out.println("=== 1. 인스턴스 레이아웃 비교 (Heap 영역) ===");
        // 두 객체 모두 동일한 헤더(Klass Word)를 가짐을 확인
        System.out.println("VTable Class Layout:\n" + ClassLayout.parseInstance(vTableObj).toPrintable());
        System.out.println("ITable Class Layout:\n" + ClassLayout.parseInstance(iTableObj).toPrintable());

        System.out.println("=== 2. 호출 메커니즘 추론 (Metaspace 영역) ===");

        // 시나리오 A: invokevirtual (VTable)
        // Klass Word -> VTable -> [Fixed Index] -> Method Address
        vTableObj.perform();
        System.out.println("[VTable] 직접 인덱싱을 통한 빠른 점프 가능");

        // 시나리오 B: invokeinterface (ITable)
        // Klass Word -> ITable -> [Search Interface Offset] -> [Method Index] -> Method Address
        Action action = iTableObj;
        action.perform();
        System.out.println("[ITable] 인터페이스 오프셋 탐색 단계가 추가로 필요");
    }
}