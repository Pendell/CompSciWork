/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 5: Neural Network
 * November 22, 2019
 *
 *
 * This is the client program that initializes the neural net
 * and sets up training conditions (update frequency, number of
 * epochs)
*/


import java.util.*;

public class main {
    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);

        int epochs = 0;
        int update_frequency = 0;
        boolean gotInput = false;
        while (!gotInput){
            System.out.println("How long would you like the network to train?");
            System.out.print("Enter number of epochs: ");
            if(sc.hasNextInt()){
                epochs = sc.nextInt();
                gotInput = true;
            } else System.out.println("Please enter a valid input.");
            
        }
        gotInput = false;
        while (!gotInput){
            System.out.println("A new neural network will be created "
                               + "and trained. How many epochs would you like "
                               + "to pass before we give you an update?");
            System.out.print("Set update_frequency: ");
            if(sc.hasNextInt()){
                update_frequency = sc.nextInt();
                gotInput = true;
            } else System.out.println("Please enter a valid input.");
        
        }

        NeuralNetwork n = new NeuralNetwork(2, 2, 1, epochs, update_frequency);
        finalTest(n);
    }

    // This is the final test for the neural network after it's been init'd and trained
    public static void finalTest(NeuralNetwork n){
        System.out.println("Commencing final exam...");
        System.out.println("Testing [0, 0, 0]");
        int output;
        output = (n.feedforward(0, 0) > 0.5)?1:0;
        if (output == 0) System.out.println("Success!");
        else System.out.println("Failure");

        System.out.println("Testing [0, 1, 1]");
        output = (n.feedforward(0, 0) > 0.5)?1:0;
        if (output == 1) System.out.println("Success!");
        else System.out.println("Failure");

        System.out.println("Testing [1, 0, 1]");
        output = (n.feedforward(0, 0) > 0.5)?1:0;
        if (output == 1) System.out.println("Success!");
        else System.out.println("Failure");

        System.out.println("Testing [1, 1, 0]");
        output = (n.feedforward(0, 0) > 0.5)?1:0;
        if (output == 0) System.out.println("Success!");
        else System.out.println("Failure");
    }
}
