/* Alex Pendell
 * CIS 303 - Operating Systems
 * p004 - Fixed-Size Record File
 * October 6th, 2019
 *
 * This is the main class for the 4th assignment: Fixed-Size Record File
 * 
 * OBJECTIVE (From the writeup): 
 *   Write a program that opens a database file in random-access mode
 *   The database file is a binary file that contains /course/ records. 
 *   When the is run, the name of the database file is provided on the command
 *   line. The program then loops, prompting the user and reading commands to
 *   create, find, edit, list, and delete courses. The program may NEVER have
 *   more than two (2) course structrues in memory at any time; the rest of the
 *   coursesremain out of the file.
 *
 * EXTRA NOTES (To keep in mind):
 *   Each course has a state (byte/char), record number (int), a dept (4 or
 *   fewer printable characters), and a then a str (22 char).
 */

#include <iostream>
#include <cstring>
#include <string>
#include <fstream>


/* The primary structure that we will write to the database file.
 * fields:
 *   present - a character that determines if the course has been deleted or not
 *   cRn - the course record number
 *   char* cDept - the char[] representation of cDeptString
 *   char* cTitle - the char[] representation of cTitleString
 */
struct Course {
  int cRn;
  char present;
  char cDept[5];
  char cTitle[21];
};


/* countRecords is a function to count the number of valid records in
 * a given database.
 * parameters:
 *   const char* filename - the name of the file that contains the records.
 * returns:
 *   int count - the total number of valid records within the file.
 */
int countRecords(const char* filename){
  std::ifstream ifile(filename, std::ios::binary);
  int count = 0;
  while(ifile.peek() != EOF) {
    Course course;
    ifile.read((char*)&course, sizeof(course));
    if(course.present == 'T'){
      std::cout << "Adding one.";
      count++;
    }
  }
  return count;
}


/* This is the basic help function. When called from main, it outputs
 * the basic commands that can be executed during the main loop.
 * it accepts no parameters, and has no return conditions.
 */
void help(const char* filename){
  // This is the help "menu" to be printed.
  std::cout << "FILE NAME: " << filename << std::endl;
  std::cout << "RECORDS IN FILE: " << countRecords(filename);
  std::cout << std::endl;
  std::cout << "Valid Commands: " << std::endl;
  std::cout << "\t -quit: exit the program." << std::endl;
  std::cout << "\t -add: add a course to the database." << std::endl;
  std::cout << "\t -edit: edit an existing database entry." << std::endl;
  std::cout << "\t -delete: delete a database entry." << std::endl;
  std::cout << "\t -find: find a database entrey." << std::endl;
  std::cout << "\t -list: list all records in database." << std::endl;
}


/* fileDelete will navigate through a binary database file and alter the 'present'
 * field of the course with a given cRn to an 'F' (signifying that the course has been
 * deleted).
 * parameters:
 *   const char* filename - the name of the database to be deleted from.
 */
void fileDelete(const char* filename) {
  int cRn;
  int count = 0;
  std::cout << "Enter the cRn of the course to be deleted: ";
  if(std::cin >> cRn) {
    std::fstream file(filename, std::ios::binary | std::ios::out | std::ios::in);
    while(file.peek() != EOF){
      Course course;
      file.read((char*)& course, sizeof(course));
      if(course.cRn == cRn && course.present == 'T'){
	// The byte we need to change to an F is going to be:
	// sizeof(Course) * count -> This will give us the course offset (since courses are 32).
	// The 5th byte of the course contains the present character.
	file.seekp((sizeof(Course)*count)+4);
	char byte = 'F';
	file.write((char*)&byte, sizeof(byte));
	file.close();	
	std::cout << "The course: " << course.cRn << " " << course.cDept << " ";
	std::cout << course.cTitle << " has been removed from the database." << std::endl;
	return;
      }
      count++;
    }
    std::cout << "No course exists with the given cRn." << std::endl;
    file.close();
  } else {
    std::cout << "Please retry with a valid cRn" << std::endl;
  }
}


/* add() will add a course to the database given.
 * parameters:
 *   const char* filename - the name of the database file we are adding to.
 * post-conditions:
 *   the database file will now contain the file added (given that the user
 *   inputted the data correctly).
 */
void add(const char* filename){
  
  // First we open the file in binary, as well as in append mode
  std::ofstream ofile(filename, std::ios::binary | std::ios::app);
  int cRn;
  std::string cDeptString;
  std::string cTitleString;

  std::cout << "Please enter the course cRn, dept, and title ";
  std::cout << "separated by spaces." << std::endl;

  if ((std::cin >> cRn) && (std::cin >> cDeptString) &&
      (getline(std::cin, cTitleString))) {
    size_t firstNonSpace = cTitleString.find_first_not_of(" ");
    if (firstNonSpace > 0){
      cTitleString.erase(0, firstNonSpace);
    }

    // This section opens the file for reading. This is necessary
    // because we don't want to the user to be able to add duplicate
    // cRn numbers into the databse. So first we scan the database
    // to ensure that the user has given us a unique cRn.
    bool exists = false;
    std::ifstream ifile(filename, std::ios::binary);
    while(ifile.peek() != EOF){
      Course course;
      ifile.read((char*)&course, sizeof(course));
      if(cRn == course.cRn && course.present == 'T'){
	exists = true;
	break;
      }
    }
    // Close the file for reading.
    ifile.close();
   
    // If the cRn is indeed unique, we can continue with adding the
    // database entry. First we create a course structure, then copy
    // the data stored at the address of the course to the file.
    if(!exists){
      Course course;
      course.cRn = cRn;
      course.present = 'T';
      // Copy into the structure the information given to use from the user.
      std::strncpy(course.cDept, cDeptString.c_str(), 5);
      std::strncpy(course.cTitle, cTitleString.c_str(), 21);
      course.cDept[4] = '\0';
      course.cTitle[20] = '\0';

      ofile.write((char*)&course, sizeof(course));
      std::cout << "The course: " << course.cRn << " " << course.cDept << " ";
      std::cout << course.cTitle << " has been added to the database." << std::endl;
      return;
    } else {
      std::cout << "Course already exists with that cRn." << std::endl;
      
    }
  } else {
    std::cout << "There was an error with that input. Try again." << std::endl;
  }
  ofile.close();
}


