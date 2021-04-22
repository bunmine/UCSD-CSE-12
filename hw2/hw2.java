/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      hw2.java
 * Description:    This program reads strings and integers from the user, 
 *                 processes them, and prints them back to the user. Program 
 *                 terminates when user enters ^D.
 */
/**
 * HW2
 * As you already know in java when you pass literal strings like
 * <P>
 *   writeline("a literal string\n", stream);
 * <P>
 * in java it's automatically
 * converted and treated as a String object.  Therefore 
 * the function writeline accepts literal strings and 
 * String types.  The getaline function returns a String type.
 */

import java.io.*;        // System.in and System.out
import java.util.*;      // Stack

class MyLibCharacter {
        private Character character;

        public MyLibCharacter (int ch) {
                character = Character.valueOf((char) ch);
        }

        public char charValue () {
                return character.charValue ();
        }

        public String toString () {
                return "" + character;
        }
}

/**
 * Class: hw2
 * Description: This class is for HW#2. It reads strings and integers
 * from user input, processes them, and prints back out the processed values.
 * 
 * Public Functions:
 * decout - converts positive number to decimal and displays it.
 * fputc - prints a single character.
 * hexout - converts positive number to hexadecimal and displays it.
 * newline - prints a newline character.
 * writeline - prints out a string.
 * clrbuf - clears System.in buffer.
 * decin - converts ASCII characters to decimal integers.
 * digiterror - deals with errors in user input.
 * getaline - processes characters from stdin.
 * fgetc - returns a character from input stream.
 * ungetc - returns character to input buffer,
 */
public class hw2 {
    private static final int ASCII_ZERO = 48;

    private static final int CR = 13;		// Carriage Return
    private static final int MAXLENGTH = 80;	// Max string length

    private static final int EOF = -1;		// process End Of File

    private static final long COUNT = 16;		// # of hex digits

    private static final long DECIMAL = 10;		// to indicate base 10
    private static final long HEX = 16;		// to indicate base 16

    private static final char digits[] = 	// for ASCII conversion
    new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

    private static final String DEBUG_GETALINE = 
      "[*DEBUG:  The length of the string just entered is ";

    private static final String DIGIT_STRING = "digit ";
    private static final String REENTER_NUMBER ="\nPlease reenter number: ";
    private static final String OUT_OF_RANGE = " out of range!!!\n";
    private static final String CAUSED_OVERFLOW = " caused overflow!!!\n";
    private static final String DEBUG_WRITELINE =
      "\n[*DEBUG:  The length of the string displayed is ";

    private static Stack<MyLibCharacter> InStream =
      new Stack<MyLibCharacter>();

    private static boolean debug_on = false;
    private static long hexCounter = 0; // counter for the number hex digits

