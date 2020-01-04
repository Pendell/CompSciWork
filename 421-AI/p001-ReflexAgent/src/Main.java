/*

Alex Pendell
CIS 421 - Artificial Intelligence
Assignment 1: Simple Reflex Agent - Vacuum Cleaner World
September 13th, 2019

This is the main class, where everything will be primed and ready to go for the future.
That is, the user will be prompted for the size of the world, and other starting conditions.
From there, that input will be used to create and run the 'vacuum'.

*/
 
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException{
        
        File file = new File("input1.txt");
        PrintStream log = new PrintStream(new File("prog1_log.txt"));
        Floorplan floorplan = new Floorplan(file);
        Agent vacuum = new Agent(floorplan);
        floorplan.update(vacuum);
        
        // These next few lines initalize and format the log file.
        int step = 0;
        log.println("TIME \t[B,Du,Df,Db,Dr,Dl,Gu,Gf,Gb,Gr,Gl]\tAction\t\tScore");
        log.println();
        
        // This is the primary loop for which the vacuum will be running around.
        while(floorplan.power > 0 && vacuum.running == true){
            Thread.sleep(175);
            
            log.print(step + "\t");
            
            vacuum.act();
            floorplan.update(vacuum);
            
            
            log.print(vacuum.context + "\t");
            log.println(floorplan.score);
            
            
            floorplan.score--;
            floorplan.power--;
            step++;
            
        }
        
        if (floorplan.power == 0) {
            floorplan.score -= 1000;
        }
        
        // Print the final score.
        System.out.println("Final Score: " + floorplan.score);
           
    }
}
    
