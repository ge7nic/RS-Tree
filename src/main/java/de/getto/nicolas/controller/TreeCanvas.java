package de.getto.nicolas.controller;

import de.getto.nicolas.graphics.GraphicalLine;
import de.getto.nicolas.graphics.GraphicalNode;
import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;

public class TreeCanvas extends Canvas {
	
	private static final int[] TREE_CONTENT = {4, 5, 3, 6, 14, 255, 2};
	private int treeHeight;

	private RedBlackTree<Integer> tree;
	private RBNode<Integer> insertNode;
	private TextField console;
	
	
	public TreeCanvas(TextField console) {
		widthProperty().addListener(evt -> drawTree());
		heightProperty().addListener(evt -> drawTree());
		this.console = console;
		
		createTree();
	}
	
	public void createTree() {
		tree = new RedBlackTree<Integer>();
		
		for (int i : TREE_CONTENT) {
			RBNode<Integer> node = new RBNode<Integer>(i);
			tree.insertNodeBU(node);
		}
		setTreeHeight(7);
		drawTree();
	}
	
	public void drawTree() {
		double width = getWidth();
		double height = getHeight();
		
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, width, height);
		
		if (tree.getRoot() != tree.getSentinel()) {
			// Draw Lines
			drawLines(gc, tree.getRoot(), tree.getSentinel(), 0, getWidth(), 0, getHeight() / treeHeight);
			// Draw Nodes
			drawNode(gc, tree.getRoot(), tree.getSentinel(), 0, getWidth(), 0, getHeight() / treeHeight);
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
	
	public boolean insertNewNode(Integer key) {
		insertNode = new RBNode<Integer>(key);
		tree.insertNodeBU(insertNode);
		if (tree.treeHeight(tree.getRoot()) == treeHeight) {
			tree.deleteRBNode(insertNode);
			return false;
		}
		
		drawTree();
		return true;
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
	
	
	public void test() {
		System.out.println(tree.verifyTree());
		System.out.println(tree.treeHeight(tree.getRoot()));
		System.out.println(getWidth() + ":" + getHeight());
	}
	
	public void writeOnConsole(String output, String outputStyle) {
		console.setStyle(outputStyle);
		console.setText(output);
	}
	
	private void setTreeHeight(int i) {
		treeHeight = i;
	}
}
