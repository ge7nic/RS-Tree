package de.getto.nicolas;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

public class App {

	public static void main(String[] args) {
		RedBlackTree<Integer> rbTree = new RedBlackTree<>();
		RBNode<Integer> x = new RBNode<Integer>(5);
		RBNode<Integer> y = new RBNode<Integer>(11);
		RBNode<Integer> z = new RBNode<Integer>(3);
		RBNode<Integer> temp = new RBNode<Integer>(7);
		rbTree.insertNodeBU(x);
		rbTree.insertNodeBU(y);
		rbTree.insertNodeBU(z);
		rbTree.insertNodeBU(temp);
		System.out.println(rbTree.sideViewPrint());
		System.out.println(rbTree.deleteRBNode(x));
		System.out.println(rbTree.sideViewPrint());
	}

}
