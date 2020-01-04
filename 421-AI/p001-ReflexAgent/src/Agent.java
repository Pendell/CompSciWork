/*

Alex Pendell
CIS 421 - Artificial Intelligence
Assignment 1: Simple Reflex Agent - Vacuum Cleaner World
September 13th, 2019

This is the Agent class. This is meat and potatoes of the 'vacuum'. As Dr. Ladd would probably say,
the Agent is only really good at doing three things: Fetching, decoding, and executing.

Fetching is where the agent updates all of it's percepts based on gathering information of the world around it.
Decoding takes place after fetching, and is where the agent decides it's best course of action based on those percepts.
Execution is where it executes the best course of action based on what it thinks about it's percepts.

After execution, the robot begins a new by resetting what it knows about it's surroundings, and then loops the cycle until
it reaches it's destination.

*/

import java.util.*;
import java.io.*;

public class Agent {

    // This is the direction that the Agent is facing
    // N - North
    // E - East
    // S - South
    // W - West
    char facing = 'N';
    
    // This is the object that allows us to interact with the world around us. It doesn't save any information about the world
    // except that information given to us by it's precepts. I needed to save it here in order for actions to become persistent
    // i.e. the clean() function actually changing the tile it's currently on.
    Floorplan world;
    
    // While the agent is not on the goal, this bool will remain true. When the agent reaches it's goal, it will power off, and the
    // simulation will cease.
    boolean running = true;
    
    // These are the current x and y coordinates of the agent w.r.t. the world around it.
    int posx;
    int posy;
    
    // These are SOLELY used to 'undraw' the agent in the ASCII as it moves to a new location. The agent makes ZERO decisions based on
    // these variables and beyond helping draw the world, they are disregarded.
    int prevposx;
    int prevposy;
    
    // This string contains the string representation of the percept vector, as well
    // as the action chosen by the agent. This primarily is used for logging outside the Agent class.
    String context;
    
    // This is the agents percept vector. It is an array (We're using Java, so even though they're referred to as percept VECTORS, an array
    // is just easier to work with, and because everything being stored in the percept vector is either a 0 or 1, it was a design decision t
    // just use an int[] instead of a vector<Integer>. This is primarily a QoL decision, as getting and setting values in an array is 
    // simpler than doing so in a vector<Integer>.
    int[] percept = new int[11];
    /* Percept:
     0: T   - Touch (Whether or not we've bonked into something and should change directions)
     1: Du  - Dirt under (There's dirt underneath us, we should clean it.)
     2: Df  - Dirt in front of us (We should move forward one cell)
     3: Db  - Dirt behind us (We should rotate 90 degrees in either direction)
     4: Dl  - Dirt to the left (Rotate 90 degrees to our left)
     5: Dr  - Dirt to the right (Rotate 90 degrees to our right)
     6: Gu  - Goal is under us (we're done?)
     7: Gf  - Goal is in front of us (move forward)
     8: Gb  - Goal is behind us (Rotate 90 degrees in either direction)
     9: Gl  - Goal is left of us (Rotate 90 degrees to face it)
    10: Gr  - Goal is right of us (Rotate 90 degrees to face it)
    */
    
    // The contstructor. This sets our world, and starts off in the position next to the Home position (0, 0).
    public Agent(Floorplan plan){
        world = plan;
        posx = plan.n-2;
        posy = 0;
        
        updateWorld();
    }
    
    // Updates the world with the agents first position
    // This function as well as the next function are strictly used to help draw the ASCII world (I'm not the most
    // ASCII art savvy, and these two methods get the job done.)
    public void updateWorld(){
        world.floorplan[posx][posy].setAgentPresent(!world.floorplan[prevposx][prevposy].getAgentPresent());
        
    }
    
    // Updates the world with the agents new position, and deletes the old position
    // This probably SHOULD be located elsewhere in the code (like in Floorplan.java) but for now this works and
    // does exactly what I need to it to do. It's only purpose is to draw and undraw the agent based on it's location
    // and previous location in the world.
    public void updateWorld(int prevposx, int prevposy){
        world.floorplan[prevposx][prevposy].setAgentPresent(!world.floorplan[prevposx][prevposy].getAgentPresent());
        world.floorplan[posx][posy].setAgentPresent(!world.floorplan[prevposx][prevposy].getAgentPresent());
        
    }
    
    // A getter function that returns the agents x-coordinate
    public int getposx(){
        return posx;
    }
    
    // A getter function that returns the agent's y-coordinate
    public int getposy(){
        return posy;
    }
    
    // A getter function that returns the character value representing which way the agent is facing.
    // See the 'facing' variable in the fields section for further details on the character representation.
    public char getfacing(){
        if (facing == 'N')
            return '^';
        else if (facing == 'S')
            return 'v';
        else if (facing == 'E')
            return '>';
        else
            return '<';
    }
    