/* edit() will allow the user to edit the bytes that make up the database file.
 * parameters:
 *   const char* filename - the name of the database file we are editing within
 * postconditions:
 *   the database has been edited to contain the user-given information for the
 *   specified course.
 */
void edit(const char* filename){
  int cRn;
  std::cout << "Enter the cRn of the course you'd like to edit: ";
  if(std::cin >> cRn) {
    std::fstream file(filename, std::ios::binary | std::ios::out | std::ios::in);

    // This will be how many courses we need to get past to get to the one we want.
    int offset = 0;

    // Cycle though the file looking for the correct cRn. If it is not found, print an error.
    while(file.peek() != EOF){
      Course course;

      // Read the course into memory
      file.read((char*)& course, sizeof(course));
      
      if(course.cRn == cRn && course.present == 'T') {

	// Once the course with the corresponding cRn is found, and the present byte is 'T', the
	// user is then prompted for the information that they would like to change the information
	// to.
	std::cout << "Course found. Please enter the desired dept and title separated by spaces";
	std::cout << std::endl;

	// The following few lines are slightly modified versions of the code that was given to us
	// within document write up.
	std::string cDeptString;
	std::string cTitleString;

	if ((std::cin >> cDeptString) && (getline(std::cin, cTitleString))) {
	  size_t firstNonSpace = cTitleString.find_first_not_of(" ");
	  if (firstNonSpace > 0){
	    cTitleString.erase(0, firstNonSpace);
	  }

	  // Copy the strings into the correct data locations as C-style strings.
	  std::strcpy(course.cDept, cDeptString.c_str());
	  std::strcpy(course.cTitle, cTitleString.c_str());

	  // Using the offset, we can navigate to the correct byte location where the desired
	  // course is kept. From there, we can simply write the new course into that location
	  // within the binary data file.
	  file.seekp((sizeof(course)*offset));
	  file.write((char*)&course, sizeof(course));
	  file.close();
	  return;	
	}

      }
      offset++;
    }
    std::cout << "No course exists with the given cRn." << std::endl;
    file.close();  
  }
}

/* find() will prompt the user for a cRn and then traverse the database and locate a course
 * with the corresponding cRn.
 * parameters:
 *   const char* filename - the name of the database we are scanning for the cRn.
 */
void find(const char* filename){
  int cRn;
  std::cout << "Please enter a cRn: ";
  if(std::cin >> cRn){
    std::ifstream ifile(filename, std::ios::binary);
    while(ifile.peek() != EOF){
      Course course;
      ifile.read((char*)&course, sizeof(course));
      if (course.cRn == cRn){
	std::cout << course.cRn << " " << course.cDept << " ";
	std::cout << course.cTitle << std::endl;
	ifile.close();
	return;
      }
    }
    std::cout << "No courses found with that cRn" << std::endl;
    ifile.close();
  } else {
    std::cout << "Please try again and enter a valid cRn." << std::endl;
  }
}


/* list() is how we print all of the courses from the database to the terminal.
 * parameters:
 *   const char* filename - The file we are printing all of the courses from.
 */
void list(const char* filename){
  std::ifstream file(filename, std::ios::binary);
  while(file.peek() != EOF){
    Course course;
    file.read((char*)&course, sizeof(Course));
    // Only print the course to the terminal if the course is present in the database.
    if(course.present == 'T'){
      std::cout << (int)course.cRn;
      std::cout << " " << course.cDept;
      std::cout << " " << course.cTitle << std::endl;
    }
  }

  // Close the file.
  file.close();
}


/* main() is our main C++ function that handles input processing as well as the
 * interactive loop that allows to user to interface with the database. There are
 * a plethora of functions that the user can call while interfacing with the database.
 */
int main(int argc, char* argv[]){

  std::cout << "Size of a course: " << sizeof(Course) << std::endl;
  
  // The following conditionals are to check the argument count and ensure we
  // have the correct number of arguments when the program is executed.
  if (argc < 2) {
    std::cout << "Please specify a database file" << std::endl;
    return 1;
  } else if (argc > 2) {
    std::cout << "Too many arguments. Please specifying the correct file path.";
    std::cout << std::endl;
    return 2;
  }
  
  while(1) {
    // At this point, we haven't created or opened the file. That is handled
    // when something is actually read/written to/from the file.
    std::cout << argv[1] << "$ ";
    
    std::string command;
    std::cin >> command;
    
    if (command == "add") {
      add(argv[1]);
    } else if (command == "help"){
      help(argv[1]);
    } else if (command == "list") {
      list(argv[1]);
    } else if (command == "find") {
      find(argv[1]);
    } else if (command == "edit") {
      edit (argv[1]);
    } else if (command == "delete") {
      fileDelete(argv[1]);
    } else if (command == "quit") {
      return 0;
    } else {
      std::cout << command << " is not a valid command." << std::endl;
      std::cout << "Type 'help' for a list of commands." << std::endl;
    }
  }
  return 0;
}
