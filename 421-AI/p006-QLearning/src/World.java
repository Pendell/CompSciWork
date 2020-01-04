/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 6: Q-Learning -- Trolls & Ponies
 * December 12, 2019
 */


/* Reference Movement Chart:
        {-1, 0}, // N
        {-1, 0}, // NE
        {0, 1}, // E
        {1, 1}, // SE
        {1, 0}, // S
        {1, -1}, // SW
        {0, -1}, // W
        {-1, -1} // NW
*/


import java.io.*;
import java.util.*;

public class World {

    double EXPLORE_RATE = 0.2;
    double LEARNING_RATE = 0.6;
    double DISCOUNT_RATE = 0.5;

    String actionSelection = "P_GREEDY";

    // Rewards & Parameters
    int escape = 15;
    int pony = 10;
    int troll = -15;
    int other = 2;

    double alpha = 0.75;
    double gamma = 0.5;

    int epochs = 10000;
           
       
    // I know variables aren't suppose to be capitalized,
    // but this one makes sense... it's N.
    int N;
    
    // Number of trolls and ponies
    int trolls;
    int ponies;

    stateAction_t[][] stateSpace;

    // A collection of dx, dy that determine how the location
    // changes with respect to direction...
    Action dir[] = {
        new Action(-1, 0), // N
        new Action(-1, 1), // NE
        new Action(0, 1), // E
        new Action(1, 1), // SE
        new Action(1, 0), // S
        new Action(1, -1), // SW
        new Action(0, -1), // W
        new Action(-1, -1) // NW
    };
    
    // This is the layout of the world.
    Cell[][] world;
    
    Random rand; 
    
    /* Constructor to build the world 
     * Parse through the world file line by line
     * Line 1: Value of N, number of trolls, and number of ponies 
     *         This line will be 3 integer values separated by spaces
     * Line 2: Escape Location (x, y)
     * Line 3: Series of (x,y) coords defining the ponies' location
     * Line 4: Series of (x,y) coords defining locations of obstructions
     * Line 5: Series of (x,y) coords defining locations of trolls */
    public World(File blueprint) throws FileNotFoundException{
        Scanner sc = new Scanner(blueprint);
        rand = new Random(System.currentTimeMillis());

        // Parse the file
        for(int lineNum = 1; lineNum <= 5; lineNum++){
            parseLine(sc.nextLine(), lineNum);
        }

        // Draw the stuff
        draw();
        System.out.println();
        System.out.println();
        drawQ();
        burglarize();
    }

    public void burglarize(){
        System.out.println("Burglarizing");
        int x, y, action;

        initStateSpace();
        
        for(int t = 0; t < 5; t++){

            System.out.println("///////////////////////////////////////////////");
            System.out.println("EPOCH #: " + t);
            System.out.println("///////////////////////////////////////////////");
                               
            // Randomize the starting position
            // We can't start on the position of an obstruction
            System.out.println("Getting a starting point");
            do {
                x = rand.nextInt(N-1) + 1;
                y = rand.nextInt(N-1) + 1;
            } while(world[x][y].getState() == 'O' || world[x][y].getState() == 'E');
            System.out.println("Got a starting point");
            System.out.println("Starting point: (" + x + ", " + y + ")");
            System.out.println("Setting location as traveled");
            while (true){
                world[y][x].traveled = true;
                System.out.println("Choosing an action in location (" + x + ", " + y + ")");
                action = chooseAction(y, x);
                System.out.println("Running update");
                updateFunction(y, x, action);
                System.out.println("Adding dx and dy to location");
                System.out.println("dx = " + dir[action].dx + " || dy = " + dir[action].dy);
                x += dir[action].dx;
                y += dir[action].dy;
                System.out.println("Newx = " + x + " || Newy = " + y);
                if (world[y][x].getState() == 'E'){
                    break;
                }
                draw();
            }
            
            draw();
            resetWorld();
        }

        showPath();

    }

