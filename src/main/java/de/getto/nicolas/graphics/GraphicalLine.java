package de.getto.nicolas.graphics;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GraphicalLine {

	private Point2D startPoint, endPoint;
	private Color lineColor;
	
	public GraphicalLine() {
		this.lineColor = Color.rgb(99, 99, 99);
	}
	
	public GraphicalLine(Point2D startPoint, Point2D endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.lineColor = Color.rgb(99, 99, 99);
	}
	
	public void draw(GraphicsContext gc) {
		gc.setLineWidth(4);
		gc.setStroke(lineColor);
		gc.strokeLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
	}
	
	public void setPoints(Point2D point1, Point2D point2) {
		this.startPoint = point1;
		this.endPoint = point2;
	}
}
