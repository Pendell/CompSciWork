/* Alex Pendell
 * CIS 421 - Artificial Intelligence
 * Assignment 5 - ANN
 * Friday, November 22, 2019
 *
 * 
 * ASSIGNMENT 5: ARTIFICAL NEURAL NETWORK
 * This assignment we are tasked with implementing a neural network
 * that recognizes the XOR operator. That is, given two inputs the
 * network should be able to recognize (output a one (1)) if one 
 * input is a zero (0), and the other a one (1).
 * 
 *            Examples:
 * | Input 1 | Input 2 | Output |
 * |    0    |    0    |    0   |
 * |    0    |    1    |    1   |
 * |    1    |    0    |    1   |
 * |    1    |    1    |    0   |
 *
 * As illustrated, the only time the output should be one, is when
 * one (and only one) of the inputs is one, zero otherwise.
 *
 * It should be noted that For this assignment, much of my solution
 * directly inspired from the chapter eight in the textbook (both the
 * lesson, and the code example which is in C). You can find the resources
 * I used for assignment in my /resources/ directory in the root assignment
 * folder.
 */

import java.util.*;
import java.io.*;

public class NeuralNetwork {

    // How many of each layer we have
    int I_neurons; // Input neurons
    int H_neurons; // Hidden neurons
    int O_neurons; // Output neurons

    double RHO = 0.9;
    
    // The weights
    // Each indices represents the weight along the edge from
    // InputNeuron to HiddenNeuron --> weights_ItoH[Input_neuron][Hidden_neuron]
    double[][] weights_ItoH; // Input to Hidden layer weights
    double[][] weights_HtoO; // Hidden to Output weights.

    // The biases
    // The indices represents the neuron, and the value is the bias
    double[] bias_hidden;
    double[] bias_output;

    // The outputs
    double[] output_input; // Not sure if this is needed yet.
    double[] output_hidden;
    public double[] output_output;

    // The errors
    double[] error_hidden;
    double[] error_output;

    // How long do we allow it to train.
    public int epochs;
    // How often do we print the updates.
    public int update_freq;
    
    // Training data:
    int[] training_sample_one   = {0, 0, 0};
    int[] training_sample_two   = {0, 1, 1};
    int[] training_sample_three = {1, 0, 1};
    int[] training_sample_four  = {1, 1, 0};
    
    public NeuralNetwork(int in, int hid, int out, int epochs, int update_f){
        
        System.out.println("Creating a new Neural Network with: ");
        System.out.println(in + " Input Neurons");
        System.out.println(hid + " Hidden Neurons");
        System.out.println(out + " Output Neurons");
        System.out.println();
        
        I_neurons = in;
        H_neurons = hid;
        O_neurons = out;

        this.epochs = epochs;
        update_freq = update_f;

        // Initialize the arrays
        weights_ItoH = new double[I_neurons][H_neurons];
        weights_HtoO = new double[H_neurons][O_neurons];
        output_input = new double[I_neurons];
        output_hidden = new double[H_neurons];
        output_output = new double[O_neurons];
        error_hidden = new double[H_neurons];
        error_output = new double[O_neurons];
        bias_hidden = new double[H_neurons];
        bias_output = new double[O_neurons];

        Arrays.fill(bias_hidden, 1.0);
        Arrays.fill(bias_output, 1.0);
        // Randomize the weights now
        randomizeWeights();
        printWeights();

        train();
    }
    
    /* randomizeWeights()
     * This is the function that we initially call to set all of the
     * weights and biases to randomized doubles.
     * postconditions:
     *   both weight vectors will contain randomized doubles
     */
    private void randomizeWeights() {
        // Set these up so we can perform the randomization.
        // Save them here so we can change these values easily if need be
        double max = 1;
        double min = -1;

        Random r = new Random();

        // Instead of doing another set of nested for loops for the hidden
        // to output layers, we can just do another nested loop with
        // an added conditional so that we only do it once. This
        // assigns the necessary random weights to the hid->out weights
        // Without the if conditional, we would have to loop through another
        // two for loops, instead we can just do it all at once.
        for (int inputs = 0; inputs < I_neurons; inputs++){
            for (int hiddens = 0; hiddens < H_neurons; hiddens++){
                weights_ItoH[inputs][hiddens] = min + (max - min) * r.nextDouble();
                if (inputs == 0) {
                    for(int outputs = 0; outputs < O_neurons; outputs++){
                        weights_HtoO[hiddens][outputs] = min + (max-min) * r.nextDouble();
                    }
                }
                
            }
        }
        bias_hidden[0] =  min + (max-min) * r.nextDouble();
        bias_hidden[1] =  min + (max-min) * r.nextDouble();
        bias_output[0] =  min + (max-min) * r.nextDouble();
        
    }

