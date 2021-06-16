/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      Heap.java
 * Description:    This program simulates a heap data structure and 
 *                 its functions. It will also track the memory usage of the
 *                 heap. Program terminates when user enters ^D.
 */

/**
 * Class: Heap
 * Description: This class is for HW#10. It simulates the heap data
 * structure, implements the heap's various functions, and 
 * tracks its memory.
 * 
 * Public Functions:
 * Heap  - initializes the fields of the heap.
 * jettisonHeap  - jettisons (disposes) of the heap and its nodes.
 * isEmpty  - returns if the heap is empty or not.
 * isFull  - returns if the heap is full or not.
 * getParent - returns the parent of a node.
 * getLeftChild - returns the index of the left child of a node.
 * getRightChild - returns the index of the right child of a node.
 * hasLeftChild - returns if a node has a left child.
 * hasRightChild - returns if a node has a right child.
 * getDebug - getter method of debug field
 * debugOn  - turns on debug statements.
 * debugOff  - turns off debug statements.
 * swap - swaps the items of two nodes.
 * insert  - inserts an item into the heap.
 * remove  - removes an item from the top of the heap.
 * reheapUp - moves inserted item up the tree until heap-order is satisfied.
 * reheapDown - moves final leaf down the tree until heap-order is satisfied.
 * write - prints all the nodes in the heap.
 */
public class Heap extends Base {

	/* data fields */
	private long occupancy; 
	private Tracker tracker;
	private Base[] heap;

	/* debug flag */
	private static boolean debug;

	/* debug messages */
	private static final String ALLOCATE = "[Allocating ";
	private static final String JETTISON = "[Jettisoning ";
	private static final String WITH = " with ";
	private static final String AND = " and ";
	private static final String CLOSE = "]\n";
	private static final String COMPARE = "[Comparing parent ";
	private static final String INSERT = "[Inserting ";
	private static final String REHEAP_UP = 
		"[Reheaping up at child index: ";
	private static final String REHEAP_DOWN = "[Reheaping down]\n";
	private static final String SWAP = "[Swapping ";
	private static final String REMOVE = "[Removing ";
	private static final String HEAP = "Heap]\n";

	/** 
	 * Function Name: Heap
	 * Description: Constructor of Heap which initializes the fields
	 * of the class.
	 *
	 * @param heapSize - size of heap
	 */
	public Heap (int heapSize) {
		tracker = new Tracker ("Heap",
		+ Size.of (occupancy)
		+ Size.of (heap)
		+ Size.of (tracker),
		" Heap CTor");
		// DO NOT CHANGE THIS PART ABOVE

		// initializing fields	
		occupancy = 0;
		heap = new Base[heapSize];
		debug = false;

		if(debug)
			System.err.print(ALLOCATE + HEAP);
	}

	/** 
	 * Function Name: jettisonHeap
	 * Description: Jettisons (Disposes) of the tree and its nodes by 
	 * removing them from the memory.
	 */
	public void jettisonHeap () {
		// debug message
		if(debug)
			System.err.print(JETTISON + HEAP);

		// if heap is empty just jettison the heap
		if(isEmpty()){
			tracker.jettison();
			return;
		}	
		// if not empty jettison all elements first then jettison heap
		for(int index = 0; index < heap.length; index++){
			if(heap[index] != null)
				heap[index].jettison();
		}
		tracker.jettison();
	}

	/** 
	 * Function Name: isEmpty
	 * Description: Returns if the heap is empty or not
	 *
	 * @return boolean:
	 * 	true: heap is empty
	 * 	false: heap is not empty
	 */
	public boolean isEmpty () {
		return occupancy==0;
	}

	/** 
	 * Function Name: isFull
	 * Description: Returns if the heap is full or not
	 *
	 * @return boolean:
	 * 	true: heap is full
	 * 	false: heap is not full
	 */
	public boolean isFull () {
		return occupancy==heap.length;
	}

	/** 
	 * Function Name: getParent
	 * Description: Returns the parent of a node.
	 *
	 * @param index - index of node
	 * @return parent of node
	 */
	private Base getParent (int index) {
		return heap[(index - 1) / 2];
	}

	/** 
	 * Function Name: getLeftChild
	 * Description: Returns the index of the left child of a node.
	 *
	 * @param index - index of node
	 * @return index of left child of node
	 */
	private int getLeftChild (int index) {
		return (index * 2) + 1;
	}

	/** 
	 * Function Name: getRightChild
	 * Description: Returns the index of the right child of a node.
	 *
	 * @param index - index of node
	 * @return index of right child of node
	 */
	private int getRightChild (int index) {
		return (index * 2) + 2;
	}

	/** 
	 * Function Name: hasLeftChild
	 * Description: Returns if a node has a left child
	 *
	 * @param index - index of node
	 * @return boolean:
	 * 	true: node has left child
	 * 	false: node does not have left child
	 */
	private boolean hasLeftChild (int index) {
		// checking if left child is out of range
		if((index * 2) + 1 >= heap.length){
			return false;
		}
		return heap[(index * 2) + 1] != null;
	}

	/** 
	 * Function Name: hasRightChild
	 * Description: Returns if a node has a right child
	 *
	 * @param index - index of node
	 * @return boolean:
	 * 	true: node has right child
	 * 	false: node does not have right child
	 */
	private boolean hasRightChild (int index) {
		// checking if right child is out of range
		if((index * 2) + 2 >= heap.length){
			return false;
		}
		return heap[(index * 2) + 2] != null;
	}

