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
 * Description: This class is for HW#8. It simulates the binary tree data
 * structure, implements the tree's various functions, and 
 * tracks its memory.
 * 
 * Public Functions:
 * debugOn  - turns on debug statements.
 * debugOff  - turns off debug statements.
 * Tree  - initializes the fields of the tree.
 * jettison  - jettisons (disposes) of the tree and its nodes.
 * isEmpty  - returns if the tree is empty or not.
 * insert  - inserts an item into the tree.
 * remove  - removes an item from the tree.
 * lookup  - look ups an item in the tree.
 * toString  - creates a string representation of the tree.
 */
public class Tree<Whatever extends Base> {

	/* data fields */
	private TNode root;
	private long occupancy; 
	private String representation;
	private long treeCount;
	private Tracker tracker;
	private static long treeCounter;

	/* debug flag */
	private static boolean debug;

	/* debug messages */
	private static final String ALLOCATE = " - Allocating]\n";
	private static final String JETTISON = " - Jettisoning]\n";
	private static final String AND = " and ";
	private static final String CLOSE = "]\n";
	private static final String COMPARE = " - Comparing ";
	private static final String INSERT = " - Inserting ";
	private static final String CHECK = " - Checking ";
	private static final String UPDATE = " - Updating ";
	private static final String REPLACE = " - Replacing ";
	private static final String TREE = "[Tree ";

	private class PointerBox {
		public TNode pointer;

		public PointerBox (TNode pointer) {
			this.pointer = pointer;
		}
	}

	/** 
	 * Function Name: Tree
	 * Description: Constructor of Tree which initializes the fields
	 * of the class.
	 *
	 * @param caller - caller of the tree
	 */
	public Tree (String caller) {
		tracker = new Tracker ("Tree", Size.of (root)
		+ Size.of (occupancy)
		+ Size.of (representation)
		+ Size.of (treeCount)
		+ Size.of (treeCounter)
		+ Size.of (tracker),
		caller + " calling Tree CTor");

		// DO NOT CHANGE THIS PART ABOVE

		// initializing fields	
		root = null;
		occupancy = 0;
		treeCounter++;
		treeCount = treeCounter;

		// debug message
		if(debug){
			System.err.print(TREE + treeCount + ALLOCATE);
		}
	}

