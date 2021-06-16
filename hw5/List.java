/**
 * Name:           Bryce Ong
 * PID:            A16287711
 * USER:           cs12sp21bjn
 * File name:      List.java
 * Description:    This program simulates a circular list data structure and 
 *                 its functions. It will also track the memory usage of the
 *                 list. Program terminates when user enters ^D.
 */

import java.io.*;

public class List extends Base {

	static boolean debug = false;	// debug status
	static int listCounter = 0;	// used to number each List

	public static final int		// List controls
		END = 0,
		FRONT = 1;

  /**
   * Class: ListEngine
   * Description: This class is for HW#5. It simulates the circular list data
   * structure, implements the list's various functions, and 
   * tracks its memory.
   * 
   * Public Functions:
   * ListEngine - initializes the fields of the list.
   * jettisonList - jettisons (disposes) of the list.
   * advanceNext - moves the end of the list forward by one Node.
   * advancePre - moves the end of the list backward by one Node.
   * checkToGoForward - Finds the most efficient path to traverse the 
   * nodes in the list.
   * isEmpty - checks if the list is empty or not.
   * insert - inserts a Base object into the list.
   * locate - Finds the location at which we wish to insert, remove, or view.
   * remove - removes a node from the list.
   * view - returns the data stored in a node.
   * writeList - prints the information of the list to stdout or stderr 
   * forwards.
   * writeReverseList - prints the information of the list to stdout or stderr
   * backwards.
   */
	private class ListEngine {

		// catastrophic error messages
		static final String 
			ADNEXT_EMPTY = "Advance next from empty list!!!\n",
			ADPRE_EMPTY = "Advance pre from empty list!!!\n",
			REMOVE_EMPTY = "Remove from empty list!!!\n",
			VIEW_EMPTY = "Viewing an empty list!!!\n",
			WRITE_NONEXISTFILE 
				= "Writing to a non-existent file!!!\n";
	
		// debug messages
		static final String 
			ADNEXT = "[List %d - Advancing next]\n",
			ADPRE = "[List %d - Advancing pre]\n",
			INSERT = "[List %d - Inserting node]\n",
			LOOKUP = "[List %d - Looking up node]\n",
			REMOVE = "[List %d - Removing node]\n",
			VIEW = "[List %d - Viewing node]\n",
			LIST_ALLOCATE 
				= "[List %d has been allocated]\n",
			LIST_JETTISON
				= "[List %d has been jettisoned]\n";

		int count;		// which list is it
		Node end;		// end of the List
		long occupancy;		// how many items stored
		Base sample;		// sample object of what is stored
		Tracker tracker;	// to track memory

		/** 
		 * Function Name: ListEngine
		 * Description: Constructor of ListEngine which initializes the
		 * fields of the class.
		 * 
		 * @param sample - sample of data that will be stored in list
		 * @param caller - caller of the list
		 */ 
		ListEngine (Base sample, String caller) {
			tracker = new Tracker ("ListEngine", 
				Size.of (count) 
				+ Size.of (end)
				+ Size.of (occupancy)
				+ Size.of (sample)
				+ Size.of (tracker),
				caller + " calling ListEngine Ctor");
			// ---- DO NOT CHANGE TRACKER ---- //
			
			// increment number of lists
			listCounter++;
			// set id of list
			count = listCounter;
			// set end node to null since there are no nodes
			end = null;
			// set occupancy to 0 since there are no nodes
			occupancy = 0;
			// empty Base object
			this.sample = sample;

			// debug message
			if(debug){
				System.err.print(String.format
						(LIST_ALLOCATE, count));
			}
		}		
		
		/** 
		 * Function Name: jettisonList
		 * Description: Jettisons (Disposes) of the list by removing it
		 * from the memory.
		 */ 	
		void jettisonList () { 
			// checking if list is not empty
			if(occupancy > 0){	
				// loop through all nodes
				for(int counter = 0;counter < occupancy - 1;
				counter++){
					// advance next first before jettison
					advanceNext();
					end.getPre().jettisonNode();	
				}
				// jettison final node
				end.jettisonNode();
			}
			// jettison list
			tracker.jettison();

			// debug message
			if(debug){
				System.err.print(String.format
						(LIST_JETTISON, count));
			}
		}

		/** 
		 * Function Name: advanceNext
		 * Description: Moves the end of the list forward by one Node.
		 */ 
		void advanceNext () {
			// checking if list is empty
			if(isEmpty()){
				System.err.print(ADNEXT_EMPTY);
				return;
			}
			// set end to next node
			end = end.getNext();

			// debug message
			if(debug){
				System.err.print(String.format(ADNEXT, count));
			}
		}