    /* train()
     * This is the training function for the neural network. This function
     * utilizes the training_data array to train the neural network to solve
     * xor
     * postconditions:
     *   the neural network has tweaked it's weights and biases (?) to solve
     *   xor. Nice...
     */
    private void train(){;
        for (int t = 0; t < epochs; t++){
            if (update_freq == 0) continue;
            else if (t % update_freq == 0) {
                System.out.println("////////////////// EPOCH " + t + " //////////////////");
                int result;

                System.out.println("Status at beginning of epoch");
                printStats();
                
                System.out.println();
                System.out.println("Training Sample One: ");
                feedforward(training_sample_one);
                backpropagate(training_sample_one[2]);
                result = (output_output[0]> 0.5) ? 1:0;
                
                if (result == training_sample_one[2])
                    System.out.println("Success!");
                else System.out.println("Failure.");

            
                System.out.println();        
                System.out.println("Training Sample Two: ");
                feedforward(training_sample_two);
                backpropagate(training_sample_two[2]);
                result = (output_output[0]> 0.5) ? 1:0;
                System.out.println("Survey says! " + result);
                if (result == training_sample_two[2])
                    System.out.println("Success!");
                else System.out.println("Failure.");

                System.out.println();
                System.out.println("Training Sample Three: ");
                feedforward(training_sample_three);
                backpropagate(training_sample_three[2]);
                result = (output_output[0] > 0.5) ? 1:0;
                System.out.println("Survey says! " + result);
                if (result == training_sample_three[2])
                    System.out.println("Success!");
                else System.out.println("Failure.");

                System.out.println();
                System.out.println("Training Sample Four: ");
                feedforward(training_sample_four);
                backpropagate(training_sample_four[2]);
                result = (output_output[0]> 0.5) ? 1:0;
                System.out.println("Survey says! " + result);
                if (result == training_sample_four[2])
                    System.out.println("Success!");
                else System.out.println("Failure.");

                System.out.println("Status at end of epoch");
                printStats();

            }
        }
        
    }

    public double feedforward(int inp1, int inp2){
        int[] data = {inp1, inp2};
        feedforward(data);
        return output_output[0];
    }

    private void feedforward(int[] data){
        System.out.println();
        System.out.println("Feeding forward: " + data[0] + " " + data[1] + "?");
        double sum;

        // Store the inputs
        for (int i = 0; i < I_neurons; i++){
            output_input[i] = data[i];
        }
        
        // TWO LAYERS

        // Hard coding some stuff for now... getting late and it's not converging
        // Layer one:
        
        output_hidden[0] = output_input[0] * weights_ItoH[0][0];
        output_hidden[0] += output_input[1] * weights_ItoH[1][0];
        output_hidden[0] = sigmoid(output_hidden[0] + bias_hidden[0]);
        System.out.println("h" + 0 + " outputs: " + output_hidden[0]);
        
        output_hidden[1] = output_input[0] * weights_ItoH[0][1];
        output_hidden[1] += output_input[1] * weights_ItoH[1][1];
        output_hidden[1] = sigmoid(output_hidden[1] + bias_hidden[1]);
        System.out.println("h" + 1 + " outputs: " + output_hidden[1]);

        // Layer two: 
        output_output[0] = output_hidden[0] * weights_HtoO[0][0];
        output_output[0] = output_hidden[1] * weights_HtoO[1][0];
        output_output[0] = sigmoid(output_output[0] + bias_output[0]);

        // PROTOTYPING SOME STUFF //
        // LAYER ONE
        // Compute the output of the hidden neurons
        /*for(int h = 0; h < H_neurons; h++){
            sum = 0.0;
            for(int i = 0; i < I_neurons; i++){
                sum += (weights_ItoH[i][h] * output_input[i]) + bias_hidden[h];
            }
            output_hidden[h] = sigmoid(sum); // Store that output
            System.out.println("h" + h + " outputs: " + output_hidden[h]);
            }

        // LAYER TWO
        // Compute the output of the output neuron
        for(int o = 0; o < O_neurons; o++){
            sum = 0.0;
            for (int h = 0; h < H_neurons; h++) {
                sum += (weights_HtoO[h][o] * output_hidden[h]) + bias_output[o];
            }
            output_output[o] = sigmoid(sum);;
            System.out.println("The GUESS?: " + output_output[o]);
            }*/
    }

