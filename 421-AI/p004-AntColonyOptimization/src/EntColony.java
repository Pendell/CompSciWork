/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 4 - Ant Colony Optimization
 * November 4th, 2019 <-- Don't let this one slip, Alex!
 *                        No more late assignments!
 *
 * Ant Colony Optimization: The start point is always Blue
 * Mountains, and the end point is always Iron Hills. Since
 * this is not a travelling sales person problem, we don't
 * care about visiting every node in Middle Earth.
 * 
 * Overview: Our ants will begin from "Blue Mountains" and will
 * always end at "Iron Hills". Since this is not TSP, we don't
 * care about visiting every node in the graph. We will run the
 * experiment 5 times.
 * 1st experiment will favor the hueristic.
 * 2nd experiment will favor the pheromone level.
 * 3rd experiment eliminates hueristic entirely.
 * 4th & 5th experiments will test the influence of different
 *     values for RHO.
 *
 * Some considerations given to use via the write-up:
 * 1. As previously stated this is not a TSP problem. Caution
 *    is advised when referring to the code in the book.
 * 2. The graph used is not fully connected. We will need a way
 *    to backtrack if the ant agent gets stuck (trapped).
 * 3. Pheromone evaporation can be be done either before OR
 *    after depositing new pheromone. It is advised to experiment
 *    with both and see if results reflect any difference.
 *
 * Table 1. Parameter values for experiments
 * o===================================================================o
 * |  PARAMETER  |  PARAMETER  |  RUN  |  RUN  |  RUN  |  RUN  |  RUN  |
 * |    NAME     |   MEANING   |   1   |   2   |   3   |   4   |   5   |
 * |=============|=============|=======|=======|=======|=======|=======|   
 * |    ALPHA    |  Pheromone  |  0.4  |  1.0  |  1.0  |  1.0  |  1.0  |
 * |             |   weight    |       |       |       |       |       |
 * |-------------|-------------|-------|-------|-------|-------|-------|
 * |    BETA     |  Hueristic  |  1.0  |  0.4  |  0.0* |  1.0  |  1.0  |
 * |             |   weight    |       |       |       |       |       |
 * |-------------|-------------|-------|-------|-------|-------|-------|
 * |     RHO     |    Trail    |  0.65 |  0.65 |  0.65 |  0.4  |  0.95 |
 * |             | Persistence |       |       |       |       |       |
 * |-------------|-------------|-------|-------|-------|-------|-------|
 * |      Q      |  Pheromone  |  100  |  100  |  100  |  100  |  100  |
 * |             |   Quantity  |       |       |       |       |       |
 * o===================================================================o
 *
 * Also note, the only hueristic we're to use is the point-to-point info.
 * AKA - road distance.
 */

import java.util.*;
import java.io.*;


public class EntColony {

    public static MiddleEarth ME;

    public static double ALPHA, BETA, RHO;
    public static int Q = 100; 
        
    
    public static int NUMOFANTS = 10;
    public static int NUMOFCYCLES = 25;
    
    public static void main(String[] args) throws Exception {
           
        ME = new MiddleEarth(new File("MiddleEarth.dat"));
        System.out.print("Please enter a trial number 1-5: ");
        Scanner sc = new Scanner(System.in);

        String input = sc.next();
        while(!input.equals("1") && !input.equals("2") && !input.equals("3")
              && !input.equals("4") && !input.equals("5")){
            System.out.print("Please enter a valid input: ");
            input = sc.next();
        }
        
        int trialNum = Integer.parseInt(input);        
 
        

        
        if(trialNum == 1){
            //First trial
            ALPHA = 0.4;
            BETA = 1.0;
            RHO = 0.65;
        } else if (trialNum == 2){
            //Second trial
            ALPHA = 1.0;
            BETA = 0.4;
            RHO = 0.65;
                
        } else if (trialNum == 3){
            //Third trial
            ALPHA = 1.0;
            BETA = 0.0;
            RHO = 0.65;
                
        } else if (trialNum == 4){
            //Fourth trial
            ALPHA = 1.0;
            BETA = 1.0;
            RHO = 0.4;

        } else if (trialNum == 5){
            //Final trial
            ALPHA = 1.0;
            BETA = 1.0;
            RHO = 0.95;
        }
        System.out.println("Trial #" + trialNum);

        /* Structure of a EntColonyOptimization program:
         * while (!termination_condition) {
         *     generateSolution()
         *     pheromoneUpdate()
         * }
         *
         * So... */
            
        int cycleNum = 0;
        while(cycleNum < NUMOFCYCLES){
            System.out.println("CYCLE#: " + cycleNum);

            // generateSolution()
            //Ent ent = new Ent();
            ArrayList<LinkedList<MiddleEarth.Road>> solution = generateSolnSet();
            HashSet<MiddleEarth.Road> set = new HashSet<MiddleEarth.Road>();
            for(int i = 0; i < solution.size(); i++) {
                Iterator<MiddleEarth.Road> it = solution.get(i).iterator();
                System.out.print("BLUEMOUNTAINS");
                int distance = 0;
                while(it.hasNext()){
                    MiddleEarth.Road road = it.next();
                    distance += road.distance;
                    // THIS IS WHERE OUR ENTS LAY PHEROMONE
                    road.pheromone += Q;
                    System.out.print(" -> " + ME.ME[road.destination].toString());
                }
                System.out.println("Total ant distance: " + distance);
            }
            // advance toward inner termination_condition
            cycleNum++;

        }
        
    }


