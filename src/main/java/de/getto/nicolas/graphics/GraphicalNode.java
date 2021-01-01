package de.getto.nicolas.graphics;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import de.getto.nicolas.node.NodeColor;
import de.getto.nicolas.node.RBNode;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GraphicalNode {

	private Font font =  Font.font("Cooper Black", FontWeight.BOLD, 16);
	private FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	
	public static final int RADIUS = 20;
	
	private RBNode<Integer> node;
	
	private Point2D point;
	private Color backgroundColor, borderColor, fontColor;
	
	public GraphicalNode(RBNode<Integer> node, Point2D point) {
		this.node = node;
		this.point = point;
		this.backgroundColor = node.getColor() == NodeColor.RED ? Color.RED : Color.BLACK;
		this.borderColor = Color.rgb(169, 169, 169);
		this.fontColor = Color.rgb(255, 255, 255);
	}
	
	public void draw(GraphicsContext gc) {
		gc.setLineWidth(3);
		
		gc.setFill(backgroundColor);
		gc.fillOval(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);
		
		gc.setStroke(borderColor);
		gc.strokeOval(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);
		
		//Text
		gc.setFont(font);
		gc.setFill(fontColor);
		gc.fillText(node.getKey().toString(),
				point.getX() - (getStringWidth(node.getKey().toString()) / 2),
				point.getY() + (fm.getAscent() / 4));
	}
	
	private float getStringWidth(String s) {
		float width = 0;
		
		for (char c : s.toCharArray()) {
			width += fm.getCharWidth(c);
		}
		
		return width;
	}
}
