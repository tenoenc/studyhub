#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
    pid_t pid = fork();

    if (pid > 0) {
        // [부모] 30초 동안 아무것도 안 하고 대기 (wait 호출 안 함)
        printf("[Parent] I'm sleeping for 30s. Check 'ps -ef | grep defunct' now!\n");
        sleep(30);
        printf("[Parent] Waking up and exiting...\n");
    } 
    else if (pid == 0) {
        // [자식] 생성되자마자 바로 종료
        printf("[Child] I'm exiting now to become a zombie...\n");
        exit(0);
    }

    return 0;
}