    void showPath(){
        System.out.println("0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
        System.out.println("Showing path now.");
        System.out.println("0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
        int x, y, action;
        actionSelection = "GREEDY";
        
        System.out.println("Getting a starting point");
        
        do {
            x = rand.nextInt(N) + 1;
            y = rand.nextInt(N) + 1;
        } while(world[x][y].getState() == 'O'|| world[x][y].getState() == 'E');
        System.out.println("Got a starting point");
        
        System.out.println("Starting point: (" + x + ", " + y + ")");
        

        while(true){
            world[y][x].traveled = true;
            if(world[y][x].getState() == 'E') break;
            
            System.out.println("Setting location as traveled");
            
            action = chooseAction(y, x);
            System.out.println("Action: " + action);
            
            System.out.println("Adding dx and dy to location");
            System.out.println("dx = " + dir[action].dx + " || dy = " + dir[action].dy);
            
            x += dir[action].dx;
            y += dir[action].dy;

            draw();
        }

        
    }

    /* This is the helper function for parsing each line.
     * To see how each line is being parsed, please refer to the World
     * constructor */
    private void parseLine(String line, int lineNum){
        Scanner sc = new Scanner(line);
        if(lineNum == 1) { // We're on the first line
            N = Integer.parseInt(sc.next());
            System.out.println("The size of N: " + N);
            world = new Cell[N][N];
            
            stateSpace = new stateAction_t[N][N];

            // Populate the world with tiles
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++){
                    world[i][j] = new Cell('c');
                }
            }

            // Grab the # of ponies & trolls from the file
            trolls = Integer.parseInt(sc.next());
            ponies = Integer.parseInt(sc.next());

            System.out.println("The number of trolls/ponies: " + trolls + "/"
                               + ponies);
        } else { // Everything after the first line is ordered pairs
            while (sc.hasNext()){
                // Grab the next ordered pair
                int x = Integer.parseInt(sc.next());
                int y = Integer.parseInt(sc.next());

                // If the ordered pair is (-1, -1), nothing to see here, next line
                if (x == -1 && y == -1) return;

                // Determine what the ordered pair represents
                // based on which line it is.
                if (lineNum == 2) {
                    // Get the exit location
                    world[y][x].changeState('E');
                    System.out.println("Exit location: (" + x + ", " + y + ")");
                } else if (lineNum == 3) {
                    // Ponies locations
                    world[y][x].changeState('P');
                    System.out.println("Pony location: (" + x + ", " + y + ")");
                } else if (lineNum == 4) {
                    // Obstruction locations
                    world[y][x].changeState('O');
                    System.out.println("Obstruction location: (" + x + ", " + y + ")");
                } else {
                    // Troll locations
                    world[y][x].changeState('T');
                    System.out.println("Troll location: (" + x + ", " +  y + ")");
                }
            }
        }
    }

    /* initStateSpace()
     * Initializes the stateSpace[] to all 0's. This is a fresh slate so we can 
     * learn the Q values and appropriate actions from there. */
    private void initStateSpace(){
        for (int y_state = 0; y_state < N; y_state++){
            for(int x_state = 0; x_state < N; x_state++){
                for(int action = 0; action < 8; action++){
                    stateSpace[y_state][x_state] = new stateAction_t();
                }
            }
        }
    }

    /* findMaxQ()
     * From the book: "findMaxQ function acceps an x and y coord,
     * finds the largest Q value, and stores it in the maxQ field. */
    private void findMaxQ(int y, int x){
        stateSpace[y][x].maxQ = 0.0;
        for(int i = 0; i < 8; i++){
            if(stateSpace[y][x].Q[i] > stateSpace[y][x].maxQ){
                stateSpace[y][x].maxQ = stateSpace[y][x].Q[i];
            }
        }
    }

    /* updateSum()
     * From the book: "updateSum walks through the Q values and
     * keeps a sum of them in sumQ field. */
    private void updateSum(int y, int x){
        stateSpace[y][x].sumQ = 0.0;
        for (int i = 0; i < 8; i++){
            stateSpace[y][x].sumQ += stateSpace[y][x].Q[i];
        }
    }

    /* legalMove()
     * function that checks to see if desired action is a legal move
     * parameters:
     *   int y_state: the current y position of the agent
     *   int x_state: the current x position of the agent
     *   int action: the desired action
     * returns: An integer (-1 or 1)
     *   -1: The action is illegal and cannot be taken 
     *    1: The action is legal and can be taken */
    boolean legalMove(int y_state, int x_state, int action){
        int y = y_state + dir[action].dy;
        int x = x_state + dir[action].dx;
        if (x >= N || y >= N) return false;
        else if (x <= 0 || y <= 0) return false;
        else if (world[x][y].getState() == 'O') return false;
        else return true;
            
    }

