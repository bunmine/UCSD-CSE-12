/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      LongStack.java
 * Description:    This program simulates a stack data structure and its 
 *                 functions. It will also track the memory usage of the
 *                 stack. Program terminates when user enters ^D.
 */

import java.io.*;

public class LongStack {

  private static boolean debug; // debug option
  private static int stackCounter = 0; // number of stacks allocated so far

  /**
   * Class: LongStackEngine
   * Description: This class is for HW#3. It simulates the stack data 
   * structure, implements the stack's various functions, and 
   * tracks its memory.
   * 
   * Public Functions:
   * LongStackEngine - initializes the fields of the stack.
   * jettisonStackEngine - jettisons (disposes) of the stack.
   * emptyStack - empties out the content of the stack.
   * getCapacity - returns the capacity of the stack.
   * getOccupancy - returns the number of elements within the stack.
   * isEmptyStack - checks if the stack is empty or not.
   * isFullStack - checks if the stack is full or not.
   * pop - removes an item from the top of the stack.
   * push - adds an item to the top of the stack.
   * top - returns the item on the top of the stack.
   * writeStack - prints the information of the stack to stdout or stderr.
   */
   private class LongStackEngine {

  	// catastrophic error messages
  	static final String 
  		POP_EMPTY = "Popping from an empty stack!!!\n",
  		PUSH_FULL = "Pushing to a full stack!!!\n",
  		TOP_EMPTY = "Topping from an empty stack!!!\n",
  		WRITE_NONEXIST_STREAM 
  			= "Attempt to write using non-existent"
  			+ " stream!!!\n";
		
  	// Debug messages
  	// HEX messages are used for negative values, used in hw4
  	static final String
  		ALLOCATE = "[Stack %d has been allocated]\n",
  		JETTISON = "[Stack %d has been jettisoned]\n",
  		HEXPOP = "[Stack %d - Popping 0x%x]\n",
  		HEXPUSH = "[Stack %d - Pushing 0x%x]\n",
  		HEXTOP = "[Stack %d - Topping 0x%x]\n",
  		POP = "[Stack %d - Popping %d]\n",
  		PUSH = "[Stack %d - Pushing %d]\n",
  		TOP = "[Stack %d - Topping %d]\n",
  		EMPTY = "[Stack %d - Emptied]\n";

  	long[] stack;		// array to hold the data in stack order
  	int stackPointer;	// index of the last occupied space
  	int stackSize;		// size of the stack
  	int stackID;		// which stack we are using
  	Tracker tracker;	// to keep track of memory usage

    /** 
     * Function Name: LongStackEngine
     * Description: Constructor of LongStackEngine which initializes the 
     * fields of the class.
     * 
     * @param stackSize - intended size of the stack.
     * @param caller - caller of the stack.
     */  
     LongStackEngine (int stackSize, String caller) {
  	// allocate a new array to represent the stack
  	stack = new long[stackSize]; 
  
  	// hold the memory size of the LongStackEngine object
  	long size = Size.of (stackPointer) 
  	+ Size.of (stack)
  	+ Size.of (stackID)
  	+ Size.of (stackSize)
  	+ Size.of (tracker);
  	tracker = new Tracker ("LongStackEngine", size, caller);
  
        //Initializing fields
  	stackPointer = -1;
        this.stackSize = stackSize;
        stackCounter++;
        stackID = stackCounter;
  
        //Debug message
        if(debug){
          System.err.print(String.format (ALLOCATE, stackID));
        }
     }

    /** 
     * Function Name: jettisonStackEngine
     * Description: Jettisons (Disposes) of the stack by removing it
     * from the memory.
     */  
     void jettisonStackEngine () {
    	if (debug)
    		System.err.print (String.format (JETTISON, stackID));
    	tracker.jettison ();
    	stackCounter--;
     }