    /* backpropogate()
     * This is the function that's behind the 'magic' behind the neural
     * network. The weights are similar to sliding gauges across the neural
     * network. Some of the knobs might be far off, and some knobs might be
     * really close. The problem is that we'll need to dial each knob in by
     * a different amount. This is where the  idea of backpropogation comes
     * in.
     * precondition:
     *   The data has been fed forward in the network (i.e. feedforward() has
     *   already been executed.
     * postcondition:
     *   the neural network will have had it's weights adjusted by an amount
     *   that is proportional to how much it was wrong. 
     */ 
    private void backpropagate(int target){
        System.out.println();

        double sum = 0.0;
        double rmse = 0.0; // Root mean squared error

        double out_err, hid0_err, hid1_err;

        out_err = ((target - output_output[0]) * sigmoidDer(output_output[0]));

        hid0_err = (out_err * weights_HtoO[0][0]);
        hid1_err = (out_err * weights_HtoO[1][0]);


        weights_HtoO[0][0] += (RHO * hid0_err * output_input[1]);
        weights_HtoO[1][0] += (RHO * hid1_err * output_input[1]);
        bias_output[0] += (RHO * out_err);

        weights_ItoH[0][0] += (RHO * hid0_err * output_input[0]);
        weights_ItoH[1][0] += (RHO * hid0_err * output_input[1]);
        bias_hidden[0] += (RHO * out_err * output_hidden[0]);

        weights_ItoH[1][0] += (RHO * hid1_err * output_input[0]);
        weights_ItoH[1][0] += (RHO * hid1_err * output_input[1]);
        bias_hidden[1] += (RHO * out_err * output_hidden[1]);

        // PROTOTYPING //
        /*

        for(int o = 0; o < O_neurons; o++){
            sum = target - output_output[o];
            sum *= sum;
            rmse += sum;
        }
        rmse = Math.sqrt(rmse);

                // Error of the output layer
        for (int o = 0; o < O_neurons; o++){
            // Give the difference between the output and the error.
            System.out.println("TARGET: " + target);
            System.out.println("GUESS: " + output_output[o]);
            error_output[o] = output_output[o] * (1 - output_output[o]);
            error_output[o] *= (target - output_output[o]);
            error_output[o] *= RHO;
            System.out.println("error of this one: " + error_output[o]);
            // Error of hidden layer and update the weights            
            for (int h = 0; h < H_neurons; h++){
                error_hidden[h] = error_output[o] * output_hidden[h];
                weights_HtoO[h][o] += error_output[o] * output_hidden[h];
            }
            bias_output[o] += (RHO * error_output[o]);
        }

        for (int h = 0; h < H_neurons; h++){
            
        }*/
        

        // Update the weights from the hidden layer to input layer
        /*for (int h = 0; h < H_neurons; h++){
            for (int i = 0; i < I_neurons; i++){
                weights_ItoH[i][h] += ((RHO * error_hidden[h]) * output_input[i]);
            }
            bias_hidden[h] += (RHO * error_hidden[h]);
            }  */
    }
    
    private double sigmoid(double value){
        return 1 / (1 + Math.exp(-value));
    }

    private double sigmoidDer(double value){
        return sigmoid(value) * (1 - (sigmoid(value)));
    }

    // Print the weights for debugging purposes
    private void printWeights(){
        System.out.println("Input -> Hidden Layer");
        for (int inputs = 0; inputs < I_neurons; inputs++){
            for (int hiddens = 0; hiddens < H_neurons; hiddens++){
                System.out.println("i" + inputs + "-> h" + hiddens + ": "
                                   + weights_ItoH[inputs][hiddens]);
                
            }
        }

        System.out.println("Hidden -> Output layer");
        for (int hiddens = 0; hiddens < H_neurons; hiddens++){
            for(int outputs = 0; outputs < O_neurons; outputs++){
                System.out.println("h" + hiddens + "-> o" + outputs + ": "
                                   + weights_HtoO[hiddens][outputs]);
            }
        }
    }
    
    private void printStats(){

        
        // Print input values
        System.out.println("Input Neuron Values");
        for (int i = 0; i < I_neurons; i++){
            System.out.println("i" + i + " -> " + output_input[i]);
        }
        
        // Print input to hidden weights
        System.out.println("Weights (I -> H)");
        for (int i = 0; i < I_neurons; i++){
            for (int h = 0; h < H_neurons; h++){
                System.out.println("i" + i + "->h" + h + ": " + weights_ItoH[i][h]);
            }
        }
        
        // Print Hidden Values
        System.out.println("Hidden neuron values");
        for (int h = 0; h < H_neurons; h++){
            System.out.println("h" + h + " -> " + output_hidden[h]);
        }
        
        System.out.println("Hidden Layer Bias");
        for (int h = 0; h < H_neurons; h++){
            System.out.println("h" + h + " bias: " + bias_hidden[h]);
        }
        
        System.out.println("Weights (H -> O)");
        for (int h = 0; h < H_neurons; h++){
            for (int o = 0; o < O_neurons; o++){
                System.out.println("h" + h + "->o " + o + ": " + weights_HtoO[h][o]);
            }
        }
        
        System.out.println("Output neuron values");
        for (int o = 0; o < O_neurons; o++){
            System.out.println("o" + o + " -> " + output_input[o]);
        }
        
        System.out.println("Output neuron bias");
        for (int o = 0; o < O_neurons; o++){
            System.out.println("o" + o + " bias: " + bias_output[o]);
        }
    }
}
