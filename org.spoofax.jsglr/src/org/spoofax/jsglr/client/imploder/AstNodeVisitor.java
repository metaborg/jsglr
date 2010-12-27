package org.spoofax.jsglr.client.imploder;

import java.util.Iterator;


/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class AstNodeVisitor implements IAstNodeVisitor {
	
	public final boolean visit(IAstNode tree) {
		Iterator<IAstNode> iterator = tryGetListIterator(tree); 
		for (int i = 0, max = tree.getChildCount(); i < max; i++) {
			IAstNode child = iterator == null ? tree.getChildAt(i) : iterator.next();
			preVisit(child);
			boolean isDone = visit(child);
			postVisit(child);
			if (isDone || isDone()) return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected static Iterator<IAstNode> tryGetListIterator(IAstNode tree) {
		if (tree instanceof Iterable) {
			return ((Iterable<IAstNode>) tree).iterator();
		} else {
			return null;
		}
	}
	
	public void preVisit(IAstNode node) {
		// No default implementation
	}
	
	public void postVisit(IAstNode node) {
		// No default implementation
	}
	
	public boolean isDone() {
		return false;
	}
}

//Local interface avoids abstract method and subsequent @Override annotation requirement

interface IAstNodeVisitor {
	void preVisit(IAstNode node);

	void postVisit(IAstNode node);
}