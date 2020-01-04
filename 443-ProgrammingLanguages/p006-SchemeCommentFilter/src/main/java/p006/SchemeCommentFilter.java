// Alex Pendell
// Programming Languages
// SchemeCommentFilter
// March 7th, 2019

import java.io.*;
import java.util.*;

//package SchemeCommentFilter;

public class SchemeCommentFilter extends PushbackReader {
	

	public SchemeCommentFilter(Reader in) {
		super(in);
	}
	
	// read() is the primary function of this extension. It takes no parameters and will print all characters
	// that are not scheme-style commented. That is, anything after ';' character or a within a #| ... |# comment.
	// For debugging purposes, the ignored characters will be printed to a seperate text file.
	public int read() {

	    try {
			// echoint stores what super.read() returns, which is the character read as an int, OR -1 if we've reached the EoF.
			int echoint;
			while((echoint = super.read()) != -1 ) {
				
				// Convert from integer representation to character representation.
				char echochar = (char)echoint;
				
				/** STRING HANDLING **/
				// First and foremost, we need to handle strings, which are opened and closed by the double quote (") character.
				// Anything inside the string is "protected" from the eyes of the comment filter, and should be printed as normal.
				// If we see a ' " ' character, the string is considered "open" until we see one of two characters:
					// 1. We find another ' " ' character: The second ' " ' dilineates the end of the string. 
					// 2. We find a '\n' character: For the purposes of this assignment, strings will not span multiple lines.
				if (echochar == '"') {
					System.out.print(echochar);
					// If we have made it this far, we are inside a string.
					// The process here is to print characters until we see one of the the end-of-string characters
					// This while loop mirrors the big while loop that controls the entire read function.
					// If we find ourselves at the EoF, we're done.
					while((echoint = super.read()) != -1) {
						
						// Just as before, convert the integer representation of the character to something we care about.
						echochar = (char)echoint;
						
						// We're looking for the two cases mentioned earlier.
						// If we find either a ' " ' or a '\n' character, we break because the string has closed.
						if (echochar == '"' || echochar == '\n') {
                                break;
						 	
						// Here, the character we're examining is neither of the characters we're looking for, so we print.
						} else {
						         System.out.print(echochar);
						}
					}
				}
				
				/** SINGLE LINE COMMENT HANDLLING **/
				// There are a few conditions in which we begin consuming characters instead of printing them.
				// The first is the single line comment, represented by a ';' character.
				if (echochar == ';') {
					
					// Here, we handle single line comments.
					// We consume characters and throw them away until we come to the end of line character.
					// As before, we need to make sure that we don't reach the end-of-file.
					// However, we are also looking for the end of line character (\n);
					while ((echoint = super.read()) != -1) {
						// Just as before, convert the integer representation of the character to something we care about.
						echochar = (char)echoint;
						
						// Is it the end of line character?
						if (echoint == '\n') {
							//super.unread('\n');
							break;
						} 
					}
				}
				
				/** MULTILINE COMMENT HANDLING **/
				// The second is the multiline comment, opened by a '#|' and closed by a '|#'.
				// The problem here, is that there are two characters to open and close. So we need to check.
				// To check this, the process is to return the character back to the file, and then pull the next two chars into a buffer.
				// This will pull the '#' into buffer[0] and whatever the next character is into buffer[1]
				// At this point, the only thing we care about is what the second character is.
				// If it is a '|' character, we know to start consuming the characters until we see a '|#' delimiter.
				// Otherwise, if it is not a '|' character, it is not a comment and should be printed as normal.
				if (echochar == '#') {
					
					// We've come across a '#' character, there are two cases here:
						// 1. We find '|' character after the '#' and we begin consuming.
						// 2. We don't find a '|' character after the '#', and we ignore both, because it's not a comment.
						
					// First, put the character back.
					super.unread(echochar);
					// We need a buffer to load the characters into because we're examining two characters.
					char[] buffer = new char[2];
					
					// Call the pushback reader read() function, which will put the next two characters into an array for us to examine.
					super.read(buffer, 0, 2);
					
					// This is case 1. It IS a multiline comment, and we begin consuming until we find the correct delimeter.
					if (buffer[1] == '|') {
						
						// As before, if we find the end of the file, we need to terminate.
						while ((echoint = super.read()) != -1) {
							
							// Convert from int to character.
							echochar = (char)echoint;
							
							if (echochar == '\n') {
                                super.unread('\n');
								break;
							
							// If the next character is a '|', we need to do the opposite as before.
							// First, put the '|' back, then read them into a buffer for examination.
							} else if (echochar == '|') {
								super.unread(echochar);
								read(buffer, 0, 2);
								
								// If the next character is a '#' then we're done with the comment, and can continue printing.
								if (buffer[1] == '#') {
                                    
                                    // If the second character in the buffer is a '#', unread a space, and move on.
									echochar = ' ';
									
									break;
									
								// If the next characer is not a '#' character, then we're not out of the comment yet, and need to continue.
								} 
							}
						}
					}
				} 
                // If we have made it this far, the character is not ';' or '#' or the start of a string (or inside a string), so we print it normally.
            System.out.print(echochar);
			} 
		} catch (IOException e) {
		    System.out.println(e.getMessage());
		}
        return 0;
    }
}
