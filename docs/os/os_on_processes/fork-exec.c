#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main() {
    pid_t pid;

    // 1. 새로운 프로세스 복제 시도
    pid = fork();

    if (pid < 0) { // 에러 처리
        fprintf(stderr, "Fork Failed");
        return 1;
    } 
    else if (pid == 0) { // [자식 프로세스 영역]
        printf("[Child] I am the child. PID: %d, Parent PID: %d\n", getpid(), getppid());
        printf("[Child] I'm going to transform into 'ls -l' command...\n");
        
        // 2. 다른 프로그램(ls)으로 실행 이미지를 덮어씌움 (exec)
        execlp("/bin/ls", "ls", "-l", NULL);
        
        // exec 성공 시 아래 코드는 절대 실행되지 않음
        printf("This should not be printed if exec succeeds.\n");
    } 
    else { // [부모 프로세스 영역]
        printf("[Parent] I am the parent. Child PID is %d\n", pid);
        
        // 자식이 종료될 때까지 대기 (동기화)
        wait(NULL);
        printf("[Parent] Child has finished its execution.\n");
    }

    return 0;
}