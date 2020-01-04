/* Alex Pendell
 * p002 - Very Simple Shell -- Phase B
 * Dr. Brian Ladd - Operating Systems
 * September 3rd, 2019
 *
 * This is the second part of the first project -- VSSH Phase B. The difference from
 * the first assignment is that this executes a replica of BASH.
 * 
 * How it works:
 * main.cpp simply creates a shell object, and executes the run() function of
 * shell. From there, run() prompts the user input for a string, and stores that
 * into a line. A vector is then created and each token of the string is
 * push_back()'d into the vector. A vector is used because of it's dynamic size.
 * That is, we don't know how much the user will type, so we can't use something
 * that uses a static size. Once the input is tokenized and into the vector,
 * we fork() and exec() the appropriate functions and executables.
 */



#include "../parser/Shell.cpp"

int main() {
    Shell shell;
    shell.run();
    return 0;
}