	/** 
	 * Function Name: jettison
	 * Description: Jettisons (Disposes) of the tree and its nodes by 
	 * removing them from the memory.
	 */
	public void jettison () {
		// debug message
		if(debug){
			System.err.print(TREE + treeCount + JETTISON);
		}

		// if tree is empty just jettison the tree
		if(isEmpty()){
			tracker.jettison();
			return;
		}	
		// if not empty jettison all nodes first then jettison tree
		root.jettisonAllTNodes();
		tracker.jettison();
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
	 * Function Name: isEmpty
	 * Description: Returns if the tree is empty or not
	 *
	 * @return boolean:
	 * 	true: tree is empty
	 * 	false: tree is not empty
	 */
	public boolean isEmpty () {
		return root==null;
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
		// checking if tree is empty
		if(isEmpty()){
			// debug message
			if(debug){
				System.err.print(TREE+treeCount+INSERT
					       +element.getName()+CLOSE);
			}

			// create a new node to act as root
			root = new TNode(element, "Tree.insert");
			occupancy++;
			return true;
		}

		// put the root pointer into a box
		PointerBox rootBox = new PointerBox(root);
		// if tree is not empty delegate insert to root
		root.insert(element, rootBox);
		// update root to new pointer
		root = rootBox.pointer;
		return true;
	}

	/** 
	 * Function Name: lookup
	 * Description: Look ups an item in the tree.
	 *
	 * @param element - item to be looked up in tree
	 * @return item located in tree or null if the item is not located
	 */
	public Whatever lookup (Whatever element) {
		// checking if tree is empty
		if(isEmpty()){
			return null;
		}
		// if tree is not empty delegate lookup to root
		return root.lookup(element);
	}

	/** 
	 * Function Name: remove
	 * Description: Removes an item from the tree.
	 * 
	 * @param element - item to be removed from tree
	 * @return removed item or null if the item is not located
	 */ 
	public Whatever remove (Whatever element) {
		// checking if tree is empty
		if(isEmpty()){
			return null;
		}
		// put the root pointer into a box
		PointerBox rootBox = new PointerBox(root);
		// if tree is not empty delegate remove to root
		// removedNodeData holds the data of the node that was removed
		Whatever removedNodeData=root.remove(element, rootBox, false);
		// update root to new pointer
		root = rootBox.pointer;
		return removedNodeData;
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

		if (root != null)
		root.writeAllTNodes();

		return representation;
	}

	/**
	 * Class: TNode
	 * Description: This class is for HW#8. It simulates a binary tree 
	 * node, implements the node's various functions, and 
	 * tracks its memory.
	 * 
	 * Functions:
	 * TNode  - initializes the fields of the node.
	 * jettisonTNodeOnly - jettisons (disposes) only of the node.
	 * jettisonTNodeAndData  - jettisons (disposes) of the node and its 
	 * data.
	 * jettisonAllTNodes  - jettisons (disposes) of all the nodes.
	 * insert  - inserts the node into the tree.
	 * remove  - removes the node from the tree.
	 * lookup  - look ups the node in the tree.
	 * replaceAndRemoveMin - replaces a node with the minimum node 
	 * needed to maintain tree structure.
	 * setHeightAndBalance - sets height and balance of nodes in tree.
	 * toString  - creates a string representation of the node.
	 * writeAllNodes - prints out all nodes.
	 */
	private class TNode {                
		private Whatever data;
		private TNode left, right;
		private Tracker tracker;

		/* left child's height - right child's height */
		private long balance;
		/* 1 + height of tallest child, or 0 for leaf */
		private long height;

		private static final long THRESHOLD = 2;

		/** 
		 * Function Name: TNode
		 * Description: Constructor of TNode which initializes the
		 * fields
		 * of the class.
		 *
		 * @param element - the data of the node
		 * @param caller - caller of the node
		 */
		public TNode (Whatever element, String caller) {
			tracker = new Tracker ("TNode", Size.of (data)
			+ Size.of (left)
			+ Size.of (right)
			+ Size.of (height)
			+ Size.of (balance)
			+ Size.of (tracker),
			caller + " calling Tree CTor");
			// DO NOT CHANGE THIS PART ABOVE

			data = element;
			left = null;
			right = null;
			balance = 0;
			height = 0;
		}

		/** 
		 * Function Name: jettisonTNodeOnly
		 * Description: Jettisons (Disposes) only of the node by 
		 * removing it from the memory.
		 */
		private void jettisonTNodeOnly () {
			tracker.jettison();
		}

		/** 
		 * Function Name: jettisonTNodeAndData
		 * Description: Jettisons (Disposes) of the node and its data 
		 * by removing them from the memory.
		 */
		private void jettisonTNodeAndData () {
			data.jettison();
			tracker.jettison();
		}

		/** 
		 * Function Name: jettisonAllTNodes
		 * Description: Jettisons (Disposes) of all the nodes by 
		 * removing them from the memory.
		 */
		private void jettisonAllTNodes () {
			// recursion until left is null
			if(left != null)
				left.jettisonAllTNodes();
			// recursion until right is null
			if(right != null)
				right.jettisonAllTNodes();
			// when both sides are null jettison node and data
			jettisonTNodeAndData();
		}

		/** 
		 * Function Name: insert
		 * Description: Inserts the node into the tree.
		 *
		 * @param element - item to be inserted into tree
		 * @param pointerInParentBox - holds current TNode pointer in
		 * parent TNode
		 * @return boolean:
		 * 	true: insert is successful
		 * 	false: insert is not successful
		 */
		private boolean insert (Whatever element,
			PointerBox pointerInParentBox) {
			// debug message
			if(debug){
				System.err.print(TREE+treeCount+COMPARE+
						element.getName()+AND+
						data.getName()+CLOSE);
			}

			// checking if element is equal to data
			if(element.equals(data)){
				// debug message
				if(debug){
					System.err.print(TREE+treeCount+INSERT
						       +element.getName()
						       +CLOSE);
				}


				// jettison previous data of node
				data.jettison();
				// insert element to node
				data = element;
			}

			// checking if element is less than data
			else if(element.isLessThan(data)){
				// checking for an empty spot for element
				if(left == null){
					// debug message
					if(debug){
						System.err.print
							(TREE+treeCount+INSERT
							       +element.
							       getName()
							       +CLOSE);
					}

					// inserting new node
					left = new TNode
						(element, "Tree.insert");
					occupancy++;
				}
				// if there's no empty spot continue locating
				else{
					// put the child pointer into a box
					PointerBox leftBox = 
						new PointerBox(left);
					// calling insert on the left
					left.insert(element, leftBox);
					// update left by the resulted pointer
					left = leftBox.pointer;
				}
			}

			// checking if element is greater than data
			else{
				// checking for an empty spot for element
				if(right == null){
					// debug message
					if(debug){
						System.err.print
							(TREE+treeCount+INSERT
							       +element.
							       getName()
							       +CLOSE);
					}

					// inserting new node
					right = new TNode
						(element, "Tree.insert");
					occupancy++;
				}
				// if there's no empty spot continue locating
				else{
					// put the child pointer into a box
					PointerBox rightBox = 
						new PointerBox(right);
					// calling insert on the right
					right.insert(element, rightBox);
					// update right by the resulted pointer
					right = rightBox.pointer;

				}
			}
			// set height and balance of node
			setHeightAndBalance(pointerInParentBox);
			return true;
		}

		/** 
		 * Function Name: lookup
		 * Description: Look ups the node in the tree.
		 *
		 * @param element - item to be looked up in tree
		 * @return item located in tree or null if the item is not 
		 * located
		 */
		@SuppressWarnings("unchecked")
		private Whatever lookup (Whatever element) {
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
				if(left == null){
					// item does not exist
					result = null;
				}
				// if there's no empty spot continue locating
				else{
					// calling lookup on the left
					result = left.lookup(element);
				}
			}

			// checking if element is greater than data
			else{
				// checking for an empty spot
				if(right == null){
					// item does not exist
					result = null;
				}
				// if there's no empty spot continue locating
				else{
					// calling lookup on the right
					result = right.lookup(element);
				}
			}
			return result;
		}

		/** 
		 * Function Name: remove
		 * Description: Removes the node from the tree.
		 * 
		 * @param element - item to be removed from tree
		 * @param pointerInParentBox - holds current TNode pointer in
		 * parent TNode
		 * @return removed item or null if the item is not located
		 */ 
		private Whatever remove (Whatever element, 
		PointerBox pointerInParentBox,
		boolean fromSHB) {
			Whatever result = null;
			// debug message
			if(debug){
				System.err.print(TREE+treeCount+COMPARE+
						element.getName()+AND+
						data.getName()+CLOSE);
			}

			// checking if element is equal to data
			if(element.equals(data)){
				// checking if leaf node
				if(left == null && right == null){
					pointerInParentBox.pointer = null;
					jettisonTNodeOnly();
					occupancy--;
					result = data;
					return result;
				}
				// checking if right has child
				else if(left == null && right != null){
					pointerInParentBox.pointer = right;
					jettisonTNodeOnly();
					occupancy--;
					result = data;
					return result;
				}
				// checking if left has child
				else if(left != null && right == null){
					pointerInParentBox.pointer = left;
					jettisonTNodeOnly();
					occupancy--;
					result = data;
					return result;
				}
				// checking if there are two children
				else{
					// go right once
					// put the child pointer into a box
					PointerBox rightBox = 
						new PointerBox(right);	
					right.replaceAndRemoveMin
						(this, rightBox);
					// update right by the resulted pointer
					right = rightBox.pointer;

					if(!fromSHB){
						setHeightAndBalance
							(pointerInParentBox);
					}

					jettisonTNodeOnly();
					occupancy--;
					result = data;
					return result;
				}	
			}

			// checking if element is less than data
			else if(element.isLessThan(data)){
				// checking for an empty spot
				if(left == null){
					// item does not exist
					result = null;
				}
				// if there's no empty spot continue locating
				else{
					// put the child pointer into a box
					PointerBox leftBox = 
						new PointerBox(left);
					// calling remove on the left
					result = left.remove
						(element, leftBox, false);
					// update left by the resulted pointer
					left = leftBox.pointer;
				}
			}

			// checking if element is greater than data
			else{
				// checking for an empty spot
				if(right == null){
					// item does not exist
					result = null;
				}
				// if there's no empty spot continue locating
				else{
					// put the child pointer into a box
					PointerBox rightBox = 
						new PointerBox(right);
					// calling remove on the right
					result = right.remove
						(element, rightBox, false);
					// update right by the resulted pointer
					right = rightBox.pointer;
				}
			}
			// set height and balance
			setHeightAndBalance(pointerInParentBox);
			return result;
		}

		/** 
		 * Function Name: replaceAndRemoveMin
		 * Description: Replaces a TNode with the minimum TNode in 
		 * its right subtree to maintain the Tree structure.
		 * 
		 * @param targetNode - node to be replaced
		 * @param pointerInParentBox - holds current TNode pointer in 
		 * parent TNode
		 */ 
		private void replaceAndRemoveMin (TNode targetTNode,
		PointerBox pointerInParentBox) {
			// debug message
			if(debug){
				System.err.print(TREE+treeCount+CHECK+
						data.getName()+CLOSE);
			}
			// continue moving left until null
			if(left != null){
				// put the child pointer into a box
				PointerBox leftBox = new PointerBox(left);
				// calling replaceAndRemoveMin on the left
				left.replaceAndRemoveMin(targetTNode, leftBox);
				// update left by the resulted pointer
				left = leftBox.pointer;
			}
			// found successor node
			else{
				// debug message
				if(debug){
					System.err.print(TREE+treeCount+
							REPLACE+data.getName()
							+CLOSE);
				}

				targetTNode.data = data;
				pointerInParentBox.pointer = null;
				jettisonTNodeAndData();
				return;	
			}
			// set height and balance
			setHeightAndBalance(pointerInParentBox);
		}

		/** 
		 * Function Name: setHeightAndBalance
		 * Description: Sets the height and balance of the nodes in 
		 * the tree
		 * 
		 * @param pointerInParentBox - holds current TNode pointer in 
		 * parent TNode
		 */ 
		private void setHeightAndBalance
			(PointerBox pointerInParentBox) {
			// debug message
			if(debug){
				System.err.print(TREE+treeCount+UPDATE+
						data.getName()+CLOSE);
			}

			// checking if both sides are null
			if(left == null && right == null){
				// update balance and height
				height = 0;
				balance = 0;
			}
			// checking if only left of node is null
			else if(left == null){
				// update balance and height
				height = right.height + 1;
				balance = -1 - right.height;
			}
			// checking if only right of node null
			else if(right == null){
				// update balance and height
				height = left.height + 1;
				balance = left.height + 1;
			}
			// else both sides are not null
			else{
				// update balance and height
				if(left.height > right.height){
					height = left.height + 1;
				}
				else{
					height = right.height + 1;
				}
				balance = left.height - right.height;
			}

			// checking if above threshold
			if(Math.abs(balance) > THRESHOLD){
				insert(remove(data, pointerInParentBox, true),
					       pointerInParentBox);	
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
			if (left != null)
				left.writeAllTNodes ();

			representation += this;

			if (right != null)
				right.writeAllTNodes ();
		}
	}
}

