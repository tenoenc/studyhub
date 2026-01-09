#include <stdio.h>
#include <stdlib.h>

// 1. Data 영역 (초기화된 전역 변수)
int global_var = 10;

// Code 영역을 확인하기 위한 함수
void test_function() {}

int main() {
    // 2. Stack 영역 (지역 변수)
    int stack_var1 = 100;
    int stack_var2 = 200;

    // 3. Heap 영역 (동적 할당 변수)
    int *heap_var1 = (int *)malloc(sizeof(int));
    int *heap_var2 = (int *)malloc(sizeof(int));

    printf("=== Process Memory Layout ===\n");
    
    // Code 영역 (Text)
    printf("[Code ]  Function Addr : %p\n", (void *)test_function);
    
    // Data 영역
    printf("[Data ]  Global Var Addr: %p\n", (void *)&global_var);
    
    // Heap 영역 (성장 방향 확인)
    printf("[Heap ]  Heap Var 1 Addr: %p\n", (void *)heap_var1);
    printf("[Heap ]  Heap Var 2 Addr: %p (Upward growth)\n", (void *)heap_var2);
    
    // Stack 영역 (성장 방향 확인)
    printf("[Stack]  Stack Var 1 Addr: %p\n", (void *)&stack_var1);
    printf("[Stack]  Stack Var 2 Addr: %p (Downward growth)\n", (void *)&stack_var2);

    free(heap_var1);
    free(heap_var2);

    return 0;
}