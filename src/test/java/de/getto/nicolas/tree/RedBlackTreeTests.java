package de.getto.nicolas.tree;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.getto.nicolas.node.NodeColor;
import de.getto.nicolas.node.RBNode;

public class RedBlackTreeTests {
	
	private RedBlackTree<Integer> rbTree;
	
	@BeforeEach
	public void setupTree() {
		rbTree = new RedBlackTree<Integer>();
	}

	/*@Test
	public void insertIntoTree() {
		rbTree.insertNodeBU(new RBNode<Integer>(11));
		rbTree.insertNodeBU(new RBNode<Integer>(14));
		rbTree.insertNodeBU(new RBNode<Integer>(15));
		rbTree.insertNodeBU(new RBNode<Integer>(2));
		rbTree.insertNodeBU(new RBNode<Integer>(1));
		rbTree.insertNodeBU(new RBNode<Integer>(7));
		rbTree.insertNodeBU(new RBNode<Integer>(8));
		rbTree.insertNodeBU(new RBNode<Integer>(5));
		rbTree.insertNodeBU(new RBNode<Integer>(4));
	}*/
	
	@Test
	public void insertNodeIntoEmpty() {
		Object[] actualElements = new Object[3];
		
		rbTree.insertNodeBU(new RBNode<Integer>(11));
		Object[] expectedElements = {rbTree.getSentinel(), rbTree.getSentinel(), rbTree.getSentinel()};
		actualElements[0] = rbTree.getRoot().getParent();
		actualElements[1] = rbTree.getRoot().getLeft();
		actualElements[2] = rbTree.getRoot().getRight();
		
		assertEquals(11, rbTree.getRoot().getKey());
		assertEquals(NodeColor.BLACK, rbTree.getRoot().getColor());
		assertArrayEquals(expectedElements, actualElements);
	}
	
	@Test
	public void rotateLeftTest() {
		// setUpTree();
		//rbTree.rotateLeft(rbTree.getRoot());
		
		//assertEquals(8, rbTree.getRoot().getKey());
	}
	
	private void setUpTree() {
		rbTree.setRoot(new RBNode<>(5));
		rbTree.getRoot().setColor(NodeColor.BLACK);
		rbTree.getRoot().setLeft(new RBNode<>(2));
		rbTree.getRoot().getLeft().setColor(NodeColor.BLACK);
		rbTree.getRoot().setRight(new RBNode<>(8));
		rbTree.getRoot().getRight().setColor(NodeColor.BLACK);
		rbTree.getRoot().getRight().setLeft(new RBNode<>(7));
		rbTree.getRoot().getRight().getLeft().setColor(NodeColor.RED);
		rbTree.getRoot().getRight().setRight(new RBNode<>(9));
		rbTree.getRoot().getRight().getRight().setColor(NodeColor.RED);
	}
	
}
