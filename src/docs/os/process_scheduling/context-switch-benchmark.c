#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <pthread.h>
#include <sys/wait.h>

#define ITERATIONS 50000

// --- [Thread Context Switch Test] ---
int pipe_t[2];
void* thread_func(void* arg) {
    char c = 't';
    for (int i = 0; i < ITERATIONS; i++) {
        write(pipe_t[1], &c, 1);
        read(pipe_t[0], &c, 1);
    }
    return NULL;
}

// --- [Process Context Switch Test] ---
void process_test() {
    int pipe_p[2];
    pipe(pipe_p);
    pid_t pid = fork();

    if (pid == 0) { // 자식
        char c = 'p';
        for (int i = 0; i < ITERATIONS; i++) {
            write(pipe_p[1], &c, 1);
            read(pipe_p[0], &c, 1);
        }
        exit(0);
    } else { // 부모
        char c = 'p';
        clock_t start = clock();
        for (int i = 0; i < ITERATIONS; i++) {
            read(pipe_p[0], &c, 1);
            write(pipe_p[1], &c, 1);
        }
        wait(NULL);
        clock_t end = clock();
        printf("[Process] Avg Context Switch: %.3f us\n", (double)(end - start) / ITERATIONS);
    }
}

int main() {
    // 1. Thread Test
    pipe(pipe_t);
    pthread_t t;
    pthread_create(&t, NULL, thread_func, NULL);
    
    char c = 't';
    clock_t start = clock();
    for (int i = 0; i < ITERATIONS; i++) {
        read(pipe_t[0], &c, 1);
        write(pipe_t[1], &c, 1);
    }
    pthread_join(t, NULL);
    clock_t end = clock();
    printf("[Thread ] Avg Context Switch: %.3f us\n", (double)(end - start) / ITERATIONS);

    // 2. Process Test
    process_test();

    return 0;
}