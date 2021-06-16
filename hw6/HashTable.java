/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      HashTable.java
 * Description:    This program simulates a hash table data structure and 
 *                 its functions. It will also track the memory usage of the
 *                 table. Program terminates when user enters ^D.
 */

/**
 * Class: HashTable
 * Description: This class is for HW#6. It simulates the hash table data
 * structure, implements the table's various functions, and 
 * tracks its memory.
 * 
 * Public Functions:
 * debugOn  - turns on debug statements.
 * debugOff  - turns off debug statements.
 * HashTable  - initializes the fields of the table.
 * jettison  - jettisons (disposes) of the table.
 * getOccupancy  - returns the occupancy of the table.
 * insert  - inserts an item into the table.
 * locate  - locates an item in the table and moves index to the item.
 * lookup  - look ups an item in the table.
 * toString  - creates a string representation of the table.
 */
public class HashTable extends Base {

	// counters, flags and constants 
	private static int counter = 0;         // number of HashTables so far
	private static boolean debug;           // allocation of debug states

	// data fields
	private long occupancy;     // how many elements are in the Hash Table
	private int size;           // size of Hash Table
	private Base table[];       // the Hash Table itself ==> array of Base
	private int tableCount;     // which hash table it is
	private Tracker tracker;    // to track memory

	// initialized by Locate function
	private int index;      // last location checked in hash table
        
	// set in insert/lookup, count of location in probe sequence
	private int count = 0;

	// messages
	private static final String DEBUG_ALLOCATE = " - Allocated]\n";
	private static final String DEBUG_LOCATE = " - Locate]\n";
	private static final String DEBUG_LOOKUP = " - Lookup]\n";
	private static final String AND = " and ";
	private static final String BUMP = "[Bumping To Next Location...]\n";
	private static final String COMPARE = " - Comparing ";
	private static final String FULL = " is full...aborting...]\n";
	private static final String FOUND_SPOT = " - Found Empty Spot]\n";
	private static final String HASH = "[Hash Table ";
	private static final String HASH_VAL = "[Hash Value Is ";
	private static final String INSERT = " - Inserting ";
	private static final String PROCESSING = "[Processing ";
	private static final String TRYING = "[Trying Index ";

	/** 
	 * Function Name: debugOn
	 * Description: Turns on debug statements.
	 */
	public static void debugOn () {
		debug = true;
	}

	/** 
	 * Function Name: debugOff
	 * Description: Turns off debug statements.
	 */
	public static void debugOff () {
		debug = false;
	}

	/** 
	 * Function Name: HashTable
	 * Description: Constructor of HashTable which initializes the fields
	 * of the class.
	 *
	 * @param sz - size of the table
	 * @param caller - caller of the table
	 */
	public HashTable (int sz, String caller) {
		// initializing fields
		index = -1;
		occupancy = 0;
		size = sz;
		table = new Base[sz];
		counter++;
		tableCount = counter;

		// debug message
		if(debug){
			System.err.print(HASH + tableCount + DEBUG_ALLOCATE);
		}

		// DO NOT CHANGE THIS PART
		tracker = new Tracker ("HashTable", 
				Size.of (index)
				+ Size.of (occupancy)
				+ Size.of (size)
				+ Size.of (table)
				+ Size.of (tableCount)
				+ Size.of (tracker),
				caller + " calling HashTable Ctor");
	}

	/** 
	 * Function Name: jettison
	 * Description: Jettisons (Disposes) of the table by removing it from
	 * the memory.
	 */
	public void jettison () {
		// loop through table
		for(int idx = 0; idx < size; idx++){
			// jettison index if not empty
			if(table[idx] != null){
				table[idx].jettison();
			}
		}
		// jettison table
		tracker.jettison();
	}

	/** 
	 * Function Name: getOccupancy
	 * Description: Returns the occupancy of the table.
	 *
	 * @return occupancy of table
	 */
	public long getOccupancy () {
		return occupancy;
	}

	/**
	 * Performs insertion into the table via delegation to the
	 * private insert method.
	 *
	 * @param   element       The element to insert.
	 * @return  true or false indicating success of insertion
	 */
	public boolean insert (Base element) {
		return insert (element, false);
	}