    // The agent first looks in front of it to see if moving forward is a legal move.
    //// i.e. there is no furniture in front of it, or it not against a wall.
    // After, it will check for dirt in it's adjacent spaces.
    // Once these two functions are complete, it's percept vector will be populated
    // with everything the agent needs to make a decision based on the world around it.
    // To make this decision however, it must traverse through an absolutely monolithic
    // chunk of logic control (ifs and else ifs) based on which indices in the percept vector
    // is set.
    // Once it makes it's choice, it will execute that action, and reset it's percept vectors
    // so they may be repopulated with new information about it's new location.
    // To see what each percept indices means, see the chart above in the Fields area.
    public void act(){
        checkforward();
        checkfordirt();
        checkforgoal();
        if (percept[1] == 1){
            clean();
        } else if (percept[6] == 1){
            poweroff();
        } else if (percept[0] == 1){
            int tmp = (int) ( Math.random() * 2 + 1);
            if (tmp == 1) {
                rotateleft();
            } else {
                rotateright();
            percept[0] = 1;
            }
        } else if (percept[2] == 1){
            moveforward();
        } else if (percept[3] == 1){
            rotateleft();
        } else if (percept[4] == 1){
            rotateleft();
        } else if (percept[5] == 1){
            rotateright();
        } else if (percept[7] == 1){
            moveforward();
        } else if (percept[8] == 1){
            rotateleft();
        } else if (percept[9] == 1){
            rotateleft();
        } else if (percept[10] == 1){
            rotateright();
        } else {
        int tmp = (int) ((Math.random() * 16) + 1);
            if(tmp > 4) {
                moveforward();
            } else if (tmp <= 4 && tmp > 2) {
                rotateleft();
            } else {
                rotateright();
            }
        }  
        updateWorld();
        resetpercept();
    }
    
    // This method simply zeroes out all of it's percepts so that it may have a clean
    // memory when it arrives to a new location.
    public void resetpercept(){
        for(int i = 0; i < 11; i++){
            percept[i] = 0;
        }
    }
    
    // Check to see if the agent will bonk into anything, this sets the Touch percept indices.
    private void checkforward() {
        if (facing == 'N'){
            if (posy+1 == world.n)
                percept[0] = 1;
            else if (world.floorplan[posx][posy+1].getState() == 'f') {
                percept[0] = 1;
            }
        } else if (facing == 'E') {
            if  (posx-1 < 0)
                percept[0] = 1;
            else if(world.floorplan[posx-1][posy].getState() == 'f') {
                percept[0] = 1;
            }
        } else if (facing == 'S') {
            if (posy-1 < 0)
                percept[0] = 1;
            else if(world.floorplan[posx][posy-1].getState() == 'f') {
                percept[0] = 1;
            }
        } else {
            if (posx+1 == world.n)
                percept[0] = 1;
            else if(world.floorplan[posx+1][posy].getState() == 'f') {
                percept[0] = 1;
            }
        }
    }
    
    // This method will set percept indices [1-5] based on whether or not there is dirt
    // in the adjacent tiles to the agent.
    private void checkfordirt(){
        if (world.floorplan[posx][posy].getState() == 'd'){
            percept[1] = 1;
        }
        if (facing == 'N'){ // If we're facing north
            if (posy+1 != world.n) {
                if(world.floorplan[posx][posy+1].getState() == 'd') 
                    percept[2] = 1;
            }
            if (posy-1 >= 0) {
                if(world.floorplan[posx][posy-1].getState() == 'd') 
                    percept[3] = 1;
            }
            if (posx+1 != world.n){
                if(world.floorplan[posx+1][posy].getState() == 'd')
                    percept[4] = 1;
            }
            if(posx-1 >= 0){
                if(world.floorplan[posx-1][posy].getState() == 'd') 
                    percept[5] = 1;
            }
                
        } else if (facing == 'E') { // If we're facing east
            if(posx-1 >= 0){
                if(world.floorplan[posx-1][posy].getState() == 'd') // Forward
                    percept[2] = 1;
            }
            if (posx+1 != world.n){
                if(world.floorplan[posx+1][posy].getState() == 'd') // Backward
                    percept[3] = 1;
            }
            if (posy+1 != world.n){
                if(world.floorplan[posx][posy+1].getState() == 'd') // Left
                    percept[4] = 1;
            } 
            if (posy-1 >= 0){
                if(world.floorplan[posx][posy-1].getState() == 'd') // Right
                    percept[5] = 1; // If we're facing north
            }
            
        } else if (facing == 'S') { // If we're facing south
            if(posy-1 >= 0){
                if(world.floorplan[posx][posy-1].getState() == 'd') // Forward
                    percept[2] = 1;
            }
            if (posy+1 != world.n){
                if(world.floorplan[posx][posy+1].getState() == 'd') // Backward
                    percept[3] = 1;
            }
            if (posx-1 >= 0){
                if(world.floorplan[posx-1][posy].getState() == 'd') // Left
                    percept[5] = 1;
            }
            if (posx+1 != world.n){
                if(world.floorplan[posx+1][posy].getState() == 'd') // Right
                    percept[5] = 1;
            }
        } else { // We're facing west!
            if(posx+1 != world.n){
                if(world.floorplan[posx+1][posy].getState() == 'd') // Forward
                    percept[2] = 1;
            }
            if(posx-1 >= 0) {
                if(world.floorplan[posx-1][posy].getState() == 'd') // Backward
                    percept[3] = 1;
            }
            if(posy-1 >= 0) {
                if(world.floorplan[posx][posy-1].getState() == 'd') // Left
                    percept[4] = 1;
            }
            if (posy+1 != world.n){
                if(world.floorplan[posx][posy+1].getState() == 'd') // Right
                    percept[5] = 1;
            }
            
        }
    }
    
