/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      Tree.java
 * Description:    This program simulates a binary tree data structure and 
 *                 its functions. It will also track the memory usage of the
 *                 tree. Program terminates when user enters ^D.
 */

import java.io.*;

/**
 * Class: Tree
 * Description: This class is for HW#9. It simulates the binary tree data
 * structure, implements the tree's various functions, and 
 * tracks its memory. It also reads/writes the data of the tree from/to a file.
 * 
 * Public Functions:
 * debugOn  - turns on debug statements.
 * debugOff  - turns off debug statements.
 * getDebug  - returns the state of debug
 * getCost - returns cost of disk reads and writes
 * getOperation - returns number of operations
 * incrementCost - increments cost
 * incrementOperation - increment operation
 * Tree  - initializes the fields of the tree.
 * jettison  - jettisons (disposes) of the tree and its nodes.
 * write - writes data of tree into datafile
 * isEmpty  - returns if the tree is empty or not.
 * insert  - inserts an item into the tree.
 * remove  - removes an item from the tree.
 * lookup  - look ups an item in the tree.
 * resetRoot - resets the root of the tree.
 * toString  - creates a string representation of the tree.
 */
public class Tree<Whatever extends Base> {

	private static final long BEGIN = 0;

	// data fields
	private RandomAccessFile fio;	// to write to and read from disk
	private long occupancy;		// number of TNode's in the Tree
	private long root;		// position of the root of the Tree
	private String representation;	// String representation of Tree
	private Base sample;		// copy Base object in TNode's read CTor
	private TNode sampleNode;	// to call TNode searchTree
	private Tracker tracker;	// track Tree's memory
	private long treeCount;		// which Tree it is
	private static long treeCounter;// how many Tree's are allocated

	// debug flag
	private static boolean debug;

	// number of disk reads and writes
	public static long cost = 0;

	// number of insert, remove, locate operations
	public static long operation = 0;

	// debug messages
	private static final String 
		TREE = "[Tree ",
		ALLOCATE = " - Allocating]\n",
		JETTISON = " - Jettisoning]\n",
		CLOSE = "]\n",
		COST_READ = "[Cost Increment (Disk Access): Reading ",
		COST_WRITE = "[Cost Increment (Disk Access): Writing ",
		AND = " and ",
		COMPARE = " - Comparing ",
		INSERT = " - Inserting ",
		CHECK = " - Checking ",
		UPDATE = " - Updating ",
		REPLACE = " - Replacing ";

	/*
	 * PositionBox class creates a PositionBox object to wrap a long type
	 * to be passed by reference in TNode methods.
	 */
	private class PositionBox {
		public long position;	// position value to be wrapped

		/*
		 * Constructor for PositionBox object, wraps position parameter.
		 *
		 * @param position the value to be wrapped by PositionBox
		 */
		public PositionBox (long position) {
			this.position = position;
		}
	}

	/** 
	 * Function Name: Tree
	 * Description: Constructor of Tree which initializes the fields
	 * of the class.
   * 
	 * @param datafile - datafile to read/write from/to
	 * @param sample - sample of a TNode
	 * @param caller - caller of the tree
	 */
	public Tree (String datafile, Whatever sample, String caller) {
		tracker = new Tracker ("Tree", Size.of (root)
			+ Size.of (occupancy)
			+ Size.of (representation)
			+ Size.of (treeCount)
			+ Size.of (tracker)
			+ Size.of (fio)
			+ Size.of (this.sample),
			caller + " calling Tree CTor");

		// DO NOT CHANGE TRACKER CODE ABOVE

		// initializing fields
		treeCounter++;
		treeCount = treeCounter;
		this.sample = sample;
		sampleNode = new TNode(caller);
		try {
			// checking datafile
			fio = new RandomAccessFile(datafile, "rw");
			fio.seek(BEGIN);
			long begin = fio.getFilePointer();
			fio.seek(fio.length());
			long end = fio.getFilePointer();

			// checking if file is empty
			if(begin == end){
				fio.seek(BEGIN);
				fio.writeLong(root);
				fio.writeLong(occupancy);
				root = fio.getFilePointer();
				occupancy = 0;
			}
			else{
				fio.seek(BEGIN);
				root = fio.readLong();
				occupancy = fio.readLong();
			}
		}
		catch (IOException ioe) {
			System.err.println("IOException in Tree CTor");
		}

		// debug message
		if(debug)
			System.err.print(TREE+treeCount+ALLOCATE);

	}