		/** 
		 * Function Name: advancePre
		 * Description: Moves the end of the list backward by one Node.
		 */ 
		void advancePre () {
			// checking if list is empty
			if(isEmpty()){
				System.err.print(ADPRE_EMPTY);
				return;
			}
			// set end to previous node
			end = end.getPre();

			// debug message
			if(debug){
				System.err.print(String.format(ADPRE, count));
			}
		}
		
		/** 
		 * Function Name: checkToGoForward
		 * Description: Finds the most efficient path to traverse the
		 * nodes in the list to a certain element.
		 * 
		 * @param where - the place in the list where the element is 
		 * supposed to be stored
		 * @return boolean:
		 * 	true: list should loop forwards from the end
		 * 	false: list should loop backwards from the end
		 */ 
		boolean checkToGoForward (long where) {
			/* checking if end pointer should move 
			forwards or backwards */
			if(occupancy / 2 - where >= 0){
				return true;
			}
			else{
				return false;
			}
		}
		
		/** 
		 * Function Name: isEmpty
		 * Description: Checks if the list is empty.
		 * 
		 * @return boolean:
		 * 	true: list is empty
		 * 	false: list is not empty
		 */ 
		boolean isEmpty () {
			// checking if list is empty
			return occupancy == 0;	
		}

		/** 
		 * Function Name: insert
		 * Description: Inserts a Base object into the list.
		 *
		 * @param element - Base object that is supposed to be inserted
		 * @param where - the place in the list where the element is 
		 * supposed to be stored
		 * @return boolean:
		 * 	true: function is successful
		 * 	false: function is unsuccessful
		 */ 
		boolean insert (Base element, long where) {
			// debug message
			if(debug){
				System.err.print(String.format(INSERT, count));
			}

			// checking if list is empty
			if(isEmpty()){
				// add new node if list is empty
				end = new Node(element);
				// increment occupany
				occupancy++;
				return true;
			}
			// saving previous end
			Node temp = end;
			// locating insertion location
			locate(where);


			/* move back once since new node will be inserted after
			current node */ 
			if(where != END){
				advancePre();
			}

			// inserting new node
			end.insert(element);
			// increment occupancy
			occupancy++;

			// move forwards once if new node is added to end
			if(where == END){
				advanceNext();
			}
			// if not appended to end, restore previous end
			else{
				end = temp;
			}

			return true;
		}

		/** 
		 * Function Name: locate
		 * Description: Finds the location at which we wish to insert,
		 * remove, or view.
		 * 
		 * @param where - the place in the list where the element is 
		 * supposed to be stored
		 * @return boolean:
		 * 	true: function is successful
		 * 	false: function is unsuccessful
		 */ 
		boolean locate (long where) {
			// checking for optimal path to location where
			if(checkToGoForward(where)){
				// loop for moving forwards
				for(int forward = 0;forward < where;forward++){
					advanceNext();
				}
				return true;
			}	
			else{
				// loop for moving backwards
				for(long back = occupancy;back > where;back--){
					advancePre();
				}
				return true;
			}
		}

		/** 
		 * Function Name: remove
		 * Description: Removes a node from the list.
		 * 
		 * @param where - the place in the list where the element is 
		 * supposed to be stored
		 * @return data of the removed node
		 */ 
		Base remove (long where) {
			// checking if list is empty
			if(isEmpty()){
				System.err.print(REMOVE_EMPTY);
				return null;
			}
			// saving previous end
			Node temp = end;
			// locating removal location
			locate(where);
			/* move back once to prevent end pointer from
			becoming null */ 
			advancePre();
			// remove next node and store its data
			Base data = end.getNext().remove();
			// decrement occupancy
			occupancy--;

			// do nothing if node is removed from end
			if(where == END){
			}
			// if not removed from end, restore previous end
			else{
				end = temp;
			}

			// debug message
			if(debug){
				System.err.print(String.format(REMOVE, count));
			}
			return data;
		}

		/** 
		 * Function Name: view
		 * Description: Returns the data stored at location where.
		 * 
		 * @param where - the place in the list where the element is
		 * supposed to be stored
		 * @return data of the node at location where
		 */ 
		Base view (long where) {
			// checking if list is empty
			if(isEmpty()){
				System.err.print(VIEW_EMPTY);
				return null;
			}
			// saving previous end
			Node temp = end;
			// locating viewing location
			locate(where);
			// storing viewed data
			Base data = end.view();
			// restore previous end
			end = temp;

			// debug message
			if(debug){
				System.err.print(String.format(VIEW, count));
			}
			return data;
		}
		
