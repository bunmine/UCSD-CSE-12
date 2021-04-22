/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      Calc.java
 * Description:    This program simulates a calculator by making use of stacks
 *                 from LongStack.java. Program terminates when user enters ^D.
 */

import java.io.*;      // System.in and System.out

/**
 * Class: Calc
 * Description: This class is for HW#4. It simulates a calculator (with the 
 * basic operations of addition, subtraction, multiplication, division, 
 * exponentiation, and factorial) using stacks.
 * 
 * Public Functions:
 * getIndex - find index of an operator in the operators array.
 * getOperator - extracts the operator from a word.
 * getPriority - returns the priority of an operator.
 * isOperator - checks if an item is an operator.
 * isNumber - checks if an item is a number.
 * eval - evaluates the postfix notation of a mathematical expression.
 * intopost - converts infix mathematical expressions into their postfix 
 * equivalent.
 * add - adds two operands together.
 * divide - divides the divisor by the dividend.
 * exponent - raise the base to the power exponent.
 * fact - calculates the factorial of a number.
 * mult - multiplies two numbers together.
 * sub - subtracts the minuend from the subtrahend.
 * setupword - sets up an operator to be stored in the stack.
 */
public class Calc {
	// the calculator operation interface
	interface Operation {
		long operation (long op1, long op2);
	}

	// the calculator operations in priority order
	private static Operation[] functions = new Operation[] {
		null,
		null,
		new Operation () { public long operation (long op1, long op2) 
		{ return add (op1, op2); }},
		new Operation () { public long operation (long op1, long op2) 
		{ return sub (op1, op2); }},
		new Operation () { public long operation (long op1, long op2) 
		{ return mult (op1, op2); }},
		new Operation () { public long operation (long op1, long op2) 
		{ return divide (op1, op2); }},
		new Operation () { public long operation (long op1, long op2) 
		{ return exponent (op1, op2); }},
		null,
		new Operation() { public long operation (long op1, long op2) 
		{ return fact (op1, op2); }}
	};

	// maximum size of a calculator stack
	private static final int CALCSTACKSIZE = 100;

	// constants for EOF and true/false values
	private static final int
		EOF = -1,
		TRUE = 1,
		FALSE = 0,
		BYTE = 8;

	// array of operators, according to their priority
	private static final char[] operators = "()+-*/^ !".toCharArray ();

	// sign bit for the operator when setupword is called
	private static final long SIGN_BIT = 1L << ((Long.BYTES << 3) - 1);

	/**
	 * Getter method to find index of operator in the operators array
	 * 
	 * @param word: the operator word to get the index of
	 * @return long: index of operator in the operators array
	 */
	private static int getIndex (long word) {
		return (int)(word & 0xFF00) >> BYTE;
	}

	/**
	 * Getter method to extract the operator from a word
	 * 
	 * @param word: the word to extract the operator from
	 * @return char: the operator as a char
	 */
	private static char getOperator (long word) {
		return (char) (word & 0xFF);
	}

	/**
	 * Getter method that returns the priority of an operator
	 * 
	 * @param word: the word that contains the operator
	 * @return long: priority of the operator
	 */
	private static long getPriority (long word) {
		return (word & 0xFE00);
	}

	/**
	 * Checks if an item is an operator
	 * 
	 * @param item: the item to check if it is an operator
	 * @return boolean:
	 *      true: item is an operator
	 *      false: item is not an operator
	 */
	private static boolean isOperator (long item) {
		return item < 0;
	}

	/**
	 * Checks if an item is a number
	 * 
	 * @param item: the item to check if it is a number
	 * @return boolean:
	 *      true: item is a number
	 *      false: item is not a number
	 */
	private static boolean isNumber (long item) {
		return item >= 0;
	}

	/**
	 * Function Name: eval
	 * Description: Evaluate the postfix notation of a mathematical 
	 * expression
	 * 
	 * @param stack1: the stack the calculator is initialized with
	 * @return long: the result of evaluating the mathematical expression
	 */
	public static long eval (LongStack stack1) {
		// op1 is the first operand
		// op2 is the second operand
		// word is the operator to be used
		// result is the final result of the evaluation
		long op1, op2 = 0, word, result;
		// initialize new stack for function
		LongStack stack2=new LongStack(CALCSTACKSIZE, "evalStack");
		// reverse stack1 into stack2
		while(!stack1.isEmptyStack()){
			stack2.push(stack1.pop());
		}

		// continue to evaluate until there are no more numbers
		while(!stack2.isEmptyStack()){
			// checking if next input is number
			if(isNumber(stack2.top())){
				stack1.push(stack2.pop());
			}
			// checking if next input is operator
			else if(isOperator(stack2.top())){
				// pop only 1 operand if operator is factorial
				if(getOperator(stack2.top()) == '!'){
					op1 = stack1.pop();
					word = stack2.pop();
				}
				// pop 2 operands for all other operators
				else{
					op1 = stack1.pop();
					op2 = stack1.pop();
					word = stack2.pop();
				}
				// calculate result of operation
				result = functions[getIndex(word)].operation
				(op1, op2);
				stack1.push(result);
			}
		}
		// jettison stack to prevent memory leaks
		stack2.jettisonStack();
		return stack1.pop();
	}

