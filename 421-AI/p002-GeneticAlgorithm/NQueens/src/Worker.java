/*  Alex Pendell
 *  CIS 421 - Artificial Intelligence
 *  Assignment 2 - Genetic Algorithms - NQueens
 *  September 27th, 2019
 *
 *  This is the Worker class. A worker is a potential solution
 *  to the N-Queens problem.
 *  As such, a worker has an array of size N (given by main) that
 *  reperesents the configuration of the board. 
 */

import java.util.*;

public class Worker{

    // A global so that everything knows how many queens they're working with.
    int QUEENS;

    // id is going to be this workers position in the population array.
    int id;

    // This is the array representation of the board.
    int[] genotype;

    // This is how fit the Worker is at it's job. Used in parent selection.
    double fitness;

    // Climate change is doing a well enough job destroying the planet, we don't need to
    // take any risks by dividing by zero. So we'll add a small epsilon to prevent any disasters
    // when computing fitness.
    double EPSILON = 0.0001;

    // Boolean that determines whether or not this Worker is a solution to the problem.
    boolean THEPROPHET = false;


    /* Constructor for the worker. The ID is simply it's position within the population array.
     * We must give it the number of queens because that will determine the size of it's 
     * genotype.
     */
    public Worker(int identification, int n){
        QUEENS = n;
        id = identification;
        genotype = new int[n];
        initialize(n);
        compute_fitness();
    }

    /*  This is another constructor that is only for testing purposes. The parameter passed in
     *  is an int[] containing a solution to the N-Queens problem.
     *  When actually executed for experiementing, this function should never be called.
     */
    public Worker(int identification, int n, int[] genome, boolean mutation){
        id = identification;
        QUEENS = n;
        genotype = genome;
        if (mutation){
            Random rand = new Random();
            int pos1 = rand.nextInt(QUEENS);
            int pos2 = rand.nextInt(QUEENS);
            swap(pos1, pos2);
        }
        compute_fitness();
    }
   

    /* Initilialize is how we randomly seed the genotype. It first fills in the array, then
     * performs a random number of swaps (1 to 25 times).
     */
    private void initialize(int n){

        // This constructs a vanilla array: <1, 2, 3, ... , n>;
        // We will randomize the order afterward.
        for(int i = 0; i < n; i++){
            genotype[i] = i+1;
        }

        // Shuffle the array around.
        Random rand = new Random();

        for(int i = 0; i < genotype.length; i++){
            int rand_position = rand.nextInt(genotype.length);
            swap(i, rand_position);
        }
    }

    /* This is the method for computing fitness (checking conflicts).
     * To see the reasoning and how it works, please consult ConflictChecking.pdf.
     */
    private void compute_fitness() {
        int conflicts = 0;

        for(int i = 0; i < QUEENS - 1; i++){
            for (int j = 1; j < QUEENS - i; j++){
                if(genotype[i] + j == genotype[i+j]) {
                    conflicts++;
                }
                if(genotype[i] - j == genotype[i+j]){
                    conflicts++;
                }
            }
        }

        fitness = 1 / (conflicts + EPSILON);

        if (fitness == 10000.0) {
            THEPROPHET = true;
        }
    }



    /* The simple swap function that we've done since CIS 201 
     */
    private void swap(int first, int second){
        int temp = genotype[second];
        genotype[second] = genotype[first];
        genotype[first] = temp;
    }

    public String toString(){
        return Arrays.toString(genotype);
    }

}