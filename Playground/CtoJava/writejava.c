/*
* Alex Pendell
* March 25, 2019
* writejava.c - A C program that writes and compiles java
*/


/* This is a simple program that opens a .java file called "HelloWorld.java",
 * writes a HelloWorld java program to that file, then compiles the program,
 * and finally executes the program as if it was a .java file. */

#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

/* write_program() either: Opens a "HelloWorld.java" file, or creates a new one
 * if one cannot be found. After that, a series of char*'s are created that
 * contain the necessary sequence of characters to create a java file.
 * The strings are all separate to maintain readability, instead of haveing a
 * single monolithic sequence of characters. Each char* is then written to the
 * .java file, and the file is then closed.
 * @post-condition: A file now exists in the cwd named "HelloWorld.java" and
 *                  contains the necessary code to be compiled and executed. */
void write_program() {
  printf("Writing...\n");
  FILE* java_file = fopen("HelloWorld.java", "w");

  char* class_declare = "public class HelloWorld {\n";
  char* main_declare = "\tpublic static void main(String[] args) {\n";
  char* main_println = "\t\tSystem.out.println(\"Hello world!\");System.exit(7);\n";
  char* main_close = "\t}\n";
  char* class_close = "}\n";

  fprintf(java_file, "%s%s%s%s%s", class_declare, main_declare, main_println,
            main_close, class_close);

  fclose(java_file);
}

/* compile_and_execute() takes no parameters. It forks() a new process and calls
 * execl().
 *
 * The first execl() takes 4 arguments and invokes javac.
 * @arg1: the directory where javac lives: /usr/bin/javac
 * @arg2: Any additional options for java compiling. "NULL" here.
 * @arg3: the file to be compiled by javac
 * @arg4: a NULL character so we can tell execl that we're done with args
 *
 * The parent process will wait for the child process to finish before continuing
 * execution (otherwise, it would try to execute the java program before it's
 * compiled). Once the child process has returned, the parent will then call the
 * second execl().
 *
 * The second execl() is similar to the first one, except we are invoking java
 * instead of javac.
 * The only change is the 3rd parameter, we need to identify which .class file
 * to be executed, and specify it without the extension.*/
void compile_and_execute(){
  pid_t pid = fork();
  if (pid == -1) { // An error occured
    printf("Can't fork, there was an error... \n");
    exit(EXIT_FAILURE);
  } else if (pid == 0){ // We are the child
    printf("Compiling...\n");
    execl("/usr/bin/javac", "NULL", "HelloWorld.java", NULL);
  } else { // We're the parent
    waitpid(pid, 0, 0);
    printf("Executing...\n");
    execl("/usr/bin/java", "NULL", "HelloWorld", NULL);
    printf("Hello");
  }
}

int main() {
  write_program();
  compile_and_execute();
  return 0;
}