	/**
	 * Disable debug messager
	 */
	public static void debugOff () {
		debug = false;
	}

	/**
	 * Enable debug messages
	 */
	public static void debugOn () {
		debug = true;
	}

	/**
	 * Debug accessor
	 *
	 * @return true if debug is one, false otherwise
	 */
	public static boolean getDebug () {
		return debug;
	}

	/**
	 * Getter method for cost
	 *
	 * @return number of disk reads and writes of TNode
	 */
	public long getCost () {
		return cost;
	}

	/**
	 * Getter method for operation
	 *
	 * @return number of insert, lookup, remove operations
	 */
	public long getOperation () {
		return operation;
	}

	/**
	 * Count a TNode disk read or write
	 */
	public void incrementCost () {
		cost++;
	}

	/**
	 * Count an insert, lookup, or remove
	 */
	public void incrementOperation () {
		operation++;
	}

	/** 
	 * Function Name: insert
	 * Description: Inserts an item into the tree.
	 *
	 * @param element - item to be inserted into tree
	 * @return inserted item
	 */
	public Whatever insert (Whatever element) {
		// debug message
		if(debug){
			System.err.print(TREE+treeCount+INSERT+
					element.getTrimName()+CLOSE);
		}

		incrementOperation();
		// checking is tree is empty
		if(isEmpty()){
			// create new root node if empty
			TNode rootNode = new TNode(element, "Tree.insert");
			rootNode.jettisonTNode();
		}
		else{	
			// inserting new node if not empty
			PositionBox rootBox = new PositionBox(root);
			sampleNode.searchTree(element, rootBox, "insert");
			root = rootBox.position;
		}
		return element;
	}

	/** 
	 * Function Name: isEmpty
	 * Description: Returns if the tree is empty or not
	 *
	 * @return boolean:
	 * 	true: tree is empty
	 * 	false: tree is not empty
	 */
	public boolean isEmpty () {
		return occupancy == 0;
	}

	/*
	 * jettison method for the Tree object. Untracks all memory associated
	 * with the Tree.
	 */
	public void jettison () {
		// Debug messages
		if (debug) {
			System.err.print (TREE);
			System.err.print (treeCount);
			System.err.print (JETTISON);
		}

		write (); // write the final root and occupancy to disk

		try {
			fio.close (); // close the file accessor
		} catch (IOException ioe) {
			System.err.println ("IOException in Tree's jettison");
		}

		// Jettison TNodes and then tree itself
		sampleNode.jettisonTNode ();
		sampleNode = null;
		tracker.jettison ();
		tracker = null;
		treeCounter--;
	}

	/** 
	 * Function Name: write
	 * Description: Writes the data of the tree to a datafile
	 */
	public void write () {
		// writing tree data into datafile
		try {
			fio.seek(BEGIN);
			fio.writeLong(root);
			fio.writeLong(occupancy);
		}
		catch (IOException ioe) {
			System.err.println("IOException in Tree.write");
		}
	}

	/** 
	 * Function Name: lookup
	 * Description: Look ups an item in the tree.
	 *
	 * @param element - item to be looked up in tree
	 * @return item located in tree or null if the item is not located
	 */
	public Whatever lookup (Whatever element) {
		Whatever result = null;
		incrementOperation();
		// checking if tree is empty
		if(isEmpty())
			return null;
		else{
			// looking up node
			PositionBox rootBox = new PositionBox(root);
			result = sampleNode.searchTree(element, rootBox, "lookup");
		}
		return result;
	}

	/** 
	 * Function Name: remove
	 * Description: Removes an item from the tree.
	 * 
	 * @param element - item to be removed from tree
	 * @return removed item or null if the item is not located
	 */ 
	public Whatever remove (Whatever element) {
		Whatever result = null;
		incrementOperation();
		// checking if tree is empty
		if(isEmpty())
			return null;
		else{
			// removing node from tree
			PositionBox rootBox = new PositionBox(root);
			result = sampleNode.searchTree(element, rootBox, "remove");
			root = rootBox.position;
			if(isEmpty()){
				resetRoot();
			}
		}
		return result;
	}

	/** 
	 * Function Name: resetRoot
	 * Description: Resets the root of the tree.
	 */ 
	private void resetRoot () {
		try {
			fio.seek(fio.length());
			root = fio.getFilePointer();
		} catch (IOException ioe) {
			System.err.println ("IOException in Tree's jettison");
		}
	}