    // returns a random action in the current position (x, y)
    int getRandomAction(int y, int x){
        int action;
        do{
            action = rand.nextInt(8);
        } while(!legalMove(y, x, action));
        return action; 
    }

    /* chooseAction()
     * The function that controls how we choose an action.
     * An action is chosen first based on whether we're choosing greedily or p_greedily
     * paramters:
     *   int y: the y position of the agent
     *   int x: the x position of the agent
     * returns:
     *   returns an integer that represents the action chosen */
    int chooseAction(int y, int x){
        System.out.println("Choosing an action");
        int action = getRandomAction(y,x);
        int count = 0;
        do{

            if (actionSelection.equals("GREEDY")){
                findMaxQ(y,x);
                for(action = 0; action < 8; action++){
                    // If the action is highest one calculated, return it if it's legal
                    if (stateSpace[y][x].maxQ == stateSpace[y][x].Q[action]){
                        if(legalMove(y,x,action)) return action;
                    }
                }

            
            } else if (actionSelection.equals("P_GREEDY")){
                if (EXPLORE_RATE > Math.random() || stateSpace[y][x].sumQ == 0.0) {
                    action = getRandomAction(y, x);
                    System.out.println("Exploring!");
                } else {
                    System.out.println("Exploiting!");
                    double prob;
                    action = getRandomAction(y, x);
                    for (count = 0; count < 8; count++){
                        if (legalMove(y,x,action)){
                            prob = (stateSpace[y][x].Q[action] / stateSpace[y][x].sumQ);
                            if(prob > Math.random()){
                                return action;
                            }
                        }
                        if (++action == 8) action = 0;
                    }
                     
                }
                // Went through possible actions but did not pick one...
                // So we'll pick one randomly
                if(count == 8){
                    action = getRandomAction(y,x);
                }
            
            }                   
            return action;
        } while(!legalMove(y,x,action) && action < 8);
    }

    void updateFunction(int y, int x, int action){
        int newy = y + dir[action].dy;
        int newx = x + dir[action].dx;

        double reward = (double) world[newy][newx].getReward();
        findMaxQ(newy, newx);

        stateSpace[y][x].Q[action] += LEARNING_RATE * (reward + (DISCOUNT_RATE * stateSpace[newy][newx].maxQ) - stateSpace[y][x].Q[action]);

        updateSum(y, x);
    }
    
    /* draw() and drawQ()
     * These two functions accept no paramaters and return void
     * These exist to simply draw the world to the terminal
     * draw() draws the world in terms of ponies and trolls
     * drawQ() draws the world in terms of each cell's Q value */
    public void draw(){
        System.out.println("Layout of the world:");
        System.out.print("+");
        for (int i = 0; i < N; i++){
            System.out.print("-");
        }
        System.out.println("+");
        for(int i = N-1; i >= 0; i--){    
            System.out.print("|");
            for(int j = N-1; j >= 0; j--){  
                if(world[j][i].burglarPresent)
                    //System.out.print(agent.getfacing());
                    System.out.println();
                else if (world[j][i].traveled == true){
                    System.out.print('X');
                }else {
                    System.out.print(world[j][i].getState());
                }
            }
            System.out.println("|");
        }
        System.out.print("+");
        for(int i = 0; i < N; i++){
            System.out.print("-");
        }
        System.out.println("+");
    }

    
    public void drawQ(){
        System.out.println("Q values by Cell: ");
        for(int i = N-1; i >= 0; i--){    
            for(int j = N-1; j >= 0; j--){  
                if(world[j][i].burglarPresent)
                    System.out.println(); // Not yet implemented
                else if (world[j][i].traveled == true){
                    System.out.print('X');
                }else {
                    System.out.print(world[j][i].getRewardForDraw());
                }
            }
            System.out.println();
        }
    }

    public void resetWorld(){
        for(int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                world[i][j].traveled = false;
            }
        }
    }


    // EXTRA CLASSES
    
    class Action /* lawsuit! get it?? */{
        int dx;
        int dy;
        public Action(int x, int y){
            dx = x;
            dy = y;
        }
    } /* Cause the line says Class Action Lawsuit?? No? Okay. */ 

    
    class stateAction_t{
        double Q[];
        double sumQ;
        double maxQ;

        public stateAction_t(){
            sumQ = 0;
            maxQ = 0;
            Q = new double[8]; // There are eight possible directions
        }
    }
}
