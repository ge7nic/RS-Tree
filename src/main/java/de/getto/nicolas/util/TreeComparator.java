package de.getto.nicolas.util;

import de.getto.nicolas.node.RBNode;

public class TreeComparator {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isEqualTree(RBNode root, RBNode rootToCompareTo) {
		if (root.getKey() == null && rootToCompareTo.getKey() == null) {
			return true;
		}
		if (root.getKey().compareTo(rootToCompareTo.getKey()) != 0) {
			return false;
		}
		return isEqualTree(root.getLeft(), rootToCompareTo.getLeft())
				&& isEqualTree(root.getRight(), rootToCompareTo.getRight());
	}
}
