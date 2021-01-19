package de.getto.nicolas.tree;

import de.getto.nicolas.node.*;
import de.getto.nicolas.util.SideViewPrinter;

/**
 * Class that represents a RedBlackTree. It implements the following functions:
 * 	-	Insertion Bottom-Up
 * 	-	Delete a Node by Node
 * 	-	Delete a Node by Value
 * 	-	Verify a Tree
 * 	-	Rotate Left 
 * 	-	Rotate Right
 * 	TODO:
 * 	-	Get Height
 * 	-	Get BlackHeight
 * 	-	Find a Node by value
 * 	-	Join two Trees
 * 	-	Split a Tree 
 * @author Nicolas Getto
 *
 * @param <T> The Value of the Tree. Every Tree can only hold one type of value.
 */
public class RedBlackTree<T extends Comparable<T>> {
	private RBNode<T> root;
	private RBNode<T> sentinel = RBNode.emptyNode();
	private SideViewPrinter<T> printer;

	private void setupSentinel() {
		sentinel.setLeft(sentinel);
		sentinel.setRight(sentinel);
		sentinel.setColor(NodeColor.BLACK);
		printer = new SideViewPrinter<>(sentinel);
	}
	
	public RedBlackTree() {
		setupSentinel();
		root = sentinel;
		root.setLeft(sentinel);
		root.setRight(sentinel);
		root.setParent(sentinel);
	}
	
	public RedBlackTree(RBNode<T> root) {
		setupSentinel();
		this.root = root;
		root.setColor(NodeColor.BLACK);
		root.setLeft(sentinel);
		root.setRight(sentinel);
		root.setParent(sentinel);
	}
	
	public RBNode<T> getRoot() {
		return root;
	}
	
	public void setRoot(RBNode<T> root) {
		this.root = root;
		this.root.setParent(sentinel);
	}
	
	public RBNode<T> getSentinel() {
		return sentinel;
	}
	
	/**
	 * If this method is called, insertNodeBUFixup is always going to be used.
	 * @param node
	 */
	public void insertNodeBU(RBNode<T> node) {
		insertNodeBU(node, true);
	}
	
	/**
	 * Insert a node using a Bottom up method.
	 * @param node The node to add to T
	 */
	public void insertNodeBU(RBNode<T> node, boolean doFixup) {
		  RBNode<T> temp = sentinel;
		  RBNode<T> tempRoot = root;
		  // Traverse the Tree: Go Left if the new Key is less than the current node, otherwise
		  // go right.
		  while (tempRoot != sentinel) {
			  temp = tempRoot;
			  if (node.getKey().compareTo(tempRoot.getKey()) <= -1) {
				  tempRoot = tempRoot.getLeft();
			  } else {
				  tempRoot = tempRoot.getRight();
			  }
		  }
		  // Set the new node as the new Root if temp didn't change => The Tree is empty.
		  node.setParent(temp);
		  if (temp == sentinel) {
			  root = node;
			  root.setParent(sentinel);
		  // If value of the new node is smaller than the current one, set it as a left child
		  } else if (node.getKey().compareTo(temp.getKey()) <= -1) {
			  temp.setLeft(node);
		  // Otherwise it's a right child
		  } else {
			  temp.setRight(node);
		  }
		  node.setLeft(sentinel);
		  node.setRight(sentinel);
		  node.setColor(NodeColor.RED);
		  if (doFixup) {
			  insertNodeBUFixup(node);  
		  }
	}
	
	public void insertNodeBUFixup(RBNode<T> node) {
		while (node != root && node.getParent().getColor() == NodeColor.RED) {
			if (node.getParent() == node.getParent().getParent().getLeft()) {
				RBNode<T> rightUncle = node.getParent().getParent().getRight();
				if (rightUncle.getColor() == NodeColor.RED) {
					// Case 1
					node.getParent().setColor(NodeColor.BLACK);
					rightUncle.setColor(NodeColor.BLACK);
					node.getParent().getParent().setColor(NodeColor.RED);
					node = node.getParent().getParent();
				} else {
					if (node == node.getParent().getRight()) {
						// Case 2
						node = node.getParent();
						rotateLeft(node);
					}
					// Case 3
					node.getParent().setColor(NodeColor.BLACK);
					node.getParent().getParent().setColor(NodeColor.RED);
					rotateRight(node.getParent().getParent());
				}
			} else {
				// left Uncle
				RBNode<T> leftUncle = node.getParent().getParent().getLeft();
				if (leftUncle.getColor() == NodeColor.RED) {
					// Case 1
					node.getParent().setColor(NodeColor.BLACK);
					leftUncle.setColor(NodeColor.BLACK);
					node.getParent().getParent().setColor(NodeColor.RED);
					node = node.getParent().getParent();
				} else {
					if (node == node.getParent().getLeft()) {
						// Case 2
						node = node.getParent();
						rotateRight(node);
					}
					// Case 3
					node.getParent().setColor(NodeColor.BLACK);
					node.getParent().getParent().setColor(NodeColor.RED);
					rotateLeft(node.getParent().getParent());
				}
			}
		}
		root.setColor(NodeColor.BLACK);
		sentinel.setParent(root);
	}
	
