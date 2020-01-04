/* Alex Pendell
 * p007 - Scheme Tokenizer
 * Dr. Ladd - Programming Languages
 * April 1st, 2019
 */

import java.io.*;
import java.util.*;

/* The Program */

public class Tokenizer /*implements Iterable*/ {

    Reader reader1;

    public void Tokenizer() throws FileNotFoundException {
	reader1 = new BufferedReader(new FileReader("filtered.scheme"));
    }

    // We need: <string>, <integer>, <symbol>, <eof>, <error>, ( ) . ` ' , @
    public void tokenize() throws IOException {
	Map<String, String> tokenMap = new HashMap<String,String>() {{
		put("(", "Left Paren");
		put(")", "Right paren");
		put(".", "Period");
		put("`", "Tick");
		put("'", "Single Quote");
		put(",", "Comma");
		put("@", "At");
		put("<string>", "String");
		put("<integer>", "Integer");
		put("<eof>", "End of File");
		put("<error>", "Error");
	    }};

	

	int echo;

	Reader reader = new BufferedReader(new FileReader("filtered.scheme"));

	// Go through the file and print each token as what it is in the map
	while ((echo = reader.read()) != -1) {
	    char echoChar = (char) echo;

	    // First thing we are checking if the character already exists in the map.
	    if (tokenMap.containsKey(""+echoChar))
		System.out.println(echoChar + " : " + tokenMap.get(""+echoChar));

	    // If the character is the beginning of a string, we need to go through and collect the string
	    else if (echoChar == '\"') {
		String str = "";
		while ((echo = reader.read()) != -1) {
		    echoChar = (char) echo;
		    if (echoChar == '\"') {
			System.out.println(str + " : String");
			break;
		    }
		    else str += echoChar;
		}
	    } else if (echoChar >= 48 && echoChar <= 57) {
		// If we enter this loop, we have encountered the beginning of an integer.
		String str = "" + echoChar;
		while((echo = reader.read()) != -1) {
		    echoChar = (char)echo;
		    if (echoChar < 48 || echoChar > 57) {
			System.out.println(str + " : Integer");
			break;
		    }
		    str += echoChar;
		}
	    }

	}
	
    }




}