	/**
	 * Creates a string representation of this tree. This method first
	 * adds the general information of this tree, then calls the
	 * recursive TNode function to add all nodes to the return string
	 *
	 * @return  String representation of this tree
	 */
	public String toString () {

		representation = "Tree " + treeCount + ":\n"
			+ "occupancy is " + occupancy + " elements.\n";

		try {
			fio.seek (fio.length ());
			long end = fio.getFilePointer ();

			long oldCost = getCost ();

			if (root != end) {
				TNode readRootNode = new TNode (root,
							"Tree's toString");
				readRootNode.writeAllTNodes ();
				readRootNode.jettisonTNode ();
				readRootNode = null;
			}

			cost = oldCost;
		} catch (IOException ioe) {
			System.err.println ("IOException in Tree's toString");
		}

		return representation;
	}

	/**
	 * Class: TNode
	 * Description: This class is for HW#9. It simulates a binary tree 
	 * node, implements the node's various functions, and 
	 * tracks its memory. It also reads/writes the data of the node from/to
	 * a file.
	 * 
	 * Functions:
	 * TNode - default constructor of the node.
	 * TNode - write constructor of the node.
	 * TNode - read constructor of the node.
	 * read - reads the data of the node from a datafile.
	 * write - writes the data of the node to a datafile.
	 * jettisonTNode - jettisons (disposes) of the node and its data.
	 * insert - inserts the node into the tree.
	 * remove - removes the node from the tree.
	 * lookup - look ups the node in the tree.
	 * replaceAndRemoveMin - replaces a node with the minimum node 
	 * needed to maintain tree structure.
	 * searchTree - reads the data of the node from the data file and
	 * performs an action.
	 * setHeightAndBalance - sets height and balance of nodes in tree.
	 * toString  - creates a string representation of the node.
	 * writeAllTNodes - prints out all nodes.
	 */
	private class TNode {
		private Whatever data;	// data to be stored in the TNode
		// 1 + height of tallest child, or 0 for leaf
		private long height;
		// left child's height - right child's height
		private long balance;
		// positions of the TNode and its left and right children
		private long left, right, position;
		private Tracker tracker;// to track memory of the tree


		// threshold to maintain in the Tree
		private static final long THRESHOLD = 2;

		/*
		 * TNode constructor to create an empty TNode
		 *
		 * @param caller method object was created in
		 */
		public TNode (String caller) {
			tracker = new Tracker ("TNode", Size.of (data)
				+ Size.of (left)
				+ Size.of (right)
				+ Size.of (position)
				+ Size.of (height)
				+ Size.of (balance)
				+ Size.of (tracker),
				caller + " calling TNode CTor");
		}

		/** 
		 * Function Name: TNode
		 * Description: Write constructor of TNode which initializes the
		 * fields of the class.
		 *
		 * @param element - the data of the node
		 * @param caller - caller of the node
		 */
		public TNode (Whatever element, String caller) {
			tracker = new Tracker ("TNode", Size.of (data)
				+ Size.of (left)
				+ Size.of (right)
				+ Size.of (position)
				+ Size.of (height)
				+ Size.of (balance)
				+ Size.of (tracker),
				caller + " calling TNode CTor");

			// DO NOT CHANGE TRACKER CODE ABOVE

			// initializing fields
			data = element;
			left = 0;
			right = 0;
			occupancy++;
			try{
				fio.seek(fio.length());
				position = fio.getFilePointer();
			}
			catch (IOException ioe) {
				System.err.println
					("IOException in TNode write Ctor");
			}
			write();
		}

		/** 
		 * Function Name: TNode
		 * Description: Read constructor of TNode which reads the
		 * fields of a node from the datafile.
		 *
		 * @param position - the position of the node
		 * @param caller - caller of the node
		 */
		@SuppressWarnings ("unchecked")
		public TNode (long position, String caller) {
			tracker = new Tracker ("TNode", Size.of (data)
				+ Size.of (left)
				+ Size.of (right)
				+ Size.of (position)
				+ Size.of (height)
				+ Size.of (balance)
				+ Size.of (tracker),
				caller + " calling TNode CTor");

			// DO NOT CHANGE TRACKER CODE ABOVE

			// reading sample data
			data = (Whatever) sample.copy();
			read(position);
		}

