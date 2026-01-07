import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    // 사용자 정보를 담는 ThreadLocal
    private static final ThreadLocal<String> userContext = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        // 단일 스레드를 사용하는 풀 생성 (오염 재현을 위해)
        ExecutorService executor = Executors.newFixedThreadPool(1);

        // 1. 첫 번째 작업: 사용자 A의 요청 처리
        executor.submit(() -> {
            processRequest("A");
            // 주의: 여기서 remove()를 호출하지 않고 작업을 끝냄
        });

        Thread.sleep(1000); // 작업 순서 보장을 위한 대기

        // 2. 두 번째 작업: 사용자 B의 요청 처리
        executor.submit(() -> {
            System.out.println("\n--- Task 2 실행 ---");
            System.out.println("현재 스레드: " + Thread.currentThread().getName());

            // 기대값은 null이어야 하지만, 이전 작업의 데이터가 남아있음 (오염)
            String currentUser = userContext.get();
            if (currentUser != null) {
                System.err.println("❗보안 사고 발생: 이전 사용자의 데이터가 노출됨 -> " + currentUser);
            } else {
                System.out.println("안전함: 컨텍스트가 비어있음");
            }
        });

        executor.shutdown();
    }

    public static void processRequest(String userId) {
        try {
            userContext.set(userId);
            executeBusinessLogic();
        } finally {
            // 작업이 성공하든 실패하든, 스레드가 풀로 돌아가기 전에 반드시 제거
            userContext.remove();
        }
    }

    private static void executeBusinessLogic() {
        System.out.println("--- Task 1 실행 ---");
        System.out.println("현재 스레드: " + Thread.currentThread().getName());
        System.out.println("설정된 사용자: " + userContext.get());
    }
}