    /* What do we need to do...
     * 1. Distribute pheromone randomly about the edges (This should be done in
     *       MiddleEarth.java -> Just add a random number generator to each edge as
     *       it's created.
     * 2. Initialize an Ant -> ??? Unsure about what I'm going to need to attach to
     *       it (DATA). Perhaps keep track of the path it's taken so far 
     *       (Would make back-tracking a lot easier).
     * 3. After the Ant has completed it's trail, distribute pheromone to each of
     *       the edges it chose. 
     * 4. Evaporate the phermone via the evaporation equation. This can be done 
     *       before or after the previous step (3).
     * 5. Jump back to step 2 and repeat until the number of trials has elapsed.
     */ 


    /* STEP 1 COMPLETE
     * I'm going to modify MiddleEarth.java to assign a random pheromone level
     * to each edge so that this step get's done when the edge is created. It
     * would seem counter-intuitive to create the edges, then reiterate through
     * them to assign random pheromone levels when it can be done on init.
     */

    /* STEP 2 COMPLETE
     * Initialization of the ent (ant). I think the ant is just going to be a list
     * that contains the current path taken by the ant. Perhaps it's own class?
     * unsure yet... 
     */
    
    
    public static ArrayList<LinkedList<MiddleEarth.Road>> generateSolnSet(){
        ArrayList<LinkedList<MiddleEarth.Road>> solutionSet = new ArrayList<LinkedList<MiddleEarth.Road>>();

        // Simulate ants finding solutions
        for (int i = 0; i < 10; i++){
            solutionSet.add(i, generateSoln());
        }
        return solutionSet;
    }

