import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * The maze class generates a random maze based on user input. It uses a dfs
 * search algorithm to generate the maze.
 * 
 * @author Jon Sobocinski
 * @date 3/15/2014
 */
public class Maze {

	public Node[][] nodeArray;// Our 2d array
	ArrayList<Node> nodeList;// a list of nodes for quick iteration
	int depth;// depth of maze
	int width;// width of maze
	int totalNodes;// total number of nodes.
	int visitedNodes;// total number of visited number of nodes.
	boolean debug;// debug boolean
	boolean solved;
	ArrayList<Node> sol;

	/**
	 * The only constructor of this class. Takes the params and generates a maze
	 * off of them.
	 * 
	 * @param width
	 *            the width the user would like
	 * @param depth
	 *            the depth the user would like
	 * @param debug
	 *            if the user would like the maze to print to console each time
	 *            it makes a move.
	 */
	public Maze(int width, int depth, boolean debug) {
		nodeArray = new Node[width][depth];// Creates an 2d array of proper size
		nodeList = new ArrayList<Node>();
		totalNodes = width * depth;// Determines how many total nodes to make
		this.depth = width;
		this.width = depth;
		visitedNodes = 0;
		this.debug = debug;
		solved = false;
		sol = new ArrayList<Node>();

		createNodes(totalNodes, width, depth);// Creates the nodes
		setRelations();// sets the relations
		makeMaze(nodeArray[0][0]);// makes the maze.

	}

