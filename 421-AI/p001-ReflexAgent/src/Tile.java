/*

Alex Pendell
CIS 421 - Artificial Intelligence
Assignment 1: Simple Reflex Agent - Vacuum Cleaner World
September 13th, 2019

This is the Tile class. This class is used to store information about the state of individual tiles for the floorplan to store so 
that the agent can use this information to make decisions.

*/
public class Tile {

    // The list of booleans that represent the state of the tile. They are initialized to false.
    boolean agent_present = false;
    char state;
    /* Tile of Contents (heh)
    'c' indicates the tile is clean and nothing need be drawn.
    'f' indicates there is a piece of furniture on the tile.
    'd' indicates that this tile is dirty.
    */
    
    // Char state - this is what is currently sitting on the tile. See state chart above.
    public Tile(char s) {
        state = s;
    }
    
    // Change the state of the tile to char s
    public void changeState(char s) {
        state = s;
    }
    
    // Return the state of the tile.
    public char getState() {
        return state;
    }
    
    // This will set whether or not the agent is present on the current tile
    public void setAgentPresent(boolean bool) {
        agent_present = bool;
    }
    
    // Return whether or not the agent is present
    public boolean getAgentPresent() {
        return agent_present;
    }
}
