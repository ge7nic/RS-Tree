package de.getto.nicolas.util;

import de.getto.nicolas.node.*;

/**
 * This Class prints the Tree in a side view. It uses Preorder Traversal.
 * @author Nicolas Getto
 *
 * @param <T> Value of the Tree to print.
 */
public class SideViewPrinter<T extends Comparable<T>> {
	
	private RBNode<T> sentinel;
	
	public SideViewPrinter(RBNode<T> sentinel) {
		this.sentinel = sentinel;
	}
	
	/**
	 * This Method gets called from the public print Method. It starts the printing process
	 * by creating a new StringBuilder "stringTree (st)" which will hold the finished tree.
	 * @param root The Treeroot.
	 * @return The finished Tree to print on the console.
	 */
	private String preOrderTraverse(RBNode<T> root) {
		if (root == sentinel) {
			return "";
		}
		
		StringBuilder st = new StringBuilder();
		st.append(root.getKey());
		st.append(":");
		st.append(root.getColor() == NodeColor.BLACK ? "B" : "R");
		

		String leftPointer = (root.getRight() != sentinel) ? "├──L:" : "└──L:";
		String rightPointer = "└──R:";
		
		goThroughNodes(st, "", leftPointer, root.getLeft(), root.getRight() != sentinel);
		goThroughNodes(st, "", rightPointer, root.getRight(), false);
		
		return st.toString();
	}
	
	/**
	 * This is used to traverse every node except the root. This is necessary to not have a lot 
	 * of unnecessary lines.
	 * @param stringTree The StringBuilder that holds the Tree.
	 * @param padding The padding between nodes.
	 * @param pointer The Pointer is either "└──" if the node is left, "├──" if the node is right
	 * 					and it has a right child or "└──" if not.
	 * @param node	The node from which to draw next.
	 * @param hasRightSibling Boolean to check if node has a right Sibling.
	 */
	private void goThroughNodes(StringBuilder stringTree, String padding, String pointer,
			RBNode<T> node, boolean hasRightSibling) {
		if (node != sentinel) {
			stringTree.append("\n");
			stringTree.append(padding);
			stringTree.append(pointer);
			stringTree.append(node.getKey());
			stringTree.append(":");
			stringTree.append(node.getColor() == NodeColor.BLACK ? "B" : "R");
			
			StringBuilder pBuilder = new StringBuilder(padding);
			if (hasRightSibling) {
				pBuilder.append("|    ");
			} else {
				pBuilder.append("     ");
			}
			
			String newPadding = pBuilder.toString();
			String leftPointer = (node.getRight() != sentinel) ? "├──L:" : "└──L:";
			String rightPointer = "└──R:";
			
			goThroughNodes(stringTree, newPadding, leftPointer, node.getLeft(), node.getRight() != sentinel);
			goThroughNodes(stringTree, newPadding, rightPointer, node.getRight(), false); 
		}
	}
	
	public String print(RBNode<T> root) {
		return preOrderTraverse(root);
	}
}
