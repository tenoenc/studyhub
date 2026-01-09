package docs.os.linkers_loaders;

import java.lang.reflect.Constructor;

public class DynamicLoadingTest {
    public static void main(String[] args) {
        System.out.println("=== 1. App Started (Plugin not loaded yet) ===");
        
        // 시나리오: 설정 파일이나 사용자 입력에서 클래스 이름을 문자열로 받음
        // 컴파일 타임에는 이 클래스의 존재를 모름 (import 안 함)
        // 편의상 내부 클래스를 사용하지만, 외부 .class 파일이어도 동일하게 동작함
        String className = "DynamicLoadingTest$DynamicPlugin"; 

        try {
            // [핵심] 이 시점에 JVM은 클래스 로더에게 해당 클래스를 찾아 메모리에 적재(Load)하라고 명령함
            System.out.println(">>> Loading class: " + className);
            Class<?> clazz = Class.forName(className);

            // 인스턴스 생성 (Reflection)
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object plugin = constructor.newInstance();

            System.out.println("=== 2. Class Loaded & Instantiated ===");
            System.out.println("Instance: " + plugin.toString());

        } catch (ClassNotFoundException e) {
            System.err.println("Class not found! (Check classpath)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 테스트를 위해 내부에 static 클래스로 정의
    // 하지만 main 메서드에서는 이 클래스를 직접 참조(new DynamicPlugin)하지 않음!
    public static class DynamicPlugin {
        public DynamicPlugin() {
            System.out.println("   [System] DynamicPlugin Constructor Called! (Memory Allocated)");
        }

        @Override
        public String toString() {
            return "I am a dynamically loaded plugin!";
        }
    }
}