		/** 
		 * Function Name: read
		 * Description: Reads the data of the node from the datafile
		 */
		public void read (long position) {
			incrementCost();
			// reading data from datafile
			try {	
				fio.seek(position);
				data.read(fio);
				height = fio.readLong();
				balance = fio.readLong();
				left = fio.readLong();
				right = fio.readLong();
				this.position = fio.readLong();
			}
			catch (IOException ioe) {
				System.err.println
					("IOException in TNode.read");
			}

			// debug message
			if(debug){
				System.err.print(COST_READ+data.getTrimName()
						+CLOSE);
			}
			
		}

		/** 
		 * Function Name: write
		 * Description: Writes the data of the node to the datafile
		 */
		public void write () {
			incrementCost();
			// writing data to datafile
			try {	
				fio.seek(this.position);
				data.write(fio);
				fio.writeLong(height);
				fio.writeLong(balance);
				fio.writeLong(left);
				fio.writeLong(right);
				fio.writeLong(this.position);
			}
			catch (IOException ioe) {
				System.err.println
					("IOException in TNode.write");
			}

			// debug message
			if(debug){	
				System.err.print(COST_WRITE+data.getTrimName()
						+CLOSE);
			}

		}

		/** 
		 * Function Name: insert
		 * Description: Inserts the node into the tree.
		 *
		 * @param element - item to be inserted into tree
		 * @param positionInParentBox - holds current TNode position in
		 * parent TNode
		 * @return inserted node
		 */
		private Whatever insert (Whatever element,
				PositionBox positionInParentBox) {
			// debug message
			if(debug){
				System.err.print(TREE+treeCount+COMPARE+
						element.getTrimName()+AND+
						data.getTrimName()+CLOSE);
			}

			// checking if element is equal to data
			if(element.equals(data)){
				// debug message
				if(debug){
					System.err.print(TREE+treeCount+INSERT+
							element.getTrimName()+
							CLOSE);
				}

				// jettison previous data of node
				data.jettison();
				// insert element to node
				data = element;
				write();
			}

			// checking if element is less than data
			else if(element.isLessThan(data)){
				// checking for an empty spot for element
				if(left == 0){
					// debug message
					if(debug){
						System.err.print(TREE+treeCount
								+INSERT+element
								.getTrimName()+
								CLOSE);
					}

					// inserting new node
					TNode newNode = new TNode
						(element, "TNode.insert");
					left = newNode.position;
					newNode.jettisonTNode();
				}
				// if there's no empty spot continue locating
				else{
					// creating box for left node
					PositionBox leftBox = 
						new PositionBox(left);
					// calling searchTree on the left
					searchTree(element, leftBox, "insert");
					// update left by the new position
					left = leftBox.position;
				}
			}

			// checking if element is greater than data
			else{
				// checking for an empty spot for element
				if(right == 0){
					// debug message
					if(debug){
						System.err.print(TREE+treeCount
								+INSERT+element
								.getTrimName()+
								CLOSE);
					}

					// inserting new node
					TNode newNode = new TNode
						(element, "TNode.insert");
					right = newNode.position;
					newNode.jettisonTNode();
				}
				// if there's no empty spot continue locating
				else{
					// creating box for right node
					PositionBox rightBox = 
						new PositionBox(right);
					// calling searchTree on the right
					searchTree
						(element, rightBox, "insert");
					// update right by the new position
					right = rightBox.position;

				}
			}
			// set height and balance of node 
			setHeightAndBalance(positionInParentBox);
			return element;
		}

		/*
		 * Jettison method for TNode object, untracks memory associated
		 * with the calling TNode.
		 */
		private void jettisonTNode () {
			left = right = 0; // reset left and right positions

			// jettison the data stored
			if (data != null) {
				data.jettison ();
				data = null;
			}

			// jettison tracker
			tracker.jettison ();
			tracker = null;
		}

		/** 
		 * Function Name: lookup
		 * Description: Look ups the node in the tree.
		 *
		 * @param element - item to be looked up in tree
		 * @return item located in tree or null if the item is not 
		 * located
		 */
		@SuppressWarnings ("unchecked")
		private Whatever lookup (Whatever element) {
			// debug message
			if(debug){
				System.err.print(TREE+treeCount+COMPARE+
						element.getTrimName()+AND+
						data.getTrimName()+CLOSE);
			}

			// variable to hold result of lookup
			Whatever result = null;
			// checking if element is equal to data
			if(element.equals(data)){
				// return deep copy of data
				result = (Whatever) data.copy();
			}

			// checking if element is less than data
			else if(element.isLessThan(data)){
				// checking for an empty spot
				if(left == 0){
					// item does not exist
					result = null;
				}
				// if there's no empty spot continue locating
				else{
					// creating box for left node
					PositionBox leftBox = new 
						PositionBox(left);
					// calling searchTree on the left
					result = searchTree
						(element, leftBox, "lookup");
				}
			}

			// checking if element is greater than data
			else{
				// checking for an empty spot
				if(right == 0){
					// item does not exist
					result = null;
				}
				// if there's no empty spot continue locating
				else{
					// creating box for right node
					PositionBox rightBox = new 
						PositionBox(right);
					// calling searchTree on the right
					result = searchTree
						(element, rightBox, "lookup");
				}
			}
			return result;
		}

