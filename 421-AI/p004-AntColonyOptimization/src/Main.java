/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 4 - Ant Colony Optimization
 * November 4th, 2019 <-- Don't let this one slip, Alex!
 *                        No more late assignments!
 */

import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {
	System.out.println("Hello world! I'm a main function!");
	MiddleEarth ME = new MiddleEarth(new File("MiddleEarth.dat"));
	ME.printMap();
    }
    
}
