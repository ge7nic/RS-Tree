package de.getto.nicolas.tree;

import de.getto.nicolas.node.*;
import de.getto.nicolas.util.SideViewPrinter;

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
	 * Insert a node using a Bottom up method.
	 * @param node The node to add to T
	 */
	public void insertNodeBU(RBNode<T> node) {
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
		  insertNodeUBFixup(node);
	}
	
	//TODO : FIX
	private void insertNodeUBFixup(RBNode<T> node) {
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
					rotateRight(node);
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
					rotateLeft(node);
				}
			}
		}
		root.setColor(NodeColor.BLACK);
		sentinel.setParent(root);
	}
	
	/**
	 * Rotate the Tree to the left. Used for Insert-Fixup.
	 * @param node The node which the rotation "pivots" around.
	 */
	protected void rotateLeft(RBNode<T> node) {
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
	protected void rotateRight(RBNode<T> node) {
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
}
