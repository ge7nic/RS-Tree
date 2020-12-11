package de.getto.nicolas;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;
import de.getto.nicolas.util.TreeComparator;

public class App {

	public static void main(String[] args) {
		RedBlackTree<Integer> rbTree = new RedBlackTree<>();
		rbTree.insertNodeBU(new RBNode<Integer>(11));
		rbTree.insertNodeBU(new RBNode<Integer>(14));
		rbTree.insertNodeBU(new RBNode<Integer>(2));
		rbTree.insertNodeBU(new RBNode<Integer>(1));
		rbTree.insertNodeBU(new RBNode<Integer>(7));
		rbTree.insertNodeBU(new RBNode<Integer>(8));
		rbTree.insertNodeBU(new RBNode<Integer>(5));
		rbTree.insertNodeBU(new RBNode<Integer>(15));
		rbTree.insertNodeBU(new RBNode<Integer>(4));
		System.out.println(rbTree.sideViewPrint());
	}

}
