/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      Driver.java
 * Description:    This program is the main driver for the hash table. 
 *                 It will act as the interface for where the table's functions
 *                 can be used. Program terminates when user enters ^D.
 */

import java.io.*;

/**
 * Class: UCSDStudent
 * Description: This class is for HW#6. It holds the data of UCSD students,
 * implements various functions using the data, and tracks their memory.
 * 
 * Public Functions:
 * UCSDStudent - initializes the fields of the student.
 * jettison - jettisons (disposes) of the student.
 * equals - checks if the names of two students are equal.
 * getName - returns the name of the student.
 * hashCode - creates a hash code using the name of the student.
 * isLessThan - checks if the name of current student is less than name of
 * other student.
 * toString - creates a string representation of the student.
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
				" calling UCSDStudent Ctor");
	}

	/** 
	 * Function Name: jettison
	 * Description: Jettisons (Disposes) of the student data by removing 
	 * them from the memory.
	 */
	public void jettison () {
		tracker.jettison();
		tracker = null;
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
	 * Function Name: getName
	 * Description: Returns the name of the student.
	 *
	 * @return name of student
	 */
	public String getName () {
		return name;	
	}

	/** 
	 * Function Name: hashCode
	 * Description: Creates a hash code using the name of the student.
	 *
	 * @return hash code of student
	 */
	public int hashCode () {
		// hash code is created using ASCII sum of name
		int asciiSum = 0;
		// looping through name string
		for(int index = 0; index < name.length(); index++){
			asciiSum += name.charAt(index);
		}
		return asciiSum;
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
		return "name: " + name + " with studentNum: " + studentNum;
	}
}

public class Driver {
	private static final int EOF = -1;
	private static final int HASH_TABLE_SIZE = 5;

	public static void main (String [] args) {

		/* initialize debug states */
		HashTable.debugOff();

		/* check command line options */
		for (int index = 0; index < args.length; ++index) {
			if (args[index].equals("-x"))
				HashTable.debugOn();
		}

		/* The real start of the code */
		SymTab symtab = new SymTab (HASH_TABLE_SIZE, "Driver");
		String buffer = null;
		int command;
		long number = 0;

		System.out.print ("Initial Symbol Table:\n" + symtab);

		while (true) {
			command = 0;    // reset command each time in loop
			System.out.print ("Please enter a command:\n"
					 + "(c)heck memory, "
					 + "(i)nsert, (l)ookup, "
					 + "(o)ccupancy, (w)rite:  ");

			command = MyLib.getchar ();
			if (command == EOF) 
				break;
			MyLib.clrbuf ((char) command); // get rid of return

			switch (command) {			

			case 'c':	// check memory leaks
				Tracker.checkMemoryLeaks ();
				System.out.println ();
				break;

			case 'i':
				System.out.print (
				"Please enter UCSD Student name to insert:  ");
				buffer = MyLib.getline ();// formatted input

				System.out.print (
					"Please enter UCSD Student number:  ");

				number = MyLib.decin ();

				// remove extra char if there is any
				MyLib.clrbuf ((char) command);

				// create Student and place in symbol table
				if(!symtab.insert (
					new UCSDStudent (buffer, number))) {

					System.out.println ("Couldn't insert " 
							    + "student!!!"); 
				}
				break;

			case 'l':
				Base found;     // whether found or not

				System.out.print (
				"Please enter UCSD Student name to lookup:  ");

				buffer = MyLib.getline ();// formatted input

				UCSDStudent stu = new UCSDStudent (buffer, 0);
				found = symtab.lookup (stu);

				if (found != null) {
					System.out.println ("Student found!!!");
					System.out.println (found);
				}
				else
					System.out.println ("Student " 
						+ buffer
						+ " not there!");

				stu.jettison ();
				break;

			case 'o':	// occupancy
				System.out.println ("The occupancy of"
						    + " the hash table is "
						    + symtab.getOccupancy ());
				break;

			case 'w':
				System.out.print (
				"The Symbol Table contains:\n" + symtab);
			}
		}

		/* DON'T CHANGE THE CODE BELOW THIS LINE */
		System.out.print ("\nFinal Symbol Table:\n" + symtab);

		symtab.jettison ();
		Tracker.checkMemoryLeaks ();
	}
}
