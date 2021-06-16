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
 * Description: This class is for HW#9. It holds the data of UCSD students,
 * implements various functions using the data, and tracks their memory.
 * It also reads/writes the data of students from/to a file.
 * 
 * Public Functions:
 * UCSDStudent - default constructor of UCSDStudent.
 * UCSDStudent - initializes the fields of the student.
 * UCSDStudent - copies the fields of another student.
 * jettison - jettisons (disposes) of the student.
 * copy - creates a copy of the current UCSD object.
 * equals - checks if the names of two students are equal.
 * getName - returns the name of the student.
 * getTrimName - returns the trimmed name of the student.
 * isLessThan - checks if the name of current student is less than the name of
 * another student.
 * toString - creates a string representation of the student.
 * read - reads the data of a student from a datafile.
 * write - writes the data of the student to a datafile.
 */
class UCSDStudent extends Base {

	private String name;
	private long studentNum;
	private Tracker tracker;
	private static long counter = 0;
	private long count;

	/*
	 * Default constructor for the UCSDStudent object.
	 * Tracks the memory associated with the UCSDStudent object.
	 */
	public UCSDStudent () {
		tracker = new Tracker ("UCSDStudent " + count + " " + name,
		Size.of (name) 
		+ Size.of (studentNum)
		+ Size.of (count)
		+ Size.of (tracker),
		"UCSDStudent Ctor");

		count = ++counter;
		name = String.format ("%1$-14s", "default");
	}

