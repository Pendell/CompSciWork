/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 2 - Genetic Algorithms - NQueens
 * September 27th, 2019
 *
 * This is the main class of the GA. This is used for initialization of the other
 * objects that will be used with the algorithm.
 * 
 * This is also where handling the experiments takes place. (I.E. Choosing N, and the No.
 * of experiments to run).
 */

import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        // Prompt for input
        System.out.print("Please enter a value for N: ");
        Scanner sc = new Scanner(System.in);

        // Process input
        String input = sc.next();
        int n = Integer.parseInt(input);
        System.out.println("You chose " + n + " queens!");

        System.out.print("How many times would you like to run this experiment? ");
        input = sc.next();
        int experiment = Integer.parseInt(input);
        System.out.println("Running " + experiment + " times");

        PrintWriter writer = new PrintWriter("Results.txt", "UTF-8");

        for(int i = 0; i < experiment; i++) {
            if (i == 0) {
                System.out.print(". ");
            } else if (i == experiment/2) {
                System.out.print(". ");
            } else if( i == experiment - 1) {
                System.out.println(".");
            }
            Population pop = new Population(n);
            writer.println("Experiment " + (i + 1) + " results: " + pop.generate_solution());
        }

        writer.close();
        System.out.println("Please check Results.txt for the results of this experiment.");
    }

}