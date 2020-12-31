package de.getto.nicolas.tree;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.getto.nicolas.node.NodeColor;
import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.util.TreeComparator;

public class RedBlackTreeTests {
	
	private RedBlackTree<Integer> emptyTree;
	private RedBlackTree<Integer> nonEmptyTree = new RedBlackTree<>(new RBNode<>(15));
	private final TreeComparator comparator = new TreeComparator();
	private static final int[] TEST_TREE = {11, 14, 2, 1, 7, 8, 5, 15, 4};
	
	@BeforeEach
	public void setup() {
		emptyTree = new RedBlackTree<Integer>();
	}

	@Test
	public void insertAlotOfNodesIntoTree() {
		RedBlackTree<Integer> expectedTree = setupCorrectTree();
		
		fillTree();
		
		assertTrue(comparator.isEqualTree(expectedTree.getRoot(), emptyTree.getRoot(),
				expectedTree.getSentinel(), emptyTree.getSentinel()));
	}
	
	@Test
	public void insertNodeIntoNonEmptyTree() {
		nonEmptyTree.insertNodeBU(new RBNode<>(9));
		nonEmptyTree.insertNodeBU(new RBNode<>(17));
		
		assertEquals(15, nonEmptyTree.getRoot().getKey());
		assertEquals(NodeColor.BLACK, nonEmptyTree.getRoot().getColor());
		assertEquals(9, nonEmptyTree.getRoot().getLeft().getKey());
		assertEquals(NodeColor.RED, nonEmptyTree.getRoot().getLeft().getColor());
		assertEquals(17, nonEmptyTree.getRoot().getRight().getKey());
		assertEquals(NodeColor.RED, nonEmptyTree.getRoot().getRight().getColor());
		assertEquals(nonEmptyTree.getSentinel(), nonEmptyTree.getRoot().getLeft().getLeft());
		assertEquals(nonEmptyTree.getSentinel(), nonEmptyTree.getRoot().getLeft().getRight());
		assertEquals(nonEmptyTree.getSentinel(), nonEmptyTree.getRoot().getRight().getLeft());
		assertEquals(nonEmptyTree.getSentinel(), nonEmptyTree.getRoot().getRight().getRight());
	}
	
	@Test
	public void insertNodeIntoEmpty() {
		Object[] actualElements = new Object[3];
		
		emptyTree.insertNodeBU(new RBNode<Integer>(11));
		Object[] expectedElements = {emptyTree.getSentinel(), emptyTree.getSentinel(), emptyTree.getSentinel()};
		actualElements[0] = emptyTree.getRoot().getParent();
		actualElements[1] = emptyTree.getRoot().getLeft();
		actualElements[2] = emptyTree.getRoot().getRight();
		
		assertEquals(11, emptyTree.getRoot().getKey());
		assertEquals(NodeColor.BLACK, emptyTree.getRoot().getColor());
		assertArrayEquals(expectedElements, actualElements);
	}
	
	@Test
	public void rotateLeftTest() {
		setupTreeForLeftRotation();
		emptyTree.rotateLeft(emptyTree.getRoot());
		
		assertEquals(8, emptyTree.getRoot().getKey());
		assertEquals(9, emptyTree.getRoot().getRight().getKey());
		assertEquals(5, emptyTree.getRoot().getLeft().getKey());
		assertEquals(2, emptyTree.getRoot().getLeft().getLeft().getKey());
		assertEquals(7, emptyTree.getRoot().getLeft().getRight().getKey());
	}
	
	@Test
	public void rotateRightTest() {
		setupTreeForRightRotation();
		emptyTree.rotateRight(emptyTree.getRoot());
		
		assertEquals(5, emptyTree.getRoot().getKey());
		assertEquals(2, emptyTree.getRoot().getLeft().getKey());
		assertEquals(8, emptyTree.getRoot().getRight().getKey());
		assertEquals(7, emptyTree.getRoot().getRight().getLeft().getKey());
		assertEquals(9, emptyTree.getRoot().getRight().getRight().getKey());
	}
	
	@Test
	public void verifyEmptyTree() {
		assertTrue(emptyTree.verifyTree());
	}
	
	@Test
	public void verifySmallTree() {
		assertTrue(nonEmptyTree.verifyTree());
	}
	
	@Test
	public void verifyBigTree() {
		fillTree();
		
		assertTrue(emptyTree.verifyTree());
	}
	
	@Test
	public void verifyTreeThatConflictsWithPropertyTwo() {
		nonEmptyTree.getRoot().setColor(NodeColor.RED);
		
		assertFalse(nonEmptyTree.verifyTree());
	}
	
