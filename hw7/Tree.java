/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      Tree.java
 * Description:    This program simulates a binary tree data structure and 
 *                 its functions. It will also track the memory usage of the
 *                 tree. Program terminates when user enters ^D.
 */

/**
 * Class: Tree
 * Description: This class is for HW#7. It simulates the binary tree data
 * structure, implements the tree's various functions, and 
 * tracks its memory.
 * 
 * Public Functions:
 * debugOn  - turns on debug statements.
 * debugOff  - turns off debug statements.
 * Tree  - initializes the fields of the tree.
 * jettison  - jettisons (disposes) of the tree and its nodes.
 * jettisonAllNodes  - jettisons (disposes) of all the tree's nodes.
 * isEmpty  - returns if the tree is empty or not.
 * insert  - inserts an item into the tree.
 * remove  - removes an item from the tree.
 * lookup  - look ups an item in the tree.
 * toString  - creates a string representation of the tree.
 */
public class Tree<Whatever extends Base> {

	// data fields
	private TNode root;
	private long occupancy; 
	private String treeName;
	private String representation;
	private Tracker tracker;

	// debug flag
	private static boolean debug;

	// debug messages
	private static final String ALLOCATE = " - Allocating]\n";
	private static final String AND = " and ";
	private static final String CLOSE = "]\n";
	private static final String COMPARE = " - Comparing ";
	private static final String INSERT = " - Inserting ";
	private static final String TREE = "[Tree ";

	/** 
	 * Function Name: Tree
	 * Description: Constructor of Tree which initializes the fields
	 * of the class.
	 *
	 * @param name - name of the tree
	 * @param caller - caller of the tree
	 */
	public Tree (String name, String caller) {

		tracker = new Tracker ("Tree", Size.of (root) 
			+ Size.of (occupancy) 
			+ Size.of (treeName) 
			+ Size.of (representation) 
			+ Size.of (tracker),
			caller + " calling Tree Ctor");
		// --------- DO NOT CHANGE ABOVE ---------
	
		// initializing fields	
		root = null;
		occupancy = 0;
		treeName = name;

		// debug message
		if(debug){
			System.err.print(TREE + treeName + ALLOCATE);
		}
	}

	/** 
	 * Function Name: jettison
	 * Description: Jettisons (Disposes) of the tree and its nodes by 
	 * removing them from the memory.
	 */
	public void jettison () {
		// if tree is empty just jettison the tree
		if(isEmpty()){
			tracker.jettison();
			return;
		}	
		// jettison all nodes first then jettison tree
		jettisonAllNodes(root);
		tracker.jettison();
	}

	/** 
	 * Function Name: jettisonAllNodes
	 * Description: Jettisons (Disposes) of all the tree's nodes, in an 
	 * in-order traversal, by removing them from the memory.
	 */
	public void jettisonAllNodes (TNode root) {
		// recursion until left is null
		if(root.left != null)
			jettisonAllNodes(root.left);
		// recursion until right is null
		if(root.right != null)
			jettisonAllNodes(root.right);
		// when both sides are null it is safe to jettison node
		root.jettison();
	}

	/** 
	 * Function Name: debugOff
	 * Description: Turns off debug statements.
	 */
	public static void debugOff () {
		debug = false;
	}

	/** 
	 * Function Name: debugOn
	 * Description: Turns on debug statements.
	 */
	public static void debugOn () {
		debug = true;
	}

