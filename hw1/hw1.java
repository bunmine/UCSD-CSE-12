package hw1;

/******************************************************************************
 *                                                           Bryce Ong 
 *                                                           A16287711
 *                                                           CSE 12, SP21 
 *                                                           April 3rd, 2021 
 *                                                           cs12sp21bjn 
 *                                 Assignment 1 
 * File Name:   hw1.java 
 * Description: This program prints strings and integers to System.out and  
 *              System.err. 
 *****************************************************************************/
  
import java.io.*;  
  
/**
 * Class: hw1
 * Description: This class is for HW#1. It is used to display various
 * outputs to System.out and System.err.
 * 
 * Fields:
 * ADD - To be added in the loop of the main function
 * COUNT - Amount of hexadecimal digits that need to be displayed
 * DECIMAL - Base of decimal numbers
 * HEX - Base of hexadecimal numbers
 * digits - Array used for ASCII conversion
 * hexCounter - Counter for the number of hexadecimal digits
 * 
 * Public Functions:
 * decout - converts positive number to decimal and displays it.
 * fputc - prints a single character.
 * hexout - converts positive number to hexadecimal and displays it.
 * newline - prints a newline character.
 * writeline - prints out a string.
 */
public class hw1 {  
    private static final int ADD = 12;     // add to the var in the main loop  
    private static final int COUNT = 16;   // number of hex digits to display  
    private static final int DECIMAL = 10; // indicate base 10  
    private static final int HEX = 16;     // indicate base 16  
  
    private static final char digits[] =   // used for ASCII conversion  
    new String ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray ();  
    private static int hexCounter = 0;     // counter for the number hex digits  
  
    /** 
     * Function Name: baseout
     * Description: Takes in a positive number and displays in a given base. 
     * 
     * @param number - Numeric value to be displayed. 
     * @param base - Base to used to display number. 
     * @param stream - Where to display, likely System.out or System.err. 
     */  
    private static void baseout (int number, int base, PrintStream stream) {
        //String for if the number is to be converted to hexadecimal
        String hexNum = "";
        //Maximum power of base before it is greater than number
        int maxPow = 1;
        int currNum = base;
        //Used to find the maximum power
        while (currNum <= number){
            currNum *= base;
            maxPow += 1;
        }
        //Looping through every digit in number
        for (int i = maxPow; i > 0; i--){
            //Integer to be subtracted from number
            int subtrahend = 0;
            //New value of digit after base conversion
            int portion = 0;
            //Making value of subtrahend equal to maxPow of base
            for (int j = 0; j < i; j++){
                if (subtrahend == 0){
                    subtrahend += 1;
                }
                else{
                    subtrahend *= base;
                }
            }
            //While number is bigger than subtrahend, keep subtracting
            while (number >= subtrahend){
                number -= subtrahend;
                //Add 1 to portion everytime there is a subtraction
                portion += 1;
            }
            //Checking if the number in being converted to hexadecimal
            if (base == 16){
                hexNum += Integer.toHexString(portion).toUpperCase();
            }
            //Continue here if the base is not hexadecimal
            else{
                //Printing out new value for every digit in number
                fputc((char) (portion + '0'), stream);
            }
        }
        //Printing for hexadecimal conversion
        if (base == 16){
            for (int m = 0; m < COUNT - hexNum.length(); m++){
                fputc('0', stream);
            }
            for (int n = 0; n < hexNum.length(); n++){
                fputc(hexNum.charAt(n), stream);
            }
        }
    }
  
    /** 
     * Function Name: decout
     * Description: Takes in a positive number and displays it in decimal. 
     * 
     * @param number - Positive numeric value to be displayed. 
     * @param stream - Where to display, likely System.out or System.err. 
     */  
    public static void decout (int number, PrintStream stream) {  
        baseout(number, DECIMAL, stream);
    }  
  
    /** 
     * Function Name: fputc
     * Description: Displays a single character. 
     * 
     * @param CharToDisp - Character to display. 
     * @param stream - Where to display, likely System.out or System.err. 
     */  
    public static void fputc (char CharToDisp, PrintStream stream) {  
        // Print a single character.  
        stream.print (CharToDisp);     
  
        // Flush the system.out buffer, now.   
        stream.flush ();  
    }  
  
    /** 
     * Function Name: hexout
     * Description: Takes in a positive number and displays it in hex. 
     * 
     * @param number - A positive numeric value to be displayed in hex. 
     * @param stream - Where to display, likely System.out or System.err. 
     */  
    public static void hexout (int number, PrintStream stream) {  
        // Output "0x" for hexidecimal.  
        writeline ("0x", stream);  
        baseout (number, HEX, stream);  
    }  
  
    /** 
     * Function Name: newline
     * Description: Prints out a newline character. 
     * @param stream - Where to display, likely System.out or System.err. 
     */  
    public static void newline (PrintStream stream) {  
        //Prints newline character
        fputc('\n', stream);
    }  
  
    /** 
     * Function Name: writeline
     * Description: Prints out a string. 
     * 
     * @param message - A string to print out. 
     * @param stream - Where to display, likely System.out or System.err. 
     * @return The length of the string. 
     */  
    public static int writeline (String message, PrintStream stream) {
        //Loop through string and print every character
        for (int i = 0; i < message.length(); i++){
            fputc (message.charAt(i), stream);
        }
        return message.length();
    }  
    public static void main (String[] args) {  
        int element = 0;  
        int count = 0;  
  
        while (count < 3) {  
            element += ADD;  
            count++;  
        }  
  
        writeline ("Hello World", System.err); 
        newline (System.err); 
        writeline ("Ni Hao Shi Jie", System.out);  
        newline (System.out);  
        decout (123, System.out);  
        newline (System.out);  
        decout (0, System.out);  
        newline (System.out);  
        hexout (0xFEEDDAD, System.out);  
        newline (System.out);  
    }  
}  