		/** 
		 * Function Name: writeList
		 * Description: Prints the elements of the list forwards.
		 * 
		 * @param stream - stream where the elements will be printed
		 */ 
		void writeList (PrintStream stream) {
			if (stream == null) {
				System.err.print (WRITE_NONEXISTFILE);
				return;
			}

			// extra output if we are debugging
			if (stream == System.err) {
				stream.print ("List " 
					+ count + " has "
					+ occupancy + " items in it:\n");
			}

			// display each Node in the List
			Node oldEnd = end;  // to save prior front
			if (occupancy > 0) {
				advanceNext ();
			}
			for (long idx = 1; idx <= occupancy; idx++) {
				stream.print (" element " + idx + ": ");
				end.writeNode (stream);
				advanceNext ();
			}

			// memory tracking output if we are debugging
			if (debug) {
				System.err.print (tracker);
			}
			
			// restore front to prior value
			end = oldEnd;
		}

		/** 
		 * Function Name: writeReverseList
		 * Description: Prints the elements of the list backwards.
		 * 
		 * @param stream - stream where the elements will be printed
		 */ 
		void writeReverseList (PrintStream stream) {
			if (stream == null) {
				System.err.print (WRITE_NONEXISTFILE);
				return;
			}

			// extra output if we are debugging
			if (stream == System.err) {
				stream.print ("List " 
					+ count + " has "
					+ occupancy + " items in it:\n");
			}

			// display each Node in the List
			Node oldEnd = end;  // to save prior end
			for (long idx = 1; idx <= occupancy; idx++) {
				stream.print (" element " + idx + ": ");
				end.writeNode (stream);
				advancePre ();
			}

			// memory tracking output if we are debugging
			if (debug) {
				System.err.print (tracker);
			}
			
			// restore end to prior value
			end = oldEnd;
		}

		private class Node {

  /**
   * Class: NodeEngine
   * Description: This class is for HW#5. It simulates a node in the circular
   * list, implements the node's various functions, and tracks its memory.
   * 
   * Public Functions:
   * NodeEngine - initializes the fields of the node.
   * jettisonNodeAndData - jettisons (disposes) of the node and its data.
   * jettisonNodeOnly - jettisons (disposes) of the node only.
   * insert - inserts a new node into the list.
   * remove - removes the node from the list.
   * view - returns the data stored in the node.
   * writeNode - prints the information of the node to stdout or stderr.
   */
			private class NodeEngine {

				static final String WRITE_NONEXISTFILE 
					= "Writing to a " 
					+ "non-existent file!!!\n";

				Base data;	// the item stored
				Node next;	// to get to following item
				Node pre;	// to get to previous item
				Tracker tracker; // to track memory

				/** 
				 * Function Name: NodeEngine
				 * Description: Constructor of NodeEngine which
				 * initializes the fields of the class.
				 * 
				 * @param newNode - node that the newly created
				 * NodeEngine belongs to
				 * @param element - Base object to be stored
				 * in the node
				 * @param caller - caller of the node
				 */  
				NodeEngine (Node newNode, 
					Base element, String caller) {
					
					tracker = new Tracker ("NodeEngine", 
						Size.of (data) 
						+ Size.of (next) 
						+ Size.of (pre)
						+ Size.of (tracker),
						caller 
						+= " calling NodeEngine Ctor");
					// ---- DO NOT CHANGE TRACKER ---- //
					// checking is sample object is null
					if(sample == null){
						data = element;
					}
					else{
						data = sample.copy(element);
					}

					// set next and pre of node to itself
					next = newNode;
					pre = newNode;
				}
							
				/** 
				 * Function Name: jettisonNodeAndData
				 * Description: Jettisons (Disposes) of the 
				 * node and its data by removing it from the 
				 * memory.
				 */ 	 
				void jettisonNodeAndData () {
					// jettison node
					tracker.jettison();
					// jettison data
					data.jettison();
				} 
				
				/** 
				 * Function Name: jettisonNodeOnly
				 * Description: Jettisons (Disposes) of the 
				 * node only by removing it from the memory.
				 */ 	 
				void jettisonNodeOnly () {
					// jettison node
					tracker.jettison();
				} 
				
