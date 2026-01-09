#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <unistd.h>
#include <fcntl.h>

int main() {
    const char *filepath = "shared_data.bin";
    const char *message = "Hello, Shared Memory! (Direct Access via Pointer)";
    
    // 파일 열기 (공유 메모리 객체 역할)
    int fd = open(filepath, O_RDWR | O_CREAT, 0666);
    if (fd == -1) { perror("open"); return 1; }
    
    // 파일 크기 확보 (메모리 공간 확보)
    ftruncate(fd, 1024);

    // [핵심] mmap: 파일을 프로세스의 메모리 주소에 매핑 (System Call)
    char *shared_memory = mmap(NULL, 1024, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    close(fd); // 매핑 후에는 파일 디스크립터가 필요 없음

    printf("Memory Mapped at address: %p\n", shared_memory);

    // [핵심] Data Write: 시스템 콜(write) 없이, 순수 메모리 연산으로 데이터 씀
    // 커널 모드로 전환되지 않음 -> 고성능
    memcpy(shared_memory, message, strlen(message) + 1);
    
    printf("Data written to memory: %s\n", shared_memory);

    munmap(shared_memory, 1024); // 메모리 해제
    return 0;
}