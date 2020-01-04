/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 6: Q-Learning -- Trolls & Ponies
 * December 12, 2019
 */


public class Cell {
    
    char state;
    boolean burglarPresent = false;

    boolean traveled = false;

    /* Constructor
     * char s determines what is on the cell
     * c: tile is clean, nothing here
     * O: obstruction is on the tile
     * T: troll is on the tile
     * P: a pony is on the tile
     * E: Exit
     * B: the burglar is here! */
    public Cell(char s){
        state = s;
    }

    public void changeState(char s) {
        state = s;
    }
    
    // Return the state of the tile.
    public char getState() {
        if(state == 'c') return ' ';
        else return state;
    }
    
    // This will set whether or not the agent is present on the current tile
    public void setBurglarPresent(boolean bool) {
        burglarPresent = bool;
    }
    
    // Return whether or not the agent is present
    public boolean getBurglarPresent() {
        return burglarPresent;
    }

    // Return the reward value of the this cell
    public int getReward(){
        if(state == 'c'){
            return 2;
        } else if(state == 'T'){
            return -15;
        } else if (state == 'P'){
            return 10;
        } else if (state == 'E'){
            return 15;
        } else {
            return 0;
        }
    }

    // This is the same as the function above, except the return is returned
    // as a string all nicely spaced so it looks good when printing to the
    // terminal.
    public String getRewardForDraw(){
        if(state == 'c'){
            return "[ 2 ]";
        } else if(state == 'T'){
            return "[-15]";
        } else if (state == 'P'){
            return "[ 10]";
        } else if (state == 'E'){
            return "[ 15]";
        } else {
            return "[ 0]";
        }   
    }
}
