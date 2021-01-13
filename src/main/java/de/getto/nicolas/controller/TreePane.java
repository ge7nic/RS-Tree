package de.getto.nicolas.controller;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import de.getto.nicolas.node.NodeColor;
import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

import javafx.animation.*;
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
	
	private static final int[] STARTER_TREE = {6, 3, 11, 2, 5, 8, 14, 4, 20, 17, 0, 1, 7, 9};
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
	
	public TreePane(HBox controlPanel, TextField console) {
		widthProperty().addListener(evt -> drawTree());
		heightProperty().addListener(evt -> drawTree());
		
		this.controlPanel = controlPanel;
		this.console = console;
		
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
	}
	
	public void insertWithAnimation(int val) {
		insertNode = new RBNode<Integer>(val);
		RBNode<Integer> place = tree.getSentinel();
		RBNode<Integer> temp = tree.getRoot();

		Group newNode = createNode(new Group(), insertNode);

		newNode.setLayoutX(40);
		newNode.setLayoutY((heightProperty().get() / height) / 2);
		
		getChildren().add(newNode);
		
		SequentialTransition seq = new SequentialTransition();
		FadeTransition ft;
		PauseTransition pt;
		StrokeTransition st;
		
		// find coordinates for new Node
		double xMin = 0, yMin = 0, xMax = widthProperty().get(), yMax = heightProperty().get() / height;
		
		while (temp != tree.getSentinel()) {
			place = temp;
			final int nodeVal = temp.getKey();
			
			ft = new FadeTransition(Duration.millis(10), console);
			st = new StrokeTransition(Duration.millis(10), (Circle)lookup("#" + nodeVal), NORMAL_BORDER, HIGHLIGHT);
			
			if (val < temp.getKey()) {
				xMax = (xMin + xMax) / 2;
				yMin = yMin + yMax;
				temp = temp.getLeft();
				
				ft.setOnFinished(e -> {
					console.setText("Key " + nodeVal + " of this Node is bigger than " + val + " -> Go Left.");
				});
				pt = new PauseTransition(Duration.seconds(1));
			} else {
				xMin = (xMin + xMax) / 2;
				yMin = yMin + yMax;
				temp = temp.getRight();
	
				ft.setOnFinished(e -> {
					console.setText("Key " + nodeVal + " of this Node is smaller or equal than " + val + " -> Go Right.");
				});
				pt = new PauseTransition(Duration.seconds(1));
			}
			seq.getChildren().addAll(ft, st, pt);
		}
		
		double newX = ((xMin + xMax) / 2) - 40;
		double newY = yMin + yMax / 2 - ((heightProperty().get() / height) / 2);
		// move to coordinates
		TranslateTransition tt = new TranslateTransition(Duration.seconds(1.5), newNode);
		tt.setToX(newX);
		tt.setToY(newY);
		
		seq.getChildren().add(tt);
		// clean up the tree
		
		
		// finish and draw the tree
		seq.play();
		seq.setOnFinished(e -> {
			tree.insertNodeBU(insertNode);
			drawTree();
		});
	}
	
	public void startAnimateRotation(int val, RotationDirection dir) {
		RBNode<Integer> temp = tree.getRoot();
		RBNode<Integer> node = tree.findNode(val);
		double xMin = 0, xMax = getWidth(), yMin = 0, yMax = getHeight() / height;
		
		while(temp != tree.getSentinel()) {
			if (temp.getKey() == val) {
				// found
				if (dir == RotationDirection.LEFT) {
					animateRotateLeft(node, dir, xMin, xMax, yMin, yMax);
				} else {
					animateRotateRight(node, dir, xMin, xMax, yMin, yMax);
				}
				break;
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
	public void animateRotateLeft(RBNode<Integer> node, RotationDirection dir, double xMin, double xMax, double yMin, double yMax) {
		// Insert a Method to delete all edges in this subtree first. Maybe this looks better?
		Circle nodeX = (Circle)lookup("#" + node.getKey());
		Circle rightChild = (Circle)lookup("#" + node.getRight().getKey());
		
		TranslateTransition t1 = new TranslateTransition(Duration.seconds(1), nodeX.getParent());
		TranslateTransition t2 = new TranslateTransition(Duration.seconds(1), rightChild.getParent());
		SequentialTransition seq = new SequentialTransition();
		ParallelTransition par = new ParallelTransition();
		PauseTransition pt = new PauseTransition(Duration.seconds(1));
		FadeTransition fd = new FadeTransition(Duration.millis(10), console);
		
		//go left so we can move nodeX there
		xMax = (xMin + xMax) / 2;
		yMin = yMin + yMax;
		double layoutLeftX = ((xMin + xMax) / 2);
		double layoutLeftY = yMin + yMax / 2;
		
		t1.setByX(-Math.abs(nodeX.getParent().getLayoutX() - layoutLeftX));
		t1.setByY(Math.abs(nodeX.getParent().getLayoutY() - layoutLeftY));
		t2.setByX(-Math.abs(rightChild.getParent().getLayoutX() - nodeX.getParent().getLayoutX()));
		t2.setByY(-Math.abs(rightChild.getParent().getLayoutY() - nodeX.getParent().getLayoutY()));
		
		// Helper-Text for the first step
		fd.setOnFinished(e -> {
			console.setText("We rotate the main node and it's right Child to the left.");
		});
		
		par.getChildren().addAll(fd, t1, t2);
		seq.getChildren().add(par);
		
		ParallelTransition par2;
		// fix subtree alpha
		// to fix alpha, we just move it down one level
		if (node.getLeft() != tree.getSentinel()) {
			// Helper-Text
			pt = new PauseTransition(Duration.seconds(1));
			fd = new FadeTransition(Duration.millis(10), console);
			fd.setOnFinished(e -> {
				console.setText("Since Subbtree alpha is not empty, we fix it.");
			});
			
			par2 = new ParallelTransition();
			par2 = moveSubtreeDown(node.getLeft(), par2, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
			
			par2.getChildren().add(fd);
			seq.getChildren().addAll(pt, par2);
		}
		// fix subtree beta
		// to fix beta we move it to be the new right child of x and move it to that position
		if (node.getRight().getLeft() != tree.getSentinel()) {
			// Helper-Text
			pt = new PauseTransition(Duration.seconds(1));
			fd = new FadeTransition(Duration.millis(10), console);
			fd.setOnFinished(e -> {
				console.setText("Since Subbtree beta is not empty, we fix it.");
			});
			
			par2 = new ParallelTransition();
			// Even though node.getRight().getLeft() is the node we are moving, we want the ending position to be
			// at node.getLeft().getRight()
			par2 = moveSubtreeUp(node.getRight().getLeft(), par2, dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
			
			par2.getChildren().add(fd);
			seq.getChildren().addAll(pt, par2);
		}
		// fix subtree gamma
		// to fix gamma all we have to do is move it up one level and set it as a child of the original node
		// for that we have to move up one level again though
		if (node.getRight().getRight() != tree.getSentinel()) {
			// Helper-Text
			pt = new PauseTransition(Duration.seconds(1));
			fd = new FadeTransition(Duration.millis(10), console);
			fd.setOnFinished(e -> {
				console.setText("Since Subbtree Gamma is not empty, we fix it.");
			});
						
			par2 = new ParallelTransition();
			// go up one level again
			xMax = 2 * xMax - xMin;
			yMin = yMin - yMax;
			par2 = moveSubtreeUp(node.getRight().getRight(), par2, dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
			
			par2.getChildren().add(fd);
			seq.getChildren().addAll(pt, par2);
		}
		seq.play();
		seq.setOnFinished(e -> {
			tree.rotateLeft(node);
			drawTree();
		});
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
	public void animateRotateRight(RBNode<Integer> node, RotationDirection dir, double xMin, double xMax, double yMin, double yMax) {
		Circle nodeX = (Circle)lookup("#" + node.getKey());
		Circle leftChild = (Circle)lookup("#" + node.getLeft().getKey());
		
		TranslateTransition t1 = new TranslateTransition(Duration.seconds(1), nodeX.getParent());
		TranslateTransition t2 = new TranslateTransition(Duration.seconds(1), leftChild.getParent());
		ParallelTransition par = new ParallelTransition();
		SequentialTransition seq = new SequentialTransition();
		PauseTransition pt = new PauseTransition(Duration.seconds(1));
		FadeTransition fd = new FadeTransition(Duration.millis(10), console);
		
		//go right so we can move nodeX there
		xMin = (xMin + xMax) / 2;
		yMin = yMax + yMin;
		double layoutRightX = ((xMin + xMax) / 2);
		double layoutRightY = yMin + yMax / 2;
		
		t1.setByX(Math.abs(nodeX.getParent().getLayoutX() - layoutRightX));
		t1.setByY(Math.abs(nodeX.getParent().getLayoutY() - layoutRightY));
		t2.setByX(Math.abs(leftChild.getParent().getLayoutX() - nodeX.getParent().getLayoutX()));
		t2.setByY(-Math.abs(leftChild.getParent().getLayoutY() - nodeX.getParent().getLayoutY()));
		
		// Helper-Text for the first step
		fd.setOnFinished(e -> {
			console.setText("We rotate the main node and it's left Child to the right.");
		});
				
		par.getChildren().addAll(fd, t1, t2);
		seq.getChildren().add(par);
		
		ParallelTransition par2;
		// fix subtree gamma
		// to fix gamma all we have to do is move it down one level
		if (node.getRight() != tree.getSentinel()) {
			// Helper-Text
			pt = new PauseTransition(Duration.seconds(1));
			fd = new FadeTransition(Duration.millis(10), console);
			fd.setOnFinished(e -> {
				console.setText("Since Subbtree Gamma is not empty, we fix it.");
			});
			
			par2 = new ParallelTransition();
			par2 = moveSubtreeDown(node.getRight(), par2, dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax);
			par2.getChildren().add(fd);
			seq.getChildren().addAll(pt, par2);
		}
		// fix subtree beta
		// this also works symmetrically to left rotation
		if (node.getLeft().getRight() != tree.getSentinel()) {
			// Helper-Text
			pt = new PauseTransition(Duration.seconds(1));
			fd = new FadeTransition(Duration.millis(10), console);
			fd.setOnFinished(e -> {
				console.setText("Since Subbtree Beta is not empty, we fix it.");
			});
			
			par2 = new ParallelTransition();
			// Even though node.getLeft().getRight() is the node we are moving, we want the ending position to be
			// at node.getRight().getLeft()
			par2 = moveSubtreeUp(node.getLeft().getRight(), par2, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
			par2.getChildren().add(fd);
			seq.getChildren().addAll(pt, par2);
		}
		// fix subtree alpha
		// to fix alpha we do it symetrically to fix gamma in a left rotation
		if (node.getLeft().getLeft() != tree.getSentinel()) {
			// Helper-Text
			pt = new PauseTransition(Duration.seconds(1));
			fd = new FadeTransition(Duration.millis(10), console);
			fd.setOnFinished(e -> {
				console.setText("Since Subbtree Alpha is not empty, we fix it.");
			});
						
			par2 = new ParallelTransition();
			// go up one level again
			xMin = 2 * xMin - xMax;
			yMin = yMin - yMax;
			par2 = moveSubtreeUp(node.getLeft().getLeft(), par2, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
			par2.getChildren().add(fd);
			seq.getChildren().addAll(pt, par2);
		}
		
		seq.play();
		seq.setOnFinished(e -> {
			tree.rotateRight(node);
			drawTree();
		});
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
			moveSubtreeDown(x.getLeft(), par, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
			
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
			moveSubtreeUp(x.getLeft(), par, dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax);
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
}
