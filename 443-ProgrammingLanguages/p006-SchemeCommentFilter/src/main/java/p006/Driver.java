import java.io.*;

public class Driver {

    public static void main(String [] args) {
	
        // First and foremost, we need to check if 0 args were given.
        if (args.length == 0) {
            System.out.println("There were no arguments given, please try again.");
        } else {

            // If there were arguments given, open a reader on that file.
            try {
                Reader reader = new SchemeCommentFilter(new FileReader(args[0]));
                reader.read();
            } catch (IOException e) {
            System.out.println(e.getMessage());
            }
        }
    }
}