	@Test
	public void verifyTreeThatConflictsWithPropertyThree() {
		RBNode<Integer> node = nonEmptyTree.getRoot();
		while (node != nonEmptyTree.getSentinel()) {
			node = node.getLeft();
		}
		node.setColor(NodeColor.RED);
		assertFalse(nonEmptyTree.verifyTree());
	}
	
	@Test
	public void deleteNodeThatHasChildrenInBothDirections() {
		RBNode<Integer> x = new RBNode<Integer>(11);
		RBNode<Integer> y = new RBNode<Integer>(14);
		RBNode<Integer> z = new RBNode<Integer>(2);
		RBNode<Integer> a = new RBNode<Integer>(1);
		RBNode<Integer> b = new RBNode<Integer>(7);
		RBNode<Integer> c = new RBNode<Integer>(8);
		RBNode<Integer> d = new RBNode<Integer>(5);
		RBNode<Integer> e = new RBNode<Integer>(15);
		RBNode<Integer> f = new RBNode<Integer>(4);
		emptyTree.insertNodeBU(x);
		emptyTree.insertNodeBU(y);
		emptyTree.insertNodeBU(z);
		emptyTree.insertNodeBU(a);
		emptyTree.insertNodeBU(b);
		emptyTree.insertNodeBU(c);
		emptyTree.insertNodeBU(d);
		emptyTree.insertNodeBU(e);
		emptyTree.insertNodeBU(f);

		assertEquals(4, emptyTree.deleteRBNode(z));
		assertTrue(emptyTree.verifyTree());	
	}
	
	@Test
	public void deleteNodeThatHasNoLeftChildButARightOne() {
		RBNode<Integer> x = new RBNode<Integer>(11);
		RBNode<Integer> y = new RBNode<Integer>(14);
		RBNode<Integer> z = new RBNode<Integer>(2);
		RBNode<Integer> a = new RBNode<Integer>(1);
		RBNode<Integer> b = new RBNode<Integer>(7);
		RBNode<Integer> c = new RBNode<Integer>(8);
		RBNode<Integer> d = new RBNode<Integer>(5);
		RBNode<Integer> e = new RBNode<Integer>(15);
		RBNode<Integer> f = new RBNode<Integer>(4);
		emptyTree.insertNodeBU(x);
		emptyTree.insertNodeBU(y);
		emptyTree.insertNodeBU(z);
		emptyTree.insertNodeBU(a);
		emptyTree.insertNodeBU(b);
		emptyTree.insertNodeBU(c);
		emptyTree.insertNodeBU(d);
		emptyTree.insertNodeBU(e);
		emptyTree.insertNodeBU(f);

		assertEquals(14, emptyTree.deleteRBNode(y));
		assertTrue(emptyTree.verifyTree());	
	}
	
	@Test
	public void deleteNodeThatHasNoRightChildButALeftOne() {
		RBNode<Integer> x = new RBNode<Integer>(11);
		RBNode<Integer> y = new RBNode<Integer>(14);
		RBNode<Integer> z = new RBNode<Integer>(2);
		RBNode<Integer> a = new RBNode<Integer>(1);
		RBNode<Integer> b = new RBNode<Integer>(7);
		RBNode<Integer> c = new RBNode<Integer>(8);
		RBNode<Integer> d = new RBNode<Integer>(5);
		RBNode<Integer> e = new RBNode<Integer>(15);
		RBNode<Integer> f = new RBNode<Integer>(4);
		emptyTree.insertNodeBU(x);
		emptyTree.insertNodeBU(y);
		emptyTree.insertNodeBU(z);
		emptyTree.insertNodeBU(a);
		emptyTree.insertNodeBU(b);
		emptyTree.insertNodeBU(c);
		emptyTree.insertNodeBU(d);
		emptyTree.insertNodeBU(e);
		emptyTree.insertNodeBU(f);

		assertEquals(5, emptyTree.deleteRBNode(d));
		assertTrue(emptyTree.verifyTree());	
	}
	
	@Test
	public void searchNodeByExistingValueOnTheRight() {
		fillTree();
		
		assertEquals(8, emptyTree.findNode(8).getKey());
	}
	
	@Test
	public void searchNodeByExistingValueOnTheLeft() {
		fillTree();
		
		assertEquals(2, emptyTree.findNode(2).getKey());
	}
	
	@Test
	public void searchNodeByNonExistingValue() {
		fillTree();
		
		assertEquals(null, emptyTree.findNode(255).getKey());
	}
	
