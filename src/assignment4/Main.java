package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Hasan Saleemi
 * has2375
 * Jerry Yang
 * jhy395
 * Fall 2018
 */

import java.lang.reflect.InvocationTargetException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.io.*;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) {
        Critter.clearWorld();
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        primaryLoop:
        while (true) {
            System.out.print("critters>");
            String fullLine = kb.nextLine();
            Scanner parseCmd = new Scanner(fullLine);
            if (!parseCmd.hasNext())
                continue;
            parseCmd.next();
            String[] cmds = fullLine.split(" ");

            switch (cmds[0]) {
                case "quit":
                    // error check
                        if (cmds.length != 1) {
                            System.out.println("error processing: " + fullLine);
                            continue;
                        }
                    // command
                        break primaryLoop;
                case "show":
                    // error check
                        if (cmds.length != 1) {
                            System.out.println("error processing: " + fullLine);
                            continue;
                        }
                    // command
                        Critter.displayWorld();
                    break;
                case "step": {
                    // error check
                        if (cmds.length != 1 && cmds.length != 2) {
                            System.out.println("error processing: " + fullLine);
                            continue;
                        }
                    // command
                        try{
                            int count = 1;
                            if(cmds.length == 2)
                                count = parseCmd.nextInt();

                            for (int i = 0; i < count; i++)
                                Critter.worldTimeStep();
                            if(DEBUG){
                                System.out.print("stepped " + count);
                                if (count != 1)
                                    System.out.println(" times");
                                else
                                    System.out.println(" time");
                            }
                        } catch(InputMismatchException ignored) {
                            System.out.println("error processing: " + fullLine);
                            continue;
                        }
                    break;
                }
                case "seed":
                    // error check
                        if (cmds.length != 2) {
                            System.out.println("error processing: " + fullLine);
                            continue;
                        }
                    // command
                    try{
                        long seed = parseCmd.nextLong();

                        if(DEBUG)
                            System.out.println("random seed set to " + seed);
                        Critter.setSeed(seed);
                    }catch(InputMismatchException ignored){
                        System.out.println("error processing: " + fullLine);
                        continue;
                    }
                    break;
                case "make": {
                    // error check
                        if (cmds.length != 2 && cmds.length != 3){
                            System.out.println("error processing: " + fullLine);
                            continue;
                        }
                    // command
                        try{
                            String className = parseCmd.next();
                            int count = 1;
                            if(cmds.length == 3)
                                count = parseCmd.nextInt();
                            try {
                                for (int i = 0; i < count; i++)
                                    Critter.makeCritter(className);
                                if(DEBUG)
                                    System.out.println("added " + count + " " + className);
                            } catch (InvalidCritterException e) {
                                System.out.println("error processing: " + fullLine);
                            }
                        }catch(InputMismatchException ignored){
                            System.out.println("error processing: " + fullLine);
                            continue;
                        }
                    break;
                }
                case "stats": {
                    // error check
                        if (cmds.length != 2 || cmds[1].equals("Critter")) {
                            System.out.println("error processing: " + fullLine);
                            continue;
                        }
                    // command
                        try{
                            String className = parseCmd.next();
                            try{
                                List<Critter> getCrits = Critter.getInstances(className);
                                Class.forName(myPackage + "." + className).getMethod("runStats", List.class).invoke(null, getCrits); // lol
                            } catch (InvalidCritterException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                System.out.println("error processing: " + fullLine);
                            }
                        }catch(InputMismatchException ignored){
                            System.out.println("error processing: " + fullLine);
                            continue;
                        }
                    break;
                }
                default: {
                    System.out.println("invalid command: " + fullLine);
                    break;
                }
            }
        }

        System.out.flush();

    }
}