    /** 
     * Function Name: emptyStack
     * Description: Empties the stack by removing all of its content.
     */  
     void emptyStack () {
        //Intialize new empty array for stack
        stack = new long[stackSize];
        //Returns the stackPointer to -1
        stackPointer = -1;
  
        //Debug message
        if(debug){
          System.err.print(String.format (EMPTY, stackID));
        }
     }

    /** 
     * Function Name: getCapacity
     * Description: Returns the size of the stack.
     * 
     * @return size of stack.
     */  
     Integer getCapacity () {
        return stackSize;
     }

    /** 
     * Function Name: getOccupancy
     * Description: Returns the number of elements in the stack.
     * 
     * @return number of elements in the stack.
     */  
     Integer getOccupancy () {
        return stackPointer + 1;
     }

    /** 
     * Function Name: isEmptyStack
     * Description: Checks if the stack is empty.
     * 
     * @return true or false depending on whether the stack is empty or not.
     */  
     boolean isEmptyStack () {
        return stackPointer == -1;
     }

    /** 
     * Function Name: isFullStack
     * Description: Checks if the stack is full.
     * 
     * @return true or false depending on whether the stack is full or not.
     */  
     boolean isFullStack () {
        return stackPointer >= stackSize - 1;
     }

    /** 
     * Function Name: pop
     * Description: Removes an item from the top of the stack.
     * 
     * @return item that was popped from the stack.
     */  
     Long pop () {
        //Catastrophic error message for when stack is empty
        if(isEmptyStack()){
          System.err.print(POP_EMPTY);
          return null;
        }
        //Popped item from top of stack
        long poppedItem = stack[stackPointer];
        stackPointer--;
  
        //Debug message
        if(debug){
	  if(poppedItem >= 0){
		System.err.print(String.format (POP, stackID, poppedItem));
	  }
	  else{
		System.err.print(String.format(HEXPOP, stackID, poppedItem));
	  }
        }
        return poppedItem;
     }

    /** 
     * Function Name: push
     * Description: Adds an item to the top of the stack.
     * 
     * @param item - new item to be added to top of stack.
     * @return true or false depending on whether push was successful.
     */  
     boolean push (long item) {
        //Catastrophic error message for when stack is full
        if (isFullStack()){
          System.err.print(PUSH_FULL);
          return false;
        }
        stackPointer++;
        //Set top of stack to item
        stack[stackPointer] = item;
  
        //Debug message
        if(debug){
          if(item >= 0){
		System.err.print(String.format(PUSH, stackID, item));
	  }
	  else{
		System.err.print(String.format(HEXPUSH, stackID, item));
	  }

        }
        return true;
     }

    /** 
     * Function Name: top
     * Description: Returns the item on the top of the stack.
     * 
     * @return item on the top of the stack. 
     */  
     Long top () {
        //Catastrophic error message for when stack is full
        if(isEmptyStack()){
          System.err.print(TOP_EMPTY);
          return null;
        }
  
        //Debug message
        if(debug){
	  if(stack[stackPointer] >= 0){
		System.err.print(String.format(TOP, stackID, 
		stack[stackPointer]));
	  }
	  else{
		System.err.print(String.format(HEXTOP, stackID, 
		stack[stackPointer]));
	  }
        }
        return stack[stackPointer];
     }

		void writeStack (PrintStream stream) {

			int index = 0;	// index into the stack

			if (stream == null) {
				System.err.print (WRITE_NONEXIST_STREAM);
				return;
			}

			int stackOccupancy = getOccupancy ();

			if (stream.equals (System.err)) {
				stream.print (
				"\nStack " + stackID + ":\n"
				+ "Stack's capacity is " + stackSize + ".\n"
				+ "Stack has "
				+ stackOccupancy + " item(s) in it.\n"
				+ "Stack can store "
				+ (stackSize - stackOccupancy) 
				+ " more item(s).\n");
				Tracker.checkMemoryLeaks ();
			}

			for (index = 0; index < stackOccupancy; index++) {
				if (stream.equals (System.err))
					stream.print (String.format (
						"Value on stack is |0x%x|\n", 
						stack[index]));
				else {
					if (stack[index] < 0)
						stream.print (String.format (
							"%c ", 
							(byte) stack[index]));
					else
						stream.print (String.format (
							"%d ", stack[index]));
				}
			}
		}
	}

