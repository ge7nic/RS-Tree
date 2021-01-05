package de.getto.nicolas.controller;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import de.getto.nicolas.node.NodeColor;
import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

public class TreePane extends Pane {
	
	private static final int[] STARTER_TREE = {7, 2, 11, 1, 5, 8, 14, 4, 15};
	private static final int RADIUS = 26;
	private static final Color NORMAL_BORDER = Color.rgb(169, 169, 169), HIGHLIGHT = Color.GOLD
								, NORMAL_LINE = Color.rgb(90, 90, 90);

	private Font font = Font.font("Cooper Black", FontWeight.BOLD, 16);
	private FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	
	private RedBlackTree<Integer> tree;
	private int height;
	private RBNode<Integer> insertNode;
	
	public TreePane() {
		widthProperty().addListener(evt -> drawTree());
		heightProperty().addListener(evt -> drawTree());
		
		createTree();
	}
	
	private void createTree() {
		tree = new RedBlackTree<Integer>();
		setHeight(7);
		
		for (int i : STARTER_TREE) {
			RBNode<Integer> node = new RBNode<Integer>(i);
			tree.insertNodeBU(node);
		}
		drawTree();
	}
	
	public void drawTree() {
		getChildren().clear();
		
		if (tree.getRoot() != tree.getSentinel()) {
			drawLines(tree.getRoot(), tree.getSentinel(), 0, widthProperty().get(), 0, heightProperty().get() / height);
			drawNodes(tree.getRoot(), tree.getSentinel(), 0, widthProperty().get(), 0, heightProperty().get() / height);
		}
	}
	
