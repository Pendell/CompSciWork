#include <stdio.h>
/**
 * g - the magic function
 *
 * Your job is to draw the top two (2) activation records on the calling stack
 * (with addresses and names of the variables, the return address, and the FP)
 * the _FIRST_ time product is 7.
 */
int g(int m, int n) {
    // printf("g(%d, %d)\n", m, n);
    int product = m * n;

    if (m == 0) {
        return n + 1;
    }

    if (n == 0) {
        return g(m - 1, 1);
    }

    return g(m - 1, g(m, n - 1));
}

int main(int argc, char *argv[]) {
    int m = 2;
    int n = 3;
    printf("g(%d, %d) = %d", m, n, g(m, n));
    return 0;
}