	public boolean deleteNodeByValue(T val) {
		RBNode<T> node = findNode(val);
		
		if (node != sentinel) {
			deleteRBNode(node);
			return true;
		}
		return false;
	}
	
	/**
	 * Find Node by value.
	 * @param val value to search for.
	 * @return Either the node or sentinel if the value wasnt in the Tree.
	 */
	public RBNode<T> findNode(T val) {
		RBNode<T> node = root;
		
		while (node != sentinel) {
			if (node.getKey().compareTo(val) <= -1) {
				node = node.getRight();
			} else if (node.getKey().compareTo(val) >= 1) {
				node = node.getLeft();
			} else {
				return node;
			}
		}
		return sentinel;
	}
	
	/**
	 * Gets the Height of this Tree.
	 * @param node Root of the current subtree.
	 * @return Tree height
	 */
	public int treeHeight(RBNode<T> node) {
		return node == sentinel ? -1 : Integer.max(treeHeight(node.getLeft()), treeHeight(node.getRight())) + 1;
	}
	
	/**
	 * Deletes a given node. 
	 * @param node The node to delete.
	 * @return value of either the deleted node itself (if either the right or left was empty) or the direct successor.
	 */
	public T deleteRBNode(RBNode<T> node) {
		RBNode<T> y = node.getRight() == sentinel || node.getLeft() == sentinel ? node : getTreeSuccessor(node);
		RBNode<T> z = y.getLeft() != sentinel ? y.getLeft() : y.getRight();
		z.setParent(y.getParent());
		if (y.getParent() == sentinel) {
			root = z;
		} else if (y == y.getParent().getLeft()) {
			y.getParent().setLeft(z);
		} else {
			y.getParent().setRight(z);
		}
		if (y != node) {
			node.setKey(y.getKey());
			// copy satellite data from y to node - in this case there is no satellite data
		}

		if (y.getColor() == NodeColor.BLACK) {
			deleteNodeFixup(z);
		}
		return y.getKey();
	}
	
	/**
	 * Fixup method for delete. Only gets executed if y is Black.
	 * 
	 * @param node either the left of y or the right depending on if left was the minimum in it's subtree or not.
	 */
	private void deleteNodeFixup(RBNode<T> node) {
		RBNode<T> w;
		while (node != root && node.getColor() == NodeColor.BLACK) {
			if (node == node.getParent().getLeft()) {
				w = node.getParent().getRight();
				if (w.getColor() == NodeColor.RED) {
					// Case 1
					w.setColor(NodeColor.BLACK);
					node.getParent().setColor(NodeColor.RED);
					rotateLeft(node.getParent());
					w = node.getParent().getRight();
				}
				if (w.getLeft().getColor() == NodeColor.BLACK && w.getRight().getColor() == NodeColor.BLACK) {
					// Case 2
					w.setColor(NodeColor.RED);
					node = node.getParent();
				} else {
					if (w.getRight().getColor() == NodeColor.BLACK) {
						// Case 3
						w.getLeft().setColor(NodeColor.BLACK);
						w.setColor(NodeColor.RED);
						rotateRight(w);
						w = node.getParent().getRight();
					}
					// Case 4
					w.setColor(node.getParent().getColor());
					node.getParent().setColor(NodeColor.BLACK);
					w.getRight().setColor(NodeColor.BLACK);
					rotateLeft(node.getParent());
					node = root;
				}
			} else {
				// Same as if clause, but right and left reversed
				w = node.getParent().getLeft();
				if (w.getColor() == NodeColor.RED) {
					// Case 1
					w.setColor(NodeColor.BLACK);
					node.getParent().setColor(NodeColor.RED);
					rotateRight(node.getParent());
					w = node.getParent().getLeft();
				}
				if (w.getRight().getColor() == NodeColor.BLACK && w.getLeft().getColor() == NodeColor.BLACK) {
					// Case 2
					w.setColor(NodeColor.RED);
					node = node.getParent();
				} else {
					if (w.getLeft().getColor() == NodeColor.BLACK) {
						// Case 3
						w.getRight().setColor(NodeColor.BLACK);
						w.setColor(NodeColor.RED);
						rotateLeft(w);
						w = node.getParent().getLeft();
					}
					// Case 4
					w.setColor(node.getParent().getColor());
					node.getParent().setColor(NodeColor.BLACK);
					w.getLeft().setColor(NodeColor.BLACK);
					rotateRight(node.getParent());
					node = root;
				}
			}
		}
		node.setColor(NodeColor.BLACK);
	}
	