	/**
	 * A private method that determines if every node has been visited BigO :
	 * O(n)
	 * 
	 * @return Returns true if every node has been visited.
	 */
	private boolean allVisited() {
		for (Node node : nodeList) {
			if (node.visited == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Private method that makes the maze. Is the main recursive method that
	 * sets the paths. Big O: O(n)
	 * 
	 * @param node
	 *            Takes the node at 0,0 the first time, and after that takes
	 *            each recursive node.
	 */
	private void makeMaze(Node node) {

		if (node == nodeArray[depth - 1][width - 1]) {
			solved = true;
		}

		if (!solved) {
			node.solution = true;
			sol.add(node);
		}

		if (debug)// Displays each step if debug is on
			display();

		ArrayList<Node> validNodes = node.validNodes();// gets a list of valid
		                                               // nodes this node can
		                                               // travel to.

		if (allVisited()) {// Base case. If all nodes have been visited it
			               // creates the entrance at 0,0 and exit at n,m
			nodeArray[depth - 1][width - 1].visitedSouth = true;
			nodeArray[0][0].visitedNorth = true;
			return;// then breaks out of method.
		}

		Random rand = new Random();// The rand number gen we will use.
		node.visited = true;// Sets this node to visited.
		visitedNodes = visitedNodes + 1;// increments nodes visited.

		if (validNodes.size() == 0) {// If there are no valid nodes in this
			                         // list, we recurse to the last node we
			                         // visited.
			makeMaze(node.cameHereFrom);
			if (!solved) {
				sol.remove(node);
			}
			visitedNodes = visitedNodes - 1;// and decrement the total number of
			                                // visited nodes.
			return;
		}
		else {// Otherwise

			int nodeI = rand.nextInt(validNodes.size());// we choose a random
			                                            // node in the list

			Node nodeToVisit = validNodes.get(nodeI);

			if (node.north == nodeToVisit) {// If its our norther node, we set
				                            // out visited north to true, and
				                            // its visited south to true.
				node.visitedNorth = true;
				node.north.cameHereFrom = node;
				node.north.visitedSouth = true;
			}
			if (node.east == nodeToVisit) {// Same with this, but east.
				node.visitedEast = true;
				node.east.cameHereFrom = node;
				node.east.visitedWest = true;
			}
			if (node.south == nodeToVisit) {// And here but south.
				node.visitedSouth = true;
				node.south.cameHereFrom = node;
				node.south.visitedNorth = true;
			}
			if (node.west == nodeToVisit) {// and here but west.
				node.visitedWest = true;
				node.west.cameHereFrom = node;
				node.west.visitedEast = true;
			}

			makeMaze(nodeToVisit);// Finally we pass this next node back to our
			                      // method.
		}

	}

	/**
	 * A private method that sets every nodes relation to its neighbors. Big O :
	 * O(n)
	 */
	private void setRelations() {
		for (Node node : nodeList) {// Goes through the list and gets a node
			                        // based on location and then sets it as its
			                        // neighbor at its correct spot.
			node.north = getNodeBasedOnLoc(node.x, node.y - 1);
			node.south = getNodeBasedOnLoc(node.x, node.y + 1);
			node.east = getNodeBasedOnLoc(node.x + 1, node.y);
			node.west = getNodeBasedOnLoc(node.x - 1, node.y);

		}

	}

	/**
	 * A private method that returns a node base on a x/y location that is
	 * passed to it. Big O: O(n)
	 * 
	 * @param x
	 *            takes the x location of the node you're looking for
	 * @param y
	 *            takes the y location of the node you're looking for
	 * @return returns the node if it exists, and null otherwise.
	 */
	private Node getNodeBasedOnLoc(int x, int y) {
		for (Node node : nodeList) {// Searches through the list of nodes.
			if (node.x == x && node.y == y) {// If the x and y match this node,
				                             // it returns it.
				return node;
			}
		}
		return null;// Otherwise it will return false, as the null does not
		            // exist.
	}

	/**
	 * A private method that creates the nodes and adds them to both the 2d
	 * array and the arraylist. Big O : O(n)
	 * 
	 * @param totalNodes
	 *            Takes the total number of nodes to create
	 * @param width
	 *            the width of the array.
	 * @param depth
	 *            and finally the depth of the array.
	 */
	private void createNodes(int totalNodes, int width, int depth) {
		for (int i = 0; i < width; i++) {// double nested loop to act as a 2d
			                             // array
			for (int j = 0; j < depth; j++) {
				Node node = new Node(i, j);// Creates a new node at the i j
				                           // cords
				nodeList.add(node);// adds the node to the list
				nodeArray[i][j] = node;// and to the 2d array
			}
		}

	}

	/**
	 * The display method displays the maze. Hardest thing to write. Big O: O(n)
	 */
	public void display() {

		for (int i = 0; i < width; i++) {// tripple nested loop. Ensures that
			                             // each node is visited from left to
			                             // right 3 times before procedding to
			                             // the next row. Prints the nodes top
			                             // string then moves to the next node
			                             // and prints its top string. Once its
			                             // finished with the top, it moves back
			                             // to the first and prints its middle
			                             // string, then moves to the next node
			                             // and prints its middle. Once done it
			                             // moves back to the first and prints
			                             // its bottom string, then on to the
			                             // next one. Once its prinnted each
			                             // node in the first row's top middle
			                             // and bottom in the order above, it
			                             // moves to the next row and repeats
			                             // the process until each node has been
			                             // printed.
			for (int j = 0; j < 3; j++) {

				for (int k = 0; k < depth; k++) {
					if (j == 0) {
						System.out.print(nodeArray[k][i].topToString());
					}
					else if (j == 1) {
						System.out.print(nodeArray[k][i].middletoString());
					}
					else {
						System.out.print(nodeArray[k][i].bottomtoString());
					}
				}
				System.out.println();
			}

		}
	}

	/**
	 * An inner class representing a node, used to hold visited information.
	 * 
	 * @author Jon Sobocinski
	 * @date 3/15/2014
	 */
	public class Node {

		Node north;// This nodes north node
		Node south;// This nodes south node
		Node east;// This nodes east node
		Node west;// This nodes west node

		Node cameHereFrom;// Which node came to this node

		boolean visitedEast;// Whether this node went east
		boolean visitedWest;// or west
		boolean visitedNorth;// or north
		boolean visitedSouth;// or south

		boolean visited;// If this node has been visited

		boolean solution = false;

		int x;// This nodes x loc on the 2d array
		int y;// This nodes y loc on the 2d array

		/**
		 * Only constructor for this class. sets everything to false, and sets
		 * the x and y to the x and y passed to it. Big O: O(k)
		 * 
		 * @param x
		 * @param y
		 */
		private Node(int x, int y) {
			this.x = x;
			this.y = y;
			visited = false;
			visitedEast = false;
			visitedWest = false;
			visitedNorth = false;
			visitedSouth = false;

		}

		/**
		 * Overridden toString() to print node information.
		 */
		@Override
		public String toString() {
			return this.x + " , " + this.y + "\nNorth: " + this.visitedNorth + "\nEast: "
			        + this.visitedEast + "\nSouth: " + this.visitedSouth + "\nWest: "
			        + this.visitedWest + "\n-------";

		}

		/**
		 * This method returns a string for just the top of this node. So, it
		 * will either print "X   X" or "X X X" Will check of node has gone
		 * north. Big O: O(k)
		 * 
		 * @return returns a string representing the top of this node.
		 */
		public String topToString() {
			StringBuilder sb = new StringBuilder();

			sb.append("X ");// Appends the x that the top left will always have.

			if (this.visitedNorth) {// appends a space if the node has visited
				                    // north, and an x otehrwise.
				sb.append("  ");
			}
			else
				sb.append("X ");
			sb.append("X");// then appends another x that the top right will
			               // always have.

			return sb.toString();
		}

		/**
		 * This method returns a string for just the middle of this node. It
		 * will check if it has visited east and west. Possible outcomes are
		 * "   ", "X  X", "   X" or "X   " Big O: O(k)
		 * 
		 * @return Returns a string of just the middle part of this node.
		 */
		public String middletoString() {
			StringBuilder sb = new StringBuilder();

			if (this.visitedWest) {// checks if this node has gone west and if
				                   // so gives it a blank.

				// if(solution){
				// sb.append(" V");
				// }else
				sb.append("  ");
			}
			else
				sb.append("X ");// Otherwise appends an X
			sb.append("  ");// Appends the middle of the node which is spaces.
			if (this.visitedEast) {
				sb.append(" ");// Checks if the node has gone east. If so, gives
				               // it spaces.
			}
			else
				sb.append("X");// Otherwise gives it an X.
			return sb.toString();
		}

		/**
		 * This will return a string that represents just the bottom of this
		 * node. Will check if the node has visited south. Will either return
		 * "X X X" or "X    X" Big O: O(k)
		 * 
		 * @return returns a string that represents just the bottom of this
		 *         node.
		 */
		public String bottomtoString() {
			StringBuilder sb = new StringBuilder();
			sb.append("X ");// Appends an x that the bottom left will always
			                // have.

			if (this.visitedSouth) {
				sb.append("  ");// Checks if the node went south. If so, appends
				                // a space
			}
			else
				sb.append("X ");// Otherwise appends an x.
			sb.append("X");// Then appends an x that the bottom right will
			               // always have

			return sb.toString();
		}

		/**
		 * A private method that will return a list of valid nodes that are not
		 * null and have not yet been visited. Big O : O(k)
		 * 
		 * @return Returns a list of nodes that are valid to travel to, from the
		 *         node calling the method.
		 */
		private ArrayList<Node> validNodes() {
			ArrayList<Node> list = new ArrayList<Node>();// Declares the list to
			                                             // return.

			if (this.north != null && this.north.visited == false) {// If the
				                                                    // node
				                                                    // calling
				                                                    // has a
				                                                    // north
				                                                    // neighboor
				                                                    // that
				                                                    // isn't
				                                                    // null and
				                                                    // hasnt
				                                                    // been
				                                                    // visited,
				                                                    // it adds
				                                                    // that node
				                                                    // to the
				                                                    // list.
				list.add(this.north);
			}
			if (this.east != null && this.east.visited == false) {// Same with
				                                                  // this except
				                                                  // east
				list.add(this.east);
			}
			if (this.south != null && this.south.visited == false) {// And here
				                                                    // but
				                                                    // south.
				list.add(this.south);
			}
			if (this.west != null && this.west.visited == false) {// And here
				                                                  // but west.
				list.add(this.west);
			}

			return list;// Returns the list.

		}
	}
}