    /** 
     * Function Name: baseout
     * Description: Takes in a positive number and displays in a given base. 
     * 
     * @param number - Numeric value to be displayed. 
     * @param base - Base to used to display number. 
     * @param stream - Where to display, likely System.out or System.err. 
     */  
    private static void baseout (long number, long base, PrintStream stream) {
        //String for if the number is to be converted to hexadecimal
        String hexNum = "";
        //Maximum power of base before it is greater than number
        int maxPow = 1;
        long currNum = base;
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
                if (portion <= 9){
                    fputc((char) (portion + '0'), stream);
                }
                //If the new value is greater than 9 then use ASCII conversion
                else{
                    fputc(digits[portion], stream);
                }
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
     * Function Name: clrbuf
     * Description: Removes any characters from System.in buffer.
     * 
     * @param character - most recent character read from System.in.
     * Result: System.in buffer is cleared.
     */  
    public static void clrbuf (int character) {
        //Ends loop if newline is detected
        while (character != '\n'){
            character = fgetc(System.in);
        }
    }


	  /** 
     * Function Name: decin
     * Description: Converts ASCII characters into decimal integer.
     * 
     * @return stdin converted into decimal integer
     */  
    public static long decin() {
        //The number that is inputed
        int inputNum = 0;
        //Temporary holder used in calculations
        int tempNum = 0;
        int character = fgetc(System.in);
        //If input is ^D, end program
        if (character == -1){
            return EOF;
        }
        //Ends loop if newline is detected
        while (character != '\n'){
            //Checking for non-digits
            if (!(character >= '0' && character <= '9')){
                digiterror(character, OUT_OF_RANGE);
                character = fgetc(System.in);
                continue;
            }

            //Converting into decimal integer
            tempNum = inputNum;
            inputNum = (inputNum * 10) + (character - '0');

            //Checking for overflow
            if (tempNum != inputNum / 10){
                digiterror(character, CAUSED_OVERFLOW);
                //Reset input if there is overflow
                inputNum = 0;
            }
            character = fgetc(System.in);
        }
        return inputNum;
    }


    /** 
     * Function Name: decout
     * Description: Takes in a positive number and displays it in decimal. 
     * 
     * @param number - Positive numeric value to be displayed. 
     * @param stream - Where to display, likely System.out or System.err. 
     */  
    public static void decout (long number, PrintStream stream) {  
        baseout(number, DECIMAL, stream);
    }  


    /*---------------------------------------------------------------------
    Function Name:          digiterror
    Purpose:                This function handles erroneous user input.
    Description:            This function  displays and error message to the
          user, and asks for fresh input.
    Input:                  character: The character that began the problem.
          message:  The message to display to the user.
    Result:                 The message is displayed to the user.
          The result in progress needs to be set to 0 in
          decin after the call to digiterror.
    ----------------------------------------------------------------------*/
    public static void digiterror (int character, String message) {
        /* handle error */
        clrbuf (character);

        /* output error message */
        writeline (DIGIT_STRING, System.err);
        fputc ( (char)character, System.err);
        writeline (message, System.err);

        writeline (REENTER_NUMBER, System.err);
    }


	  /** 
     * Function Name: getaline
     * Description: This function handles characters read from stdin.
     * 
     * @param message[] - Array that will hold the characters read from stdin.
     * @param maxlength - Max length of the message.
     * @return Length of the message.
     */  
    public static long getaline (char message[], int maxlength) {
        //Clear previous messages from array
        for (int i = 0; i < message.length; i++){
            message[i] = '\0';
        }
        //Length of message
        int length = 0;
        int character = fgetc(System.in);
        //If input is ^D, end program
        if (character == -1){
            return EOF;
        }
        //Ends loop if newline is detected
        while (character != '\n'){
            //Inputs read character into array
            message[length] = (char) character;
            length++;
            //Checking if message is over max length
            if (length >= maxlength - 1){
                //Terminates string
                message[length] = '\0';
                //Clears System.in buffer
                clrbuf(0);
                break;
            }
            character = fgetc(System.in);
        }
        //Terminates string if message does not reach max length
        message[length] = '\0';
        if (debug_on){
            System.err.println(DEBUG_GETALINE + length);
        }
        return length;
    }

    /** 
     * Function Name: hexout
     * Description: Takes in a positive number and displays it in hex. 
     * 
     * @param number - A positive numeric value to be displayed in hex. 
     * @param stream - Where to display, likely System.out or System.err. 
     */  
    public static void hexout (long number, PrintStream stream) {  
        // Output "0x" for hexidecimal.  
        writeline ("0x", stream);  
        baseout (number, HEX, stream);  
    }  

        /**
        * Returns a character from the input stream.
        *
        * @return  <code>char</code> 
        */
        public static int fgetc (InputStream stream) {

                char ToRet = '\0';

                // Check our local input stream first.
                //   If it's empty read from System.in
                if (InStream.isEmpty ()) {

                        try {
                                // Java likes giving the user the
                                // CR character too. Dumb, so just 
                                // ignore it and read the next character
                                // which should be the '\n'.                  
                                ToRet = (char) stream.read ();
                                if (ToRet == CR)
                                        ToRet = (char) stream.read ();
                                
                                // check for EOF
                                if ((int) ToRet == 0xFFFF)
                                        return EOF;
                        }

                        // Catch any errors in IO.
                        catch (EOFException eof) {

                                // Throw EOF back to caller to handle
                                return EOF;
                        }

                        catch (IOException ioe) {

                                writeline ("Unexpected IO Exception caught!\n",
                                                        System.out);
                                writeline (ioe.toString (), System.out);
                        }

                }

                // Else just pop it from the InStream.
                else
                        ToRet = ((MyLibCharacter) InStream.pop ()).charValue();
                return ToRet;
        }


        /**
        * Displays a single character.
        *
        * @param    Character to display.
        */
        public static void fputc(char CharToDisp, PrintStream stream) {

                // Print a single character.
                stream.print (CharToDisp);   

                // Flush the system.out buffer, now. 
                stream.flush ();
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
        int length = 0;
        //Loop through string and print every character
        for (int i = 0; i < message.length(); i++){
            fputc (message.charAt(i), stream);
            length++;
        }
        if (debug_on){
          System.err.println(DEBUG_WRITELINE + length);
        }
        return length;
    }  


    /**
    * Places back a character into the input stream buffer.
    *
    * @param    A character to putback into the input buffer stream.
    */
    public static void ungetc (int ToPutBack) {
        // Push the char back on our local input stream buffer.
        InStream.push (new MyLibCharacter (ToPutBack));
    }


    public static void main( String[] args ) {
        char buffer[] = new char[MAXLENGTH];       /* to hold string */

        long number;                  /* to hold number entered */
        long strlen;                  /* length of string */
        long base;		      /* to hold base entered */

        /* initialize debug states */
        debug_on = false;

        /* check command line options for debug display */
        for (int index = 0; index < args.length; ++index) {
            if (args[index].equals("-x"))
                debug_on = true;
        } 

        /* infinite loop until user enters ^D */
        while (true) {
            writeline ("\nPlease enter a string:  ", System.out);

            strlen = getaline (buffer, MAXLENGTH);
            newline (System.out);

            /* check for end of input */
            if ( EOF == strlen )
              break;

            writeline ("The string is:  ", System.out);
            writeline ( new String(buffer), System.out);

            writeline ("\nIts length is ", System.out);
            decout (strlen, System.out);
            newline (System.out);

            writeline ("\nPlease enter a decimal number:  ", 
            System.out);
            if ((number = decin ()) == EOF)
              break;

            writeline ("\nPlease enter a decimal base:  ", 
            System.out);
            if ((base = decin ()) == EOF)
              break;

            /* correct bases that are out of range */
            if (base < 2)
              base = 2;
            else if (base > 36)
              base = 36;

            newline (System.out);

            writeline ("Number entered in base ", System.out);
            decout (base, System.out);
            writeline (" is: ", System.out);
            baseout (number, base, System.out);

            writeline ("\nAnd in decimal is:  ", System.out);
            decout (number, System.out);

            writeline ("\nAnd in hexadecimal is:  ", System.out);
            hexout (number, System.out);

            writeline ("\nNumber entered multiplied by 8 is:  ", 
            System.out);
            decout (number << 3, System.out);
            writeline ("\nAnd in hexadecimal is:  ", System.out);
            hexout (number << 3, System.out);

            newline (System.out);
        }
    }
}
