/*
Alex Pendell
CIS 421 - Artificial Intelligence
Assignment 1: Simple Reflex Agent - Vacuum Cleaner World
September 13th, 2019

This is the Floorplan class. This is where most of the data about the state of the world is created and stored.
The primary purpose of this class is to provide a means of translating information from the world to the agent
based on the agents location, as well as providing an agent with the means of changing the world when it decides
it's time to clean up piles of dirt.

*/


import java.util.*;
import java.io.*;

public class Floorplan {
    // This is the n for the size of the room
    int n;
    
    // Power = (n^2)*10
    int power;
    int score;
    
    // Number of furniture
    int furniture_count;  
    // Number of dirt piles
    int dirt_count;     
    // This is an array representation of the floor. See Tile.java for more details.
    Tile[][] floorplan;     
    
    // Create a floorplan of size nxn
    // Here, plan is the file that serves as the 'blueprint' for the floorplan
    public Floorplan(File plan) throws FileNotFoundException{
        Scanner sc = new Scanner(plan);
        
        // Since each line will have a different number of inputs to the next, how we parse the lines must be handled
        // differentely for each line.
        for(int i = 1; i <= 4; i++) {
            if(i == 1){
                // 1st Line: value of n, number of furniture in the room, number of piles of dirt.
                floorplan = parseLine1(sc.nextLine());
            } else if (i == 2){
                // 2nd Line: goal location of form (x, y)(example: 2,3)
                parseLine2(sc.nextLine());
            } else if (i == 3){
                // 3rd Line: Series of (x, y) coordinates determining where the furniture will be.
                ////   Note: The furniture location comes in pairs, there will be more than one piece of furniture.
                parseLine3(sc.nextLine());
            } else {
                // 4th line: Another series of (x, y) coordinates determining where the dirt will be located. 
                ////   Note: Again, this will be a series of coordinates, read above.
                parseLine4(sc.nextLine());
            }
        }
        floorplan[n-1][0].changeState('h');
    }
    
    
    // Line one has a set number of parameters (3), and parsing it is straightforward.
    // To be honest, the numbers past the first number are redundant. Since we really don't care HOW MANY dirt piles there are; just where they're located.
    // After this returns, floorplan will be a Tile[][] populated with empty tiles (No furniture or dirt, yet. That's line2).
    private Tile[][] parseLine1(String line){
        Scanner sc = new Scanner(line); 
        // First number is n -> Create a floorplan of size n*n;
        String s = sc.next();
        n = Integer.parseInt(s);
        System.out.println("The size of n is " + n);
        Tile[][] layout = new Tile[n][n];
        power = (n*n) * 10;
        
        // Populate the array with Tiles.
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                layout[i][j] = new Tile('c');
            }
        }
        
        // The next integer will be the furniture count
        s = sc.next();
        furniture_count = Integer.parseInt(s);
        System.out.println("Pieces of furniture: " + furniture_count);
        
        // The last integer of the triplet will be how many dirt piles there are.
        s = sc.next();
        dirt_count = Integer.parseInt(s);
        System.out.println("Dirt piles: " + dirt_count);
        
        return layout;
    }
    
    // The second line is just a set of two numbers: the x & y coordinates of the goal.
    private void parseLine2(String line){
        Scanner sc = new Scanner(line);
        int x = Integer.parseInt(sc.next());
        int y = Integer.parseInt(sc.next());
        System.out.println("Goal should be in location: (" + x + ", " + y + ")");
        floorplan[n-(x+1)][y].changeState('g');
    }
    
    // The third line the first line we receive that is variable in length, so we need to be more careful about how we approach.
    // HOWEVER, we do have one crucial piece of info: Since we know that this will be a list of coordinates, we can assume that
    // there will be an even number of entries on this line. This info is critical to our plan of attack.
    private void parseLine3(String line){
        Scanner sc = new Scanner(line);
        // Since this is a list of coordinates, we're going to be grabbing things two at a time. By the end, we shouldn't be left
        // with any numbers left over. Nice.
        while (sc.hasNext()){
            // Grab the x, y coordinates here
            int x = Integer.parseInt(sc.next());
            int y = Integer.parseInt(sc.next());
            floorplan[n-(x+1)][y].changeState('f');
        }
    }
    
    // The last line will be identical to line three, though we are marking where dirt is located (instead of furniture).
    private void parseLine4(String line){
        Scanner sc = new Scanner(line);
        // Since this is a list of coordinates, we're going to be grabbing things two at a time. By the end, we shouldn't be left
        // with any numbers left over. Nice.
        while (sc.hasNext()){
            // Grab the x, y coordinates here
            int x = Integer.parseInt(sc.next());
            int y = Integer.parseInt(sc.next());
            floorplan[n-(x+1)][y].changeState('d');
        }
    }
    
    public Tile[][] getplan(){
        return floorplan;
    }
    
    
    // This is the method to print the floor plan onto the terminal.
    public void update(Agent agent){
        // Two loops (loops 1 & 2) surround another for loop (3) with a nested loop (4) inside loop 3
        // Loops 1 & 2 print the top and bottom of the room.
        // Loop 3 creates the walls around the inside of the room
        // Loop 4 prints through the actual contents of the room.
        System.out.print("+");
        for(int i = 0; i < n; i++) {        // Loop 1
            System.out.print("-");
        }
        System.out.println("+\tScore: " + score +"\tPower: " + power);
        for(int i = n-1; i >= 0; i--){      // Loop 3
            System.out.print("|");
            for(int j = n-1; j >= 0; j--){  // Loop 4
                if(floorplan[j][i].agent_present)
                    System.out.print(agent.getfacing());
                else {
                    if(floorplan[j][i].getState() == 'f'){
                        System.out.print("X");
                    } else if (floorplan[j][i].getState() == 'd') {
                        System.out.print("#");
                    } else if (floorplan[j][i].getState() == 'g') {
                        System.out.print("G");
                    } else if (floorplan[j][i].getState() == 'h') {
                        System.out.print("H");
                    } else if (floorplan[j][i].getState() == 'c') {
                    System.out.print(" ");
                    }
                }
            }
            System.out.println("|");
        }
        System.out.print("+");
        for(int i = 0; i < n; i++) {        // Loop 2
            System.out.print("-");
        }
        System.out.println("+");
    }
        

}