	/** 
	 * Function Name: insert
	 * Description: Inserts an item into the table.
	 *
	 * @param element - item to be inserted into table
	 * @param recursiveCall - whether insert should be called recursively
	 * @return boolean:
	 * 	true: insert is successful
	 * 	false: insert is not successful
	 */
	public boolean insert (Base element, boolean recursiveCall) {
		// debug message
		if(debug){
			System.err.println
				(HASH+tableCount+INSERT+element.getName()+']');
		}
		// checking if insert is called recursively
		if(recursiveCall == false){
			index = -1;
		}

		// always initialize count to 0 before locating
		count = 0;
		// locating space for element
		Base locatedItem = locate(element);
		// checking if there is an empty space
		if(locatedItem == null){
			table[index] = element;
			occupancy++;
			return true;
		}

		// checking if element is equal to the located item
		if(locatedItem.equals(element)){
			table[index].jettison();
			table[index] = element;
			return true;
		}
		// checking if located item is less than element
		else if(locatedItem.isLessThan(element)){
			// checking if table is full
			if(occupancy == size){
				// debug message
				if(debug){
					System.err.print
						(HASH + tableCount + FULL);
				}
				return false;
			}
			// if not full then bump lesser item out for element
			else{
				table[index] = element;
			}

			// debug message
			if(debug){
				System.err.print(BUMP);
			}

			// recursive insert for bumped item
			insert(locatedItem, true);
			return true;
		}

		// debug message
		if(debug){
			System.err.print(HASH + tableCount + FULL);
		}

		/* for when final item is bigger than element 
		(should only happen when table is full)*/
		return false;
	}

	/** 
	 * Function Name: locate
	 * Description: Locates an item in the table and moves index to the 
	 * item.
	 *
	 * @param element - item to be located in table
	 * @return item located in table or null if item is not located
	 */
	private Base locate (Base element) {
		// debug message
		if(debug){
			System.err.print(HASH + tableCount + DEBUG_LOCATE);
			System.err.println(PROCESSING+element.getName()+']');
			System.err.println(HASH_VAL+element.hashCode()+']');
		}

		// hash code of element
		int hash = element.hashCode();
		// increment of element
		int increment = (hash % (size - 1)) + 1;
		// checking for base case
		if(index == -1){
			index = hash % size;
		}
		else{
			// index for when recursion happens
			index = (index + increment) % size;

			// ensuring that count is in proper location in probe
			while(hash != index){
				hash = (hash + increment) % size;
				count++;
			}
		}

		// loops until probe sequence ends
		while(count < 4){
			// debug message
			if(debug){
				System.err.println(TRYING + index + ']');
			}

			// checking if there is an empty space
			if(table[index] == null){
				// debug message
				if(debug){
					System.err.print
						(HASH +tableCount+ FOUND_SPOT);
				}
				return null;
			}
			
			// debug message
			if(debug){
				System.err.println(HASH+tableCount+COMPARE+
						 element.getName()+AND+ 
						 table[index].getName()+']');
			}

			// checking if both elements are equal
			if(table[index].equals(element)){
				return table[index];
			}
			// checking if array element is less than element
			else if(table[index].isLessThan(element)){
				// checking if table is full	
				if(occupancy == size){
				}
				// if not then just return the array element
				else{
					return table[index];
				}
			}
			// incrementing index
			index = (index + increment) % size;
			// incrementing count
			count += 1;
		}
		// should only return if table is full
		return table[index];
	}

	/** 
	 * Function Name: lookup
	 * Description: Look ups an item in the table.
	 *
	 * @param element - item to be looked up in table
	 * @return item located in table or null if item is not located
	 */
	public Base lookup (Base element) {
		// debug message
		if(debug){
			System.err.print(HASH + tableCount + DEBUG_LOOKUP);
		}

		// creating base case
		index = -1;
		count = 0;

		// locating element in table
		Base locatedItem = locate(element);
		// final check if locatedItem is equal to element
		if(locatedItem.equals(element)){
			return locatedItem;
		}
		else{
			return null;
		}
	}


	/**
	 * Creates a string representation of the hash table. The method 
	 * traverses the entire table, adding elements one by one ordered
	 * according to their index in the table. 
	 *
	 * @return  String representation of hash table
	 */
	public String toString () {
		String string = "Hash Table " + tableCount + ":\n";
		string += "size is " + size + " elements, "; 
		string += "occupancy is " + occupancy + " elements.\n";

		/* go through all table elements */
		for (int index = 0; index < size; index++) {

			if (table[index] != null) {
				string += "at index " + index + ": ";
				string += "" + table[index];
				string += "\n";
			}
		}

		string += "\n";

		if(debug)
			System.err.println(tracker);

		return string;
	}
}
