package de.getto.nicolas.node;

public class RBNode<T extends Comparable<T>> {
	private NodeColor color;
	private T key;
	private RBNode<T> left;
	private RBNode<T> right;
	private RBNode<T> parent;
	private boolean isHighlighted;
	
	@SuppressWarnings("rawtypes")
	public static final RBNode SENTINEL = new RBNode();
	
	@SuppressWarnings("unchecked")
	public static final <T extends Comparable<T>> RBNode<T> emptyNode() {
		return (RBNode<T>) SENTINEL;
	}
	
	public RBNode() {
		this.key = null;
		this.color = NodeColor.RED;
	}
	
	public RBNode(T key) {
		this.key = key;
		this.color = NodeColor.RED;
	}

	public NodeColor getColor() {
		return color;
	}

	public T getKey() {
		return key;
	}

	public RBNode<T> getLeft() {
		return left;
	}

	public RBNode<T> getRight() {
		return right;
	}

	public RBNode<T> getParent() {
		return parent;
	}
	
	public boolean getIsHighlighted() {
		return isHighlighted;
	}

	public void setColor(NodeColor color) {
		this.color = color;
	}

	public void setKey(T key) {
		this.key = key;
	}

	public void setLeft(RBNode<T> left) {
		this.left = left;
	}

	public void setRight(RBNode<T> right) {
		this.right = right;
	}

	public void setParent(RBNode<T> parent) {
		this.parent = parent;
	}
	
	public void setIsHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}
}