				/** 
				 * Function Name: insert
				 * Description: Inserts a new node into the 
				 * list.
				 *
				 * @param thisNode - node that the new node
				 * will be inserted after
				 * @param element - Base object that is stored
				 * in the new node
				 * @return new node that is created
				 */  
				Node insert (Node thisNode, 
					Base element) {
					// new node to be inserted
					Node newNode = new Node(element);
					// set pre of next node to new node
					thisNode.getNext().setPre(newNode);
					// set next of new node to next node
					newNode.setNext(thisNode.getNext());
					// set pre of new node to current node
					newNode.setPre(thisNode);
					// set next of current node to new node
					thisNode.setNext(newNode);
					return newNode;
				}
				
				/** 
				 * Function Name: remove
				 * Description: Removes the node from the list.
				 * 	
				 * @return data of the removed node
				 */  
				Base remove () {
					// remove references to node
					this.pre.setNext(this.next);
					this.next.setPre(this.pre);
					// jettison node only
					jettisonNodeOnly();
					return data;	
				}

				/** 
				 * Function Name: view
				 * Description: Returns the data stored in the
				 * node.
				 * 
				 * @return data of the node
				 */  
				Base view () {
					return data;
				}

				/** 
				 * Function Name: writeNode
				 * Description: Prints the data of the node.
				 * 
				 * @param stream - stream where the data will
				 * be printed
				 */ 
				void writeNode (PrintStream stream) {
					// checking if stream exists
					if (stream == null) {
						System.err.print (
							WRITE_NONEXISTFILE);
						return;
					}
					// print data to stream
					stream.print (data + "\n");
				}
			}

			// -------- YOUR CODE SHOULD GO ABOVE --------
			// NOTE: 
			// READ THE CODE BELOW TO SEE WHAT METHOD YOU CAN USE
			
			static final String
				GETPRE_NONEXISTNODE
				= "Getting pre of a non-existent node!!!\n",
				GETNEXT_NONEXISTNODE
				= "Getting next of a non-existent node!!!\n",
				SETPRE_NONEXISTNODE
				= "Setting pre of a non-existent node!!!\n",
				SETNEXT_NONEXISTNODE
				= "Setting next of a non-existent node!!!\n",
				JETTISON_NONEXISTNODE 
				= "Jettisoning a non-existent node!!!\n",
				LOOKUP_NONEXISTNODE 
				= "Looking up a non-existent node!!!\n",
				INSERT_NONEXISTNODE
				= "Inserting a non-existent node!!!\n",
				REMOVE_NONEXISTNODE
				= "Removing a non-existent node!!!\n",
				VIEW_NONEXISTNODE 
				= "Viewing a non-existent node!!!\n",
				WRITE_NONEXISTNODE 
				= "Writing from a non-existent node!!!\n";

			NodeEngine nodeEngine;	// To be wrapped by a Node

			// Node CTOR METHOD 
			Node (Base element) {
				nodeEngine = new NodeEngine (
					this, element, "Node Ctor");
			}
			
			// Node GETPRE METHOD
			Node getPre () {
				if (!exist ()) {
					System.err.print (
						GETPRE_NONEXISTNODE);
					return null;
				}
				return nodeEngine.pre;
			}

			// Node GETNEXT METHOD
			Node getNext () {
				if (!exist ()) {
					System.err.print (
						GETNEXT_NONEXISTNODE);
					return null;
				}
				return nodeEngine.next;
			}
			
			// Node SETNEXT METHOD
			void setNext (Node next) {
				if (!exist ()) {
					System.err.print (
						SETNEXT_NONEXISTNODE);
					return;
				}
				nodeEngine.next = next;
			}

			void setPre (Node pre) {
				if (!exist ()) {
					System.err.print (
						SETPRE_NONEXISTNODE);
					return;
				}
				nodeEngine.pre = pre;
			}

			// Node JETTISON METHOD
			boolean jettisonNode () {
				if (!exist ()) {
					System.err.print (
						JETTISON_NONEXISTNODE);
					return false;
				}
				nodeEngine.jettisonNodeAndData ();
				nodeEngine = null;
				return true;
			} 
			
			// Node EXIST METHOD 
			boolean exist () {
				return nodeEngine != null;
			}

			// Node INSERT METHOD 
			Node insert (Base element) {
				if (!exist ()) {
					System.err.print (INSERT_NONEXISTNODE);
					return null;
				}
				return nodeEngine.insert (this, element);
			} 

