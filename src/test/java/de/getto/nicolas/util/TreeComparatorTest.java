package de.getto.nicolas.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

public class TreeComparatorTest {

	private final TreeComparator treeC = new TreeComparator();
	private RedBlackTree<Integer> intTreeOne;
	private RedBlackTree<Integer> intTreeTwo;
	
	@BeforeEach
	public void setUp() {
		intTreeOne = new RedBlackTree<>();
		intTreeTwo = new RedBlackTree<>();
	}
	
	@Test
	public void testEmptyTrees() {
		assertTrue(treeC.isEqualTree(intTreeOne.getRoot(), intTreeTwo.getRoot(),
				intTreeOne.getSentinel(), intTreeTwo.getSentinel()));
	}
	
	@Test
	public void testTreesWithDifferentRoots() {
		intTreeOne.insertNodeBU(new RBNode<>(5));
		intTreeTwo.insertNodeBU(new RBNode<>(1));
		assertFalse(treeC.isEqualTree(intTreeOne.getRoot(), intTreeTwo.getRoot(),
				intTreeOne.getSentinel(), intTreeTwo.getSentinel()));
	}
	
	@Test
	public void testTreesWithOnlyRoots() {
		intTreeOne.insertNodeBU(new RBNode<>(3));
		intTreeTwo.insertNodeBU(new RBNode<>(3));
		assertTrue(treeC.isEqualTree(intTreeOne.getRoot(), intTreeTwo.getRoot(),
				intTreeOne.getSentinel(), intTreeTwo.getSentinel()));
	}
}
