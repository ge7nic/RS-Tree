package de.getto.nicolas.controller;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

public class TreePane extends Pane {
	
	private static final int[] STARTER_TREE = {24, 12, 255, 6, 28, 106};
	private static final int RADIUS = 26;
	private static final Color NORMAL_BORDER = Color.rgb(169, 169, 169), HIGHLIGHT = Color.rgb(255, 215, 0)
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
		
		
		drawLines(tree.getRoot(), tree.getSentinel(), 0, widthProperty().get(), 0, heightProperty().get() / height);
		drawNodes(tree.getRoot(), tree.getSentinel(), 0, widthProperty().get(), 0, heightProperty().get() / height);
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
		graphicalNode.setStrokeWidth(3);
		graphicalNode.setStrokeType(StrokeType.INSIDE);
		graphicalNode.setFill(Color.BLACK);
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
	
	public void animateSearchNodeTest() {
		int val = 255;
		
		highlightNode(tree.findNode(val));
	}
	
	// TODO
	// Find a way to highlight a node, then write something and wait 
	private void highlightNode(RBNode<Integer> node) {
		Circle gNode = (Circle) lookup("#" + node.getKey());
		Circle testNode = (Circle) lookup("#" + tree.findNode(12));
		Timeline t = new Timeline();
		
		KeyValue highlight = new KeyValue(gNode.strokeProperty(), HIGHLIGHT);
		KeyValue normalize = new KeyValue(gNode.strokeProperty(), Color.DARKRED);
		
		KeyFrame highlightNode = new KeyFrame(Duration.ZERO, e -> {
			try {
				t.pause();
				System.out.println("ye");
				Thread.sleep(2500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			t.play();
		}, highlight);
		KeyFrame finishUp = new KeyFrame(Duration.millis(250), e -> {
			
		}, normalize);
		
		t.getKeyFrames().addAll(highlightNode, finishUp);
		t.setAutoReverse(false);
		t.setCycleCount(1);
		
		/* Maybe add: t.SetOnFinished(new EventHandler<Action Event>() {
				@Override
				public void handle(EventAction event) {
				
				}
			}); if needed
		*/
		
		t.play();
	}
	
	private void animateBorder(final Circle circle) {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Paint paint = circle.getStroke();
				
				if (paint == NORMAL_BORDER) {
					circle.setStroke(HIGHLIGHT);
				} else {
					circle.setStroke(NORMAL_BORDER);
				}
			}
		}));
		timeline.setCycleCount(1000);
		timeline.play();
	}
}
