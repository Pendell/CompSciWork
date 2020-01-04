/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 2 - Genetic Algorithms - NQueens
 * September 27th, 2019
 *
 * This is the Population. This is where all of our workers will
 * live while the generations pass and we look for solutions.
 *
 * NOTES TO DR. GRABOWSKI:
 * PARENT SELECTION IS DONE WITHOUT REPLACEMENT -> A candidate cannot be selected again.
 * SURVIVOR SELECTION IS AGE-BIASED -> Children replace their parents.
 */

import java.util.*;

public class Population {

    int QUEENS;

    // The size of the population
    int pop_size;

    // How many generations have passed since we've started
    int generation = 0;

    // The rate at which mutations will occur in our population
    // when children are spawned
    // private double mutation_rate = .1;

    // This is the population.
    private Worker[] population;

    // This will determine if we terminate early based on whether or not a soln was produced.
    private boolean solution_found = false;

    private int prophet_id;

    /* The constructor. The 'n' being passed in the is the number of queens,
     * which given the assignment we must multiply by 10 to get the size of our
     * population.
     */
    public Population(int n){
        QUEENS = n;
        pop_size = n*10;
        population = new Worker[pop_size];
        populate();
        check_for_solutions();
    }

    /* When this function is called, our population will live out their lives until a prophet
     * is born (a solution has been found) or until 1,000 generations have passed.
     */
    public String generate_solution() {
        while (!solution_found && generation < 1500){
            int[] parent_pool = select_parents();
            generate_offspring(parent_pool);
            check_for_solutions();
            generation += 1;
        }

        if (solution_found) {
            return population[prophet_id].toString() + " Generation : " + generation;
        } else {
            return "No solutions.";
        }
    }

    /* Given a parent_pool, we pair up each parent with the ones next to it to generate offspring.
     * The child that's being produced (via Crossover) will replace it's parent in the population.
     * That is, we are AGE BIASED! <-- SURVIVOR SELECTION.                                      
     */
    private void generate_offspring(int[] parent_pool) {
        for(int i = 0; i < parent_pool.length-1; i+=2){
            // Select two parents
            Worker parent1 = population[parent_pool[i]];
            Worker parent2 = population[parent_pool[i+1]];
            // Generate a breakpoint for crossover
            Random random = new Random();

            // This will produce a number between 0 and QUEENS - 1 to be our breakpoint.
            int breakpoint = random.nextInt(QUEENS);

            // Produce the child genotypes (potential solutions)
            int[] child1_genotype = new int[QUEENS];
            int[] child2_genotype = new int[QUEENS];

            // Fill in the first portion (before the breakpoint)
            for(int j = 0; j < breakpoint; j++) {
                child1_genotype[j] = parent1.genotype[j];
                child2_genotype[j] = parent2.genotype[j];
            }

            // Fill in the rest of the genotype for child1 and child2
            int fill_position = breakpoint;
            for(int j = 0; j < QUEENS; j++){
                // Check to see if the value is already in the array
                if(contains(child1_genotype, parent2.genotype[j]))
                    continue;
                else {
                    // if not, fill it in
                    child1_genotype[fill_position] = parent2.genotype[j];
                    fill_position++;
                }
            }

            fill_position = breakpoint;
            for(int j = 0; j < QUEENS; j++){
                // Check to see if the value is already in the array
                if(contains(child2_genotype, parent1.genotype[j]))
                    continue;
                else{
                    // if not, fill it in
                    child2_genotype[fill_position] = parent1.genotype[j];
                    fill_position++;
                }
            }

            // Did mutation occur for child1?
            int mutate = random.nextInt(10);
            boolean mutation1 = false;
            if (mutate == 0)
                mutation1 = true;

            // Did mutation occur for child2?
            mutate = random.nextInt(10);
            boolean mutation2 = false;
            if (mutate == 0)
                mutation2 = true;

            // Construct the new workers and replace the old workers
            Worker child1 = new Worker(parent1.id, QUEENS, child1_genotype, mutation1);
            Worker child2 = new Worker(parent2.id, QUEENS, child2_genotype, mutation2);
            
            population[child1.id] = child1;
            population[child2.id] = child2;

        }
    }

    /* This is how we populate our population. We simply iterate through the array
     * and generate Workers (who have random array elements) and then add them to the pop.
     */ 
    private void populate() {
        for (int i = 0; i < pop_size; i++){
            Worker worker = new Worker(i, QUEENS);
            population[i] = worker;
        }
    }

    /* We randomly select a number of parents equal to pop_size / 10.
     * From there, we randomly select three Workers. The Workers will get
     * added to a set of selected ID's to make sure we don't select them again.
     * Then, we compare the fitness values, and select the highest fitness of the three
     * to add to pool of parents to generate offspring. 
     * We repeat this process until the parent_pool is filled.
     */
    private int[] select_parents() {
        Set<Integer> selectedIDs = new HashSet<Integer>();
        int parent_pool_size;
        if (QUEENS % 2 == 1){
            parent_pool_size = QUEENS + 1;
        } else {
            parent_pool_size = QUEENS;
        }
        int[] parent_pool = new int[parent_pool_size];
        for(int i = 0; i < parent_pool.length; i++){
            int[] candidate_pool = new int[3];

            // Select three random candidates.
            for(int j = 0; j < 3; j++){
                Random rand_ID = new Random();

                // Select a random candidate from 0 - pop_size.
                int candidate = rand_ID.nextInt(pop_size);

                // Check if the candidate has been selected before.
                if (!selectedIDs.contains((Integer)candidate)){
                    // If not, add the candidate ID to the pool.
                    candidate_pool[j] = candidate;
                    selectedIDs.add((Integer)candidate);
                } else {
                    j--;
                }
            }


            // Selected the highest fitness of the three candidates.
            int candidate_to_add = candidate_pool[0];
            for(int j = 0; j < 3; j++) {
                if(population[candidate_to_add].fitness < population[candidate_pool[j]].fitness)
                    candidate_to_add = candidate_pool[j];
            }

            // Add the highest fitness to parent pool for offspring generation.
            parent_pool[i] = candidate_to_add;
        }
        return parent_pool;
    }


    private void check_for_solutions(){
        for(int i = 0; i < pop_size; i++) {
            if(population[i].THEPROPHET){
                solution_found = true;
                prophet_id = i;
                // System.out.println();
                // System.out.println("A Prophet has been born! He lives in position " + i + " and was born into generation " + generation + "!");
                // System.out.println("His solution is: " + population[i].toString() + " (Hint: that's over 9000!)");
                break;
            }
        }
    }

    public void print_population(){
        for(int i = 0; i < pop_size; i++){
            System.out.println("Pop[" + i + "]: " + population[i].toString());
        }
    }
    

    private boolean contains(int[] a, int value){

        for(int i : a) {
            if(i == value)
                return true;
        }
        return false;
    }
}