    // NOTE TO SELF
    // IF IT'S GREATER THAN THE PREVIOUS && LESS THAN BESTFOUND
    /* This function is how we find a single solution set.
     * That is, when this function runs, we generate a solution as if one
     * ent had run it's course to it's destination.
     */
    private static LinkedList<MiddleEarth.Road> generateSoln(){
        
        int currPos = ME.getCityPos("BLUEMOUNTAINS");
        int goalPos = ME.getCityPos("IRONHILLS");
        
        LinkedList<MiddleEarth.Road> pathTaken = new LinkedList<MiddleEarth.Road>();
        HashSet<MiddleEarth.City> visitedCities = new HashSet<MiddleEarth.City>();
        visitedCities.add(ME.ME[currPos]);

        // Debug variable
        int i = 0;
        while((currPos != goalPos) /*&& (i < 20)*/){
            // Calculate the denominator for the equation.
            // Then find the next best hop.
            double denom = calculateDenom(currPos, visitedCities);
            Iterator<MiddleEarth.Road> roadIt = ME.ME[currPos].roads.iterator();
            double sum = 0; 
            while(roadIt.hasNext()){
                // Grab the next road to examine.
                MiddleEarth.Road examineRoad = roadIt.next();

                // We reset a roads f value to zero each time.
                // If we didn't do this, we might get stuck in loops.
                // Having an f value of zero means there's a 0% chance
                // that we take this road.
                // If ALL roads connected to this point is zero, we
                // backtrack (we've visited all connected nodes).
                // Also note, the f value in this context (as in, not ASTAR)
                // f value is simply the probability of this road being chosen.
                // It does NOT represent a fitness value.
                // Every f value should be between zero and 1.
                examineRoad.f = 0;

                // Okay so, we need this number because the way that we're
                // going to determine which road to travel down needs to be
                // probabilistic.
                // So instead of every f value having unique values, the next
                // road's f value will be determined partially by where the
                // previous roads f value ended, and then the result of this
                // roads probability function will be added to that value.
                // So the result will be that we have a scale from 0-1, and
                // what value get's chosen on that scale (randomly) will be
                // what road we choose to travel down. Roads who have higher
                // probability will 'own' more of the scale.
                

                // Next lines calculate the f value of the road.
                // We only calculate the value if we haven't been to the
                // destination yet.
                if(!visitedCities.contains(ME.ME[examineRoad.destination])
                   && denom != 0){
                    // These two lines create the numerator of the value
                    examineRoad.f += Math.pow(examineRoad.distance, ALPHA);
                    examineRoad.f += Math.pow(examineRoad.pheromone, BETA);
                    
                    // This finishes the probability calculation.
                    examineRoad.f /= denom;
                    // System.out.println("Unmodified: " + examineRoad.f);
                    
                    // Have to store f somewhere real fast, get's dicey if not
                    // (since we're adding one to the other, then the to one)
                    double temp = examineRoad.f;
                    examineRoad.f += sum;
                    sum += temp;

                    // DEBUG PRINTLN
                    // System.out.println("Probability of travelling to "
                    //                   + ME.ME[examineRoad.destination].toString()
                    //                   + ": " +examineRoad.f);
                    
                }
                //System.out.println("SUM: " + sum);
            }

            // Now we actually have to fetch the chosen path.
            double choice = Math.random();
            
            // restart the iterator
            roadIt = ME.ME[currPos].roads.iterator();
            MiddleEarth.Road bestChoice = null;
            while(roadIt.hasNext()){
                MiddleEarth.Road roadExamine = roadIt.next();
                // DEBUG PRINT
                // System.out.println(ME.ME[roadExamine.destination].toString()
                //                + " fval: " +  roadExamine.f);
                
                if (bestChoice != null && roadExamine.f != 0.0) {
                    if (choice >= bestChoice.f && roadExamine.f != 0){
                        bestChoice = roadExamine;
                    }
                } else if (bestChoice == null && roadExamine.f != 0.0){
                    bestChoice = roadExamine;
                } else {
                    break;
                }
            }

            
            
            // If we're here, and we fail to get inside the 'if',
            // it means that we're at at a node whose all adjacent nodes are
            // visited. So we're going to backtrack a node and pick a
            // different path.
            if(bestChoice != null) {
                currPos = bestChoice.destination;
                pathTaken.add(bestChoice);
                visitedCities.add(ME.ME[bestChoice.destination]);
                // DEBUG PRINTS
                // System.out.println("Travelling to: "
                //                   + ME.ME[bestChoice.destination].toString());
                //System.out.println("Algorithm chose road to "
                //                   + ME.ME[bestChoice.destination].toString());
            } else {
                pathTaken.remove(pathTaken.peekLast());
                if(pathTaken.peekLast() == null){
                    currPos = ME.getCityPos("BLUEMOUNTAINS");
                } else {
                    currPos = pathTaken.peekLast().destination;
                }
            }
            
            // Jump to the next location.
            i++;
        }
        
        return pathTaken;
    }

    private static double calculateDenom(int pos, HashSet<MiddleEarth.City> cities){
        double sum = 0;
        Iterator<MiddleEarth.Road> roadIt = ME.ME[pos].roads.iterator();
        while(roadIt.hasNext()){
            MiddleEarth.Road road = roadIt.next();
            
            // We only want to increase the value if it's an unvisited city.
            if(!cities.contains(ME.ME[road.destination])){
                sum += Math.pow(road.distance, ALPHA) + Math.pow(road.pheromone, BETA);
            }
            
        }
        return sum;
    }

    /* This is the MetaData we will use to run through the tests.
     * Putting it all in a class to see if it's easier to keep track of
     * variables. They can get away from me pretty easily sometimes.
     */
    public class MetaData {

        double ALPHA, BETA, RHO;
        int Q = 100;

        
        public MetaData (double a, double b, double rho){
            this.ALPHA = a;
            this.BETA = b;
            this.RHO = rho;
        }
        
    }

    /* STEP 4
     * For this step, I'm going to create a set of all of the edges that have
     * been created thus far. This will be a variable in MiddleEarth.java. I
     * think a set will be the best because we can just do a for-each loop and
     * run the evaporation function on each of the edges in the set.
     */
    public static void pheromoneUpdate(){
        Iterator<MiddleEarth.Road> it = ME.ROADSOFMIDDLEEARTH.iterator();
        while(it.hasNext()) {
            MiddleEarth.Road road = it.next();
            road.pheromone = road.pheromone * (1 - RHO);
        }
    }
    

} /* END OF FILE */