	/** 
	 * Function Name: insert
	 * Description: Inserts an item into the tree.
	 *
	 * @param element - item to be inserted into tree
	 * @return boolean:
	 * 	true: insert is successful
	 * 	false: insert is not successful
	 */
	public boolean insert (Whatever element) {
		// create a temp working pointer to traverse tree
		TNode working = root;
		// checking if tree is empty
		if(isEmpty()){

			// debug message
			if(debug){
				System.err.print(TREE+treeName+INSERT+
						element.getName()+CLOSE);
			}

			// create a new node to act as root
			root = new TNode(element, "Tree.insert");
			occupancy++;
			return true;
		}
		// if tree is not empty then loop through entire tree
		while(true){

			// debug message
			if(debug){
				System.err.print(TREE+treeName+COMPARE+
						element.getName()+AND+
						working.data.getName()+CLOSE);
			}

			// checking if element is equal to the pointer
			if(element.equals(working.data)){
				// jettison previous data of node
				working.data.jettison();
				// insert element to node
				working.data = element;
				// checking if the node has been deleted
				if(working.hasBeenDeleted){
					// re-inserting node
					occupancy++;
					working.hasBeenDeleted = false;
				}
				break;
			}

			// checking if element is less than pointer
			else if(element.isLessThan(working.data)){
				// checking for an empty spot for element
				if(working.left == null){
					// inserting new node
					working.left = new TNode
						(element, "Tree.insert");
					working.left.parent = working;
					occupancy++;
					break;
				}
				// if there's no empty spot continue locating
				else{
					working = working.left;
				}
			}

			// checking if element is greater than pointer
			else{
				// checking for an empty spot for element
				if(working.right == null){
					// inserting new node
					working.right = new TNode
						(element, "Tree.insert");
					working.right.parent = working;
					occupancy++;
					break;
				}
				// if there's no empty spot continue locating
				else{
					working = working.right;
				}
			}
		}

		// debug message for insertion
		if(debug){
			System.err.print(TREE+treeName+INSERT+element.getName()
					+CLOSE);
		}

		// looping backwards to the root to update balance and height
		while(working != null){

			// checking if both sides are null
			if(working.left == null && working.right == null){
			}
			// checking if only left of node is null
			else if(working.left == null){
				// update balance and height
				working.height = working.right.height + 1;
				working.balance = -1 - working.right.height;
			}
			// checking if only right of node null
			else if(working.right == null){
				// update balance and height
				working.height = working.left.height + 1;
				working.balance = working.left.height + 1;
			}
			// else both side are not null
			else{
				// update balance and height
				if(working.left.height > working.right.height){
					working.height = working.left.height+1;
				}
				else{
					working.height=working.right.height+1;
				}
				working.balance = working.left.height
					- working.right.height;
			}
			// move up the tree once
			working = working.parent;
		}
		return true;
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

	/** 
	 * Function Name: remove
	 * Description: Removes an item from the tree.
	 * 
	 * @param element - item to be removed from tree
	 * @return removed item or null if the item is not located
	 */ 
	public Whatever remove (Whatever element) {
		// create a temp working pointer to traverse tree
		TNode working = root;
		// checking if tree is empty
		if(isEmpty()){
			return null;
		}

		// if tree is not empty then loop through entire tree
		while(true){

			// debug message
			if(debug){
				System.err.print(TREE+treeName+COMPARE+
						element.getName()+AND+
						working.data.getName()+CLOSE);
			}

			// checking if element is equal to the pointer
			if(element.equals(working.data)){
				// checking if the node has been deleted
				if(working.hasBeenDeleted){
					return null;
				}
				else{
					// removing node
					working.hasBeenDeleted = true;
					occupancy--;
					return working.data;
				}
			}

			// checking if element is less than the pointer
			else if(element.isLessThan(working.data)){
				// checking for an empty space
				if(working.left == null){
					// item does not exist
					return null;			
				}
				// if there's no empty space continue locating
				else{
					working = working.left;
				}
			}

			// checking if element is greater than the pointer
			else{
				// checking for an empty space
				if(working.right == null){
					// item does not exist
					return null;			
				}

				// if there's no empty space continue locating
				else{
					working = working.right;
				}
			}	
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
		// create a temp working pointer to traverse tree
		TNode working = root;
		// checking if tree is empty
		if(isEmpty()){
			return null;
		}

		// if tree is not empty then loop through entire tree
		while(true){
			
			// debug message
			if(debug){
				System.err.print(TREE+treeName+COMPARE+
						element.getName()+AND+
						working.data.getName()+CLOSE);
			}

			// checking if element is equal to the pointer
			if(element.equals(working.data)){
				// checking if the node has been deleted
				if(working.hasBeenDeleted){
					return null;
				}
				else{
					return working.data;
				}
			}

			// checking if element is less than the pointer
			else if(element.isLessThan(working.data)){
				// checking for an empty space
				if(working.left == null){
					// item does not exist
					return null;			
				}
				// if there's no empty space continue locating
				else{
					working = working.left;
				}
			}

			// checking if element is greater than the pointer
			else{
				// checking for an empty space
				if(working.right == null){
					// item does not exist
					return null;			
				}
				// if there's no empty space continue locating
				else{
					working = working.right;
				}
			}	
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

		representation = "Tree " + treeName + ":\noccupancy is ";
		representation += occupancy + " elements.";

		if (root != null)
			root.writeAllTNodes ();

		if (debug)
			System.err.println (tracker);
		
		return representation;
	}

	/**
	 * Class: TNode
	 * Description: This class is for HW#7. It simulates a node in the 
	 * binary tree, implements the node's various functions, and 
	 * tracks its memory.
	 * 
	 * Public Functions:
	 * TNode  - initializes the fields of the node.
	 * jettison  - jettisons (disposes) of the node and its data.
	 * toString  - creates a string representation of the node.
	 * writeAllTNodes  - writes all nodes in their string representation.
	 */
	private class TNode {

		public Whatever data;
		public TNode left, right, parent;
		public boolean hasBeenDeleted;
		private Tracker tracker;

		// left child's height - right child's height
		public long balance;
		// 1 + height of tallest child, or 0 for leaf
		public long height;

		/** 
		 * Function Name: TNode
		 * Description: Constructor of TNode which initializes the fields
		 * of the class.
		 *
		 * @param element - data contained in node
		 * @param caller - caller of the node
		 */
		public TNode (Whatever element, String caller) {

			tracker = new Tracker ("TNode", Size.of (data) 
				+ Size.of (left) + Size.of (right) 
				+ Size.of (parent) 
				+ Size.of (balance) + Size.of (height),
				caller + " calling TNode Ctor");

			// --------- DO NOT CHANGE ABOVE ---------
	
			// initializing fields	
			data = element;
			left = null;
			right = null;
			parent = null;
			hasBeenDeleted = false;
			balance = 0;
			height = 0;
		}

		/** 
		 * Function Name: jettison
		 * Description: Jettisons (Disposes) of the node and its data by 
		 * removing them from the memory.
		 */
		public void jettison () {
			data.jettison();
			tracker.jettison();
		}

		/**
		* Creates a string representation of this node. Information
		* to be printed includes this node's height, its balance,
		* and the data its storing.
		*
		* @return  String representation of this node 
		*/

		public String toString () {
			return "at height:  " + height + "  with balance:  " +
				balance + "  " + data;
		}

		/**
		* Writes all TNodes to the String representation field. 
		* This recursive method performs an in-order
		* traversal of the entire tree to print all nodes in
		* sorted order, as determined by the keys stored in each
		* node. To print itself, the current node will append to
		* tree's String field.
		*/
		public void writeAllTNodes () {
			if (left != null)
				left.writeAllTNodes ();
			if (!hasBeenDeleted) 
				representation += "\n" + this;          
			if (right != null)
				right.writeAllTNodes ();
		}
	}
}
