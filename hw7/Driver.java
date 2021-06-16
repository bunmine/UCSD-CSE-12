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
 * Description: This class is for HW#7. It holds the data of UCSD students,
 * implements various functions using the data, and tracks their memory.
 * 
 * Public Functions:
 * UCSDStudent - initializes the fields of the student.
 * jettison - jettisons (disposes) of the student.
 * equals - checks if the names of two students are equal.
 * getName - returns the name of the student.
 * isLessThan - checks if the name of current student is less than the name of
 * another student.
 * toString - creates a string representation of the student.
 */
class UCSDStudent extends Base {

	public String name;
	public long studentnum;
	private Tracker tracker;

	/** 
	 * Function Name: UCSDStudent
	 * Description: Constructor of UCSDStudent which initializes the fields
	 * of the class.
	 *
	 * @param nm - name of student
	 * @param sn - student number of student
	 * @param caller - where the constructor is being called from
	 */
	public UCSDStudent (String nm, long sn, String caller) {
		// initializing fields
		name = nm;
		studentnum = sn;
		
		// DO NOT CHANGE THIS PART
		tracker = new Tracker ("UCSDStudent", 
				Size.of (name)
				+ Size.of (studentnum)
				+ Size.of (tracker),
				caller + " calling UCSDStudent Ctor");
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
	 * Function Name: equals
	 * Description: Checks if the names of two students are the same.
	 *
	 * @param object - the data of the other student
	 * @return boolean:
	 * 	true: names are the same
	 * 	false: names are not the same
	 */
	public boolean equals (Object object) {
		// checking if this student and object are the same
		if(this == object){
			return true;
		}
		// rejects object if not instance of UCSDStudent
		if(!(object instanceof UCSDStudent)){
			return false;
		}
		// wrapping object with UCSDStudent class
		UCSDStudent student = (UCSDStudent) object;
		return name.equals(student.getName());
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
		return "name:  " + name + "  studentnum:  " + studentnum;
	}
}

public class Driver {
	private static final short NULL = 0;

	public static void main (String [] args) {

	// initialize debug states
	Tree.debugOff ();

	// check command line options
	for (int index = 0; index < args.length; ++index) {
		if (args[index].equals ("-x"))
			Tree.debugOn ();
	}


	// The real start of the code
	SymTab<UCSDStudent> symtab =
		new SymTab<UCSDStudent> ("UCSDStudentTree", "main");
		String buffer = null;
		char command;
		long number = 0;
		UCSDStudent stu = new UCSDStudent (buffer, 0, "main"); 

		System.out.println ("Initial Symbol Table:\n" + symtab);

		while (true) {
			command = NULL; // reset command each time in loop
				System.out.print ("Please enter a command:\n" 
				+ "    (a)llocate, is(e)mpty, (c)heck memory,\n"
				+ "    (i)nsert, (l)ookup, (r)emove,\n"
				+ "    (w)rite:  ");

			try {
			command = MyLib.getchar ();
			MyLib.clrbuf (command); // get rid of return

			switch (command) {
			case 'a':
				System.out.print ("Please enter name of new"
				+ " Tree to allocate:  ");
				// Delete the old tree to make memeory clean
				symtab.jettison ();
				buffer = MyLib.getline (); // formatted input
				symtab = new SymTab<UCSDStudent> (buffer, 
					"main");

				break;

			case 'c':
				Tracker.checkMemoryLeaks();
				System.out.println();

				break;

			case 'e':
				if (symtab.isEmpty ()) {
					System.out.println ("Tree is empty.");
				} else {
					System.out.println ("Tree is"
					+ " not empty.");
				} 

				break;

			case 'i':
				System.out.print ("Please enter UCSD student"
				+ " name to insert:  ");

				buffer = MyLib.getline (); // formatted input

				System.out.print ("Please enter UCSD student"
				+ " number:  ");

				number = MyLib.decin ();
				MyLib.clrbuf (command); // get rid of return

				// create student and place in symbol table
				symtab.insert (new UCSDStudent (buffer, number,
					"main"));

				break;

			case 'l':
				UCSDStudent found;      // whether found or not

				System.out.print ("Please enter UCSD student"
				+ " name to lookup:  ");
				
				stu.name = MyLib.getline ();
				found = symtab.lookup (stu);

				if (found != null) {
					System.out.println ("Student found!");
					System.out.println (found);
				}
				else {
					System.out.println ("student "
					+ stu.name + " not there!");
				}

				break;

			case 'r':
				UCSDStudent removed; // data to be removed

				System.out.print ("Please enter UCSD student"
				+ " name to remove:  ");

				stu.name = MyLib.getline ();
				removed = symtab.remove (stu);

				if (removed != null) {
					System.out.println ("Student removed!"); 
					System.out.println (removed);
				}
				else {
					System.out.println ("student "
					+ stu.name + " not there!");
				}
				
				break;

			case 'w':
				System.out.println ("The Symbol Table"
				+ " contains:\n" + symtab);
			}
			}
			catch (EOFException eof) {
				break;
			}
		}

		System.out.println ("\nFinal Symbol Table:\n" + symtab);
		stu.jettison ();
		symtab.jettison ();
		Tracker.checkMemoryLeaks ();
	}
}
