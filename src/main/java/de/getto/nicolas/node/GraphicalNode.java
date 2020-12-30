package de.getto.nicolas.node;


import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class GraphicalNode {
	
	final Font font = Font.font("Cooper Black", FontWeight.BOLD, 16);
	final FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	
	private RBNode<Integer> treeNode;
	private Point2D point;
	private Color borderColor, fontColor;
	
	private final Color RED_NODE = Color.RED;
	private final Color BLACK_NODE = Color.BLACK;
	private static final int RADIUS = 26;

	public GraphicalNode(RBNode<Integer> node, Point2D point) {
		this.treeNode = node;
		this.point = point;
		this.borderColor = Color.rgb(169, 169, 169);
		this.fontColor = Color.rgb(255, 255, 255);
	}
	
	public void draw(GraphicsContext gc) {
		gc.setLineWidth(3);
		
		if (treeNode.getColor() == NodeColor.RED) {
			gc.setFill(RED_NODE);
		} else {
			gc.setFill(BLACK_NODE);
		}
		gc.fillOval(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);
		
		gc.setStroke(borderColor);
		gc.strokeOval(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);
		
		// Draw Text
		gc.setFont(font);
		gc.setFill(fontColor);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(treeNode.getKey().toString(), point.getX(), point.getY());
	}
}
