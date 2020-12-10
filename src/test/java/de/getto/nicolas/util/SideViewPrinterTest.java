package de.getto.nicolas.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

public class SideViewPrinterTest {

private RedBlackTree<Integer> rbTree;
	
	@BeforeEach
	public void setupTree() {
		rbTree = new RedBlackTree<Integer>();
	}

	@Test
	public void testPrintEmptyTree() {
		String expected = "";
		String actual = rbTree.sideViewPrint();
		
		assertEquals(expected, actual);
	}
	
	/*public void testTree() {
		String expected = "11:B\n├──L:14:B";
		rbTree.insertNodeBU(new RBNode<Integer>(11));
		rbTree.insertNodeBU(new RBNode<Integer>(14));
		rbTree.insertNodeBU(new RBNode<Integer>(15));
		rbTree.insertNodeBU(new RBNode<Integer>(2));
		rbTree.insertNodeBU(new RBNode<Integer>(1));
		rbTree.insertNodeBU(new RBNode<Integer>(7));
		rbTree.insertNodeBU(new RBNode<Integer>(8));
		rbTree.insertNodeBU(new RBNode<Integer>(5));
		rbTree.insertNodeBU(new RBNode<Integer>(4));
		String actual = rbTree.sideViewPrint();
		
		assertEquals(expected, actual);
	}*/
}
