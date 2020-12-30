package de.getto.nicolas.controller;

import de.getto.nicolas.node.GraphicalNode;
import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class TreeCanvas extends Canvas {
	
	private final static int[] TREE_CONTENT = {1, 3, 5, 120, 22, 16};
	private final static int TREE_HEIGHT = 5;
	private RedBlackTree<Integer> tree;

	public TreeCanvas() {
		widthProperty().addListener(listener -> drawTree());
		heightProperty().addListener(listener -> drawTree());
		
		initTree();
	}
	
	private void initTree() {
		tree = new RedBlackTree<Integer>();
		
		for (int i : TREE_CONTENT) {
			RBNode<Integer> node = new RBNode<Integer>(i);
			tree.insertNodeBU(node);
		}
		
		drawTree();
	}
	
	public void test() {
		System.out.println(getWidth() + ":" + getHeight());
	}
	
	public void drawTree() {
		double width = getWidth();
		double height = getHeight();
		GraphicsContext gc = getGraphicsContext2D();
		
		gc.clearRect(0, 0, width, height);
		
		if (tree.getRoot() != tree.getSentinel()) {
			// draw Lines
			// draw Nodes
			drawNode(gc, tree.getRoot(), tree.getSentinel(), 0, this.getWidth(), 0, this.getHeight() / TREE_HEIGHT);
		}
	}
	
	public void drawNode(GraphicsContext gc, RBNode<Integer> node, RBNode<Integer> sentinel, double xMin, double xMax, double yMin, double yMax) {
		Point2D point = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);
		
		GraphicalNode graphNode = new GraphicalNode(node, point);
		graphNode.draw(gc);
		
		if (node.getLeft() != sentinel) {
			drawNode(gc, node.getLeft(), sentinel, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		}
		if (node.getRight() != sentinel) {
			drawNode(gc, node.getRight(), sentinel, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
	}
}