	/**
	 * Function Name: intopost
	 * Description: Convert infix mathematical expressions into their 
	 * postfix equivalent
	 * 
	 * @param stack1: the stack the calculator is initialized with
	 * @return int:
	 * 	1: function is successful
	 * 	0: function is not successful
	 */
	public static int intopost (LongStack stack1) {
		// initialize new stack for function
		LongStack stack2=new LongStack(CALCSTACKSIZE, "intopostStack");
		// get input from stdin
		int input = MyLib.getchar();
		// used to compare priorities of operators
		long currOperator = 0;

		// loop until newline is detected
		while(input != '\n'){
			// checking for EOF
			if(input == EOF){
				stack2.jettisonStack();
				stack1.jettisonStack();
				return EOF;
			}
			// checking for blank spaces
			else if(input == ' '){
			}
			// checking for digits
			else if (input >= '0' && input <= '9'){
				MyLib.ungetc((char) input);
				// push number as a decimal
				stack1.push(MyLib.decin());
			}
			// checking for left parenthesis
			else if(input == '('){
				stack2.push(setupword((char) input));
			}
			// checking for right parenthesis
			else if(input == ')'){
				// searching for mathcing left parenthesis
				while(getOperator(stack2.top()) != '('){
					stack1.push(stack2.pop());
				}
				// discard left parenthesis after found
				stack2.pop();
			}
			// checking for operators
			else{
				// set currOperator to the current operator 
				currOperator = setupword((char) input);
				// checking priority of previous operators
				while(!stack2.isEmptyStack()){
					if (getPriority(stack2.top()) < 
					getPriority(currOperator)){
						break;
					}
					stack1.push(stack2.pop());
				}
				// pushes current operator to stack
				stack2.push(currOperator);
			}
			input = MyLib.getchar();
		}

		// empty out leftovers of stack2 into stack1
		while(!stack2.isEmptyStack()){
			stack1.push(stack2.pop());
		}
		// jettison stack to prevent memory leaks
		stack2.jettisonStack();
		// return successful function usage
		return TRUE;
	}

	/**
	 * Add two operands together
	 * 
	 * @param augend: the first operand
	 * @param addend: the second operand
	 * 
	 * @return long: the result of the adding augend and addend
	 */
	private static long add (long augend, long addend) {
		return augend + addend;
	}

	/**
	 * Divide the divisor by the dividend
	 * 
	 * @param divisor: the long to be divided
	 * @param dividend: the long that divides the divisor
	 * 
	 * @return long: the result of dividing the divisor by the dividend
	 */
	private static long divide (long divisor, long dividend) {
		return dividend / divisor;
	}

	/**
	 * Function Name: exponent
	 * Description: Raise the base to the power exponent
	 * 
	 * @param power: the long that raises the base
	 * @param base: the long to be raised
	 *  
	 * @return long: the result of raising the base by the power exponent`
	 */
	private static long exponent (long power, long base) {
		// final result of the calculation
		long result = 1;
		// continue to multiply the base by itself until power is 0
		while (power > 0){
			result *= base;
			power--;
		}
		return result;
	}

	/**
	 * Function Name: fact
	 * Description: Calculate the factorial of a number
	 * 
	 * @param xxx: the long whose factorial will be calculated for
	 * @param ignored: the long which will be ignored by the function
	 *
	 * @return long: the result of calculating xxx factorial
	 */
	private static long fact (long xxx, long ignored) {
		// recursive function to calculate factorial
		if (xxx == 0){
			return 1;
		}
		else{
			return (xxx * fact(xxx-1, ignored));
		}
	}

	/**
	 * Multiply two numbers together
	 * 
	 * @param multiplier: the num by which the multiplicand is multiplied
	 * @param factory: the num to be multiplied
	 * 
	 * @return long: the result of the multiplication
	 */
	private static long mult (long multiplier, long multiplicand) {
		return multiplier * multiplicand;
	}

	/**
	 * Subtract the minuend from the subtrahend
	 * 
	 * @param subtrahend: the number to subtract from
	 * @param minuend: the number to subtract from subtrahend
	 * 
	 * @return long: value of subtracting the minuend from subtrahend
	 */
	private static long sub (long subtrahend, long minuend) {
		return minuend - subtrahend;
	}

	/**
	 * Function Name: setupword
	 * Description: Set up an operator to be stored in the stack
	 * 
	 * @param character: the character which represents the operator
	 * @return long: the hexadecimal long representation of the operator
	 */
	private static long setupword (char character) {
		// index is the index of the operator in the operators array
		// result is the final result of the function
		long index = 0, result;

		// searching for index of character in operators array
		for(int counter = 0; counter < operators.length; counter++){
			if(operators[counter] == character){
				index = counter;	
			}
		}

		// creating hexadecimal representation of operator
		result = SIGN_BIT | index << BYTE | character;
		return result;
	}
}
