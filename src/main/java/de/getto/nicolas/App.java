package de.getto.nicolas;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

public class App {

	public static void main(String[] args) {
		RedBlackTree<Integer> dummyTree = new RedBlackTree<>();
		dummyTree.buildDummyTree(new RBNode<Integer>(5),new RBNode<Integer>(2),new RBNode<Integer>(15),
			new RBNode<Integer>(14),new RBNode<Integer>(16));
		dummyTree.sideViewPrint(System.out);
	}

}
