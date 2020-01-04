#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>

// gets(charBuffer)
// this code _might_ be unsafe.
char * gets(char *buffer) {
    fgets(buffer, 1024, stdin);
    return buffer;
}

void h() {
    // <YOUR CODE HERE>
    char buf[20] = "";
    volatile int i = -1;

    while (i != 0x40414243) {
	printf ("Enter a string: ");
	scanf ("%s", buf);
	printf ("You entered: %s\n", buf);
	
    }
    
    printf("Congratulations!");
}

int main(int argc, char *argv[]) {
    // <A PRINTF GOES HERE>
    printf("@ABC\n");
    h();
    return 0;
}