	/*
	 * Constructor for the UCSDStudent object given a name and student
	 * number. Tracks the memory associated with the UCSDStudent.
	 *
	 * @param nm the name of the UCSDStudent being created
	 * @param sn the student number of the UCSDStudent being created
	 */
	public UCSDStudent (String nm, long sn) {
		tracker = new Tracker ("UCSDStudent " + count + " " + nm,
		nm.length ()
		+ Size.of (studentNum)
		+ Size.of (count)
		+ Size.of (tracker),
		"UCSDStudent Ctor");

		count = ++counter;
		name = String.format ("%1$-14s", nm);
		studentNum = sn;
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
		name = String.format ("%1$-14s", student.name);
		studentNum = student.studentNum;  
		count = student.count;  

		tracker = new Tracker ("UCSDStudent " + student.count + " " + 
				student.name,
				Size.of (student.name)
				+ Size.of (student.studentNum)
				+ Size.of (student.count)
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
		tracker = null;
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
	 * Function Name: getTrimName
	 * Description: Returns the trimmed name of the student.
	 *
	 * @return trimmed name of student
	 */
	public String getTrimName () {
		return name.trim();	
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

	public String toString () {
		if (Tree.getDebug ())
			return "UCSDStudent #" + count + ":  name:  " 
				+ name.trim () + "  studentnum:  " +studentNum;

		return "name:  " + name.trim () + "  studentnum:  "
			+ studentNum;
	}

	/** 
	 * Function Name: read
	 * Description: Reads the data of a student from a datafile. 
	 *
	 * @param fio - datafile containing data of student and tree.
	 */
	public void read (RandomAccessFile fio) {
		// reading data
		try{
			name = fio.readUTF();
			studentNum = fio.readLong();
		}
		catch(IOException ioe){
			System.err.println("IOException in UCSDStudent Read");
		}
	}

	/** 
	 * Function Name: read
	 * Description: Writes the data of the student to a datafile. 
	 *
	 * @param fio - datafile containing data of student and tree.
	 */
	public void write (RandomAccessFile fio) {
		// writing data
		try{
			fio.writeUTF(name);
			fio.writeLong(studentNum);	
		}
		catch(IOException ioe){
			System.err.println("IOException in UCSDStudent Write");
		}
	}
}

public class Driver {
	private static final int
		NULL = 0,
		FILE = 0,
		KEYBOARD = 1,
		EOF = -1;

	public static void main (String [] args) {

		/* initialize debug states */
		Tree.debugOff ();

		/* check command line options */
		for (int index = 0; index < args.length; ++index) {
			if (args[index].equals ("-x"))
				Tree.debugOn ();
		}

		UCSDStudent sample = new UCSDStudent ();
		/* The real start of the code */
		SymTab<UCSDStudent> symtab = 
			new SymTab<UCSDStudent> ("Driver.datafile",
						sample, "Driver");

		String buffer = null;
		int command;
		long number = 0;
		UCSDStudent stu = null;

		Writer os = new FlushingPrintWriter (System.out, true);
		Reader is = new InputStreamReader (System.in);
		int readingFrom = KEYBOARD;

		System.out.println ("Initial Symbol Table:\n" + symtab);

		// SUGGESTED TEST STUDENT NUMBERS FOR VIEWING IN OCTAL DUMPS
		// 255, 32767, 65535, 8388607, 16777215
		// FF	7FFF	FFFF	7FFFFF	FFFFFF
		while (true) {
		try {
			command = NULL; // reset command each time in loop
			os.write ("Please enter a command ((c)heck memory, "
				+ "(f)ile, (i)nsert, (l)ookup, (r)emove, "
				+ "(w)rite):  ");
			command = MyLib.getchar (is);

			if (command == EOF) {
				// checking where program is reading from
				if(readingFrom == KEYBOARD)
					break;
				else{
					is = new InputStreamReader(System.in);
					os = new FlushingPrintWriter(System.out, true);
				}
			}

			if (command != EOF)
				MyLib.clrbuf ((char) command, is);

			switch (command) {
			case 'c':
				Tracker.checkMemoryLeaks ();
				System.out.println();

				break;

			case 'f':
				// changing where program is reading from
				os.write
				("Please enter file name for commands:  ");
				buffer = MyLib.getline (is);
				is = new FileReader(buffer);
				os = new FlushingFileWriter(buffer);
				readingFrom = FILE;

				break;

			case 'i':
				os.write
				("Please enter UCSD student name to insert:  ");

				buffer = MyLib.getline (is);

				os.write
					("Please enter UCSD student number:  ");

				number = MyLib.decin (is);
				MyLib.clrbuf ((char) command, is);

				// create student and place in symtab
				stu = new UCSDStudent (buffer, number);
				symtab.insert (stu);

				break;

			case 'l': 
				UCSDStudent found;

				os.write
					("Please enter UCSD student name to "
					+ "lookup:  ");
				buffer = MyLib.getline (is);

				stu = new UCSDStudent (buffer, 0);
				found = symtab.lookup (stu);
				stu.jettison ();
				stu = null;
					
				if (found != null) {
					System.out.println 
						("Student found!!!\n");
					System.out.println (found);

					found.jettison ();
					found = null;
				}
				else
					System.out.println 
						("student " + buffer 
						+ " not there!");
					
					break;

			case 'r':
				// data to be removed 	
				UCSDStudent removed;

				os.write 
				("Please enter UCSD student name to remove:  ");

				buffer = MyLib.getline (is);

				stu = new UCSDStudent (buffer, 0);
				removed = symtab.remove (stu);

				stu.jettison ();
				stu = null;

				if (removed != null) {
					System.out.println ("Student "
								+ "removed!!!"); 
					System.out.println (removed);

					removed.jettison ();
					removed = null;
				}
				else
					System.out.println 
					("student " + buffer + " not there!");

				break;

			case 'w':
				System.out.print ("The Symbol Table " +
				"contains:\n" + symtab);
				break;
			}
		}
		catch (IOException ioe) {
			System.err.println ("IOException in Driver main");
		}
		}


		System.out.print ("\nFinal Symbol Table:\n" + symtab);

		if (symtab.getOperation () != 0){
			System.out.print ("\nCost of operations:    ");
			System.out.print (symtab.getCost());
			System.out.print (" tree accesses");

			System.out.print ("\nNumber of operations:  ");
			System.out.print (symtab.getOperation());

			System.out.print ("\nAverage cost:          ");
			System.out.print (((float) (symtab.getCost ())) 
					  / (symtab.getOperation ()));
			System.out.print (" tree accesses/operation\n");
		}
		else{
			System.out.print ("\nNo cost information available.\n");
		}
		symtab.jettison ();
		sample.jettison ();
		Tracker.checkMemoryLeaks ();
	}
}
