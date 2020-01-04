/* Alex Pendell
 * p002 - Very Simple Shell -- Phase B
 * Dr. Brian Ladd - Operating Systems
 * September 3rd, 2019
 *  This is the implementation of the Shell.h
 *  The only notable thing in this file is run().
 * 
 *  run() doesn't take any parameters. It simply
 *  prompts for user input, and then parses the 
 *  input string into a vector, then prints them
 *  back out in the order they were typed and how
 *  they appear in the vector.
 */

#include "Shell.h"
#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <experimental/filesystem>
#include <fstream>
#include <sys/types.h>
#include <errno.h>
#include <sys/wait.h>
#include <unistd.h>

using namespace std;


// exists() simply searches for the file and returns whether or not the file exists.
// string file - the string representation of the file we're searching for.
bool exists(string file){
    ifstream filestream (file);
    return filestream.good();
}

/*
This is the primary function of the Shell object. This prompts the user to type
into a virtual bash.

While looping, it loads the user input into a vector of strings so that we can parse the
line into an instruction followed by the parameters needed to execute said function.

*/
int Shell::run(){

    cout << "Type something!" << endl;
    
    while (true) {
        // Create the string and vector which we will store input
        string input;
        vector<string> v;
        string file = experimental::filesystem::current_path();
        cout << file << "$ ";

        // Grab the entered line from the user and store in input
        getline(cin, input);

        // If the user typed 'exit', break out of the loop and return 0
        if (input == "exit") {
            break;
        } else {
            stringstream stream (input);
            while (stream >> input)
	      v.push_back(input);

	    // Create an array representation of the strings which are the
	    //   potential parameters of the program to be ran.
             char **  parameters = new char*[v.size()+1];

	    // Fill in the char ** array.
	    for (unsigned int i = 1; i < v.size()-1; ++i){
	      parameters[i] = const_cast<char *>(v.at(i).c_str());
	    }
	    // Then add the sentinel character.
	    parameters[v.size()] = NULL;
	    
            if (v.at(0) == "cd") {
                experimental::filesystem::current_path(v.at(1));
            } else if ((v.at(0).at(0) == '/' || v.at(0).at(0) == '.')) {
		if (exists(v.at(0))) {
		  pid_t pid = fork();
		  if (pid == 0) { // We're the child
		    execv(v.at(0).c_str(), parameters);
		    continue;
		  }
		}
	    // This next chunk of else-ifs go through each one of the locations specified
	    //   in the handout in the specified order.
	    } else if (exists("/usr/local/bin/" + v.at(0))){
	      pid_t pid = fork();
	      
	      if (pid == 0){
	        string dir = "/usr/local/bin/" + v.at(0);
		execv(dir.c_str(), parameters);
		continue;
	      }

	    } else if (exists("/usr/bin/"+v.at(0))) {
	      pid_t pid = fork();
	      
	      if(pid == 0) {
		string dir = "/usr/bin/" + v.at(0);
		execv(dir.c_str(), parameters);
		continue;
	      }
	    } else if (exists("/bin/"+v.at(0))){
	      pid_t pid = fork();
	      if (pid == 0){
		string dir = "/bin/" + v.at(0);
		execv(dir.c_str(), parameters);
		continue;
	      }
	    } else {
	      cout << "File Not Found." << endl;
	    }

	    
     	
        }
        
    }

    return 0;
}


