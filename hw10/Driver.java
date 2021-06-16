/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      Driver.java
 * Description:    This program is the main driver for the heap. 
 *                 It will act as the interface for where the heap's functions
 *                 can be used. Program terminates when user enters ^D.
 */

import java.io.*;

/**
 * Class: UCSDStudent
 * Description: This class is for HW#10. It holds the data of UCSD students,
 * implements various functions using the data, and tracks their memory.
 * 
 * Public Functions:
 * UCSDStudent - initializes the fields of the student.
 * UCSDStudent - copies the fields of another student.
 * jettison - jettisons (disposes) of the student.
 * copy - creates a copy of the current UCSD object.
 * equals - checks if the names of two students are equal.
 * getName - returns the name of the student.
 * isLessThan - checks if the name of current student is less than the name of
 * another student.
 * toString - creates a string representation of the student.
 * assign - assigns a new value to studentNum and creates new student object.
 */
class UCSDStudent extends Base {

	private String name;
	private long studentNum;
	private Tracker tracker;

	/** 
	 * Function Name: UCSDStudent
	 * Description: Constructor of UCSDStudent which initializes the fields
	 * of the class.
	 *
	 * @param nm - name of student
	 * @param sn - student number of student
	 */
	public UCSDStudent (String nm, long sn) {
		// initializing fields
		name = nm;
		studentNum = sn;
		
		// DO NOT CHANGE THIS PART
		tracker = new Tracker ("UCSDStudent", 
				Size.of (name)
				+ Size.of (studentNum)
				+ Size.of (tracker),
				"UCSDStudent Ctor");
	}

	/** 
	 * Function Name: UCSDStudent
	 * Description: Copy constructor of UCSDStudent which copies the fields
	 * of another UCSDStudent object.
	 *
	 * @param student - the other UCSDStudent object
	 */
	public UCSDStudent (UCSDStudent student) {  
		// initializing fields
		name = student.name;
		studentNum = student.studentNum;  

		tracker = new Tracker ("UCSDStudent", 
				Size.of (student.name)
				+ Size.of (student.studentNum)
				+ Size.of (student.tracker),
				"UCSDStudent Ctor"); 
	} 

	/** 
	 * Function Name: jettison
	 * Description: Jettisons (Disposes) of the student data by removing 
	 * them from the memory.
	 */
	public void jettison () {
		tracker.jettison();
	}

	/** 
	 * Function Name: copy
	 * Description: Creates a copy of the current UCSDStudent object.
	 *
	 * @return copy of the UCSDStudent object
	 */
	public Base copy () {
		return new UCSDStudent(name, studentNum);
	}

	/** 
	 * Function Name: getName
	 * Description: Returns the name of the student.
	 *
	 * @return name of student
	 */
	public String getName () {
		return name;	
	}

	/** 
	 * Function Name: equals
	 * Description: Checks if the names of two students are the same.
	 *
	 * @param object - the data of the other student
	 * @return boolean:
	 * 	true: names are the same
	 * 	false: names are not the same
	 */
	public boolean equals (Object object) {
		// checking if names are the same
		if(this == object){
			return true;
		}
		// rejects object if not instance of UCSDStudent
		if(!(object instanceof UCSDStudent)){
			return false;
		}
		// wrapping object with UCSDStudent class
		UCSDStudent student = (UCSDStudent) object;
		// recursive function
		return name.equals(student.getName());
	}

	/** 
	 * Function Name: isLessThan
	 * Description: Checks if the name of current student is less than 
	 * name of other student.
	 *
	 * @param bbb - the data of other student
	 * @return boolean:
	 * 	true: name is less than other student's name
	 * 	false: name is more than other student's name
	 */
	public boolean isLessThan (Base bbb) {
		return (name.compareTo(bbb.getName()) < 0) ? true : false;
	}

	/**
	 * Function Name: toString
	 * Description: Creates a string representation of the student.
	 *
	 * @return string representation of student
	 */
	public String toString () {
		return "name:  " + name + " with studentNum:  " + studentNum;
	}

	/** 
	 * Function Name: assign
	 * Description: Assigns a new value to studentNum and creates a new 
	 * UCSDStudent object
	 *
	 * @param sn - new value for studentNum
	 * @return new UCSDStudent object
	 */
	public UCSDStudent assign (long sn) {  
		UCSDStudent retval;        // return value  
		// give studentnum its sn value  
		studentNum = sn;  
		retval = new UCSDStudent(this);  
		return retval;  
	} 
}

public class Driver {
private static final short NULL = 0;
private static final int EOF = -1;

public static void main (String [] args) {

	String buffer = null;
	int command;
	long number = 0;
	UCSDStudent stu = null;

	// asking for heap size
	System.out.print
	("\nPlease enter the number of objects to be able to store:  ");
	command = (int) MyLib.decin();
	// exit program if command if EOF
	if(command == EOF)
		System.exit(0);
	Heap heap = new Heap(command);	

	System.out.print("Initial Heap:\n"
	+ "The Heap has 0 items:\n");

	MyLib.clrbuf ((char) command); // get rid of return

	while (true) {
		command = NULL; // reset command each time in loop

		System.out.print ("Please enter a command:  "
		+ "(c)heck memory, ");

		// print different statements based on heap status
		if(heap.getDebug() == false)
			System.out.print("(d)ebug on ");
		else
			System.out.print("(d)ebug off ");

		if(heap.isEmpty())
			System.out.print("(i)nsert, (w)rite:  ");
		else if(heap.isFull())
			System.out.print("(r)emove, (w)rite:  ");
		else
			System.out.print("(i)nsert, (r)emove, (w)rite:  ");

		command = MyLib.getchar ();

		if (command == EOF) 
			break;

		MyLib.clrbuf ((char) command); // get rid of return

		switch (command) {
			case 'c':
				Tracker.checkMemoryLeaks ();
				System.out.println ();
				break;

			case 'd': 
				// checking for debug status of heap
				if(heap.getDebug() == false) 
					heap.debugOn();
				else
					heap.debugOff();
				break;

			case 'i':
				// checking if heap is full
				if(heap.isFull())
					break;

				System.out.print
				("Please enter UCSD student name to insert:  ");

				buffer = MyLib.getline ();// formatted input

				System.out.print
					("Please enter UCSD student number:  ");

				number = MyLib.decin ();

				// get rid of return
				MyLib.clrbuf ((char) command); 

				// create student and place in heap
				stu = new UCSDStudent (buffer, number);

				heap.insert (stu);
				stu = null;
				break;

			case 'r': 
				// checking if heap is empty
				if(heap.isEmpty())
					break;

				UCSDStudent removed; // data to be removed

				removed = (UCSDStudent) heap.remove ();

				System.out.println (
					"Student removed!!!"); 
				System.out.println (removed);

				removed.jettison ();
				removed = null;

				break;

			case 'w':
				heap.write();
			}
		}

		System.out.print ("\nFinal Heap:\n");
		heap.write();
		heap.jettisonHeap();

		Tracker.checkMemoryLeaks ();
	}
}