	@Test
	public void deleteNodeByValue() {
		fillTree();
		
		assertTrue(emptyTree.deleteNodeByValue(8));
	}
	
	@Test
	public void deleteLeafByValue() {
		fillTree();
		
		assertTrue(emptyTree.deleteNodeByValue(15));
	}
	
	@Test
	public void deleteNodeByValueOnTheLeft() {
		fillTree();
		
		assertTrue(emptyTree.deleteNodeByValue(2));
	}
	
	@Test
	public void deleteNodeByNonExistingValue() {
		fillTree();
		
		assertFalse(emptyTree.deleteNodeByValue(255));
	}
	
	private void fillTree() {
		for (int i : TEST_TREE) {
			RBNode<Integer> node = new RBNode<Integer>(i);
			emptyTree.insertNodeBU(node);
		}
	}
	
	private void setupTreeForLeftRotation() {
		emptyTree.setRoot(new RBNode<>(5));
		emptyTree.getRoot().setColor(NodeColor.BLACK);
		emptyTree.getRoot().setLeft(new RBNode<>(2));
		emptyTree.getRoot().getLeft().setColor(NodeColor.BLACK);
		emptyTree.getRoot().setRight(new RBNode<>(8));
		emptyTree.getRoot().getRight().setColor(NodeColor.BLACK);
		emptyTree.getRoot().getRight().setLeft(new RBNode<>(7));
		emptyTree.getRoot().getRight().getLeft().setColor(NodeColor.RED);
		emptyTree.getRoot().getRight().setRight(new RBNode<>(9));
		emptyTree.getRoot().getRight().getRight().setColor(NodeColor.RED);
	}
	
	private void setupTreeForRightRotation() {
		emptyTree.setRoot(new RBNode<>(8));
		emptyTree.getRoot().setColor(NodeColor.BLACK);
		emptyTree.getRoot().setRight(new RBNode<>(9));
		emptyTree.getRoot().getRight().setColor(NodeColor.RED);
		emptyTree.getRoot().setLeft(new RBNode<>(5));
		emptyTree.getRoot().getLeft().setColor(NodeColor.BLACK);
		emptyTree.getRoot().getLeft().setLeft(new RBNode<>(2));
		emptyTree.getRoot().getLeft().getLeft().setColor(NodeColor.BLACK);
		emptyTree.getRoot().getLeft().setRight(new RBNode<>(7));
		emptyTree.getRoot().getLeft().getRight().setColor(NodeColor.RED);
	}
	
	private RedBlackTree<Integer> setupCorrectTree() {
		RedBlackTree<Integer> correctTree = new RedBlackTree<>(new RBNode<>(7));
		correctTree.getRoot().setRight(new RBNode<>(11));
		correctTree.getRoot().getRight().setLeft(new RBNode<>(8));
		correctTree.getRoot().getRight().getLeft().setLeft(correctTree.getSentinel());
		correctTree.getRoot().getRight().getLeft().setRight(correctTree.getSentinel());
		correctTree.getRoot().getRight().getLeft().setColor(NodeColor.BLACK);
		correctTree.getRoot().getRight().setRight(new RBNode<>(14));
		correctTree.getRoot().getRight().getRight().setLeft(correctTree.getSentinel());
		correctTree.getRoot().getRight().getRight().setColor(NodeColor.BLACK);
		correctTree.getRoot().getRight().getRight().setRight(new RBNode<>(15));
		correctTree.getRoot().getRight().getRight().getRight().setLeft(correctTree.getSentinel());
		correctTree.getRoot().getRight().getRight().getRight().setRight(correctTree.getSentinel());
		
		correctTree.getRoot().setLeft(new RBNode<>(2));
		correctTree.getRoot().getLeft().setLeft(new RBNode<>(1));
		correctTree.getRoot().getLeft().getLeft().setLeft(correctTree.getSentinel());
		correctTree.getRoot().getLeft().getLeft().setRight(correctTree.getSentinel());
		correctTree.getRoot().getLeft().getLeft().setColor(NodeColor.BLACK);
		correctTree.getRoot().getLeft().setRight(new RBNode<>(5));
		correctTree.getRoot().getLeft().getRight().setRight(correctTree.getSentinel());
		correctTree.getRoot().getLeft().getRight().setColor(NodeColor.BLACK);
		correctTree.getRoot().getLeft().getRight().setLeft(new RBNode<>(4));
		correctTree.getRoot().getLeft().getRight().getLeft().setLeft(correctTree.getSentinel());
		correctTree.getRoot().getLeft().getRight().getLeft().setRight(correctTree.getSentinel());
		
		return correctTree;
	}
}
