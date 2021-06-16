/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      BaseStack.java
 * Description:    This program simulates a stack data structure using 
 *                 list functions. Program terminates when user enters ^D.
 */

/**
 * Class: BaseStack
 * Description: This class is for HW#4. a stack data structure using 
 * list functions. It is a subclass of List.
 * 
 * Public Functions:
 * BaseStack - initializes the fields of the stack.
 * pop - removes an item from the top of the stack.
 * push - adds an item to the top of the stack.
 * top - returns the item on the top of the stack.
 */
public class BaseStack extends List {

  /** 
   * Function Name: BaseStack
   * Description: Constructor of BaseStack which initializes the 
   * fields of the class.
   * 
   * @param sample - sample of data that will be stored in stack
   * @param caller - caller of the stack.
   */  
	public BaseStack (Base sample, String caller) {
		super (sample, caller + " calling BaseStack Ctor");
	}

  /** 
   * Function Name: pop
   * Description: Removes an item from the top of the stack.
   * 
   * @return Base object that was popped from the stack
   */ 
	public Base pop () {
		return remove (END);
	}

  /** 
   * Function Name: push
   * Description: Adds an item to the top of the stack.
   * 
   * @param element - Base object to be added to top of stack
   * @return boolean:
   * 	true: function is successful
   * 	false: function is not successful
   */ 
	public boolean push (Base element) {
		return insert (element, END);
	}

  /** 
   * Function Name: top
   * Description: Returns the item on the top of the stack.
   * 
   * @return Base object on the top of the stack
   */ 
	public Base top () {
		return view (END);
	}
}
