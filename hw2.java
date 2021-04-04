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

	/*---------------------------------------------------------------------
		Copy your function header and code for baseout from hw1
	---------------------------------------------------------------------*/


	// YOUR HEADER FOR clrbuf GOES HERE
        public static void clrbuf (int character) {
		// YOUR CODE GOES HERE
        }


	// YOUR HEADER FOR decin GOES HERE
        public static long decin() {
		// YOUR CODE GOES HERE
		
		return 0; // this line is to prevent compiler errors
        }


	/*---------------------------------------------------------------------
		Copy your function header and code for decout from hw1
	---------------------------------------------------------------------*/


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


	// YOUR HEADER FOR getaline GOES HERE
        public static long getaline (char message[], int maxlength ) {
		// YOUR CODE GOES HERE
	
		return 0; // this line is to prevent compiler errors
        }


	/*---------------------------------------------------------------------
		Copy your function header and code for hexout from hw1
	---------------------------------------------------------------------*/


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
                        ToRet = ((MyLibCharacter) InStream.pop ()).charValue ();
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


	/*---------------------------------------------------------------------
		Copy your function header and code for newline() from hw1
	---------------------------------------------------------------------*/


	/*---------------------------------------------------------------------
		Copy your function header and code for writeline() from hw1
	---------------------------------------------------------------------*/


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