    // This method will set percept indices [6-10] based on whether or not the goal is located
    // in an adjacent tile to the agent.
    private void checkforgoal(){
        if (world.floorplan[posx][posy].getState() == 'g'){
            percept[6] = 1;
        }
        if (facing == 'N'){ // If we're facing north
            if (posy+1 != world.n) {
                if(world.floorplan[posx][posy+1].getState() == 'g') 
                    percept[7] = 1;
            }
            if (posy-1 >= 0) {
                if(world.floorplan[posx][posy-1].getState() == 'g') 
                    percept[8] = 1;
            }
            if (posx+1 != world.n){
                if(world.floorplan[posx+1][posy].getState() == 'g')
                    percept[10] = 1;
            }
            if(posx-1 >= 0){
                if(world.floorplan[posx-1][posy].getState() == 'g') 
                    percept[9] = 1;
            }
                
        } else if (facing == 'E') { // If we're facing east
            if(posx-1 >= 0){
                if(world.floorplan[posx-1][posy].getState() == 'g') // Forward
                    percept[7] = 1;
            }
            if (posx+1 != world.n){
                if(world.floorplan[posx+1][posy].getState() == 'g') // Backward
                    percept[8] = 1;
            }
            if (posy+1 != world.n){
                if(world.floorplan[posx][posy+1].getState() == 'g') // Left
                    percept[9] = 1;
            } 
            if (posy-1 >= 0){
                if(world.floorplan[posx][posy-1].getState() == 'g') // Right
                    percept[10] = 1; // If we're facing north
            }
            
        } else if (facing == 'S') { // If we're facing south
            if(posy-1 >= 0){
                if(world.floorplan[posx][posy-1].getState() == 'g') // Forward
                    percept[7] = 1;
            }
            if (posy+1 != world.n){
                if(world.floorplan[posx][posy+1].getState() == 'g') // Backward
                    percept[8] = 1;
            }
            if (posx-1 >= 0){
                if(world.floorplan[posx-1][posy].getState() == 'g') // Left
                    percept[9] = 1;
            }
            if (posx+1 != world.n){
                if(world.floorplan[posx+1][posy].getState() == 'g') // Right
                    percept[10] = 1;
            }
        } else { // We're facing west!
            if(posx+1 != world.n){
                if(world.floorplan[posx+1][posy].getState() == 'g') // Forward
                    percept[7] = 1;
            }
            if(posx-1 >= 0) {
                if(world.floorplan[posx-1][posy].getState() == 'g') // Backward
                    percept[8] = 1;
            }
            if(posy-1 >= 0) {
                if(world.floorplan[posx][posy-1].getState() == 'g') // Left
                    percept[9] = 1;
            }
            if (posy+1 != world.n){
                if(world.floorplan[posx][posy+1].getState() == 'g') // Right
                    percept[10] = 1;
            }
            
        }
    }
    
    // Actions
    
    // This moves the agent 'forward' one tile.
    private void moveforward(){
        prevposx = posx;
        prevposy = posy;
        if(facing == 'N'){
            posy++;
        } else if (facing == 'E') {
            posx--;
        } else if (facing == 'S') {
            posy--;
        } else {
            posx++;
        }
        updateWorld(prevposx, prevposy);
        context = Arrays.toString(percept) + "\tMOVE FORWARD";
    }
    
    // This will vacuum any dirt underneath the agent.
    private void clean(){
        if (world.floorplan[posx][posy].getState() == 'd') {
            world.floorplan[posx][posy].changeState('c');
            world.score += 100;
        }
        context = Arrays.toString(percept) + "\tCLEAN\t";
    }
    
    private void rotateleft(){
        if (facing == 'N') {
            facing = 'W';
        } else if (facing == 'E'){
            facing = 'N';
        } else if (facing == 'S'){
            facing = 'E';
        } else {
            facing = 'S';
        }
        context = Arrays.toString(percept) + "\tROTATELEFT";
    }
    
    private void rotateright(){
        if (facing == 'N') {
            facing = 'E';
        } else if (facing == 'E'){
            facing = 'S';
        } else if (facing == 'S'){
            facing = 'W';
        } else {
            facing = 'N';
        }
        context = Arrays.toString(percept) + "\tROTATE RIGHT";
    }
    
    private void poweroff(){
        context = Arrays.toString(percept) + "\tPOWEROFF";
        running = false;
    }
    
}
