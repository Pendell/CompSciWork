/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 2 - Genetic Algorithms - NQueens
 * September 27th, 2019
 *
 * This program is used only to test my conflict checking algorithm. It's given an array with a known
 * number of conflictions for the N-Queens problem. Since it is impossible to get column and row conflicts
 * using this encoding method, the only thing we need to check for is diagonal conflicts. Consult the 
 * Discussion for more information.
 */

public class ConflictChecker {

    public static void main(String[] args){
        int QUEENS = 8;
        System.out.println("The array we will be checking for conflicts is: [1, 2, 6, 5, 7, 3, 4, 8]");
        System.out.println("If everything goes according to plan, there should be 7 CONFLICTS");
        int[] soln = new int[]{1 ,2, 6, 5, 7, 3, 4, 8};

        int conflicts = 0;

        for(int i = 0; i < QUEENS - 1; i++){
            System.out.println("Checking position " + i + " for conflictions...");
            for (int j = 1; j < QUEENS - i; j++){
                if(soln[i] + j == soln[i+j]) {
                    System.out.println("Conflict found! Position " + i + " conflicts with position " + j);
                    conflicts++;
                }
                if(soln[i] - j == soln[i+j]){
                    System.out.println("Conflict found! Position " + i + " conflicts with position " + j);
                    conflicts++;
                }
            }
        }

        System.out.println("Conflicts Expected: 7");
        System.out.println("Conflicts found: " + conflicts);
    }

}