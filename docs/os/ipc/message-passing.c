#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/wait.h>

int main() {
    int pipe_fd[2]; // [0]: Read, [1]: Write
    pid_t pid;
    char buffer[100];

    // [핵심] 파이프 생성 (커널 내부 버퍼 생성)
    if (pipe(pipe_fd) == -1) { perror("pipe"); return 1; }

    pid = fork(); // 프로세스 복제

    if (pid > 0) { // 부모 프로세스 (Reader)
        close(pipe_fd[1]); // 쓰기 채널 닫기
        
        // [핵심] Data Read: 시스템 콜 발생 (Kernel Buffer -> User Buffer)
        read(pipe_fd[0], buffer, sizeof(buffer));
        printf("Parent received: %s\n", buffer);
        
        close(pipe_fd[0]);
        wait(NULL); // 자식 종료 대기
    } else { // 자식 프로세스 (Writer)
        close(pipe_fd[0]); // 읽기 채널 닫기
        
        const char *msg = "Hello from Child via Pipe!";
        // [핵심] Data Write: 시스템 콜 발생 (User Buffer -> Kernel Buffer)
        // 문맥 교환 발생
        write(pipe_fd[1], msg, strlen(msg) + 1);
        
        close(pipe_fd[1]);
    }
    return 0;
}