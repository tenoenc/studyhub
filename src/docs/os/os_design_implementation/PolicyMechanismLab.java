package docs.os.os_design_implementation;

import java.util.*;

// 1. Policy Interface (무엇을 할 것인가?)
interface SchedulingPolicy {
    void sort(List<Task> tasks);
}

// 2. Concrete Policies (다양한 정책들)
class FifoPolicy implements SchedulingPolicy {
    public void sort(List<Task> tasks) { /* 그대로 둠 */ }
}

class PriorityPolicy implements SchedulingPolicy {
    public void sort(List<Task> tasks) {
        tasks.sort(Comparator.comparingInt(t -> t.priority));
    }
}

// 3. Mechanism (어떻게 실행할 것인가? - 변하지 않는 부분)
class TaskExecutor {
    private SchedulingPolicy policy;

    public void setPolicy(SchedulingPolicy policy) {
        this.policy = policy;
    }

    public void runTasks(List<Task> tasks) {
        System.out.println("\n[System] Applying Policy: " + policy.getClass().getSimpleName());
        policy.sort(tasks); // 정책에 따라 순서 결정
        
        for (Task t : tasks) {
            System.out.println("Executing: " + t.name);
        }
    }
}

class Task {
    String name;
    int priority;
    Task(String name, int priority) { this.name = name; this.priority = priority; }
}

public class PolicyMechanismLab {
    public static void main(String[] args) {
        List<Task> tasks = new ArrayList<>(Arrays.asList(
            new Task("Low-Priority-Task", 10),
            new Task("High-Priority-Task", 1)
        ));

        TaskExecutor executor = new TaskExecutor();

        // 정책 1 적용 (FIFO)
        executor.setPolicy(new FifoPolicy());
        executor.runTasks(tasks);

        // 정책 2 적용 (Priority) - 메커니즘 코드는 수정 없이 정책만 교체
        executor.setPolicy(new PriorityPolicy());
        executor.runTasks(tasks);
    }
}