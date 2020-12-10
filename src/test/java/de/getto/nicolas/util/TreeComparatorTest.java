package de.getto.nicolas.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		assertTrue(treeC.isEqualTree(intTreeOne.getRoot(), intTreeTwo.getRoot()));
	}
	
	@Test
	public void testTreeWithOnlyRoot() {
		
	}
}
