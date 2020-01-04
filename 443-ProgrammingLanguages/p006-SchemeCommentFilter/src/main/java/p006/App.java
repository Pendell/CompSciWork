


// This is a re-creation of the App function so it will play nice with gradle
// ... hopefully.
public class App {

    // The only thing this does is invoke main() from the driver program.
    public static void main(String[] args) {
	Driver driver = new Driver();
	driver.main(args);
    }
}