	/** 
	 * Function Name: getDebug
	 * Description: Getter method of debug field
	 *
	 * @return boolean:
	 * 	true: debug statements are on
	 * 	false: debug statements are off
	 */
	public boolean getDebug () {
		return debug;
	}

	/** 
	 * Function Name: debugOn
	 * Description: Turns on debug statements.
	 */
	public void debugOn () {
		debug = true;
	}

	/** 
	 * Function Name: debugOff
	 * Description: Turns off debug statements.
	 */
	public void debugOff () {
		debug = false;
	}

	/** 
	 * Function Name: swap
	 * Description: Swaps the items of two nodes.
	 *
	 * @param index - index of node
	 * @param indexToSwap - index of node to swap with
	 */
	private void swap (int index, int indexToSwap){
		// debug message
		if(debug){
			System.err.print(SWAP + heap[index].getName() + AND
					+ heap[indexToSwap].getName() + CLOSE);
		}

		// temporary holder for node item
		Base temp;
		// swap nodes
		temp = heap[index];
		heap[index] = heap[indexToSwap];
		heap[indexToSwap] = temp;
	}

	/** 
	 * Function Name: insert
	 * Description: Inserts an item into the heap.
	 *
	 * @param element - item to be inserted into heap
	 * @return boolean:
	 * 	true: insert is successful
	 * 	false: insert is not successful
	 */
	public boolean insert (Base element) {
		// debug message
		if(debug)
			System.err.print(INSERT + element.getName() + CLOSE);

		// checking if heap is empty
		if(isEmpty()){
			// inserting node
			heap[0] = element;
			occupancy++;
			return true;
		}

		// finding nearest empty space for node
		for(int index = 0; index < heap.length; index++){
			if(heap[index] == null){
				// inserting node
				heap[index] = element;
				occupancy++;
				reheapUp(index);
				break;
			}
		}
		return true;
	}

	/** 
	 * Function Name: remove
	 * Description: Removes an item from the top of the heap.
	 * 
	 * @return removed item or null if the item is not located
	 */ 
	public Base remove () {
		// debug message
		if(debug)
			System.err.print(REMOVE + heap[0].getName() + CLOSE);

		// holds the result of remove
		Base result;
		// remove node
		result = heap[0];
		occupancy--;
		// checking if heap is empty after remove
		if(occupancy == 0){
			heap[0] = null;
			return result;
		}
		
		// finding final leaf
		for(int index = heap.length - 1; index > 0; index--){
			if(heap[index] != null){
				heap[0] = heap[index];
				heap[index] = null;
				break;
			}
		}

		// debug message
		if(debug)
			System.err.print(REHEAP_DOWN);
      
		reheapDown(0);
		return result;
	}

	/** 
	 * Function Name: reheapUp
	 * Description: Moves inserted item up the tree until heap-order is 
	 * satisfied.
	 * 
	 * @param index - index of node
	 */ 
	private void reheapUp (int index){
		// debug message
		if(debug){
			System.err.print(REHEAP_UP + index + CLOSE);
			System.err.print(COMPARE + getParent(index).getName()
					+ WITH + heap[index].getName()+CLOSE);
		}

		// checking if parent is greater than child
		if(heap[index].isLessThan(getParent(index))){
			swap(index, (index - 1) / 2);
		}
		else{
			return;
		}

		// continue reheapUp until parent is not greater than child
		reheapUp((index - 1) / 2);
	}

	/** 
	 * Function Name: reheapDown
	 * Description: Moves final leaf down the tree until heap-order is 
	 * satisfied.
	 * 
	 * @param index - index of node
	 */ 
	private void reheapDown (int index){
		// checking if node has no children
		if(!hasLeftChild(index) && !hasRightChild(index)){
			return;
		}
		// checking if node has left child
		else if(hasLeftChild(index) && !hasRightChild(index)){

			// debug message
			if(debug){
				System.err.print(COMPARE + heap[index].getName()
				+ WITH + heap[getLeftChild(index)].
				getName()+CLOSE);
			}

			// checking if left child is less than parent
			if(heap[getLeftChild(index)].isLessThan(heap[index])){
				// swapping
				swap(index, getLeftChild(index));
				reheapDown(getLeftChild(index));
			}
		}
		// checking if node has two children
		else if(hasLeftChild(index) && hasRightChild(index)){
			// checking if left child is less than parent
			if(heap[getLeftChild(index)].isLessThan
					(heap[getRightChild(index)])){
				// debug message
				if(debug){
					System.err.print(COMPARE
					+ heap[index].getName()
					+ WITH + heap[getLeftChild(index)].
					getName()+CLOSE);
				}

				// swapping
				swap(index, getLeftChild(index));
				reheapDown(getLeftChild(index));
			}
			// checking if right child is less than parent
			else{
				// debug message
				if(debug){
					System.err.print(COMPARE
					+ heap[index].getName()
					+ WITH + heap[getRightChild(index)].
					getName()+CLOSE);
				}

				// swapping
				swap(index, getRightChild(index));
				reheapDown(getRightChild(index));
			}
		}
	}

	/** 
	 * Function Name: write
	 * Description: Prints all the nodes in the heap.
	 */ 
	public void write (){
		// checking is heap only has 1 item
		if(occupancy == 1)
			System.out.print("The Heap has 1 item:\n");
		// else print out the occupancy of heap
		else{
			System.out.print("The Heap has " + occupancy +
					" items:\n");
		}

		// looping through array and printing all nodes
		for(int index = 0; index < heap.length; index++){
			if(heap[index] != null){
				System.out.print("At index " + index + ":  " +
				heap[index] + ".\n");
			}
		}
	}
}

