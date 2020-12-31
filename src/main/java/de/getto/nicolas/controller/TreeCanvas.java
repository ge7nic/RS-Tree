package de.getto.nicolas.controller;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;

public class TreeCanvas extends Canvas {
	
	private static final int[] TREE_CONTENT = {4, 5, 3, 6, 14, 255, 2};
	private static final int TREE_HEIGHT = 7;

	private RedBlackTree<Integer> tree;
	private RBNode<Integer> insertNode;
	
	
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
			drawLines(gc, tree.getRoot(), tree.getSentinel(), 0, getWidth(), 0, getHeight() / TREE_HEIGHT);
			// Draw Nodes
			drawNode(gc, tree.getRoot(), tree.getSentinel(), 0, getWidth(), 0, getHeight() / TREE_HEIGHT);
		}
	}
	
	public void drawLines(GraphicsContext gc, RBNode<Integer> node, RBNode<Integer> sentinel, double xMin, double xMax, double yMin, double yMax) {
		Point2D linePoint1;
		Point2D linePoint2;
		
		GraphicalLine newLine = new GraphicalLine();
		
		if (node.getLeft() != sentinel) {
			linePoint1 = new Point2D(((xMin + xMax) / 2), yMin + yMax / 2);
			linePoint2 = new Point2D(((xMin + (xMin + xMax) / 2) / 2), yMin + yMax + yMax / 2);
			newLine.setPoints(linePoint1, linePoint2);
			newLine.draw(gc);
			
			drawLines(gc, node.getLeft(), sentinel, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		}
		if (node.getRight() != sentinel) {
			linePoint1 = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);
			linePoint2 = new Point2D((xMax + (xMin + xMax) / 2) / 2, yMin + yMax + yMax / 2);
			newLine.setPoints(linePoint1, linePoint2);
			newLine.draw(gc);
			
			drawLines(gc, node.getRight(), sentinel, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
	}
	
	public void drawNode(GraphicsContext gc, RBNode<Integer> node, RBNode<Integer> sentinel, double xMin, double xMax, double yMin, double yMax) {
		Point2D point = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);
		
		GraphicalNode drawable = new GraphicalNode(node, point);
		drawable.draw(gc);
		
		if (node.getLeft() != sentinel) {
			drawNode(gc, node.getLeft(), sentinel, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		}
		if (node.getRight() != sentinel) {
			drawNode(gc, node.getRight(), sentinel, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
	}
	
	public void insertNewNode(Integer key) {
		insertNode = new RBNode<Integer>(key);
		tree.insertNodeBU(insertNode);
		
		drawTree();
	}
	
	public boolean removeNode(Integer key) {
		if (!tree.deleteNodeByValue(key)) {
			return false;
		}
		
		drawTree();
		return true;
	}
	
	public boolean searchNode(Integer key) {
		RBNode<Integer> foundNode = tree.findNode(key);
		
		return foundNode != tree.getSentinel();
	}
	
	public void writeOnConsole(TextField console, String output, String outputStyle) {
		console.setStyle(outputStyle);
		console.setText(output);
	}
	
	public void test() {
		System.out.println(tree.verifyTree());
	}
}
