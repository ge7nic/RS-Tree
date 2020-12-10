package de.getto.nicolas.util;

import de.getto.nicolas.node.RBNode;

public class TreeComparator {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isEqualTree(RBNode root, RBNode rootToCompareTo, RBNode rootSentinel, RBNode compareSentinel) {
		if (root == rootSentinel && rootToCompareTo == compareSentinel) {
			return true;
		}
		if (root.getKey().compareTo(rootToCompareTo.getKey()) != 0) {
			return false;
		}
		return isEqualTree(root.getLeft(), rootToCompareTo.getLeft(), rootSentinel, compareSentinel)
				&& isEqualTree(root.getRight(), rootToCompareTo.getRight(), rootSentinel, compareSentinel);
	}
}