		/** 
		 * Function Name: remove
		 * Description: Removes the node from the tree.
		 * 
		 * @param element - item to be removed from tree
		 * @param positionInParentBox - holds current TNode position in
		 * parent TNode
		 * @param fromSHB - checks if method is being called from SHB
		 * @return removed item or null if the item is not located
		 */ 
		@SuppressWarnings("unchecked")
		private Whatever remove (Whatever element,
			PositionBox positionInParentBox, boolean fromSHB) {
		Whatever result = null;
		// debug message
		if(debug){
		  System.err.print(TREE+treeCount+COMPARE+
		      element.getTrimName()+AND+
		      data.getTrimName()+CLOSE);
		}
	  
		// checking if element is equal to data
		if(element.equals(data)){
		  // checking if leaf node
		  if(left == 0 && right == 0){
		    positionInParentBox.position = 0;
		    occupancy--;
		    result = (Whatever) data.copy();
		    return result;
		  }
		  // checking if right has child
		  else if(left == 0 && right != 0){
		    positionInParentBox.position = right;
		    occupancy--;
		    result = (Whatever) data.copy();
		    return result;
		  }
		  // checking if left has child
		  else if(left != 0 && right == 0){
		    positionInParentBox.position = left;
		    occupancy--;
		    result = (Whatever) data.copy();
		    return result;
		  }
		  // checking if there are two children
		  else{
		    // go right once
		    // creating box for right node
		    PositionBox rightBox = 
		      new PositionBox(right);	
		    result = searchTree(element, rightBox, "RARM");
		    // update right by the resulted pointer
		    right = rightBox.position;
	  
		    if(!fromSHB){
		      setHeightAndBalance
			(positionInParentBox);
		    }
		    else{
			    write();
		    }

		    data = result;
		    occupancy--;
		    return result;
		  }	
		}
	  
		// checking if element is less than data
		else if(element.isLessThan(data)){
		  // checking for an empty spot
		  if(left == 0){
		    // item does not exist
		    result = null;
		  }
		  // if there's no empty spot continue locating
		  else{
		    // creating box for left node
		    PositionBox leftBox = 
		      new PositionBox(left);
		    // calling searchTree on the left
		    result = searchTree(element, leftBox, "remove");
		    // update left by the resulted pointer
		    left = leftBox.position;
		  }
		}
	  
		// checking if element is greater than data
		else{
		  // checking for an empty spot
		  if(right == 0){
		    // item does not exist
		    result = null;
		  }
		  // if there's no empty spot continue locating
		  else{
		    // creating box for right node
		    PositionBox rightBox = 
		      new PositionBox(right);
		    // calling searchTree on the right
		    result = searchTree(element, rightBox, "remove");
		    // update right by the resulted pointer
		    right = rightBox.position;
		  }
		}
		// set height and balance
		setHeightAndBalance(positionInParentBox);
		return result;
		}

		/** 
		 * Function Name: replaceAndRemoveMin
		 * Description: Replaces a TNode with the minimum TNode in 
		 * its right subtree to maintain the Tree structure.
		 * 
		 * @param positionInParentBox - holds current TNode position in 
		 * parent TNode
		 * @return replaced TNode
		 */ 
		@SuppressWarnings ("unchecked")
		private Whatever replaceAndRemoveMin
				(PositionBox positionInParentBox) {
			// debug message
			if(debug){
				System.err.print(TREE+treeCount+CHECK+
						data.getTrimName()+CLOSE);
			}

			Whatever result = null;
			// continue moving left until null
			if(left != 0){
				// creating box for left node
				PositionBox leftBox = new PositionBox(left);
				// calling searchTree on the left
				result = searchTree
					(data, positionInParentBox, "RARM");
				// update left by the resulted pointer
				left = leftBox.position;
			}
			// found successor node
			else{
				// debug message
				if(debug){
					System.err.print(TREE+treeCount+
							REPLACE+data.
							getTrimName()
							+CLOSE);
				}

				positionInParentBox.position = 0;
				return (Whatever) data.copy();
			}
			// set height and balance
			setHeightAndBalance(positionInParentBox);
			return result;
		}