	/**
	 * Method to search for the subtree that succeeds node.
	 * If the right subtree is not empty, the direct successor tree is the minimum of the right subtree.
	 * Otherwise if the right subtree is empty and node has a successor temp, then temp is the successor of node.
	 * @param node root of subtree
	 * @return the direct successor node, marking the root of the subtree.
	 */
	public RBNode<T> getTreeSuccessor(RBNode<T> node) {
		if (node.getRight() != sentinel) {
			return getTreeMinimum(node.getRight());
		}
		RBNode<T> temp = node.getParent();
		while (temp != sentinel && node == temp.getRight()) {
			node = temp;
			temp = temp.getParent();
		}
		return temp;
	}
	
	/**
	 * Search for the node with min value. Symmetrical to getTreeMaximum.
	 * @param node Node to start with.
	 * @return The node with the minimum value.
	 */
	private RBNode<T> getTreeMinimum(RBNode<T> node) {
		RBNode<T> minNode = node;
		while (minNode.getLeft() != sentinel) {
			minNode = minNode.getLeft();
		}
		return minNode;
	}
	
	/**
	 * Search for the node with the max value. Symmetrical to getTreeMinimum.
	 * @param node Node to start with.
	 * @return The node with the maximum value.
	 */
	@SuppressWarnings("unused")
	private RBNode<T> getTreeMaximum(RBNode<T> node) {
		RBNode<T> maxNode = node;
		while (maxNode.getRight() != sentinel) {
			maxNode = maxNode.getRight();
		}
		return maxNode;
	}
	
	/**
	 * Rotate the Tree to the left. Used for Insert-Fixup.
	 * @param node The node which the rotation "pivots" around.
	 */
	public void rotateLeft(RBNode<T> node) {
		RBNode<T> y = node.getRight();
		node.setRight(y.getLeft());
		if (y.getLeft() != sentinel) {
			y.getLeft().setParent(node);
		}
		y.setParent(node.getParent());
		if (node.getParent() == sentinel) {
			root = y;
		} else if (node == node.getParent().getLeft()) {
			node.getParent().setLeft(y);
		} else {
			node.getParent().setRight(y);
		}
		y.setLeft(node);
		node.setParent(y);
	}
	
	/**
	 * Rotate the Tree to the right. Used for Insert-Fixup.
	 * @param node The node which the rotation "pivots" around.
	 */
	public void rotateRight(RBNode<T> node) {
		RBNode<T> y = node.getLeft();
		node.setLeft(y.getRight());
		if (y.getRight() != sentinel) {
			y.getRight().setParent(node);
		}
		y.setParent(node.getParent());
		if (node.getParent() == sentinel) {
			root = y;
		} else if (node == node.getParent().getRight()) {
			node.getParent().setRight(y);
		} else {
			node.getParent().setLeft(y);
		}
		y.setRight(node);
		node.setParent(y);
	}
	
	public String sideViewPrint() {
		return printer.print(root);
	}
	
	public boolean contains(T val) {
		return findNode(val) != sentinel ? true : false;
	}
	
	/**
	 * 5 Properties:
	 * 	1. Every node is colored.
	 * 	2. Root is always Black.
	 * 	3. Every leaf is Black.
	 * 	4. Is a node Red, are both Children Black.
	 * 	5. Every Path to underlying nodes has the same number of black nodes.
	 * @return Returns true if the Tree is valid.
	 */
	public boolean verifyTree() {
		RBNode<T> node = root;
		int noOfBlackNodes = 0;
		while (node != sentinel) {
			if (node.getColor() == NodeColor.BLACK) {
				noOfBlackNodes++;
			}
			node = node.getLeft();
		}

		return checkSubtree(root, noOfBlackNodes);
	}
	
	private boolean checkSubtree(RBNode<T> node, int noOfBlackNodes) {
		// Case 2
		if (node == root && node.getColor() == NodeColor.RED) {
			return false;
		}
		// Case 3
		if (node == sentinel && node.getColor() == NodeColor.BLACK) {
			return noOfBlackNodes == 0;
		}
		// Case 4
		if (node.getColor() == NodeColor.RED) {
			return (node.getLeft().getColor() == NodeColor.BLACK 
					&& node.getRight().getColor() == NodeColor.BLACK) ? true : false;
		} else {
			noOfBlackNodes--;
		}
		return checkSubtree(node.getLeft(), noOfBlackNodes) && checkSubtree(node.getRight(), noOfBlackNodes);
	}
	
}