	private void drawNodes(RBNode<Integer> node, RBNode<Integer> sentinel, double xMin, double xMax, double yMin, double yMax) {
		Group group = createNode(new Group(), node);
		
		group.setLayoutX(((xMin + xMax) / 2));
		group.setLayoutY(yMin + yMax / 2);
		
		getChildren().add(group);
		if (node.getLeft() != sentinel) {
			drawNodes(node.getLeft(), sentinel, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		} 
		if (node.getRight() != sentinel) {
			drawNodes(node.getRight(), sentinel, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
	}
	
	private void drawLines(RBNode<Integer> node, RBNode<Integer> sentinel, double xMin, double xMax, double yMin, double yMax) {
		Line edge;
		
		if (node.getLeft() != sentinel) {
			edge = new Line();
			edge.setStrokeWidth(4);
			edge.setStroke(NORMAL_LINE);
			edge.setStartX(((xMin + xMax) / 2));
			edge.setStartY(yMin + yMax / 2);
			edge.setEndX(((xMin + (xMin + xMax) / 2) / 2));
			edge.setEndY(yMin + yMax + yMax / 2);
			edge.setId("edgeTo" + node.getLeft().getKey().toString());
			
			getChildren().add(edge);
			
			drawLines(node.getLeft(), sentinel, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		}
		if (node.getRight() != sentinel) {
			edge = new Line();
			edge.setStrokeWidth(4);
			edge.setStroke(NORMAL_LINE);
			edge.setStartX((xMin + xMax) / 2);
			edge.setStartY(yMin+ yMax / 2);
			edge.setEndX((xMax + (xMin + xMax) / 2) / 2);
			edge.setEndY(yMin + yMax + yMax / 2);
			edge.setId("edgeTo" + node.getRight().getKey().toString());
			
			getChildren().add(edge);
			
			drawLines(node.getRight(), sentinel, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
	}
	
	private Group createNode(Group group, RBNode<Integer> node) {
		final Circle graphicalNode = new Circle(RADIUS);
		graphicalNode.setStroke(NORMAL_BORDER);
		graphicalNode.setStrokeWidth(5);
		graphicalNode.setStrokeType(StrokeType.INSIDE);
		graphicalNode.setFill(node.getColor() == NodeColor.BLACK ? Color.BLACK : Color.RED);
		graphicalNode.setId(node.getKey().toString());
		
		group.getChildren().add(graphicalNode);
		return createText(group, node);
	}
	
	private Group createText(Group group, RBNode<Integer> node) {
		final Text text = new Text(node.getKey().toString());
		text.setFont(font);
		text.setFill(Color.WHITE);
		text.setBoundsType(TextBoundsType.VISUAL);
		text.setLayoutX(text.getLayoutX() - getStringWidth(node.getKey().toString()) / 2);
		text.setLayoutY(fm.getAscent() / 4);
		
		group.getChildren().add(text);
		return group;
	}
	
	/**
	 * Inserts a new Node. If the Height exceeds 7, it gets incremented up until 10 where a new Node that would exceed that height 
	 * will be deleted.
	 * @param val Key of the new Node.
	 */
	public void insert(int val) {
		insertNode = new RBNode<Integer>(val);
		tree.insertNodeBU(insertNode);
		drawTree();

		if (tree.treeHeight(tree.getRoot()) >= height - 1 && height < 10) {
			setHeight(++height);
		} else if (tree.treeHeight(tree.getRoot()) >= 10) {
			tree.deleteRBNode(insertNode);
		}
	}
	
	public boolean delete(int val) {
		if (!tree.deleteNodeByValue(val)) {
			return false;
		}
		drawTree();
		return true;
	}
	
	public void search(int val) {
		RBNode<Integer> node = tree.findNode(val);
		if (node != tree.getSentinel()) {
			// found node
			System.out.println(node);
		} else {
			// found nothing
			System.out.println("Found nothing.");
		}
	}
	
	private void setHeight(int height) {
		this.height = height;
	}
	
	public void test() {
		System.out.println(getWidth() + ":" + getHeight());
	}
	
	private float getStringWidth(String s) {
		float width = 0;
		
		for (char c : s.toCharArray()) {
			width += fm.getCharWidth(c);
		}
		
		return width;
	}
	
	public void animateSearchNodeTest(TextField console) {
		int val = 4;
		
		findNodeAnimation(console, val, 3);
	}
	
	/**
	 * Animates the Algorithm to find a Node in a RedBlack-Tree.
	 * TODO: If the users searches for a node that doesn't exist, there is no error message yet.
	 * @param console The TextField giving information to the User.
	 * @param val The Value the Users wants to find.
	 * @param animationLength How long the PauseTransitions take.
	 */
	public void findNodeAnimation(TextField console, int val, double animationLength) {
		Circle c;
		SequentialTransition seq = new SequentialTransition();
		PauseTransition p;
		StrokeTransition st;
		FadeTransition fd;
		
		RBNode<Integer> node = tree.getRoot();
		RBNode<Integer>	sentinel = tree.getSentinel();
		
		while (node != sentinel) {
			c = (Circle)lookup("#" + node.getKey());
			
			st = new StrokeTransition(Duration.seconds(0.1), c, NORMAL_BORDER, HIGHLIGHT);
			seq.getChildren().add(st);
			
			fd = new FadeTransition(Duration.seconds(0.1), console);
			final int nodeVal = node.getKey();
			
			fd = new FadeTransition(Duration.millis(10), console);
			p = new PauseTransition(Duration.seconds(animationLength));
			if (node.getKey() == val) {
				fd.setOnFinished(e -> {
					console.setText("Found node with value " + val +"!");
				});
				seq.getChildren().addAll(fd, p);
				break;
			} else if (node.getKey() > val) {
				fd.setOnFinished(e -> {
					console.setText("Key " + nodeVal + " of this Node is bigger than the value we're searching for -> Go Left.");
				});
				node = node.getLeft();
			} else {
				fd.setOnFinished(e -> {
					console.setText("Key " + nodeVal + " of this Node is smaller than the value we're searching for -> Go Right.");
				});
				node = node.getRight();
			}
			seq.getChildren().addAll(fd, p);
		}
		seq.play();
	}
	
	// TODO
	// Use SequentialTransition in Combination with TranslateTransition to animate a FindNode Operation!
	private void highlightNode(RBNode<Integer> node, RBNode<Integer> sentinel, int val) {
		double xMin = 0;
		double xMax = widthProperty().get();
		double yMin = 0;
		double yMax = heightProperty().get() / height;
		
		final Circle c = createHighlightCircle();
		Circle gNode = (Circle)lookup("#" + val);
		getChildren().add(c);
		
		TranslateTransition tt = new TranslateTransition(Duration.seconds(1), c);
		
		// Translates Circle c not TO the Coordinate X and Y but add's those to the Coordinates. Thanks JavaFX for that naming.
		tt.setFromX(c.getTranslateX());
		tt.setFromY(c.getTranslateY());
		
		// go right
		xMin = (xMin + xMax) / 2;
		yMin = yMin + yMax;
		
		double xDest = (xMin + xMax) / 2;
		double yDest = yMin + yMax / 2;
		
		double xTranslate = xDest - c.getCenterX();
		double yTranslate = yDest - c.getCenterY();
		
		tt.setByX(xTranslate);
		tt.setByY(yTranslate);
		
		tt.setOnFinished(e -> {
			System.out.println("done!");
		});
		
		tt.play();
	}
	
	private Circle createHighlightCircle() {
		Circle hCircle = new Circle(getWidth() / 2, (getHeight() / height) / 2, RADIUS);
		hCircle.setStroke(HIGHLIGHT);
		hCircle.setStrokeWidth(7);
		hCircle.setFill(Color.TRANSPARENT);
		
		return hCircle;
	}
}
