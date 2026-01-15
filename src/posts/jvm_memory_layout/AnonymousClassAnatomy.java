package posts.jvm_memory_layout;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * 익명 클래스의 hidden 필드(this$0) 및 변수 캡처로 인한 레이아웃 변화 실측
 */
public class AnonymousClassAnatomy {

    static class Button {
        String name = "SubmitButton";

        public Runnable getListener() {
            // 로컬 변수 캡처 대상 (상수 최적화 방지)
            final String action = "CLICK".toLowerCase() + System.currentTimeMillis();

            // 익명 클래스 생성: 외부의 name과 action을 참조함
            return new Runnable() {
                @Override
                public void run() {
                    System.out.println(name + " performed " + action);
                }
            };
        }
    }

    public static void main(String[] args) {
        Button btn = new Button();
        Runnable listener = btn.getListener();

        System.out.println(VM.current().details());
        System.out.println("----------------------------------\n");

        // 1. 익명 클래스 인스턴스의 레이아웃 출력
        System.out.println("=== [익명 Runnable 객체의 메모리 맵 (Heap)] ===");
        System.out.println(ClassLayout.parseInstance(listener).toPrintable());

        // 2. 외부 클래스 참조 필드(this$0) 존재 확인 안내
        System.out.println("레이아웃 내부의 'this$0'는 Button 인스턴스를, ");
        System.out.println("'val$action'은 로컬 변수 action을 가리키는 숨겨진 필드입니다.");
    }
}