			// Node REMOVE METHOD
			Base remove () {
				if (!exist ()) {
					System.err.print (REMOVE_NONEXISTNODE);
					return null;
				}
				return nodeEngine.remove ();
			}

			// Node VIEW METHOD
			Base view () {
				if (!exist ()) {
					System.err.print (
						VIEW_NONEXISTNODE);
					return null;
				}
				return nodeEngine.view ();
			}

			// Node WRITENODE METHOD
			void writeNode (PrintStream stream) {
				nodeEngine.writeNode (stream);
			}
		}
	}

	// catastrophic error messages
	static final String 
		ADNEXT_NONEXIST = "Advance next from non-existent list!!!\n",
		ADPRE_NONEXIST = "Advance pre from non-existent list!!!\n",
		EMPTY_NONEXIST = "Empyting from non-existent list!!!\n",
		ISEMPTY_NONEXIST = "Is empty check from non-existent list!!!\n",
		INSERT_NONEXIST = "Inserting to a non-existent list!!!\n",
		JETTISON_NONEXIST = "Jettisoning from non-existent list!!!\n",
		REMOVE_NONEXIST = "Removing from non-existent list!!!\n",
		OCCUPANCY_NONEXIST 
			= "Occupancy check from non-existent list!!!\n",
		VIEW_NONEXIST = "Viewing a non-existent list!!!\n",
		WRITE_NONEXISTLIST = "Writing from a non-existent list!!!\n",
		WRITE_MISSINGFUNC = "Don't know how to write out elements!!!\n";

	private ListEngine listEngine;	// The ListEngine instance
	
	public static void debugOn () {
		debug = true;
	}

	public static void debugOff () {
		debug = false;
	}

	// List CTOR METHOD
	public List (Base sample, String caller) {
		caller += "\n\tcalling List Ctor";	
		listEngine = new ListEngine (sample, caller);
	}
	
	// list JETTISON
	public void jettison () {
		jettisonList ();
	}

	// list JETTISON
	public boolean jettisonList () {
		
		if (!exist ()) {
			System.err.print (JETTISON_NONEXIST);
			return false;
		}

		listEngine.jettisonList ();
		listEngine = null;
		return true;
	}

	// List ADVANCENPRE METHOD
	public void advancePre () {
		
		if (!exist ()) {
			System.err.print (ADPRE_NONEXIST);
			return;
		}
		
		listEngine.advancePre ();
	}

	// List ADVANCENEXT METHOD
	public void advanceNext () {
		
		if (!exist ()) {
			System.err.print (ADNEXT_NONEXIST);
			return;
		}

		listEngine.advanceNext ();
	}

	// List EMPTY METHOD
	public void empty () {
		
		if (!exist ()) {
			System.err.print (EMPTY_NONEXIST);
			return;
		}
		while (!isEmpty ()) {
			listEngine.remove (0);
		}
	}

	// List EXIST METHOD
	public boolean exist () {
		
		return listEngine != null;
	}

	// List GETOCCUPANCY METHOD
	public long getOccupancy () {
		
		return listEngine.occupancy;
	}

	// List ISEMPTY METHOD
	public boolean isEmpty () {
		
		if (!exist ()) {
			System.err.print (ISEMPTY_NONEXIST);
			return false;
		}

		return listEngine.isEmpty ();
	}

	// List INSERT METHOD
	public boolean insert (Base element, long where) {
		
		if (!exist ()) {
			System.err.print (INSERT_NONEXIST);
			return false;
		}
		
		return listEngine.insert (element, where); 	
	}

	// List REMOVE METHOD
	public Base remove (long where) {
		
		if (!exist ()) {
			System.err.print (REMOVE_NONEXIST);
			return null;
		}
		
		return listEngine.remove (where);
	}

	// List TOSTRING METHOD
	public String toString () {
		writeList (System.out);
		return "";
	}
	
	// List VIEW METHOD
	public Base view (long where) {
		
		if (!exist ()) {
			System.err.print (VIEW_NONEXIST);
			return null;
		}
		
		return listEngine.view (where);
	}
	
	// List WRITELIST METHOD
	public void writeList (PrintStream stream) {
		
		if (!exist ()) {
			System.err.print (WRITE_NONEXISTLIST);
			return;
		}
		
		listEngine.writeList (stream);
	}

	// List WRITEREVERSELIST METHOD
	public void writeReverseList (PrintStream stream) {
		
		if (!exist ()) {
			System.err.print (WRITE_NONEXISTLIST);
			return;
		}
		
		listEngine.writeReverseList (stream);
	}
}

