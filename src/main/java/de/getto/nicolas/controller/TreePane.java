package de.getto.nicolas.controller;

import java.util.Arrays;
import java.util.List;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import de.getto.nicolas.node.NodeColor;
import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

public class TreePane extends Pane {
	
	private static final int[] STARTER_TREE = {50, 25, 75, 20, 80};
	private static final int RADIUS = 26;
	private static final Color NORMAL_BORDER = Color.rgb(169, 169, 169), HIGHLIGHT = Color.GOLD
								, NORMAL_LINE = Color.rgb(90, 90, 90);

	private Font font = Font.font("Cooper Black", FontWeight.BOLD, 16);
	private FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	
	private RedBlackTree<Integer> tree;
	private int height;
	private RBNode<Integer> insertNode;
	private HBox controlPanel;
	private TextField console;
	
	private TranslateTransition tempt;
	private Circle subtreeTempCircle;
	
	private double translateXForLeftRotationProp;
	
	public TreePane(HBox controlPanel, TextField console) {
		widthProperty().addListener(evt -> drawTree());
		heightProperty().addListener(evt -> drawTree());
		
		this.controlPanel = controlPanel;
		this.console = console;
		
		translateXForLeftRotationProp = 0;
		
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
	
	public void insert(int val, double animationLength) {
		drawTree();
		
		setButtonDisableToValue(true);
		CheckBox ab = (CheckBox) controlPanel.lookup("#animButton");
		
		if (ab.isSelected()) {
			insertWithAnimation(val);
		} else {
			insertWithoutAnimation(val);
		}
	}
	
	/**
	 * Inserts a new Node. If the Height exceeds 7, it gets incremented up until 10 where a new Node that would exceed that height 
	 * will be deleted.
	 * @param val Key of the new Node.
	 */
	public void insertWithoutAnimation(int val) {
		insertNode = new RBNode<Integer>(val);
		if (tree.findNode(val) != tree.getSentinel()) {
			Alert newAlert = new Alert(Alert.AlertType.WARNING, "A Node with the value " + val + " already exists in this Tree.");
			newAlert.showAndWait();
		}
		
		tree.insertNodeBU(insertNode);
		drawTree();

		if (tree.treeHeight(tree.getRoot()) >= height - 1 && height < 10) {
			setHeight(++height);
		} else if (tree.treeHeight(tree.getRoot()) >= 10) {
			tree.deleteRBNode(insertNode);
		}
		setButtonDisableToValue(false);
	}
	
	// Test Animation for Insert
	public void insertWithAnimation(int val) {
		double xMin = 0, xMax = widthProperty().get(), yMin = 0, yMax = heightProperty().get() / height;
		insertNode = new RBNode<Integer>(val);
		RBNode<Integer> tempRoot = tree.getRoot();
		
		SequentialTransition seq = new SequentialTransition();
		
		// create a new node
		Group g = createNode(new Group(), insertNode);
		
		// find pos for new Node g
		while (tempRoot != tree.getSentinel()) {
			if (insertNode.getKey() < tempRoot.getKey()) {
				xMax = (xMin + xMax) / 2;
				yMin = yMin + yMax;
				
				tempRoot = tempRoot.getLeft();
			} else {
				xMin = (xMin + xMax) / 2;
				yMin = yMin + yMax;
				
				tempRoot = tempRoot.getRight();
			}
		}
		double gLayoutX = ((xMin + xMax) / 2);
		double gLayoutY = yMin + yMax / 2;
		g.setLayoutX(gLayoutX);
		g.setLayoutY(gLayoutY);
		
		// insert the node without fixing it so we can animate the fixing part
		tree.insertNodeBU(insertNode, false);
		
		getChildren().add(g);
		
		
		seq.getChildren().add(animateFixup(insertNode));
		seq.play();
		
		seq.setOnFinished(e -> {
			System.out.println("Done!");
			drawTree();
			setButtonDisableToValue(false);
		});
	}
	
	// animates the fixup || Both Case 2 have an animation error, because two rotations want to be
	// displayed in one method call - This leads to the second rotatation animation not being displayed
	
	// To fix it, we have to remember xMin, xMax, yMin and yMax of the nodes (maybe) 
	public SequentialTransition animateFixup(RBNode<Integer> node) {
		final double animationLength = 3;
		
		SequentialTransition seq = new SequentialTransition();
		
		ParallelTransition par;
		FadeTransition fade;
		PauseTransition pause;
		FillTransition fill;
		Circle c;
		
		while (node != tree.getRoot() && node.getParent().getColor() == NodeColor.RED) {
			par = new ParallelTransition();
			if (node.getParent() == node.getParent().getParent().getLeft()) {
				RBNode<Integer> rightUncle = node.getParent().getParent().getRight();
				if (rightUncle.getColor() == NodeColor.RED) {
					// Case 1
					final int val = node.getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("Since both " + val + " parent and uncle are red, we swap color's with their parent.");
					});
					pause = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(fade, pause);
					
					node.getParent().setColor(NodeColor.BLACK);
					c = (Circle)lookup("#" + node.getParent().getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fill);
					
					rightUncle.setColor(NodeColor.BLACK);
					c = (Circle)lookup("#" + rightUncle.getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fill);
					
					node.getParent().getParent().setColor(NodeColor.RED);
					c = (Circle)lookup("#" + node.getParent().getParent().getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
					par.getChildren().add(fill);
					
					node = node.getParent().getParent();
					seq.getChildren().add(par);
				} else {
					if (node == node.getParent().getRight()) {
						// Case 2
						final int val = node.getKey();
						fade = new FadeTransition(Duration.millis(10), console);
						fade.setOnFinished(e -> {
							console.setText(val + " is the right child, so we rotate left pivoting its parent.");
						});
						pause = new PauseTransition(Duration.seconds(animationLength));
						
						node = node.getParent();
						seq.getChildren().addAll(fade, pause,
								startAnimateRotation(node.getKey(), RotationDirection.LEFT));
						tree.rotateLeft(node);
						pause = new PauseTransition(Duration.seconds(animationLength));
						seq.getChildren().add(pause);
					}
					// Case 3
					final int val = node.getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("Since only " + val + " parent is Red, we just swap colors with its parent.");
					});
					pause = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(fade, pause);
					
					node.getParent().setColor(NodeColor.BLACK);
					c = (Circle)lookup("#" + node.getParent().getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fill);
					
					node.getParent().getParent().setColor(NodeColor.RED);
					c = (Circle)lookup("#" + node.getParent().getParent().getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
					par.getChildren().add(fill);
					
					final int ppVal = node.getParent().getParent().getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("And now we just rotate right pivoting " + ppVal + ".");
					});
					pause = new PauseTransition(Duration.seconds(animationLength));
					
					seq.getChildren().addAll(par, fade, pause, 
							startAnimateRotation(node.getParent().getParent().getKey(), RotationDirection.RIGHT));
					tree.rotateRight(node.getParent().getParent());
				}
			} else {
				RBNode<Integer> leftUncle = node.getParent().getParent().getLeft();
				if (leftUncle.getColor() == NodeColor.RED) {
					// Case 1
					final int val = node.getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("Since both " + val + "'s parent and uncle are red, we swap color's with their parent.");
					});
					pause = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(fade, pause);
					
					node.getParent().setColor(NodeColor.BLACK);
					c = (Circle)lookup("#" + node.getParent().getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fill);
					
					leftUncle.setColor(NodeColor.BLACK);
					c = (Circle)lookup("#" + leftUncle.getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fill);
					
					node.getParent().getParent().setColor(NodeColor.RED);
					c = (Circle)lookup("#" + node.getParent().getParent().getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
					par.getChildren().add(fill);
					
					node = node.getParent().getParent();
					seq.getChildren().add(par);
				} else {
					if (node == node.getParent().getLeft()) {
						// Case 2
						final int val = node.getKey();
						fade = new FadeTransition(Duration.millis(10), console);
						fade.setOnFinished(e -> {
							console.setText(val + " is the left child, so we rotate right pivoting its parent.");
						});
						
						node = node.getParent();
						pause = new PauseTransition(Duration.seconds(animationLength));
						seq.getChildren().addAll(fade, pause,
								startAnimateRotation(node.getKey(), RotationDirection.RIGHT));
						pause = new PauseTransition(Duration.seconds(animationLength));
						seq.getChildren().add(pause);
						tree.rotateRight(node);
					}
					// Case 3
					final int val = node.getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("Since only " + val + "'s parent is Red, we just swap colors with its parent.");
					});
					pause = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(fade, pause);
					
					node.getParent().setColor(NodeColor.BLACK);
					c = (Circle)lookup("#" + node.getParent().getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fill);
					
					node.getParent().getParent().setColor(NodeColor.RED);
					c = (Circle)lookup("#" + node.getParent().getParent().getKey());
					fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
					par.getChildren().add(fill);
					
					final int ppVal = node.getParent().getParent().getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("And now we just rotate left pivoting " + ppVal + ".");
					});
					pause = new PauseTransition(Duration.seconds(animationLength));
					
					seq.getChildren().addAll(par, fade, pause, 
							startAnimateRotation(node.getParent().getParent().getKey(), RotationDirection.LEFT));
					tree.rotateLeft(node.getParent().getParent());
					pause = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().add(pause);
				}
			}
		}
		
		fade = new FadeTransition(Duration.millis(10), console);
		fade.setOnFinished(e -> {
			console.setText("To finish, we just repaint the root black and paint the edges.");
		});
		pause = new PauseTransition(Duration.seconds(0.5));
		
		c = (Circle)lookup("#" + tree.getRoot().getKey());
		fill = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
		seq.getChildren().addAll(fade, pause, fill);
		tree.getRoot().setColor(NodeColor.BLACK);
		
		return seq;
	}
	
	public ParallelTransition startAnimateRotation(int val, RotationDirection dir) {
		RBNode<Integer> temp = tree.getRoot();
		RBNode<Integer> node = tree.findNode(val);
		ParallelTransition par = new ParallelTransition();
		
		double xMin = 0, xMax = getWidth(), yMin = 0, yMax = getHeight() / height;
		while(temp != tree.getSentinel()) {
			if (temp.getKey() == val) {
				// found
				if (dir == RotationDirection.LEFT) {
					par = animateRotateLeft(node, dir, xMin, xMax, yMin, yMax);
					break;
				} else {
					par = animateRotateRight(node, dir, xMin, xMax, yMin, yMax);
					break;
				}
			} else if (temp.getKey() > val) {
				// go left
				xMax = (xMin + xMax) / 2;
				yMin = yMin + yMax;
				temp = temp.getLeft();
			} else {
				// go right
				xMin = (xMin + xMax) / 2;
				yMin = yMin + yMax;
				temp = temp.getRight();
			}
		}
		// error
		return par;
	}
	
	/**
	 * Method that animates a rotation to the left around a node.
	 * This Method should only be called if the right child of node is NOT null, otherwise it will crash.
	 * As of right now, the edges are NOT animated.
	 * 
	 * This method works symmetrically to animateRotateRight.
	 * @param node The node to rotate around
	 * @param dir  Rotation direction
	 * @param xMin xMin position of node
	 * @param xMax xMax position of node
	 * @param yMin yMin position of node
	 * @param yMax yMax position of node
	 */
	public ParallelTransition animateRotateLeft(RBNode<Integer> node, RotationDirection dir, double xMin, double xMax, double yMin, double yMax) {
		// Insert a Method to delete all edges in this subtree first. Maybe this looks better?
		Circle nodeX = (Circle)lookup("#" + node.getKey());
		Circle rightChild = (Circle)lookup("#" + node.getRight().getKey());
		
		TranslateTransition t1 = new TranslateTransition(Duration.seconds(1), nodeX.getParent());
		TranslateTransition t2 = new TranslateTransition(Duration.seconds(1), rightChild.getParent());
		ParallelTransition par = new ParallelTransition();
		FadeTransition fd = new FadeTransition(Duration.millis(10), console);
		
		//pos for right Child
		double layoutChildX = ((xMin + xMax) / 2);
		double layoutChildY = yMin + yMax / 2;
		
		//go left so we can move nodeX there
		xMax = (xMin + xMax) / 2;
		yMin = yMin + yMax;
		double layoutLeftX = ((xMin + xMax) / 2);
		double layoutLeftY = yMin + yMax / 2;
		
		t1.setToX(-Math.abs(nodeX.getParent().getLayoutX() - layoutLeftX));
		t1.setToY(Math.abs(nodeX.getParent().getLayoutY() - layoutLeftY));
		t2.setToX(-Math.abs(rightChild.getParent().getLayoutX() - layoutChildX));
		t2.setToY(-Math.abs(rightChild.getParent().getLayoutY() - layoutChildY));
		
		// Helper-Text for the first step
		final int val = node.getKey();
		fd.setOnFinished(e -> {
			console.setText("We rotate " + val + " and it's right Child to the left.");
		});
		
		par.getChildren().addAll(fd, t1, t2);

		// fix subtree alpha
		// to fix alpha, we just move it down one level
		if (node.getLeft() != tree.getSentinel()) {
			par = moveSubtreeDown(node.getLeft(), par, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		}
		// fix subtree beta
		// to fix beta we move it to be the new right child of x and move it to that position
		if (node.getRight().getLeft() != tree.getSentinel()) {
			// Even though node.getRight().getLeft() is the node we are moving, we want the ending position to be
			// at node.getLeft().getRight()
			par = moveSubtreeUp(node.getRight().getLeft(), par, dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
		// fix subtree gamma
		// to fix gamma all we have to do is move it up one level and set it as a child of the original node
		// for that we have to move up one level again though
		if (node.getRight().getRight() != tree.getSentinel()) {
			// go up one level again
			xMax = 2 * xMax - xMin;
			yMin = yMin - yMax;
			par = moveSubtreeUp(node.getRight().getRight(), par, dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);

		}
		
		return par;
	}

	/**
	 * Method that animates a rotation to the right around a node.
	 * This Method should only be called if the left child of node is NOT null, otherwise it will crash.
	 * As of right now, the edges are NOT animated.
	 * 
	 * This Method works symmetrically to animateRotateLeft.
	 * @param node The node to rotate around
	 * @param dir  Rotation direction
	 * @param xMin xMin position of node
	 * @param xMax xMax position of node
	 * @param yMin yMin position of node
	 * @param yMax yMax position of node
	 */
	public ParallelTransition animateRotateRight(RBNode<Integer> node, RotationDirection dir, double xMin, double xMax, double yMin, double yMax) {
		Circle nodeX = (Circle)lookup("#" + node.getKey());
		Circle leftChild = (Circle)lookup("#" + node.getLeft().getKey());

		TranslateTransition t1 = new TranslateTransition(Duration.seconds(1), nodeX.getParent());
		TranslateTransition t2 = new TranslateTransition(Duration.seconds(1), leftChild.getParent());
		ParallelTransition par = new ParallelTransition();

		//go right so we can move nodeX there
		xMin = (xMin + xMax) / 2;
		yMin = yMax + yMin;
		double layoutRightX = ((xMin + xMax) / 2);
		double layoutRightY = yMin + yMax / 2;
		
		t1.setToX(Math.abs(nodeX.getParent().getLayoutX() - layoutRightX));
		t1.setToY(Math.abs(nodeX.getParent().getLayoutY() - layoutRightY));
		t2.setToX(Math.abs(leftChild.getParent().getLayoutX() - nodeX.getParent().getLayoutX()));
		t2.setToY(-Math.abs(leftChild.getParent().getLayoutY() - nodeX.getParent().getLayoutY()));
		
			
		par.getChildren().addAll(t1, t2);
		
		// fix subtree gamma
		// to fix gamma all we have to do is move it down one level
		if (node.getRight() != tree.getSentinel()) {			
			par = moveSubtreeDown(node.getRight(), par, dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);

		}
		// fix subtree beta
		// this also works symmetrically to left rotation
		if (node.getLeft().getRight() != tree.getSentinel()) {

			// Even though node.getLeft().getRight() is the node we are moving, we want the ending position to be
			// at node.getRight().getLeft()
			par = moveSubtreeUp(node.getLeft().getRight(), par, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		}
		// fix subtree alpha
		// to fix alpha we do it symetrically to fix gamma in a left rotation
		if (node.getLeft().getLeft() != tree.getSentinel()) {		
			// go up one level again
			xMin = 2 * xMin - xMax;
			yMin = yMin - yMax;
			par = moveSubtreeUp(node.getLeft().getLeft(), par, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
		}

		return par;
	}
	
	/**
	 * Move a Subtree, which is defined by x as the root, downwards. This is implemented as an Inorder-Tree-Walk.
	 * @param x Root of Subtree.
	 * @param par The ParallelTransition that ultimately animates the Tree downwards
	 * @param dir Rotation Direction, dictates if the correct TranslateTransision is applied
	 * @param xMin xMin of x
	 * @param xMax xMax of x
	 * @param yMin yMin of x
	 * @param yMax yMax of x
	 * @return The done animation of Subtree
	 */
	private ParallelTransition moveSubtreeDown(RBNode<Integer> x, ParallelTransition par, RotationDirection dir,
			double xMin, double xMax, double yMin, double yMax) {
		if (x != tree.getSentinel()) {			
			//work
			subtreeTempCircle = (Circle)lookup("#" + x.getKey());
			tempt = new TranslateTransition(Duration.seconds(1), subtreeTempCircle.getParent());
			if (dir == RotationDirection.LEFT) {
				tempt.setByX(-Math.abs(subtreeTempCircle.getParent().getLayoutX() - ((xMin + xMax) / 2)));
			} else {
				tempt.setByX(Math.abs(subtreeTempCircle.getParent().getLayoutX() - ((xMin + xMax) / 2)));
			}
			tempt.setByY(Math.abs(subtreeTempCircle.getParent().getLayoutY() - (yMin + yMax / 2)));
			par.getChildren().add(tempt);
			
			moveSubtreeDown(x.getLeft(), par, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
			moveSubtreeDown(x.getRight(), par, dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
		return par;
	}
	
	/**
	 * Move a Subtree, which is defined by x as the root, upwards. This is implemented as an Inorder-Tree-Walk.
	 * @param x Root of Subtree.
	 * @param par The ParallelTransition that ultimately animates the Tree upwards
	 * @param dir Rotation Direction, dictates if the correct TranslateTransision is applied
	 * @param xMin xMin of x
	 * @param xMax xMax of x
	 * @param yMin yMin of x
	 * @param yMax yMax of x
	 * @return The done animation of Subtree
	 */
	private ParallelTransition moveSubtreeUp(RBNode<Integer> x, ParallelTransition par, RotationDirection dir,
			double xMin, double xMax, double yMin, double yMax) {
		if (x != tree.getSentinel()) {
			// work
			subtreeTempCircle = (Circle)lookup("#" + x.getKey());
			tempt = new TranslateTransition(Duration.seconds(1), subtreeTempCircle.getParent());
			if (dir == RotationDirection.LEFT) {
				tempt.setByX(-Math.abs(subtreeTempCircle.getParent().getLayoutX() - ((xMin + xMax) / 2)));
			} else {
				tempt.setByX(Math.abs(subtreeTempCircle.getParent().getLayoutX() - ((xMin + xMax) / 2)));
			}
			tempt.setByY(-Math.abs(subtreeTempCircle.getParent().getLayoutY() - (yMin + yMax / 2)));
			par.getChildren().add(tempt);
			
			moveSubtreeUp(x.getLeft(), par, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
			moveSubtreeUp(x.getRight(), par, dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
		}
		return par;
	}
	
	public boolean delete(int val) {
		if (!tree.deleteNodeByValue(val)) {
			return false;
		}
		drawTree();
		return true;
	}
	
	private void setHeight(int height) {
		this.height = height;
	}
	
	private float getStringWidth(String s) {
		float width = 0;
		
		for (char c : s.toCharArray()) {
			width += fm.getCharWidth(c);
		}
		
		return width;
	}
	
	public void search(final int val, double animationLength) {
		drawTree();
		
		setButtonDisableToValue(true);
		CheckBox ab = (CheckBox) controlPanel.lookup("#animButton");
		
		if (ab.isSelected()) {
			searchWithAnimation(val, animationLength);
		} else {
			searchWithoutAnimation(val);
		}
	}
	
	/**
	 * Searches for a Node without Animation.
	 * @param val
	 */
	public void searchWithoutAnimation(int val) {
		RBNode<Integer> node = tree.findNode(val);
		if (node != tree.getSentinel()) {
			// found node
			Circle c = (Circle)lookup("#" + val);
			c.setStroke(HIGHLIGHT);
			console.setText("Found node with value " + val + ".");
		} else {
			// found nothing
			console.setText("No such node with value " + val + ".");
		}
		setButtonDisableToValue(false);
	}
		
	/**
	 * Animates the Algorithm to find a Node in a RedBlack-Tree.
	 * @param console The TextField giving information to the User.
	 * @param val The Value the Users wants to find.
	 * @param animationLength How long the PauseTransitions take.
	 */
	private void searchWithAnimation(final int val, double animationLength) {
		Circle c;
		SequentialTransition seq = new SequentialTransition();
		PauseTransition p;
		StrokeTransition st;
		FadeTransition fd;
		
		final double strokeTimeInMillis = 10;
		
		RBNode<Integer> node = tree.getRoot();
		RBNode<Integer>	sentinel = tree.getSentinel();
		
		while (node != sentinel) {
			c = (Circle)lookup("#" + node.getKey());
			
			st = new StrokeTransition(Duration.millis(strokeTimeInMillis), c, NORMAL_BORDER, HIGHLIGHT);
			seq.getChildren().add(st);
			
			fd = new FadeTransition(Duration.millis(strokeTimeInMillis), console);
			final int nodeVal = node.getKey();
			
			fd = new FadeTransition(Duration.millis(strokeTimeInMillis), console);
			p = new PauseTransition(Duration.seconds(animationLength));
			if (node.getKey() == val) {
				fd.setOnFinished(e -> {
					console.setText("Found node with value " + val +"!");
				});
				seq.getChildren().add(fd);
				break;
			} else if (node.getKey() > val) {
				fd.setOnFinished(e -> {
					console.setText("Key " + nodeVal + " of this Node is bigger than " + val + " -> Go Left.");
				});
				node = node.getLeft();
			} else {
				fd.setOnFinished(e -> {
					console.setText("Key " + nodeVal + " of this Node is smaller than " + val + " -> Go Right.");
				});
				node = node.getRight();
			}
			seq.getChildren().addAll(fd, p);
		}
		
		if (node == sentinel) {
			fd = new FadeTransition(Duration.millis(strokeTimeInMillis), console);
			fd.setOnFinished(e -> {
				console.setText("No such node with value " + val + ".");
			});
			seq.getChildren().add(fd);
		}
	
		seq.play();
		
		seq.setOnFinished(e -> {
			setButtonDisableToValue(false);
		});
	}
	
	/**
	 * Disables or Enables all Control Buttons. 
	 * @param val
	 */
	private void setButtonDisableToValue(boolean val) {
		for (Node c : controlPanel.getChildren()) {
			c.setDisable(val);
		}
	}
	
	public void test() {
		ParallelTransition p = startAnimateRotation(75, RotationDirection.LEFT);
		p.play();
	}
}