		/*
		 * Reads in TNode from the disk at positionInParentBox.position
		 * so an action may be performed on that TNode. Centralizes the
		 * the operations needed when reading a TNode from the disk.
		 *
		 * @param element the data the action is to be performed on
		 * @param positionInParentBox the PositionBox holding the
		 *        position of the TNode to be read from the disk
		 * @param action the action to be performed
		 * @return returns the result of the action
		 */
		private Whatever searchTree (Whatever element,
			PositionBox positionInParentBox, String action) {

			Whatever result = null;
			TNode readNode = new TNode 
			 (positionInParentBox.position, "searchTree " + action);

			if (action.equals ("insert")) {
				result = readNode.insert (element,
							positionInParentBox);
			}
			else if (action.equals ("lookup"))
				result = readNode.lookup (element);
			else if (action.equals ("RARM")) {
				result = readNode.replaceAndRemoveMin
							(positionInParentBox);
			}
			else if (action.equals ("remove")) {
				result = readNode.remove (element,
						positionInParentBox, false);
			}

			readNode.jettisonTNode (); // rename to jettisonTNode
			readNode = null;

			return result;
		}

		/** 
		 * Function Name: setHeightAndBalance
		 * Description: Sets the height and balance of the nodes in 
		 * the tree
		 * 
		 * @param positionInParentBox - holds current TNode position in 
		 * parent TNode
		 */ 
		@SuppressWarnings ("unchecked")
		private void setHeightAndBalance 
					(PositionBox positionInParentBox) {
			// debug message
			if(debug){
				System.err.print(TREE+treeCount+UPDATE+
						data.getTrimName()+CLOSE);
			}

			// finding left node height and right node height
			long rightHeight = 0;
			long leftHeight = 0;
			if(left != 0){
				TNode leftNode = new TNode(left, "SHAB");
				leftHeight = leftNode.height;
				leftNode.jettisonTNode();
			}
			if(right != 0){
				TNode rightNode = new TNode(right, "SHAB");
				rightHeight = rightNode.height;
				rightNode.jettisonTNode();
			}	

			// checking if both sides are null
			if(left == 0 && right == 0){
				// update balance and height
				height = 0;
				balance = 0;
			}
			// checking if only left of node is null
			else if(left == 0){
				// update balance and height
				height = rightHeight + 1;
				balance = -1 - rightHeight;
			}
			// checking if only right of node null
			else if(right == 0){
				// update balance and height
				height = leftHeight + 1;
				balance = leftHeight + 1;
			}
			// else both sides are not null
			else{
				// update balance and height
				if(leftHeight > rightHeight){
					height = leftHeight + 1;
				}
				else{
					height = rightHeight + 1;
				}
				balance = leftHeight - rightHeight;
			}

			write();

			// checking if above threshold
			if(Math.abs(balance) > THRESHOLD){
				Whatever result = (Whatever) remove(data, 
					positionInParentBox, true).copy();
				searchTree(result, positionInParentBox, 
						"insert");
			}
		}

		/**
		 * Creates a string representation of this node. Information
		 * to be printed includes this node's height, its balance,
		 * and the data its storing.
		 *
		 * @return  String representation of this node
		 */

		public String toString () {
			return "at height:  " + height + " with balance:  "
				+ balance + "  " + data + "\n";
		}

		/**
		 * Writes all TNodes to the String representation field.
		 * This recursive method performs an in-order
		 * traversal of the entire tree to print all nodes in
		 * sorted order, as determined by the keys stored in each
		 * node. To print itself, the current node will append to
		 * tree's String field.
		 */
		private void writeAllTNodes () {
			if (left != 0) {
				TNode readLeftNode = new TNode (left,
							"writeAllTNodes");
				readLeftNode.writeAllTNodes ();
				readLeftNode.jettisonTNode();
				readLeftNode = null;
			}

			representation += this;

			if (right != 0) {
				TNode readRightNode = new TNode (right,
							"writeAllTNodes");
				readRightNode.writeAllTNodes ();
				readRightNode.jettisonTNode();
				readRightNode = null;
			}
		}
	}
}
