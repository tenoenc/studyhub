#include <stdio.h>

int main() {
    printf("Attempting to write to kernel memory (0x0)...\n");

    // 1. 포인터를 강제로 0번지(NULL)로 설정
    // 0번지는 통상적으로 운영체제 커널을 위해 예약되어 있거나,
    // 오류 검출을 위해 접근이 차단된(No Access) 영역입니다.
    int *forbidden_addr = (int *)0x0;

    // 2. 해당 주소에 값 쓰기 시도 (Dereference)
    // 이 시점에 하드웨어(MMU)가 접근 위반을 감지하고 Trap을 발생시킵니다.
    *forbidden_addr = 100; 

    // 프로세스는 위 라인에서 강제 종료되므로, 이 메시지는 출력되지 않습니다.
    printf("This line will never be printed.\n");

    return 0;
}