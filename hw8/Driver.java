/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      Driver.java
 * Description:    This program is the main driver for the binary tree. 
 *                 It will act as the interface for where the tree's functions
 *                 can be used. Program terminates when user enters ^D.
 */

import java.io.*;

/**
 * Class: UCSDStudent
 * Description: This class is for HW#8. It holds the data of UCSD students,
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
		return "name:  " + name + " with studentnum:  " + studentNum;
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

	/* initialize debug states */
	Tree.debugOff ();

	/* check command line options */
	for (int index = 0; index < args.length; ++index) {
		if (args[index].equals ("-x"))
		Tree.debugOn ();
	}


	/* The real start of the code */
	SymTab<UCSDStudent> symtab = 
		new SymTab<UCSDStudent> ("Driver");

	String buffer = null;
	int command;
	long number = 0;
	UCSDStudent stu = null;

	System.out.println ("Initial Symbol Table:\n" + symtab);

	while (true) {
		command = NULL; // reset command each time in loop
		System.out.print ("Please enter a command:\n  "
		+ "(c)heck memory, is(e)mpty, "
		+ "(i)nsert, (l)ookup, "
		+ "(r)emove, (w)rite:  ");

		command = MyLib.getchar ();
		if (command == EOF) 
			break;

		MyLib.clrbuf ((char) command); // get rid of return

		switch (command) {
			case 'c':
				Tracker.checkMemoryLeaks ();
				System.out.println ();
				break;

			case 'e': 
				if (symtab.isEmpty ()) 
					System.out.println ("Tree is empty.");
				else
					System.out.println (
						"Tree is not empty.");
				break;

			case 'i':
				System.out.print
				("Please enter UCSD student name to insert:  ");

				buffer = MyLib.getline ();// formatted input

				System.out.print
					("Please enter UCSD student number:  ");

				number = MyLib.decin ();

				// get rid of return
				MyLib.clrbuf ((char) command); 

				// create student and place in symbol table
				stu = new UCSDStudent (buffer, number);

				symtab.insert (stu);
				stu = null;
				break;

			case 'l':  
				UCSDStudent found;      // whether found or not

				System.out.print
				("Please enter UCSD student name to lookup:  ");
				buffer = MyLib.getline ();// formatted input

				stu = new UCSDStudent (buffer, 0);
				found = symtab.lookup (stu);

				if (found != null) {
					System.out.println ("Student found!!!");
					System.out.println (found);

					found.jettison ();
					found = null;
				}
				else
					System.out.println ("student " + buffer
						+ " not there!");

				stu.jettison ();
				stu = null;
				break;

			case 'r':  
				UCSDStudent removed; // data to be removed

				System.out.print
				("Please enter UCSD student name to remove:  ");

				buffer = MyLib.getline ();

				stu = new UCSDStudent (buffer, 0);

				removed = symtab.remove (stu);

				if (removed != null) {
					System.out.println (
						"Student removed!!!"); 
					System.out.println (removed);

					removed.jettison ();
					removed = null;
				}
				else
					System.out.println ("student "
						+ buffer
						+ " not there!");

				stu.jettison ();
				stu = null;
				break;

			case 'w':
				System.out.print ("The Symbol Table " +
					"contains:\n" + symtab);
			}
		}

		System.out.print ("\nFinal Symbol Table:\n" + symtab);
		symtab.jettison ();

		Tracker.checkMemoryLeaks ();
	}
}

