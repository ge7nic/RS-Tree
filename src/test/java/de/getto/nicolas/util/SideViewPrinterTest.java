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
	
	@Test
	public void testTree() {
		String expected = "7:B\n├──L:2:R\n|    ├──L:1:B\n|    └──R:5:B\n|         └──L:4:R\n└──R:11:R\n     ├──L:8:B\n     └──R:14:B\n          └──R:15:R";
		rbTree.insertNodeBU(new RBNode<Integer>(11));
		rbTree.insertNodeBU(new RBNode<Integer>(14));
		rbTree.insertNodeBU(new RBNode<Integer>(2));
		rbTree.insertNodeBU(new RBNode<Integer>(1));
		rbTree.insertNodeBU(new RBNode<Integer>(7));
		rbTree.insertNodeBU(new RBNode<Integer>(8));
		rbTree.insertNodeBU(new RBNode<Integer>(5));
		rbTree.insertNodeBU(new RBNode<Integer>(15));
		rbTree.insertNodeBU(new RBNode<Integer>(4));
		String actual = rbTree.sideViewPrint();
		
		assertEquals(expected, actual);
	}
}
