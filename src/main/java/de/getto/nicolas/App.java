package de.getto.nicolas;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

public class App {

	public static void main(String[] args) {
		RedBlackTree<Integer> rbTree = new RedBlackTree<>();
		
		System.out.println(rbTree.verifyTree());
	}

}
