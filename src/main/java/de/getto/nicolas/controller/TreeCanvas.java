package de.getto.nicolas.controller;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class TreeCanvas extends Canvas {
	
	private static final int[] TREE_CONTENT = {1, 5, 2, 6, 14, 255};
	private static final int TREE_HEIGHT = 7;

	private RedBlackTree<Integer> tree;
	private GraphicalNode insertNode;
	
	
	public TreeCanvas() {
		widthProperty().addListener(evt -> drawTree());
		heightProperty().addListener(evt -> drawTree());
		
		createTree();
	}
	
	public void createTree() {
		tree = new RedBlackTree<Integer>();
		
		for (int i : TREE_CONTENT) {
			RBNode<Integer> node = new RBNode<Integer>(i);
			tree.insertNodeBU(node);
		}
		
		drawTree();
	}
	
	public void drawTree() {
		double width = getWidth();
		double height = getHeight();
		
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, width, height);
		
		if (tree.getRoot() != tree.getSentinel()) {
			// Draw Lines
			// Draw Nodes
			drawNode(gc, tree.getRoot(), tree.getSentinel(), 0, getWidth(), 0, getHeight() / TREE_HEIGHT);
		}
	}
	
	public void drawNode(GraphicsContext gc, RBNode<Integer> node, RBNode<Integer> sentinel, double xMin, double xMax, double yMin, double yMax) {
		Point2D point = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);
		
		GraphicalNode drawable = new GraphicalNode(node, point);
		drawable.draw(gc);
		
		if (node.getLeft() != sentinel) {
			drawNode(gc, node.getLeft(), sentinel, xMin, (xMax + xMin) / 2, yMin + yMax, yMin);
		}
		if (node.getRight() != sentinel) {
			drawNode(gc, node.getRight(), sentinel, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
	}
	
	public void test() {
		System.out.println(tree.verifyTree());
	}
}
