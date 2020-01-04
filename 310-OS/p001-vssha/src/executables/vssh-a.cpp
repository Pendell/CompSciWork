/* Alex Pendell
 * p001 - Very Simple Shell -- Phase A
 * Dr. Brian Ladd - Operating Systems
 * September 3rd, 2019
 *
 * This is the first of a two part assignment. The task was simply to prompt
 * the user for input, and then echo the input in the format that's akin
 * to a printed array. This is important for practice, as well as beginning
 * to understand how the command line handles input from the user in bash.
 * 
 * How it works:
 * main.cpp simply creates a shell object, and executes the run() function of
 * shell. From there, run() prompts the user input for a string, and stores that
 * into a line. A vector is then created and each token of the string is
 * push_back()'d into the vector. A vector is used because of it's dynamic size.
 * That is, we don't know how much the user will type, so we can't use something
 * that uses a static size. Once the input is tokenized and into the vector,
 * we simply iterate through the vector and print each entry in the vector.
 * Once here, the program has succesfully run it's cycle and will repeat or 
 * terminate based on user input.  
 */



#include "../parser/Shell.cpp"

int main(){
    Shell shell;
    shell.run();
    return 0;
}
