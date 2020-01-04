package main;

import java.util.*;
import java.io.*;


public class WordCounter{

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("No arguments given.");
        } else {
            for(int i = 0; i < args.length; i++) {
                File file = new File(args[i]);
                parsefile(file);
            }
        }
    }

    public static void parsefile(File f) throws FileNotFoundException, IOException{
        TreeMap<String, Integer> tree = new TreeMap<String, Integer> ();
        BufferedReader in = new BufferedReader(new FileReader(f));

        // We have 3 cases as far as what we're reading. We're either reading:
        // 1. A sequence of decimal integers.
        // 2. A sequence of characters followed by either more characters or decimal                    integers.
        // 3. Or a punctuation symbol.
        
        // Getting the first character here.
        int echo = in.read();
        String word = "";

        // Do this while we have not read the end of the document.
        while (echo != -1) {
            
            // This loop will skip whitespace and unwanted characters.
            while ((echo<33 || echo > 122) && echo != -1){
                echo = in.read();
            }
            
            // Check to see if it's an integer.
            if (echo >= 48 && echo <= 57) {
                // This loop will append to the end of word the necessary sequence
                // breaking when we read a character we don't want.
                while(echo >= 48 && echo <= 57) {if (tree.get(word) == null)
                    word += (char)echo;
                    echo = in.read();
                }
            
                if (!tree.containsKey(word)) {
                    tree.put(word, 1);
                } else {
                    int value = tree.get(word);
                    tree.put(word, value+1);
                }

            // Check if it's a sequence of characters
            } else  if ((echo >= 65 && echo <= 90) || (echo >= 97 && echo <= 122)) {
                // This loop will append to the end of word the necessary sequence
                // breaking when we read a character we don't want.
                while ((echo >= 65 && echo <= 90) || (echo >= 97 && echo <= 122) ||
                    (echo >= 48 && echo <= 57)) {
                    word += (char)echo;
                    echo = in.read();
                }

                // Put the word in the map, or increment it otherwise.
                if (!tree.containsKey(word)) {
                    tree.put(word, 1);
                } else {
                    int value = tree.get(word);
                    tree.put(word, value+1);
                }

            //Echo is punctuation
            } else if (echo != -1) {
                word += (char)echo;
                if (!tree.containsKey(word)) {
                    tree.put(word, 1);
                } else {
                    int value = tree.get(word);
                    tree.put(word, value+1);
                }
                echo = in.read();
                
            }
            word = "";
        }
        
        for(Map.Entry<String, Integer> entries : tree.entrySet()) {
            System.out.println(entries.getKey() + " : " + entries.getValue());
        }
        
    }
}  



