package de.getto.nicolas.controller;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import de.getto.nicolas.node.NodeColor;
import de.getto.nicolas.node.RBNode;
import de.getto.nicolas.tree.RedBlackTree;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Node;
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
	
	private static final int[] STARTER_TREE = {7, 4, 11, 3, 6, 9, 18, 2, 14, 20, 12};
	//private static final int[] STARTER_TREE = {50, 125, 15, 10, 13, 75, 11, 5};
	private static final int RADIUS = 26;
	private static final Color NORMAL_BORDER = Color.rgb(169, 169, 169), HIGHLIGHT = Color.GOLD
								, NORMAL_LINE = Color.rgb(90, 90, 90);

	private Font font = Font.font("Cooper Black", FontWeight.BOLD, 16);
	private FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);
	
	private RedBlackTree<Integer> tree;
	private RBNode<Integer> insertNode;
	private int height;
	
	private HBox controlPanel;
	private TextField console;
	
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
	
	/*=============================================================================
	 * HERE ARE THE METHODS TO BE CALLED FROM THE CONTROLLER
	 * ============================================================================
	 */
	
	/**
	 * Method that gets called by the controller. Decides if the insertion should be animated.
	 * It delegates to insertWithAnimation if @param animationLength is true. Otherwise it delegates
	 * to insertWithoutAnimation.
	 * @param val The Value of the node that is to be inserted. Duplicates are not allowed.
	 * @param animationLength How long the animation should take. This only affects pause timers.
	 */
	public void insert(int val, double animationLength) {
		if (tree.findNode(val) != tree.getSentinel()) {
			console.setStyle("-fx-text-fill: red;");
			console.setText("A node with the value " + val + " already exists!");
			return;
		}
		CheckBox ab = (CheckBox) controlPanel.lookup("#animButton");
		
		console.setStyle("-fx-text-fill: black");
		drawTree();
		setButtonDisableToValue(true);
		if (ab.isSelected()) {
			insertWithAnimation(val, animationLength);
		} else {
			insertWithoutAnimation(val);
		}
	}
	
	public void delete(int val, double animationLength) {
		if (tree.findNode(val) == tree.getSentinel()) {
			console.setStyle("-fx-text-fill: red;");
			console.setText("A node with the value " + val + " does not exists!");
			return;
		}
		CheckBox ab = (CheckBox) controlPanel.lookup("#animButton");
		
		console.setStyle("-fx-text-fill: black");
		drawTree();
		setButtonDisableToValue(true);
		if (ab.isSelected()) {
			deleteWithAnimation(val, animationLength);
		} else {
			deleteWithoutAnimation(val);
		}
	}
	
	/**
	 * The Method to be called from the controller. Decides if search should be animated.
	 * @param val The value to be searched for.
	 * @param animationLength Lenght of the animation.
	 */
	public void search(final int val, double animationLength) {
		CheckBox ab = (CheckBox) controlPanel.lookup("#animButton");
		
		console.setStyle("-fx-text-fill: black");
		setButtonDisableToValue(true);
		drawTree();
		if (ab.isSelected()) {
			searchWithAnimation(val, animationLength);
		} else {
			searchWithoutAnimation(val);
		}
	}
	
	/**
	 * Clear the entire Tree at once by removing the root.
	 */
	public void clearTree() {
		tree.deleteTree();
		drawTree();
	}
	
	/**============================================================================
	 * HERE ARE THE METHODS THAT THE CONTROLLER-CALLED METHODS DELEGATE TO
	 * ============================================================================
	 */
	
	/**
	 * Inserts a new Node. If the Height exceeds 7, it gets incremented up until 10 where a new Node that would exceed that height 
	 * will be deleted.
	 * @param val Key of the new Node.
	 */
	private void insertWithoutAnimation(int val) {
		insertNode = new RBNode<Integer>(val);
		
		tree.insertNodeBU(insertNode);

		if (tree.treeHeight(tree.getRoot()) >= height - 1 && height < 10) {
			setHeight(++height);
		} else if (tree.treeHeight(tree.getRoot()) >= 10) {
			tree.deleteRBNode(insertNode);
			console.setStyle("-fx-text-fill: red;");
			console.setText("Tree is too big! A maximum height of 10 is allowed.");
		}
		
		drawTree();
		setButtonDisableToValue(false);
	}
	
	/**
	 * Insert a new Node with Animation. 
	 * @param val
	 */
	private void insertWithAnimation(int val, double animationLength) {
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
		
		
		seq.getChildren().add(animateInsertFixup(insertNode, animationLength));
		seq.play();
		
		seq.setOnFinished(e -> {
			
			if (tree.treeHeight(tree.getRoot()) >= height - 1 && height < 10) {
				setHeight(++height);
			} else if (tree.treeHeight(tree.getRoot()) >= 10) {
				tree.deleteRBNode(insertNode);
				console.setStyle("-fx-text-fill: red;");
				console.setText("Tree is too big! A maximum height of 10 is allowed.");
			}
			
			drawTree();
			setButtonDisableToValue(false);
		});
	}

	/**
	 * Delete a Node without Animation.
	 * @param val Value of the node that is to be deleted. It is expected that this node exists.
	 */
	private void deleteWithoutAnimation(int val) {
		tree.deleteNodeByValue(val);
		
		drawTree();
		setButtonDisableToValue(false);
	}

	/**
	 * Delete a node with explanatory animations.
	 * @param val Node to delete
	 * @param animationLength Animation time - this only affects pause transitions.
	 */
	private void deleteWithAnimation(int val, double animationLength) {
		RBNode<Integer> tempNode = tree.findNode(val);
		SequentialTransition seq = new SequentialTransition();
		double xMin = 0, xMax = widthProperty().get(), yMin = 0, yMax = heightProperty().get() / height;
		
		FadeTransition fade;
		PauseTransition pause;
		StrokeTransition st;
		TranslateTransition translate;
		ParallelTransition parallel;
		
		RBNode<Integer> toSpliceOut;
		RBNode<Integer> childOfToSpliceOut;
		final int nodeVal = tempNode.getKey();
		
		// We set the node that is to be spliced out - Either the node itself, or it's successor
		if (tempNode.getRight() == tree.getSentinel() || tempNode.getLeft() == tree.getSentinel()) {
			// we splice out the node itself, because it has one child max
			toSpliceOut = tempNode;
			Circle c = (Circle)lookup("#" + toSpliceOut.getKey());
			
			fade = new FadeTransition(Duration.millis(10), console);
			fade.setOnFinished(e -> {
				console.setText("Since " + nodeVal + " doesn't have two Children, we can just splice it out.");
			});
			st = new StrokeTransition(Duration.millis(10), c, (Color)c.getStroke(), HIGHLIGHT);
			pause = new PauseTransition(Duration.seconds(animationLength));
		} else {
			// we replace this node with the successor node, because it has two children
			toSpliceOut = tree.getTreeSuccessor(tempNode);
			final int succVal = toSpliceOut.getKey();
			Circle c = (Circle)lookup("#" + succVal);
			
			fade = new FadeTransition(Duration.millis(10), console);
			fade.setOnFinished(e -> {
				console.setText("Since " + nodeVal + " has two Children, we replace it with it's successor " + succVal + ".");
			});
			st = new StrokeTransition(Duration.millis(10), c, (Color)c.getStroke(), HIGHLIGHT);
			pause = new PauseTransition(Duration.seconds(animationLength));
		}
		seq.getChildren().addAll(fade, st, pause);

		childOfToSpliceOut = toSpliceOut.getLeft() != tree.getSentinel() ?
				toSpliceOut.getLeft() : toSpliceOut.getRight();
		RBNode<Integer> pastParent = childOfToSpliceOut.getParent();
		
		childOfToSpliceOut.setParent(toSpliceOut.getParent());
		// get coordinates of toSpliceOut
		double[] spliceOutCoord = findNodePosValues(toSpliceOut.getKey());
		xMin = spliceOutCoord[0];
		xMax = spliceOutCoord[1];
		yMin = spliceOutCoord[2];
		yMax = spliceOutCoord[3];
		
		if (toSpliceOut.getParent() == tree.getSentinel()) {
			// toSpliceOut is root, childOfToSpliceOut is either its only child or null
			tree.setRoot(childOfToSpliceOut);
			
			if (childOfToSpliceOut != tree.getSentinel()) {
				Circle c = (Circle)lookup("#" + childOfToSpliceOut.getKey());
				// copy the new node over
				fade = new FadeTransition(Duration.millis(10), console);
				fade.setOnFinished(e -> {
					console.setText("Since " + toSpliceOut.getKey() + " has an only child " + childOfToSpliceOut.getKey() + ", it is the new root.");
				});
				pause = new PauseTransition(Duration.seconds(animationLength));
				
				translate = new TranslateTransition(Duration.seconds(1), c.getParent());
				translate.setToX(((xMin + xMax) / 2) - c.getParent().getLayoutX());
				translate.setToY((yMin + yMax / 2) - c.getParent().getLayoutY());
				
				seq.getChildren().addAll(fade, translate, pause);
			}
		} else if (toSpliceOut == toSpliceOut.getParent().getLeft()) {
			// toSpliceOut is in the left subtree
			toSpliceOut.getParent().setLeft(childOfToSpliceOut);
		} else {
			// toSpliceOut is in the right subtree
			toSpliceOut.getParent().setRight(childOfToSpliceOut);
		}
		
		parallel = new ParallelTransition();
		pause = new PauseTransition(Duration.millis(10));
		
		// We replace the spliced out node and move it to it's correct place
		if (toSpliceOut != tempNode) {
			Circle c = (Circle)lookup("#" + toSpliceOut.getKey());
			Circle dest = (Circle)lookup("#" + tempNode.getKey());
			translate = new TranslateTransition(Duration.seconds(1), c.getParent());
			translate.setToX(dest.getParent().getLayoutX() - c.getParent().getLayoutX());
			translate.setToY(dest.getParent().getLayoutY() - c.getParent().getLayoutY());
			
			fade = new FadeTransition(Duration.millis(10), console);
			final int valFromSplicedOutNode = tempNode.getKey();
			fade.setOnFinished(e -> {
				console.setText("We move " + toSpliceOut.getKey() + " to it's correct spot and recolor it to " +
						valFromSplicedOutNode + "'s color.");
			});
			pause = new PauseTransition(Duration.seconds(animationLength));
			
			FillTransition fil = new FillTransition(Duration.millis(10), c,
					(Color)c.getFill(), (Color)dest.getFill());
			
			parallel.getChildren().addAll(fade, translate, fil);
		}
		
		// If a subtree exists, and we didn't already moved it to be the new root, we move it up
		if (toSpliceOut.getParent() != tree.getSentinel() && childOfToSpliceOut != tree.getSentinel()) {
			double[] childPos = findNodePosValues(childOfToSpliceOut.getKey());
			RotationDirection dir = pastParent.getRight() == childOfToSpliceOut ? 
					RotationDirection.LEFT : RotationDirection.RIGHT;
			parallel.getChildren().add(moveSubtreeUp(childOfToSpliceOut, dir,
					childPos[0], childPos[1], childPos[2], childPos[3]));
		}

		getChildren().remove(((Circle)lookup("#" + tempNode.getKey())).getParent());
		
		seq.getChildren().addAll(parallel, pause);
		
		if (toSpliceOut != tempNode) {
			tempNode.setKey(toSpliceOut.getKey());
		}
		
		seq.play();
		seq.setOnFinished(e -> {
			drawTree();
			if (toSpliceOut.getColor() == NodeColor.BLACK) {
				// repair here
				animateDeleteFixup(childOfToSpliceOut, animationLength);
			} else {
				// make buttons available again incase we don't need to repair the tree
				setButtonDisableToValue(false);
			}
		});
	}
	
	/**
	 * Searches for a Node without Animation.
	 * @param val
	 */
	private void searchWithoutAnimation(int val) {
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
	
	
	/**============================================================================
	 * HERE ARE HELPER METHODS
	 * ============================================================================
	 */
	
	/**
	 * This fixes a faulty tree and comments on it step-by-step. 
	 * It's a reconstruction of the insertNodeBUFixup method.
	 * Calling that method first will result in no Animations being shown, because
	 * the Tree will already be correct. Don't call both methods together.
	 * 
	 * @param node Node that got added.
	 * @return The Animation that plays the Tree Fix.
	 */
	private SequentialTransition animateInsertFixup(RBNode<Integer> node, double animationLength) {
		
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
						console.setText("Since both " + val + "'s parent and uncle are red, we swap colors with their parent.");
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
	
	/**
	 * Animate the Delete Fixup - TODO: Comment every Case to make it easier to understand.
	 * @param node
	 * @param animationLength
	 */
	private void animateDeleteFixup(RBNode<Integer> node, double animationLength) {
		SequentialTransition seq = new SequentialTransition();
		
		ParallelTransition par;
		FillTransition fil;
		PauseTransition pau;
		FadeTransition fade;
		
		Circle c;
		RBNode<Integer> uncle;
		while (node != tree.getRoot() && node.getColor() == NodeColor.BLACK) {
			if (node == node.getParent().getLeft()) {
				// Left Subtree
				uncle = node.getParent().getRight();
				if (uncle.getColor() == NodeColor.RED) {
					System.out.println("Left Subtree - Case 1: swap color of uncle and its parent, then rotate left.");
					par = new ParallelTransition();
					final int val1 = uncle.getKey();
					final int val2 = node.getParent().getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("We swap the colors " + val1 + " and " + val2 +". Then we rotate left.");
					});
					par.getChildren().add(fade);

					// Left Subtree - Case 1
					c = (Circle)lookup("#" + uncle.getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fil);
					uncle.setColor(NodeColor.BLACK);
					
					c = (Circle)lookup("#" + node.getParent().getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
					par.getChildren().add(fil);
					node.getParent().setColor(NodeColor.RED);
					
					pau = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(pau, par,
							startAnimateRotation(node.getParent().getKey(), RotationDirection.LEFT));
					tree.rotateLeft(node.getParent());
					uncle = node.getParent().getRight();
				}
				if (uncle.getLeft().getColor() == NodeColor.BLACK && uncle.getRight().getColor() == NodeColor.BLACK) {
					// Left Subtree - Case 2
					System.out.println("Left Subtree - Case 2: Recolor uncle red.");
					final int val1 = uncle.getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("Left Subtree - We recolor " + val1 + " red.");
					});
					
					c = (Circle)lookup("#" + uncle.getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
					pau = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(fade, pau, fil);
					uncle.setColor(NodeColor.RED);
					node = node.getParent();
				} else {
					if (uncle.getRight().getColor() == NodeColor.BLACK) {
						// Left Subtree - Case 3
						System.out.println("Left Subtree - Case 3: Swap uncle's color with it's left child. Then rotate right.");						
						par = new ParallelTransition();
						final int val1 = uncle.getKey();
						final int val2 = uncle.getLeft().getKey();
						fade = new FadeTransition(Duration.millis(10), console);
						fade.setOnFinished(e -> {
							console.setText("Left Subtree - Swap " + val1 + "'s color with " + val2 + ". Then rotate right.");
						});
						par.getChildren().add(fade);
						
						c = (Circle)lookup("#" + uncle.getLeft().getKey());
						fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
						par.getChildren().add(fil);
						uncle.getLeft().setColor(NodeColor.BLACK);
						
						c = (Circle)lookup("#" + uncle.getKey());
						fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
						par.getChildren().add(fil);
						uncle.setColor(NodeColor.RED);
						
						pau = new PauseTransition(Duration.seconds(animationLength));
						seq.getChildren().addAll(pau, par,
								startAnimateRotation(uncle.getKey(), RotationDirection.RIGHT));
						tree.rotateRight(uncle);
						uncle = node.getParent().getRight();
					}
					// Left Subtree - Case 4
					final int val1 = uncle.getKey();
					final int val2 = node.getParent().getKey();

					System.out.println("Left Subtree - Case 4: " + val2 + ", " + val1 + ".");
					par = new ParallelTransition();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("Left Subtree - Color " + val1 + " with the same color as " + val2 + ". Then rotate left.");
					});
					par.getChildren().add(fade);
					
					c = (Circle)lookup("#" + uncle.getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(),
							node.getParent().getColor() == NodeColor.BLACK ? Color.BLACK : Color.RED);
					par.getChildren().add(fil);
					uncle.setColor(node.getParent().getColor());
					
					c = (Circle)lookup("#" + node.getParent().getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fil);
					node.getParent().setColor(NodeColor.BLACK);
					
					c = (Circle)lookup("#" + uncle.getRight().getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fil);
					uncle.getRight().setColor(NodeColor.BLACK);
					
					pau = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(pau, par,
							startAnimateRotation(node.getParent().getKey(), RotationDirection.LEFT));
					tree.rotateLeft(node.getParent());
					node = tree.getRoot();
				}
			} else {
				// Right Subtree
				uncle = node.getParent().getLeft();
				if (uncle.getColor() == NodeColor.RED) {
					par = new ParallelTransition();
					final int val1 = uncle.getKey();
					final int val2 = node.getParent().getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("We swap the colors " + val1 + " and " + val2 +". Then we rotate right.");
					});
					par.getChildren().add(fade);
					
					// Right Subtree - Case 1
					System.out.println("Right Subtree - Case 1: swap color of uncle and its parent, then rotate right"
							+ " pivoting " + node.getParent().getKey() + ".");

					c = (Circle)lookup("#" + uncle.getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fil);
					uncle.setColor(NodeColor.BLACK);
					
					c = (Circle)lookup("#" + node.getParent().getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
					par.getChildren().add(fil);
					node.getParent().setColor(NodeColor.RED);
					
					pau = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(pau, par,
							startAnimateRotation(node.getParent().getKey(), RotationDirection.RIGHT));
					tree.rotateRight(node.getParent());
					uncle = node.getParent().getLeft();
				}
				if (uncle.getRight().getColor() == NodeColor.BLACK && uncle.getLeft().getColor() == NodeColor.BLACK) {
					// Right Subtree - Case 2
					final int val1 = uncle.getKey();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("Right Subtree - We recolor " + val1 + " red.");
					});
					
					System.out.println("Right Subtree - Case 2: Recolor uncle red.");
					c = (Circle)lookup("#" + uncle.getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
					pau = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(fade, pau, fil);
					uncle.setColor(NodeColor.RED);
					node = node.getParent();
				} else {
					if (uncle.getLeft().getColor() == NodeColor.BLACK) {
						par = new ParallelTransition();
						final int val1 = uncle.getKey();
						final int val2 = uncle.getLeft().getKey();
						fade = new FadeTransition(Duration.millis(10), console);
						fade.setOnFinished(e -> {
							console.setText("Right Subtree - Swap " + val1 + "'s color with " + val2 + ". Then rotate left.");
						});
						par.getChildren().add(fade);
						
						// Right Subtree - Case 3
						System.out.println("Right Subtree - Case 3: Swap uncle's color with it's right child."
								+ " Then rotate left pivoting " + uncle.getKey() + ".");
						c = (Circle)lookup("#" + uncle.getRight().getKey());
						fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
						par.getChildren().add(fil);
						uncle.getRight().setColor(NodeColor.BLACK);
						
						c = (Circle)lookup("#" + uncle.getKey());
						fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.RED);
						par.getChildren().add(fil);
						uncle.setColor(NodeColor.RED);
						
						pau = new PauseTransition(Duration.seconds(animationLength));
						seq.getChildren().addAll(pau, par,
								startAnimateRotation(uncle.getKey(), RotationDirection.LEFT));
						tree.rotateLeft(uncle);
						uncle = node.getParent().getLeft();
					}
					// Right Subtree - Case 4
					final int val1 = uncle.getKey();
					final int val2 = node.getParent().getKey();
					par = new ParallelTransition();
					fade = new FadeTransition(Duration.millis(10), console);
					fade.setOnFinished(e -> {
						console.setText("Right Subtree - Color " + val1 + " with the same color as " + val2 + ". Then rotate right.");
					});
					par.getChildren().add(fade);
					
					System.out.println("Right Subtree - Case 4 - then rotate right pivoting " + node.getParent().getKey() + ".");
					c = (Circle)lookup("#" + uncle.getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(),
							node.getParent().getColor() == NodeColor.BLACK ? Color.BLACK : Color.RED);
					par.getChildren().add(fil);
					uncle.setColor(node.getParent().getColor());
					
					c = (Circle)lookup("#" + node.getParent().getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fil);
					node.getParent().setColor(NodeColor.BLACK);
					
					c = (Circle)lookup("#" + uncle.getLeft().getKey());
					fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
					par.getChildren().add(fil);
					uncle.getLeft().setColor(NodeColor.BLACK);
					
					pau = new PauseTransition(Duration.seconds(animationLength));
					seq.getChildren().addAll(pau, par,
							startAnimateRotation(node.getParent().getKey(), RotationDirection.RIGHT));
					tree.rotateRight(node.getParent());
					node = tree.getRoot();
				}
			}
		}
		
		fade = new FadeTransition(Duration.seconds(animationLength), console);
		final int val1 = node.getKey();
		fade.setOnFinished(e -> {
			console.setText("Now recolor the node " + val1 + " black and redraw the Tree.");
		});
		
		System.out.println("Recolor the node " + node.getKey() + " black and redraw the Tree.");
		c = (Circle)lookup("#" + node.getKey());
		fil = new FillTransition(Duration.millis(10), c, (Color)c.getFill(), Color.BLACK);
		node.setColor(NodeColor.BLACK);
		seq.getChildren().addAll(fade, fil);
		
		seq.play();
		seq.setOnFinished(e -> {
			// cleanup
			drawTree();
			setButtonDisableToValue(false);
		});
	}
	
	/**
	 * Starts the Rotation Animation. This finds the correct xMin, xMax, yMin, yMax values 
	 * and reroutes to the correct direction method.
	 * @param val
	 * @param dir
	 * @return
	 */
	private ParallelTransition startAnimateRotation(int val, RotationDirection dir) {
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
	private ParallelTransition animateRotateLeft(RBNode<Integer> node, RotationDirection dir, double xMin, double xMax, double yMin, double yMax) {
		// Insert a Method to delete all edges in this subtree first. Maybe this looks better?
		Circle nodeX = (Circle)lookup("#" + node.getKey());
		Circle rightChild = (Circle)lookup("#" + node.getRight().getKey());
		
		TranslateTransition t1 = new TranslateTransition(Duration.seconds(1), nodeX.getParent());
		TranslateTransition t2 = new TranslateTransition(Duration.seconds(1), rightChild.getParent());
		ParallelTransition par = new ParallelTransition();
		
		// pos of nodeX
		double nodeXPosX = ((xMin + xMax) / 2 );
		double nodeXPosY = (yMin + yMax / 2);
		//go left so we can move nodeX there
		xMax = (xMin + xMax) / 2;
		yMin = yMin + yMax;
		//pos of nodeX destination
		double layoutLeftX = ((xMin + xMax) / 2);
		double layoutLeftY = yMin + yMax / 2;
		
		// go back up
		xMax = 2 * xMax - xMin;
		yMin = yMin - yMax;
		// go right so we get position of child
		xMin = (xMin + xMax) / 2;
		yMin = yMin + yMax;
		double rightChildPosX = (xMin + xMax) / 2;
		double rightChildPosY = yMin + yMax / 2;
		
		t1.setByX(-Math.abs(nodeXPosX - layoutLeftX));
		t1.setByY(Math.abs(nodeXPosY - layoutLeftY));
		t2.setByX(-Math.abs(nodeXPosX - rightChildPosX));
		t2.setByY(-Math.abs(nodeXPosY - rightChildPosY));
		
		par.getChildren().addAll(t1, t2);
		
		// go back up for subtrees
		xMin = 2 * xMin - xMax;
		yMin = yMin - yMax;
		
		// fix subtree alpha
		// to fix alpha, we just move it down one level
		if (node.getLeft() != tree.getSentinel()) {
			par.getChildren().add(moveSubtreeDown(node.getLeft(), dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax));
		}
		// fix subtree gamma
		// to fix gamma all we have to do is move it up one level and set it as a child of the original node
		// for that we have to move up one level again though
		if (node.getRight().getRight() != tree.getSentinel()) {
			// go up one level again
			par.getChildren().add(moveSubtreeUp(node.getRight().getRight(), dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax));

		}
		// fix subtree beta
		// to fix beta we move it to be the new right child of x and move it to that position
		if (node.getRight().getLeft() != tree.getSentinel()) {
			// Go the the starting position of the beta subtree: since we start the root, we go once right and once left
			xMin = (xMin + xMax) / 2;
			yMin = yMin + yMax;
			xMax = (xMin + xMax) / 2;
			yMin = (yMin + yMax);
			double dif = wayToMove(node.getRight().getLeft(), dir, xMin, xMax, yMin, yMax);
			par.getChildren().add(moveSubtreeSideways(node.getRight().getLeft(), dir, dif));
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
	private ParallelTransition animateRotateRight(RBNode<Integer> node, RotationDirection dir, double xMin, double xMax, double yMin, double yMax) {
		Circle nodeX = (Circle)lookup("#" + node.getKey());
		Circle leftChild = (Circle)lookup("#" + node.getLeft().getKey());

		TranslateTransition t1 = new TranslateTransition(Duration.seconds(1), nodeX.getParent());
		TranslateTransition t2 = new TranslateTransition(Duration.seconds(1), leftChild.getParent());
		ParallelTransition par = new ParallelTransition();
		
		// pos of nodeX
		double nodeXPosX = ((xMin + xMax) / 2);
		double nodeXPosY = (yMin + yMax / 2);
		//go right so we can move nodeX there
		xMin = (xMin + xMax) / 2;
		yMin = yMax + yMin;

		double layoutRightX = ((xMin + xMax) / 2);
		double layoutRightY = yMin + yMax / 2;
		// go back up
		xMin = 2 * xMin - xMax;
		yMin = yMin - yMax;
		// go left
		xMax = (xMin + xMax) / 2;
		yMin = yMin + yMax;
		double leftChildPosX = ((xMin + xMax ) / 2);
		double leftChildPosY = yMin + yMax / 2;
		
		t1.setByX(Math.abs(nodeXPosX - layoutRightX));
		t1.setByY(Math.abs(nodeXPosY - layoutRightY));
		t2.setByX(Math.abs(leftChildPosX - nodeXPosX));
		t2.setByY(-Math.abs(leftChildPosY - nodeXPosY));
		
		par.getChildren().addAll(t1, t2);
		
		// go back up for subtrees
		xMax = 2 * xMax - xMin;
		yMin = yMin - yMax;

		// fix subtree gamma
		// to fix gamma all we have to do is move it down one level
		if (node.getRight() != tree.getSentinel()) {	
			par.getChildren().add(moveSubtreeDown(node.getRight(), dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax));

		}
		// fix subtree alpha
		// to fix alpha we do it symetrically to fix gamma in a left rotation
		if (node.getLeft().getLeft() != tree.getSentinel()) {		
			// go up one level again
			par.getChildren().add(moveSubtreeUp(node.getLeft().getLeft(), dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax));
		}
		// fix subtree beta
		// this also works symmetrically to left rotation
		if (node.getLeft().getRight() != tree.getSentinel()) {
			// We go to the starting pos of the beta subtree
			xMax = (xMin + xMax) / 2;
			yMin = yMin + yMax;
			xMin = (xMin + xMax) / 2;
			yMin = yMin + yMax;
			// get dif between beta root and the destination
			double dif = wayToMove(node.getLeft().getRight(), dir, xMin, xMax, yMin, yMax);
			par.getChildren().add(moveSubtreeSideways(node.getLeft().getRight(), dir, dif));
		}
		
		return par;
	}
	
	/**
	 * Moves a Subtree, which is defined by x as the root, sideways. This is used to easily move a beta subtree
	 * in a rotation.
	 * @param x Root of Subtree.
	 * @param dir Direction of the associated Rotation.
	 * @param distance Distance to move the Nodes.
	 * @return The Animation as a parallel Transition.
	 */
	private ParallelTransition moveSubtreeSideways(RBNode<Integer> x, RotationDirection dir, double distance) {
		ParallelTransition par = new ParallelTransition();
		if (x != tree.getSentinel()) {
			par.getChildren().add(moveSubtreeSideways(x.getLeft(), dir, distance));
			par.getChildren().add(moveSubtreeSideways(x.getRight(), dir, distance));
			
			Circle c = (Circle)lookup("#" + x.getKey());
			TranslateTransition tt = new TranslateTransition(Duration.seconds(1), c.getParent());
			
			tt.setByX(distance);
			par.getChildren().add(tt);
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
	 * @return The Animation as a parallel Transition.
	 */
	private ParallelTransition moveSubtreeDown(RBNode<Integer> x, RotationDirection dir,
			double xMin, double xMax, double yMin, double yMax) {
		ParallelTransition par = new ParallelTransition();
		if (x != tree.getSentinel()) {
			par.getChildren().add(moveSubtreeDown(x.getLeft(), dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax));
			par.getChildren().add(moveSubtreeDown(x.getRight(), dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax));
			
			Circle c = (Circle)lookup("#" + x.getKey());
			TranslateTransition tt = new TranslateTransition(Duration.seconds(1), c.getParent());
			
			// pos of c
			double cLayoutX = (xMin + xMax) / 2;
			double cLayoutY = yMin + yMax / 2;
						
			// dest of c
			xMax = (xMin + xMax) / 2;
			yMin = yMin + yMax;
			double cDestX = (xMin + xMax) / 2;
			double cDestY = yMin + yMax / 2;

			if (dir != RotationDirection.LEFT) {
				tt.setByX(-Math.abs(cDestX - cLayoutX));
			} else {
				tt.setByX(Math.abs(cDestX - cLayoutX));
			}
			tt.setByY(Math.abs(cDestY - cLayoutY));
			par.getChildren().add(tt);
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
	 * @return The Animation as a parallel Transition
	 */
	private ParallelTransition moveSubtreeUp(RBNode<Integer> x, RotationDirection dir,
			double xMin, double xMax, double yMin, double yMax) {
		ParallelTransition par = new ParallelTransition();

		if (x != tree.getSentinel()) {
			par.getChildren().add(moveSubtreeUp(x.getLeft(), dir, xMin, (xMin + xMax) / 2, yMin + yMax, yMax));
			par.getChildren().add(moveSubtreeUp(x.getRight(), dir, (xMin + xMax) / 2, xMax, yMin + yMax, yMax));
			
			Circle c = (Circle)lookup("#" + x.getKey());
			TranslateTransition tt = new TranslateTransition(Duration.seconds(1), c.getParent());
			
			// pos of c
			double cLayoutX = (xMin + xMax) / 2;
			double cLayoutY = yMin + yMax / 2;
						
			// dest of c
			xMax = (xMin + xMax) / 2;
			yMin = yMin + yMax;
			double cDestX = (xMin + xMax) / 2;
			double cDestY = yMin + yMax / 2;

			if (dir == RotationDirection.LEFT) {
				tt.setByX(-Math.abs(cDestX - cLayoutX));
			} else {
				tt.setByX(Math.abs(cDestX - cLayoutX));
			}
			tt.setByY(-Math.abs(cDestY - cLayoutY));
			par.getChildren().add(tt);
		}
		return par;
	}
	
	/*
	 * Finds the distance between two subtrees to move them sideways.
	 */
	private double wayToMove(RBNode<Integer> x, RotationDirection dir, 
			double xMin, double xMax, double yMin, double yMax) {
		double originX = (xMin + xMax) / 2;
		double destX; 

		if (dir == RotationDirection.LEFT) {
			// go up twice, then left, then right to be where we want to be
			xMax = 2 * xMax - xMin;
			yMin = yMin - yMax;
			xMin = 2 * xMin - xMax;
			yMin = yMin - yMax;
			
			xMax = (xMin + xMax) / 2;
			yMin = yMin + yMax;
			xMin = (xMin + xMax) / 2;
			yMin = yMin + yMax;
		} else {
			// go up twice, then right, then left to be where we want to be
			xMin = 2 * xMin - xMax;
			yMin = yMin - yMax;
			xMax = 2 * xMax - xMin;
			yMin = yMin - yMax;
			
			xMin = (xMin + xMax) / 2;
			yMin = yMin + yMax;
			xMax = (xMin + xMax) / 2;
			yMin = yMin + yMax;
		}
		destX = (xMin + xMax) / 2;
		
		return destX - originX;
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
	
	/**
	 * Helper Method. Sets Tree Height.
	 * @param height
	 */
	private void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * Helper Method. Gets Width of String.
	 * @param s 
	 * @return string width.
	 */
	private float getStringWidth(String s) {
		float width = 0;
		
		for (char c : s.toCharArray()) {
			width += fm.getCharWidth(c);
		}
		
		return width;
	}
	
	/**
	 * Find Node pos values xMin, xMax, yMin, yMax for the node with the value val.
	 * @param val value to find
	 * @return Pos Values
	 */
	private double[] findNodePosValues(int val) {
		double xMin = 0, xMax = widthProperty().get(), yMin = 0, yMax = heightProperty().get() / height;
		RBNode<Integer> temp = tree.getRoot();
		
		while (temp != tree.getSentinel()) {
			if (temp.getKey() == val) {
				double[] ret = {xMin, xMax, yMin, yMax};
				return ret;
			} else if (temp.getKey() > val) {
				xMax = (xMin + xMax) / 2;
				yMin = yMin + yMax;
				temp = temp.getLeft();
			} else {
				xMin = (xMin + xMax) / 2;
				yMin = yMin + yMax;
				temp = temp.getRight();
			}
		}
		return null; 
	}
	
}
