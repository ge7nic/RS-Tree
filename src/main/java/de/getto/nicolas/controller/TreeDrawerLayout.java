package de.getto.nicolas.controller;

import java.net.URL;
import java.util.ResourceBundle;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;

public class TreeDrawerLayout extends AnchorPane {
	
	private RedBlackTree<Integer> tree;
	private static final int[] TEST_TREE_CONTENT = {5, 4, 6, 3, 1};
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
	
	public void drawTree() {
		
		getChildren().clear();
		
		drawTreeNodes(tree.getRoot(), tree.getSentinel(), 0, this.getWidth(), 0, this.getHeight() / 7);
	}
	
	private void drawTreeNodes(RBNode<Integer> node, RBNode<Integer> sentinel, double xMin, double xMax, double yMin, double yMax) {
		// Find the Point where the node will be placed
		Point2D point = new Point2D((xMin + xMax) / 2, yMin + yMax /2);

		// Create a new Stack Pane where the node and the value will be placed in
		StackPane stack = new StackPane();
		stack.setLayoutX(point.getX());
		stack.setLayoutY(point.getY());
		
		// Testing purposes
		Background b = new Background(new BackgroundFill(Color.DEEPPINK, CornerRadii.EMPTY, Insets.EMPTY));
		stack.setBackground(b);
		
		Circle circle = new Circle(point.getX(), point.getY(), RADIUS, Color.RED);
		Text txt = new Text(node.getKey().toString());
		txt.setFont(new Font(20));

		
		stack.getChildren().addAll(circle, txt);

		getChildren().add(stack);
		if (node.getLeft() != sentinel) {
			drawTreeNodes(node.getLeft(), sentinel, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		}
		if (node.getRight() != sentinel) {
			drawTreeNodes(node.getRight(), sentinel, (xMin +xMax) / 2, xMax, yMin+ yMax, yMax);
		}
	}
}
