package de.getto.nicolas.tree;

import java.io.PrintStream;

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
	}
	
	public RedBlackTree(RBNode<T> root) {
		setupSentinel();
		this.root = root;
		root.setLeft(sentinel);
		root.setRight(sentinel);
	}
	
	public RBNode<T> getRoot() {
		return root;
	}
	
	public RBNode<T> getSentinal() {
		return sentinel;
	}
	
	/**
	 * Insert a node using a Bottom up method.
	 * @param node The node to add to T
	 */
	public void insertNodeBU(RBNode<T> node) {
		  RBNode<T> temp = null;
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
		  if (temp == null) {
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
	
	private void insertNodeUBFixup(RBNode<T> node) {
		while (node.getParent().getColor() == NodeColor.RED) {
			// if node's parent is the left child of its parent (it's parent is node's uncle)
			if (node.getParent() == node.getParent().getParent().getLeft()) {
				RBNode<T> rightUncle = node.getParent().getParent().getRight();
				if (rightUncle.getColor() == NodeColor.RED) {
					// Case 1
					node.getParent().setColor(NodeColor.BLACK);
					rightUncle.setColor(NodeColor.BLACK);
					node.getParent().getParent().setColor(NodeColor.RED);
					node = node.getParent().getParent();
				} else if (node == node.getParent().getRight()) { // if node is the right child
					// Case 2
					node = node.getParent();
					rotateLeft(node);
					// Case 3
					node.getParent().setColor(NodeColor.BLACK);
					node.getParent().getParent().setColor(NodeColor.RED);
					rotateRight(node.getParent().getParent());
				}
			}  else { // if node is the left child (same as right, but mirrored)
				RBNode<T> leftUncle = node.getParent().getParent().getLeft();
				if (leftUncle.getColor() == NodeColor.RED) {
					node.getParent().setColor(NodeColor.BLACK);
					leftUncle.setColor(NodeColor.BLACK);
					node.getParent().getParent().setColor(NodeColor.RED);
					node = node.getParent().getParent();
				} else if (node == node.getParent().getLeft()) {
					node = node.getParent();
					rotateRight(node);
					node.getParent().setColor(NodeColor.BLACK);
					node.getParent().getParent().setColor(NodeColor.RED);
					rotateLeft(node.getParent().getParent());
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
	private void rotateLeft(RBNode<T> node) {
		RBNode<T> y = node.getRight();
		node.setRight(y.getLeft());
		if (y.getLeft() != null) {
			y.getLeft().setParent(node);
		}
		y.setParent(node.getParent());
		if (node.getParent() == null) {
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
	private void rotateRight(RBNode<T> node) {
		RBNode<T> y = node.getLeft();
		node.setLeft(y.getRight());
		if (y.getRight() != null) {
			y.getRight().setParent(node);
		}
		y.setParent(node.getParent());
		if (node.getParent() == null) {
			root = y;
		} else if (node == node.getParent().getRight()) {
			node.getParent().setRight(y);
		} else {
			node.getParent().setLeft(y);
		}
		y.setRight(node);
		node.setParent(y);
	}
	
	public void sideViewPrint(PrintStream os) {
		printer.print(os, root);
	}
	
	/**
	 * Testmethod to construct a dummy tree.
	 * @param root
	 * @param leftChild
	 * @param rightChild
	 */
	public void buildDummyTree(RBNode<T> root, RBNode<T> leftChild, RBNode<T> rightChild,
			RBNode<T> rightRight, RBNode<T> rightLeft) {
		insertNodeBU(root);
		insertNodeBU(leftChild);
		insertNodeBU(rightChild);
		insertNodeBU(rightRight);
		insertNodeBU(rightLeft);
	}
}