	// -------------------- DO NOT EDIT BELOW THIS LINE -------------------- 
	// PROVIDED INFRASTRUCTURE BELOW, YOUR CODE SHOULD GO ABOVE
	// CHANGING THE CODE BELOW WILL RESULT IN POINT DEDUCTIONS

	// catastrophic error messages
	private static final String 
	CAPACITY_NONEXIST = "Capacity check from a non-existent stack!!!\n",
	EMPTY_NONEXIST = "Emptying a non-existent stack!!!\n",
	ISEMPTY_NONEXIST = "Isempty check from a non-existent stack!!!\n",
	ISFULL_NONEXIST = "Isfull check from a non-existent stack!!!\n",
	OCCUPANCY_NONEXIST = "Occupancy check from a non-existent stack!!!\n",
	POP_NONEXIST = "Popping from a non-existent stack!!!\n",
	PUSH_NONEXIST = "Pushing to a non-existent stack!!!\n",
	TOP_NONEXIST = "Topping from a non-existent stack!!!\n",
	WRITE_NONEXIST_STACK = "Writing to a non-existent stack!!!\n";

	private LongStackEngine stackEngine; // the object that holds the data

	private boolean exists () {
		return !(stackEngine == null);
	}

	public LongStack (int stackSize, String caller) {
		stackEngine = new LongStackEngine (stackSize, caller);
	}

	// Debug state methods
	public static void debugOn () {
		debug = true;
	}

	public static void debugOff () {
		debug = false;
	}

	public boolean jettisonStack () {

		// ensure stack exists
		if (!exists ())
			return false;

		stackEngine.jettisonStackEngine ();
		stackEngine = null;
		return true;
	}

	public void emptyStack () {

		// ensure stack exists
		if (!exists ()) {
			System.err.print (EMPTY_NONEXIST);
			return;
		}
		
		stackEngine.emptyStack ();
	}
	
	public Integer getCapacity () {
			
		// ensure stack exists
		if (!exists ()) {
			System.err.print (CAPACITY_NONEXIST);
			return null;
		}
		
		return stackEngine.getCapacity ();
	}

	public Integer getOccupancy () {

		// ensure stack exists
		if (!exists ()) {
			System.err.print (OCCUPANCY_NONEXIST);
			return null;
		}

		return stackEngine.getOccupancy ();
	}

	public boolean isEmptyStack () {

		// ensure stack exists
		if (!exists ()) {
			System.err.print (ISEMPTY_NONEXIST);
			return false;
		}

		return stackEngine.isEmptyStack ();
	}

	public boolean isFullStack () {

		// ensure stack exists
		if (!exists ()) {
			System.err.print (ISFULL_NONEXIST);
			return false;
		}

		return stackEngine.isFullStack ();
	}

	public Long pop () {

		// ensure stack exists
		if (!exists ()) {
			System.err.print (POP_NONEXIST);
			return null;
		}

		return stackEngine.pop ();
	}

	public boolean push (long item) {

		// ensure stack exists
		if (!exists ()) {
			System.err.print (PUSH_NONEXIST);
			return false;
		}

		return stackEngine.push (item);
	}

	public Long top () {

		// ensure stack exists
		if (!exists ()) {
			System.err.print (TOP_NONEXIST);
			return null;
		}

		return stackEngine.top ();
	}

	public void writeStack (PrintStream stream) {

		// ensure stack exists
		if (!exists ()) {
			System.err.print (WRITE_NONEXIST_STACK);
		return;
		}

		stackEngine.writeStack (stream);
	}
}
