package de.getto.nicolas.controller;

import javax.swing.SwingUtilities;

import java.awt.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import de.getto.nicolas.node.*;
import de.getto.nicolas.tree.*;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TreeDrawer extends Canvas {
	
	private RedBlackTree<Integer> tree;
	final Font font = Font.font("Cooper Black", FontWeight.BOLD, 16);
	
	public TreeDrawer() {
		createTree();
	}

	private void createTree() {
		tree = new RedBlackTree<Integer>();
		
		for (int i = 0; i < 3; i++) {
			RBNode<Integer> node = new RBNode<Integer>(i);
			tree.insertNodeBU(node);
		}
		drawTree();
	}
	
	protected void drawTree() {
		double width = getWidth();
		double height = getHeight();
		
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, width, height);
		

		drawTreeLines(gc, tree.getRoot(), tree.getSentinel(), 0, this.getWidth(), 0, this.getHeight() / 7);
		drawTreeNodes(gc, tree.getRoot(), tree.getSentinel(), 0, this.getWidth(), 0, this.getHeight() / 7);
	}
	
	private void drawTreeNodes(GraphicsContext gc, RBNode<?> treeNode, RBNode<?> sentinel, double xMin, double xMax, double yMin, double yMax) {
		Point2D point = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);
		int radius = 26;
		
		// Draw
		Circle circle = new Circle();
		gc.setLineWidth(3);
		gc.setFill(Color.rgb(49, 116, 222));
		gc.fillOval(point.getX() - radius, point.getY() - radius, 2 * radius, 2 * radius);
		gc.setStroke(Color.rgb(99, 99, 99));
		gc.strokeOval(point.getX() - radius, point.getY() - radius, 2 * radius, 2 * radius);
		
		gc.setFont(font);
		gc.fillText(treeNode.getKey().toString(), circle.getCenterX(), circle.getCenterY());
		
		if (treeNode.getLeft() != sentinel) {
			drawTreeNodes(gc, treeNode.getLeft(), sentinel, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		}
		
		if (treeNode.getRight() != sentinel) {
			drawTreeNodes(gc, treeNode.getRight(), sentinel, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
	}
	
	private void drawTreeLines(GraphicsContext gc, RBNode<?> treeNode, RBNode<?> sentinel, double xMin, double xMax, double yMin, double yMax) {
		Point2D linePoint1;
		Point2D linePoint2;
		Line newLine = new Line();
		
		if (treeNode.getLeft() != sentinel) {
			
			linePoint1 = new Point2D(((xMin + xMax) / 2), yMin + yMax / 2);
			linePoint2 = new Point2D(((xMin + (xMin + xMax) / 2) / 2), yMin + yMax + yMax / 2);
			newLine.setStartX(linePoint1.getX());
			newLine.setStartY(linePoint1.getY());
			newLine.setEndX(linePoint2.getX());
			newLine.setEndY(linePoint2.getY());
			gc.setLineWidth(4);
			gc.setStroke(Color.rgb(99, 99, 99));
			gc.strokeLine(newLine.getStartX(), newLine.getStartY(), newLine.getEndX(), newLine.getEndY());
			
			drawTreeLines(gc, treeNode.getLeft(), sentinel, xMin, (xMin + xMax) / 2, yMin+ yMax, yMax);
		}
		if (treeNode.getRight() != sentinel) {
			
			linePoint1 = new Point2D((xMin + xMax) / 2, yMin + yMax / 2);
			linePoint2 = new Point2D((xMax + (xMin + xMax) / 2) / 2, yMin + yMax + yMax / 2);
			newLine.setStartX(linePoint1.getX());
			newLine.setStartY(linePoint1.getY());
			newLine.setEndX(linePoint2.getX());
			newLine.setEndY(linePoint2.getY());
			gc.setLineWidth(4);
			gc.setStroke(Color.rgb(99, 99, 99));
			gc.strokeLine(newLine.getStartX(), newLine.getStartY(), newLine.getEndX(), newLine.getEndY());
			
			drawTreeLines(gc, treeNode.getRight(), sentinel, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
	}
}
