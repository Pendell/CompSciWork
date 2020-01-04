/*  This is the implementation of the Shell.h
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

using namespace std;

int Shell::run(){

    cout << "Type something!" << endl;
    
    while (true) {
        // Create the string and vector which we will store input
        string input;
        vector<string> v;

        // Grab the entered line from the user and store in input
        getline(cin, input);

        // If the user typed 'exit', break out of the loop and return 0
        if (input == "exit") {
            break;

        } else {
            stringstream stream (input);
            while (stream >> input)
                v.push_back(input);
            for(unsigned int i = 0; i < v.size(); ++i){
                printf("word[%d] ", i);
                cout << v.at(i) << endl;
            }
        }
        
    }

    return 0;
}

