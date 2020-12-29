package de.getto.nicolas.controller;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class TreeDrawerLayout extends AnchorPane {
	
	private RedBlackTree<Integer> tree;
	private static final int[] TEST_TREE_CONTENT = {5, 11, 23, 205, 4, 1};
	private static final double RADIUS = 26;

	public TreeDrawerLayout() {
		widthProperty().addListener(evt -> drawTree());
		heightProperty().addListener(evt -> drawTree());
		
		createTree();
	}
	
	private void createTree() {
		tree = new RedBlackTree<Integer>();
		
		for(int e : TEST_TREE_CONTENT) {
			RBNode<Integer> node = new RBNode<Integer>(e);
			tree.insertNodeBU(node);
		}
		
		drawTree();
	}
	
	private void drawTree() {
		
		getChildren().clear();
		
		drawTreeNodes(tree.getRoot(), tree.getSentinel(), getWidth()/2, getHeight()/2);
	}
	
	private void drawTreeNodes(RBNode<Integer> node, RBNode<Integer> sentinel, double x, double y) {
		Circle circle = new Circle(x, y, RADIUS, Color.RED);
		getChildren().add(circle);
		
		if (node.getLeft() != sentinel) {
			drawTreeNodes(node.getLeft(), sentinel, x - 75, y + 75);
		}
		if (node.getRight() != sentinel) {
			drawTreeNodes(node.getRight(), sentinel, x + 75, y + 75);
		}
	}
}
